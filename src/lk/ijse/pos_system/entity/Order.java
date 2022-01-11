package lk.ijse.pos_system.entity;

public class Order {
    private String OrderId;
    private String OrderDate;
    private String CustId;

    public Order() {
    }

    public Order(String orderId, String orderDate, String custId) {
        setOrderId(orderId);
        setOrderDate(orderDate);
        setCustId(custId);
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }
}
