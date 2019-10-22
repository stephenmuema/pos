package Controllers.ShopControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.EmployeeMaster;
import MasterClasses.SalesMaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.site;

//made by steve
public class AuditController extends UtilityClass implements Initializable {
    @FXML
    private MenuItem details;
    @FXML
    private MenuItem menulogout;
    @FXML
    private TabPane maintabpane;//maintabpane
    @FXML
    private Tab tabemployeeaudits;//employee audits tab

    //table of employees
    @FXML
    private TableView<EmployeeMaster> tableemployeelist;
    @FXML
    private TableColumn<EmployeeMaster, String> employeeid;
    @FXML
    private TableColumn<EmployeeMaster, String> employeename;
    @FXML
    private TableColumn<EmployeeMaster, String> employeeemail;

//    table of sales for each selected employee
@FXML
private TableView<SalesMaster> tableemployeesales;
    @FXML
    private TableColumn<SalesMaster, String> employeetransid;
    @FXML
    private TableColumn<SalesMaster, String> transprice;
    @FXML
    private TableColumn<SalesMaster, String> transpaid;
    @FXML
    private TableColumn<SalesMaster, String> transmethod;
    @FXML
    private TableColumn<SalesMaster, String> transbalance;
    @FXML
    private TableColumn<SalesMaster, String> transcompletion;
    //
//done
//    end of table of sales per employee

    @FXML
    private Tab tabsalesaudits;//tab of sales

    @FXML
    private Tab tabstockalerts;
    //tab of alerts
    @FXML
    private TableView stockalerttable;


    @FXML
    private TableColumn stockalerttableid;
    @FXML
    private TableColumn stockalerttablename;
    @FXML
    private TableColumn stockalerttabledate;
    @FXML
    private TableColumn stockalerttablemarkasread;
    @FXML
    private Tab taballaudits;
    //tab of all audits for exports or graphical viewing
    @FXML
    private Button exportfullreport;
    @FXML
    private Button exportcategoryreport;
    @FXML
    private Button exportemployeereport;
    @FXML
    private Button getdetailedgraph;
    @FXML
    private Button getcateegorygraph;
    @FXML
    private Button getemployeegraph;
    @FXML
    private Label clock;
    @FXML
    private AnchorPane panel;
    @FXML
    private Button topanelbutton;
    @FXML
    private Button tocarwashbutton;
    @FXML
    private Button toemployeesbutton;
    @FXML
    private Button logoutbutton;
    @FXML
    private Button tosupplierbutton;
    @FXML
    private Button showempperformancegraph;
    @FXML
    private Button exportfirstempreport;
    @FXML
    private Button showEmpReport;

    @FXML
    private MenuItem endDayMenu;
    @FXML
    private MenuItem reportIssuesMenu;
    @FXML
    private MenuItem restartServerMenu;
    @FXML
    private MenuItem troubleShootMenu;

    @FXML
    private MenuItem abtMenu;
    @FXML
    private MenuItem termsMenu;
    @FXML
    private MenuItem checkUpdatesMenu;
    @FXML
    private MenuItem reachUsMenu;
    @FXML
    private MenuItem generateReportsMenu;
    @FXML
    private MenuItem documentationMenu;
    @FXML
    private MenuItem menuQuit;

    @FXML
    private MenuItem staffMenu;
    @FXML
    private MenuItem carWashMenu;
    @FXML
    private MenuItem inventoryMenu;
    @FXML
    private MenuItem mrMenu;
    @FXML
    private MenuItem auditsMenu;
    @FXML
    private MenuItem menuShutDown;
    @FXML
    private MenuItem menuRestart;

    //query report of one employee
    @FXML
    private DatePicker startDateEmployeeSales;
    @FXML
    private DatePicker endDateEmployeeSales;
    @FXML
    private Button queryEmpTimeReport;
    //db connection
    private Connection connection;
    private ObservableList<EmployeeMaster> employeeMasterObservableList = FXCollections.observableArrayList();
    private ObservableList<SalesMaster> salesMasterObservableList = FXCollections.observableArrayList();

