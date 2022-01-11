package lk.ijse.pos_system.business.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.ItemBO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.repository.DAOFactory;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.impl.ItemDAOImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(itemCode);

        ItemDTO itemDTO = new ItemDTO(
                item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand()
        );
        return itemDTO;
    }

    @Override
    public ObservableList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ObservableList<Item> allItems = itemDAO.getAll();
        ObservableList<ItemDTO> items = FXCollections.observableArrayList();
        for (Item temp : allItems) {
            items.addAll(new ItemDTO(temp.getItemCode(),temp.getDescription(),temp.getPackSize(),temp.getUnitPrice(),temp.getDiscount(),temp.getQtyOnHand()));
        }
       return items;
    }

    @Override
    public List<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        return itemDAO.getAllItemIds();
    }

    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        Item item = new Item(dto.getItemCode(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQyOnHand());
        return itemDAO.update(item);
    }

    @Override
    public boolean addItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        Item item = new Item(dto.getItemCode(),dto.getDescription(),dto.getPackSize(),dto.getUnitPrice(),dto.getDiscount(),dto.getQyOnHand());
        return itemDAO.add(item);
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(id);
    }

    public String generateItemId() throws SQLException, ClassNotFoundException {
        return itemDAO.generateItemId();
    }
}
