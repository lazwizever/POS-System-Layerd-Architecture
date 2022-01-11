package lk.ijse.pos_system.dto;

import java.util.ArrayList;

public class OrderDTO {
    private String orderId;
    private String orderDate;
    private String customerId;
    private ArrayList<ItemDetailsDTO> orders;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String orderDate, String customerId, ArrayList<ItemDetailsDTO> orders) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setCustomerId(customerId);
        this.setOrders(orders);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<ItemDetailsDTO> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<ItemDetailsDTO> orders) {
        this.orders = orders;
    }
}
