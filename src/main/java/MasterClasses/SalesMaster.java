package MasterClasses;

import javafx.beans.property.SimpleStringProperty;

public class SalesMaster {
    protected SimpleStringProperty employeetransid = new SimpleStringProperty();
    protected SimpleStringProperty transprice = new SimpleStringProperty();
    protected SimpleStringProperty transpaid = new SimpleStringProperty();
    protected SimpleStringProperty transmethod = new SimpleStringProperty();
    protected SimpleStringProperty transbalance = new SimpleStringProperty();
    protected SimpleStringProperty transcompletion = new SimpleStringProperty();

    public String getEmployeetransid() {
        return employeetransid.get();
    }

    public void setEmployeetransid(String employeetransid) {
        this.employeetransid.set(employeetransid);
    }

    public SimpleStringProperty employeetransidProperty() {
        return employeetransid;
    }

    public String getTransprice() {
        return transprice.get();
    }

    public void setTransprice(String transprice) {
        this.transprice.set(transprice);
    }

    public SimpleStringProperty transpriceProperty() {
        return transprice;
    }

    public String getTranspaid() {
        return transpaid.get();
    }

    public void setTranspaid(String transpaid) {
        this.transpaid.set(transpaid);
    }

    public SimpleStringProperty transpaidProperty() {
        return transpaid;
    }

    public String getTransmethod() {
        return transmethod.get();
    }

    public void setTransmethod(String transmethod) {
        this.transmethod.set(transmethod);
    }

    public SimpleStringProperty transmethodProperty() {
        return transmethod;
    }

    public String getTransbalance() {
        return transbalance.get();
    }

    public void setTransbalance(String transbalance) {
        this.transbalance.set(transbalance);
    }

    public SimpleStringProperty transbalanceProperty() {
        return transbalance;
    }

    public String getTranscompletion() {
        return transcompletion.get();
    }

    public void setTranscompletion(String transcompletion) {
        this.transcompletion.set(transcompletion);
    }

    public SimpleStringProperty transcompletionProperty() {
        return transcompletion;
    }
}
