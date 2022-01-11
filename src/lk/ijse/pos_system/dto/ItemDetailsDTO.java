package lk.ijse.pos_system.dto;

public class ItemDetailsDTO {
    private String orderId;
    private String itemCode;
    private int orderQty;
    private double discount;

    public ItemDetailsDTO() {
    }

    public ItemDetailsDTO(String orderId, String itemCode, int orderQty, double discount) {
        this.setOrderId(orderId);
        this.setItemCode(itemCode);
        this.setOrderQty(orderQty);
        this.setDiscount(discount);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
