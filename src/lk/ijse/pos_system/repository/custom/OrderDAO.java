package lk.ijse.pos_system.repository.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.entity.Order;
import lk.ijse.pos_system.repository.CrudDAO;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order,String> {
    ObservableList<String> getOrderIdForCustomer(String customerId) throws SQLException, ClassNotFoundException;

    public String generateOrderId() throws SQLException, ClassNotFoundException;

    public ObservableList<Order> getOrderFromDate(String date) throws SQLException, ClassNotFoundException;

    public ObservableList<Order> getOrderFromMonth(String year, String month) throws SQLException, ClassNotFoundException;

    public ObservableList<Order> getOrderFromYear(String year) throws SQLException, ClassNotFoundException;

    public ObservableList<String> getOrderSeason(String dateFrom, String dateTo) throws SQLException, ClassNotFoundException;
}
