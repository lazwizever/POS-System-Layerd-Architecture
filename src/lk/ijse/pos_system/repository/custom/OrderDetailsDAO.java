package lk.ijse.pos_system.repository.custom;

import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.CrudDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailsDAO extends CrudDAO<OrderDetail,String> {
    public ArrayList<OrderDetail> getOrderItems(String oId) throws SQLException, ClassNotFoundException;

    public boolean deleteItemsOfAnOrder(String oId) throws SQLException, ClassNotFoundException;

}
