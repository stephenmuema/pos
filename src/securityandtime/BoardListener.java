package securityandtime;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

import static java.awt.Toolkit.getDefaultToolkit;

public class BoardListener extends Thread implements ClipboardOwner {
    Clipboard sysClip = getDefaultToolkit().getSystemClipboard();

    public static void main(String[] args) {
        BoardListener b = new BoardListener();
        b.start();
    }

    public void run() {
        Transferable trans = sysClip.getContents(this);
        regainOwnership(trans);
        System.out.println("Listening to board...");

    }

    public void lostOwnership(Clipboard c, Transferable t) {
        try {
            sleep(100);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        Transferable contents = sysClip.getContents(this);
        processContents(contents);
        regainOwnership(contents);
    }

    void processContents(Transferable t) {
        System.out.println("Processing: " + t);
    }

    void regainOwnership(Transferable t) {
        sysClip.setContents(t, this);
    }

    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }
}
