package lk.ijse.pos_system.view.tm;

public class CartTM {
    private String itemCode;
    private String description;
    private int customerQty;
    private double unitPrice;
    private double discount;
    private double total;

    public CartTM() {
    }

    public CartTM(String itemCode, String description, int customerQty, double unitPrice, double discount, double total) {
        this.setItemCode(itemCode);
        this.setDescription(description);
        this.setCustomerQty(customerQty);
        this.setUnitPrice(unitPrice);
        this.setDiscount(discount);
        this.setTotal(total);
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

    public int getCustomerQty() {
        return customerQty;
    }

    public void setCustomerQty(int customerQty) {
        this.customerQty = customerQty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