    public AuditController() throws IOException {
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
        try {
            IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(900),
                    () -> {
                        try {
                            config.login.put("loggedout", true);

                            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.panel.put("panel", panel);

        menuListeners();
        buttonListeners();
        navigatoryButtonListeners();
        loadTables();
        time(clock);
    }

    private void navigatoryButtonListeners() {
        topanelbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        tocarwashbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        toemployeesbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        logoutbutton.setOnMouseClicked(event -> logout());
        tosupplierbutton.setOnMouseClicked(event -> {
            try {
                //todo change link to supplier site
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private void menuListeners() {

        carWashMenu.setOnAction(event -> {
            goToCarwash(panel);
        });
        auditsMenu.setOnAction(event -> {
            gotoAudits(panel);
        });
        staffMenu.setOnAction(event -> {
            goToStaff(panel);
        });
        inventoryMenu.setOnAction(event -> {
            goToStocks(panel);
        });

        menuShutDown.setOnAction(event -> {
            try {
                shutdown();
            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "UNSUPPORTED OS", "YOUR OS IS UNSUPPORTED BY THIS ACTION");
            }
        });
        menuRestart.setOnAction(event -> {
            try {
                restart();
            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "UNSUPPORTED OS", "YOUR OS IS UNSUPPORTED BY THIS ACTION");
            }
        });
        menuQuit.setOnAction(event -> exit());
        details.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("UserAccountManagementFiles/adminSettings.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menulogout.setOnAction(event -> logout(panel));

    }

    private void logout() {
        logout(panel);
    }

    private void buttonListeners() {
        queryEmpTimeReport.setOnAction(event -> {
//      public DatePicker startDateEmployeeSales;
//    public DatePicker endDateEmployeeSales;
//    public Button queryEmpTimeReport;

            if (startDateEmployeeSales.getValue().toString().isEmpty() && endDateEmployeeSales.getValue().toString().isEmpty()) {
                loadCashierSalesTable();
            } else if (startDateEmployeeSales.getValue().toString().isEmpty() && !endDateEmployeeSales.getValue().toString().isEmpty()) {
//loadCashierSalesTableEnd();

            }
        });
    }


    private void loadTables() {
        loadCashiersTable();
        loadCashierSalesTable();
//        loadSpecificItemsTable();// todo v1.2
//        loadCategoricalSalesTable();// todo v1.2
//        costsTableAndInput();// todo v1.2
    }


    private void loadCashiersTable() {
        try {
            try {
                connection = new UtilityClass().getConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                        DISPLAYING EMPLOYEES
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE admin=? and status=? and activated=?");

                statement.setBoolean(1, false);
                statement.setString(2, "active");
                statement.setInt(3, 1);
//                                statement.setBoolean(2, false);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    EmployeeMaster employeeMaster = new EmployeeMaster();
                    employeeMaster.setName(resultSet.getString("employeename"));
                    employeeMaster.setEmail(resultSet.getString("email"));
                    employeeMaster.setId(resultSet.getString("id"));
                    employeeMasterObservableList.add(employeeMaster);
                }
                tableemployeelist.setItems(employeeMasterObservableList);


                assert tableemployeelist != null : "fx:id=\"tab\" was not injected: check your FXML ";
                employeename.setCellValueFactory(
                        new PropertyValueFactory<>("Name"));
                employeeid.setCellValueFactory(
                        new PropertyValueFactory<>("Id"));
                employeeemail.setCellValueFactory(new PropertyValueFactory<>("email"));
                tableemployeelist.refresh();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCashierSalesTableEnd(String end) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND timeDone<?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, end);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("transactionid"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadCashierSalesTableStart(String start) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND timeDone>?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, start);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("transactionid"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadCashierSalesTable(String start, String end) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND transactionid BETWEEN ? AND ?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, start);
                preparedStatement.setString(3, end);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("transactionid"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadCashierSalesTable() {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("transactionid"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    //    fixme template pdf generator


    public TabPane getMaintabpane() {
        return maintabpane;
    }

    public void setMaintabpane(TabPane maintabpane) {
        this.maintabpane = maintabpane;
    }

    public Tab getTabemployeeaudits() {
        return tabemployeeaudits;
    }

    public void setTabemployeeaudits(Tab tabemployeeaudits) {
        this.tabemployeeaudits = tabemployeeaudits;
    }

    public TableView<EmployeeMaster> getTableemployeelist() {
        return tableemployeelist;
    }

    public void setTableemployeelist(TableView<EmployeeMaster> tableemployeelist) {
        this.tableemployeelist = tableemployeelist;
    }

    public TableColumn<EmployeeMaster, String> getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(TableColumn<EmployeeMaster, String> employeeid) {
        this.employeeid = employeeid;
    }

    public TableColumn<EmployeeMaster, String> getEmployeename() {
        return employeename;
    }

    public void setEmployeename(TableColumn<EmployeeMaster, String> employeename) {
        this.employeename = employeename;
    }

    public TableColumn<EmployeeMaster, String> getEmployeeemail() {
        return employeeemail;
    }

    public void setEmployeeemail(TableColumn<EmployeeMaster, String> employeeemail) {
        this.employeeemail = employeeemail;
    }

    public ObservableList<EmployeeMaster> getEmployeeMasterObservableList() {
        return employeeMasterObservableList;
    }

    public void setEmployeeMasterObservableList(ObservableList<EmployeeMaster> employeeMasterObservableList) {
        this.employeeMasterObservableList = employeeMasterObservableList;
    }



    public TableView<SalesMaster> getTableemployeesales() {
        return tableemployeesales;
    }

    public void setTableemployeesales(TableView<SalesMaster> tableemployeesales) {
        this.tableemployeesales = tableemployeesales;
    }

    public TableColumn<SalesMaster, String> getEmployeetransid() {
        return employeetransid;
    }

    public void setEmployeetransid(TableColumn<SalesMaster, String> employeetransid) {
        this.employeetransid = employeetransid;
    }

    public TableColumn<SalesMaster, String> getTransprice() {
        return transprice;
    }

    public void setTransprice(TableColumn<SalesMaster, String> transprice) {
        this.transprice = transprice;
    }

    public TableColumn<SalesMaster, String> getTranspaid() {
        return transpaid;
    }

    public void setTranspaid(TableColumn<SalesMaster, String> transpaid) {
        this.transpaid = transpaid;
    }

    public TableColumn<SalesMaster, String> getTransmethod() {
        return transmethod;
    }

    public void setTransmethod(TableColumn<SalesMaster, String> transmethod) {
        this.transmethod = transmethod;
    }

    public TableColumn<SalesMaster, String> getTransbalance() {
        return transbalance;
    }

    public void setTransbalance(TableColumn<SalesMaster, String> transbalance) {
        this.transbalance = transbalance;
    }

    public TableColumn<SalesMaster, String> getTranscompletion() {
        return transcompletion;
    }

    public void setTranscompletion(TableColumn<SalesMaster, String> transcompletion) {
        this.transcompletion = transcompletion;
    }

    public ObservableList<SalesMaster> getSalesMasterObservableList() {
        return salesMasterObservableList;
    }

    public void setSalesMasterObservableList(ObservableList<SalesMaster> salesMasterObservableList) {
        this.salesMasterObservableList = salesMasterObservableList;
    }


    public Tab getTabsalesaudits() {
        return tabsalesaudits;
    }

    public void setTabsalesaudits(Tab tabsalesaudits) {
        this.tabsalesaudits = tabsalesaudits;
    }

    public Tab getTabstockalerts() {
        return tabstockalerts;
    }

    public void setTabstockalerts(Tab tabstockalerts) {
        this.tabstockalerts = tabstockalerts;
    }

    public TableView getStockalerttable() {
        return stockalerttable;
    }

    public void setStockalerttable(TableView stockalerttable) {
        this.stockalerttable = stockalerttable;
    }

    public TableColumn getStockalerttableid() {
        return stockalerttableid;
    }

    public void setStockalerttableid(TableColumn stockalerttableid) {
        this.stockalerttableid = stockalerttableid;
    }

    public TableColumn getStockalerttablename() {
        return stockalerttablename;
    }

    public void setStockalerttablename(TableColumn stockalerttablename) {
        this.stockalerttablename = stockalerttablename;
    }

    public TableColumn getStockalerttabledate() {
        return stockalerttabledate;
    }

    public void setStockalerttabledate(TableColumn stockalerttabledate) {
        this.stockalerttabledate = stockalerttabledate;
    }

    public TableColumn getStockalerttablemarkasread() {
        return stockalerttablemarkasread;
    }

    public void setStockalerttablemarkasread(TableColumn stockalerttablemarkasread) {
        this.stockalerttablemarkasread = stockalerttablemarkasread;
    }

    public Tab getTaballaudits() {
        return taballaudits;
    }

    public void setTaballaudits(Tab taballaudits) {
        this.taballaudits = taballaudits;
    }

    public Button getExportfullreport() {
        return exportfullreport;
    }

    public void setExportfullreport(Button exportfullreport) {
        this.exportfullreport = exportfullreport;
    }

    public Button getExportcategoryreport() {
        return exportcategoryreport;
    }

    public void setExportcategoryreport(Button exportcategoryreport) {
        this.exportcategoryreport = exportcategoryreport;
    }

    public Button getExportemployeereport() {
        return exportemployeereport;
    }

    public void setExportemployeereport(Button exportemployeereport) {
        this.exportemployeereport = exportemployeereport;
    }

    public Button getGetdetailedgraph() {
        return getdetailedgraph;
    }

    public void setGetdetailedgraph(Button getdetailedgraph) {
        this.getdetailedgraph = getdetailedgraph;
    }

    public Button getGetcateegorygraph() {
        return getcateegorygraph;
    }

    public void setGetcateegorygraph(Button getcateegorygraph) {
        this.getcateegorygraph = getcateegorygraph;
    }

    public Button getGetemployeegraph() {
        return getemployeegraph;
    }

    public void setGetemployeegraph(Button getemployeegraph) {
        this.getemployeegraph = getemployeegraph;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }


    public AnchorPane getPanel() {
        return panel;
    }

    public void setPanel(AnchorPane panel) {
        this.panel = panel;
    }

    public Button getTopanelbutton() {
        return topanelbutton;
    }

    public void setTopanelbutton(Button topanelbutton) {
        this.topanelbutton = topanelbutton;
    }

    public Button getTocarwashbutton() {
        return tocarwashbutton;
    }

    public void setTocarwashbutton(Button tocarwashbutton) {
        this.tocarwashbutton = tocarwashbutton;
    }

    public Button getToemployeesbutton() {
        return toemployeesbutton;
    }

    public void setToemployeesbutton(Button toemployeesbutton) {
        this.toemployeesbutton = toemployeesbutton;
    }

    public Button getLogoutbutton() {
        return logoutbutton;
    }

    public void setLogoutbutton(Button logoutbutton) {
        this.logoutbutton = logoutbutton;
    }

    public Button getTosupplierbutton() {
        return tosupplierbutton;
    }

    public void setTosupplierbutton(Button tosupplierbutton) {
        this.tosupplierbutton = tosupplierbutton;
    }


    @Override
    public AuditController setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }
}
