package lk.ijse.pos_system.repository.custom;

import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.repository.CrudDAO;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item,String> {
    List<String> getAllItemIds() throws SQLException, ClassNotFoundException;

    boolean updateStock(String itemCode, int OrderQty)throws SQLException, ClassNotFoundException;

    public String generateItemId() throws SQLException, ClassNotFoundException;
}
