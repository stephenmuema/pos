package Controllers.CarWashControllers;
//deals with cr wash cashiers

import Controllers.UserAccountManagementControllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.CarWashMaster;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.*;

public class CarwashSalesController extends UtilityClass implements Initializable {
    public static int lastSelectedTabIndex = 0;
    public VBox carWash;
    public Tab clients;
    public TableView<CarWashMaster> tab;
    public TableColumn<CarWashMaster, String> Name;
    public TableColumn<CarWashMaster, String> reg;
    public TableColumn<CarWashMaster, String> id;
    public TableColumn<CarWashMaster, String> status;
    public TableColumn<CarWashMaster, String> operator;
    public TableColumn<CarWashMaster, String> payout;
    public Label clock;
    public Button home;
    public TabPane tabpane;
    public TextField name;
    public TextField registration;
    public TextField contact;
    public TextField identification;
    public Button submit;
    public Tab newclients;
    public MenuItem logoutMenu;
    public MenuItem exitMenu;
    public MenuItem accountdetailsMenu;
    public MenuItem CreatorsMenu;
    public MenuItem helpMenu;
    private double tabWidth = 415.0;
    private ObservableList<CarWashMaster> data;

    public static int getLastSelectedTabIndex() {
        return lastSelectedTabIndex;
    }

    public static void setLastSelectedTabIndex(int lastSelectedTabIndex) {
        CarwashSalesController.lastSelectedTabIndex = lastSelectedTabIndex;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time(clock);
//loadTab();
        editable();
        buttonListeners();
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        carWash.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(carWash, Event.ANY);
        clients.setOnSelectionChanged(event -> {
            data = FXCollections.observableArrayList();
            if (clients.isSelected()) {
                loadTab();
            }
        });
        menuclick();
    }

    private void menuclick() {


        helpMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(sitedocs).toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        CreatorsMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(site).toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        exitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
                System.exit(1);
            }
        });
        logoutMenu.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
