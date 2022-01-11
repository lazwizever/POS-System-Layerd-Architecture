package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemBO extends SuperBO {

    ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException;

    ObservableList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

    List<String> getAllItemIds() throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO items) throws SQLException, ClassNotFoundException;

    boolean addItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String id) throws SQLException, ClassNotFoundException;

    public String generateItemId() throws SQLException, ClassNotFoundException;
}
