package MasterClasses;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemStocksMaster extends StockMaster {
    //use name,id and amount from superclass
    private SimpleStringProperty payout = new SimpleStringProperty();
    private SimpleDoubleProperty salesperday = new SimpleDoubleProperty();

    public String getPayout() {
        return payout.get();
    }

    public SimpleStringProperty payoutProperty() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout.set(payout);
    }

    public double getSalesperday() {
        return salesperday.get();
    }

    public SimpleDoubleProperty salesperdayProperty() {
        return salesperday;
    }

    public void setSalesperday(double salesperday) {
        this.salesperday.set(salesperday);
    }
}
