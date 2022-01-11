package lk.ijse.pos_system.entity;

public class OrderDetail {
    private String OrderId;
    private String ItemCode;
    private int OderQTY;
    private double Discount;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemCode, int oderQTY, double discount) {
        setOrderId(orderId);
        setItemCode(itemCode);
        setOderQTY(oderQTY);
        setDiscount(discount);
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getOderQTY() {
        return OderQTY;
    }

    public void setOderQTY(int oderQTY) {
        OderQTY = oderQTY;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }
}