//                System.out.println("logging out");
                carWash.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public VBox getCarWash() {
        return carWash;
    }

    public void setCarWash(VBox carWash) {
        this.carWash = carWash;
    }

    public Tab getClients() {
        return clients;
    }

    public void setClients(Tab clients) {
        this.clients = clients;
    }

    public TableView<CarWashMaster> getTab() {
        return tab;
    }

    public void setTab(TableView<CarWashMaster> tab) {
        this.tab = tab;
    }

    public TableColumn<CarWashMaster, String> getName() {
        return Name;
    }

    public void setName(TextField name) {
        this.name = name;
    }

    public void setName(TableColumn<CarWashMaster, String> name) {
        Name = name;
    }

    public TextField getRegistration() {
        return registration;
    }

    public void setRegistration(TextField registration) {
        this.registration = registration;
    }

    public TextField getContact() {
        return contact;
    }

    public void setContact(TextField contact) {
        this.contact = contact;
    }

    public TextField getIdentification() {
        return identification;
    }

    public void setIdentification(TextField identification) {
        this.identification = identification;
    }

    public Button getSubmit() {
        return submit;
    }

    public void setSubmit(Button submit) {
        this.submit = submit;
    }

    public Tab getNewclients() {
        return newclients;
    }

    public void setNewclients(Tab newclients) {
        this.newclients = newclients;
    }

    public double getTabWidth() {
        return tabWidth;
    }

    public void setTabWidth(double tabWidth) {
        this.tabWidth = tabWidth;
    }

    public ObservableList<CarWashMaster> getData() {
        return data;
    }

    public void setData(ObservableList<CarWashMaster> data) {
        this.data = data;
    }

    public TableColumn<CarWashMaster, String> getReg() {
        return reg;
    }

    public void setReg(TableColumn<CarWashMaster, String> reg) {
        this.reg = reg;
    }

    public TableColumn<CarWashMaster, String> getId() {
        return id;
    }

    public void setId(TableColumn<CarWashMaster, String> id) {
        this.id = id;
    }

    public TableColumn<CarWashMaster, String> getStatus() {
        return status;
    }

    public void setStatus(TableColumn<CarWashMaster, String> status) {
        this.status = status;
    }

    public TableColumn<CarWashMaster, String> getOperator() {
        return operator;
    }

    public void setOperator(TableColumn<CarWashMaster, String> operator) {
        this.operator = operator;
    }

    public TableColumn<CarWashMaster, String> getPayout() {
        return payout;
    }

    public void setPayout(TableColumn<CarWashMaster, String> payout) {
        this.payout = payout;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public Button getHome() {
        return home;
    }

    public void setHome(Button home) {
        this.home = home;
    }

    public TabPane getTabpane() {
        return tabpane;
    }

    public void setTabpane(TabPane tabpane) {
        this.tabpane = tabpane;
    }

    private void tabpaneStyles() {
        tabpane.setSide(Side.TOP);
        tabpane.setTabMinWidth(tabWidth);
        tabpane.setTabMaxWidth(tabWidth);
        tabpane.setTabMinHeight(tabWidth - 380.0);
        tabpane.setTabMaxHeight(tabWidth - 380.0);
        tabpane.setRotateGraphic(true);

        configureTab(clients, "Existing Jobs");
        configureTab(newclients, "New Jobs");
    }


    private void configureTab(Tab tab, String title) {
        double imageWidth = 40.0;


        Label label = new Label(title);
        label.setMaxWidth(tabWidth - 20);
        label.setPadding(new Insets(5, 0, 0, 0));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 8pt; -fx-font-weight: normal;");
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane tabPane = new BorderPane();
        tabPane.setRotate(90.0);
        tabPane.setMaxWidth(tabWidth);
        tabPane.setBottom(label);

        /// 6.
        tab.setText("");
        tab.setGraphic(tabPane);
    }


    private void buttonListeners() {
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection finalConnection = connection;
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String ownername = name.getText().toUpperCase();
                String numberplate = registration.getText().toUpperCase();
                String idnum = identification.getText().toUpperCase();
                String contactnumber = contact.getText().toUpperCase();

//
//
//
                if (ownername.isEmpty() || numberplate.isEmpty() || idnum.isEmpty() || contactnumber.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, carWash.getScene().getWindow(), "FILL ALL FIELDS", "ALL FIELDS SHOULD BE FILLED TO PROCEED");
                } else {
                    PreparedStatement preparedStatement = null;

                    try {
                        assert finalConnection != null;
                        preparedStatement = finalConnection.prepareStatement("INSERT INTO carwash(`ownername`,registration,idnumber,contact,status)VALUES(?,?,?,?,?)");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (preparedStatement != null) {
                            preparedStatement.setString(1, ownername);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.setString(2, numberplate);
                            //                System.out.println("user name=="+user);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.setString(3, idnum);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.setString(4, contactnumber);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.setString(5, "PENDING");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        //executequery
                        if (preparedStatement != null) {
                            int rows = preparedStatement.executeUpdate();
                            if (rows > 0) {
                                System.out.println(rows);
                                showAlert(Alert.AlertType.INFORMATION, carWash.getScene().getWindow(), "SUCCESS ", "YOUR ITEM WAS ADDED SUCCESSFULLY");
                                name.clear();
                                registration.clear();
                                identification.clear();
                                contact.clear();
                            } else {
                                showAlert(Alert.AlertType.WARNING, carWash.getScene().getWindow(), "  FAILURE", "ERROR WHEN INSERTING ITEMS");

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        home.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    carWash.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panel.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    private void loadTab() {
        data = FXCollections.observableArrayList();
        Connection connection = null;


//        for (Node n: tab.lookupAll("TableRow")) {
//            if (n instanceof TableRow) {
//                TableRow row = (TableRow) n;
//                if (table.getItems().get(i).getWillPay()) {
//                    row.getStyleClass().add("willPayRow");
//                } else {
//                    row.getStyleClass().add("wontPayRow");
//                }
//                i++;
//                if (i == table.getItems().size())
//                    break;
//            }


        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
//                        DISPLAYING CLIENTS
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM carwash where status=?");
                statement.setString(1, "PENDING");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    CarWashMaster carWashMaster1 = new CarWashMaster();
                    carWashMaster1.Name.set(resultSet.getString("ownername"));
                    carWashMaster1.regno.set(resultSet.getString("registration"));
                    carWashMaster1.Id.set(resultSet.getString("id"));
                    carWashMaster1.idnum.set(resultSet.getString("idnumber"));
                    carWashMaster1.status.set(resultSet.getString("status"));
                    carWashMaster1.operator.set(resultSet.getString("washedby"));
                    carWashMaster1.cash.set(resultSet.getString("cashpaid"));
//                    int tempmoneyPaid = 0;
//                    tempmoneyPaid += Integer.parseInt(resultSet.getString("cashpaid"));
//                    moneyPaid = tempmoneyPaid;
                    data.add(carWashMaster1);
                }
                tab.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
        Name.setCellValueFactory(
                new PropertyValueFactory<>("Name"));
        id.setCellValueFactory(
                new PropertyValueFactory<>("idnum"));
        reg.setCellValueFactory(new PropertyValueFactory<>("regno"));
        status.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        operator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        payout.setCellValueFactory(new PropertyValueFactory<>("cash"));

        tab.refresh();
    }

    private void editable() {
        tab.setEditable(true);
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection finalConnection = connection;
        Name.setCellFactory(TextFieldTableCell.forTableColumn());
        Name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            assert finalConnection != null;
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set `ownername`=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        reg.setCellFactory(TextFieldTableCell.forTableColumn());
        reg.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set registration=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        id.setCellFactory(TextFieldTableCell.forTableColumn());
        id.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set idnumber=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set status=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        operator.setCellFactory(TextFieldTableCell.forTableColumn());
        operator.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set washedby=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );

        payout.setCellFactory(TextFieldTableCell.forTableColumn());
        payout.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set cashpaid=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
    }


    private void compete() {
        tab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("carwashFiles/carwashprice.fxml"));
                        try {
                            Parent parent = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}