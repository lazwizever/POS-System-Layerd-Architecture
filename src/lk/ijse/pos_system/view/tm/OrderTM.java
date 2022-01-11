package lk.ijse.pos_system.view.tm;

public class OrderTM {
    private String itemCode;
    private String description;
    private int orderQty;
    private double unitPrice;

    public OrderTM() {
    }

    public OrderTM(String itemCode, String description, int orderQty, double unitPrice) {
        this.setItemCode(itemCode);
        this.setDescription(description);
        this.setOrderQty(orderQty);
        this.setUnitPrice(unitPrice);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
