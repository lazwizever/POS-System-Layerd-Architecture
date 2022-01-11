package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.ItemDetailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OrderBO extends SuperBO {
    ArrayList<ItemDetailsDTO> getOrderItems(String oId) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException;

    List<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException;

    ObservableList<String> getOrderIdForCustomer(String customerId) throws SQLException, ClassNotFoundException;

    boolean deleteOrder(String id) throws SQLException, ClassNotFoundException;

    public boolean editOrder(String oId,ArrayList<ItemDetailsDTO> newOrderItems);
}
