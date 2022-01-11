package lk.ijse.pos_system.repository.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.ItemDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item itemDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Item VALUES(?,?,?,?,?,?)", itemDTO.getItemCode(), itemDTO.getDescription(), itemDTO.getPackSize(), itemDTO.getUnitPrice(), itemDTO.getDiscount(), itemDTO.getQtyOnHand());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM Item WHERE ItemCode = ?",id);
    }

    @Override
    public boolean update(Item itemDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE Item SET Description = ?,PackSize = ?,UnitPrice = ?,Discount = ?,QtyOnHand = ? WHERE ItemCode = ?",itemDTO.getDescription(),itemDTO.getPackSize(),itemDTO.getUnitPrice(),itemDTO.getDiscount(),itemDTO.getQtyOnHand(),itemDTO.getItemCode());
    }

    @Override
    public Item search(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM Item WHERE ItemCode = ?", code);
        if (resultSet.next()){
            Item itemDTO = new Item(
                    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getInt(6)
            );
            return itemDTO;
        }else{
            return null;
        }
    }

    @Override
    public ObservableList<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM Item");
        ObservableList<Item> itemDTOData = FXCollections.observableArrayList();
        while (resultSet.next()) {
            itemDTOData.add(new Item(resultSet.getString(1),
                    resultSet.getString(2), resultSet.getString(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getInt(6)));
        }
        return itemDTOData;
    }

    @Override
    public List<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT ItemCode FROM Item");
        List<String> itemIds = new ArrayList<>();
        while (resultSet.next()){
            itemIds.add(resultSet.getString(1));
        }
        return itemIds;
    }

    @Override
    public boolean updateStock(String itemCode, int OrderQty) throws SQLException, ClassNotFoundException {
        int qyOnHand = search(itemCode).getQtyOnHand();
        return CrudUtil.executeUpdate("UPDATE Item SET QtyOnHand = ? WHERE ItemCode = ?",qyOnHand-OrderQty,itemCode);
    }

    public String generateItemId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT ItemCode FROM Item ORDER BY ItemCode DESC LIMIT 1");
        if (resultSet.next()) {
            int tempId = Integer.
                    parseInt(resultSet.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId < 10) {
                return "I-00" + tempId;
            } else if (tempId < 100) {
                return "I-0" + tempId;
            } else {
                return "I-" + tempId;
            }
        } else {
            return "I-001";
        }
    }



}
