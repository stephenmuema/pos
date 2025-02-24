package Controllers;
//made by steve

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.io.IOException;

public class IdleMonitor extends UtilityClass {

    private final Timeline idleTimeline;

    private final EventHandler<Event> userEventHandler;

    public IdleMonitor(Duration idleTime, Runnable notifier, boolean startMonitoring) throws IOException {
        super();
        idleTimeline = new Timeline(new KeyFrame(idleTime, e -> notifier.run()));
        idleTimeline.setCycleCount(Animation.INDEFINITE);

        userEventHandler = e -> notIdle();

        if (startMonitoring) {
            startMonitoring();
        }
    }

    public IdleMonitor(Duration idleTime, Runnable notifier) throws IOException {
        this(idleTime, notifier, false);
    }

    public void register(Scene scene, EventType<? extends Event> eventType) {
        scene.addEventFilter(eventType, userEventHandler);
    }

    public void register(Node node, EventType<? extends Event> eventType) {
        node.addEventFilter(eventType, userEventHandler);
    }

    public void unregister(Scene scene, EventType<? extends Event> eventType) {
        scene.removeEventFilter(eventType, userEventHandler);
    }

    public void unregister(Node node, EventType<? extends Event> eventType) {
        node.removeEventFilter(eventType, userEventHandler);
    }

    private void notIdle() {
        if (idleTimeline.getStatus() == Animation.Status.RUNNING) {
            idleTimeline.playFromStart();
        }
    }

    private void startMonitoring() {
        idleTimeline.playFromStart();
    }

    public void stopMonitoring() {
        idleTimeline.stop();
    }

    public Timeline getIdleTimeline() {
        return idleTimeline;
    }

    public EventHandler<Event> getUserEventHandler() {
        return userEventHandler;
    }
}