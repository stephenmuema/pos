package Controllers.AuthenticationControllers;

import Controllers.UtilityClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Window;
import logging.LogClass;
import securityandtime.Security;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.site;

//end of imports
public class LoginController extends UtilityClass implements Initializable {

    public Label clock;
    public String emailSubmit, pass;
    @FXML
    Hyperlink link;
    @FXML
    Button login, signup;
    @FXML
    PasswordField password;
    @FXML
    TextField email;
    @FXML
    private
    ImageView imageView;
    @FXML
    private AnchorPane parent;
    @FXML
    private Label message;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//display different messages
        if (config.login.containsKey("loggedout")) {
            message.setText("YOU ARE LOGGED OUT ");
//            destroy session variables
            config.user.clear();
            config.login.clear();
        } else {
            message.setText("SIGN INTO YOUR ACCOUNT");

        }
//        go to nanotechsoftwares website
        link.setOnMousePressed(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        buttonClick();
        timeMain(clock);
        enterpressed();
    }

    private void enterpressed() {
        email.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                loginValidation();
            }
        });
        password.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                loginValidation();
            }
        });
    }

    //
//method to handle button clicks
    private void buttonClick() {
        signup.setOnMousePressed(new EventHandler<MouseEvent>() {
            //            got to sign up page
            @Override
            public void handle(MouseEvent event) {
                parent.getChildren().removeAll();
                try {
                    parent.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/signup.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        login.setOnMousePressed(event -> {
//            login and check if fields are empty
            loginValidation();
        });
    }

    private void loginValidation() {
        if (email.getText().isEmpty() || password.getText().isEmpty()) {
            LogClass.getLogger().log(Level.SEVERE, " PLEASE FILL ALL FIELDS");
            showAlert(Alert.AlertType.INFORMATION, parent.getScene().getWindow(),
                    "FILL ALL FIELDS", "PLEASE FILL ALL FIELDS");

        } else {
            login();

        }
    }

    //login method
    private void login() {
//        get input text
        emailSubmit = email.getText();
        pass = password.getText();
        try {
//            create a connection
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email=? OR employeename=?");
            statement.setString(1, emailSubmit);
            statement.setString(2, emailSubmit);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    if (!resultSet.getString("status").equalsIgnoreCase("active")) {
                        showAlert(Alert.AlertType.INFORMATION, parent.getScene().getWindow(),
                                "YUR ACCOUNT IS SUSPENDED", "PLEASE INFORM the ADMINISTRATOR TO ACTIVATE YOUR ACCOUNT");

                    } else {
                        if (resultSet.getBoolean("activated")) {
                            //if account exists and password matches hashed password
                            if ((resultSet.getString("password").equals(Security.hashPassword(pass)))) {
                                if (resultSet.getBoolean("admin")) {
//
//if user account is admin
                                    parent.getChildren().removeAll();
                                    try {
//                                    go to admin panel
                                        parent.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml"))));
                                        assert false;
//                                    work as sessions and hold user session data
                                        config.login.put("loggedinasadmin", true);
                                        config.user.put("userName", resultSet.getString("employeename"));

                                        config.user.put("user", resultSet.getString("email"));
                                        config.key.put("key", resultSet.getString("subscribername"));


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
//                                user is not admin go to normal panel
                                    parent.getChildren().removeAll();
                                    try {
                                        parent.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panel.fxml"))));
                                        assert false;
                                        //                                    work as sessions and hold user session data
                                        config.user.put("userName", resultSet.getString("employeename"));

                                        config.user.put("user", resultSet.getString("email"));
                                        config.login.put("loggedinasemployee", true);

//                                    new ShopController().setTransID(String.valueOf(new Random().nextGaussian()));
                                        //                                    create a new transaction id for local sqlite cart

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
//                            if passwords do not match
                                LogClass.getLogger().log(Level.SEVERE, " passwords do not match");
                                showAlert(Alert.AlertType.INFORMATION, parent.getScene().getWindow(),
                                        "WRONG PASSWORD!!", "ENTER THE CORRECT PASSWORD");

                            }
                        } else {
                            showAlert(Alert.AlertType.WARNING, parent.getScene().getWindow(),
                                    "Activate your license/account !!", "PLEASE ACTIVATE YOUR ACCOUNT OR INFORM THE EMPLOYER TO RENEW THE LICENSE");

                        }
                    }
                }

            } else {
                showAlert(Alert.AlertType.WARNING, parent.getScene().getWindow(),
                        "WRONG NAME/EMAIL !!", "PLEASE RE-ENTER A VALID USER NAME OR EMAIL");
//                LogClass.getLogger().log(Level.SEVERE, " LOGIN ERROR");
//name or email does not exist
            }

        } catch (Exception e) {
            e.printStackTrace();
            message.setText("CHECK YOUR CONNECTION!!");
            message.setTextFill(Paint.valueOf("RED"));
        }
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }

    //
//dispaly alert
    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public String getEmailSubmit() {
        return emailSubmit;
    }

    public void setEmailSubmit(String emailSubmit) {
        this.emailSubmit = emailSubmit;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Hyperlink getLink() {
        return link;
    }

    public void setLink(Hyperlink link) {
        this.link = link;
    }

    public Button getLogin() {
        return login;
    }

    public void setLogin(Button login) {
        this.login = login;
    }

    public Button getSignup() {
        return signup;
    }

    public void setSignup(Button signup) {
        this.signup = signup;
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public AnchorPane getParent() {
        return parent;
    }

    public void setParent(AnchorPane parent) {
        this.parent = parent;
    }

    public Label getMessage() {
        return message;
    }

    public void setMessage(Label message) {
        this.message = message;
    }

    //fetch time each second
//    todo include network time from server  later
}
