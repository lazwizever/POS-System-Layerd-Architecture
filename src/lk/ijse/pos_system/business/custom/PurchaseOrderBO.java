package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.OrderDTO;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;

public interface PurchaseOrderBO extends SuperBO {
    String generateOrderId() throws SQLException, ClassNotFoundException;

    ObservableList<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String cid) throws SQLException, ClassNotFoundException;

    boolean addCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;

    List<String> getAllItemIds() throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemId) throws SQLException, ClassNotFoundException;

    boolean addOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;

    String generateCustomerId() throws SQLException, ClassNotFoundException;

}
