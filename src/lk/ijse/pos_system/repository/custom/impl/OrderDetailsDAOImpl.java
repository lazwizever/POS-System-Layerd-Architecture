package lk.ijse.pos_system.repository.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.OrderDetailsDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public boolean add(OrderDetail itemDetailsDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Order Detail` VALUES(?,?,?,?)", itemDetailsDTO.getOrderId(), itemDetailsDTO.getItemCode(), itemDetailsDTO.getOderQTY(), itemDetailsDTO.getDiscount());
    }

    @Override
    public boolean delete(String orderId) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(OrderDetail itemDetailsDTO) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public OrderDetail search(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ObservableList<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    public ArrayList<OrderDetail> getOrderItems(String oId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order Detail` WHERE OrderId = ?", oId);
        ArrayList<OrderDetail> orderItems = new ArrayList();
        while (resultSet.next()){
            OrderDetail orderItem = new OrderDetail(
                    resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getDouble(4)

            );
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    public boolean deleteItemsOfAnOrder(String oId) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM `Order Detail` WHERE OrderId = ?", oId);
    }


    //-----------------------add method-------------------------------------

    /*public boolean addOrderIemDetails(ArrayList<ItemDetailsDTO> itemDetailDTOS) throws SQLException, ClassNotFoundException {
        for (ItemDetailsDTO temp: itemDetailDTOS
             ) {
            *//*PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Order Detail` VALUES(?,?,?,?)");
            preparedStatement.setObject(1,temp.getOrderId());
            preparedStatement.setObject(2,temp.getItemCode());
            preparedStatement.setObject(3,temp.getOrderQty());
            preparedStatement.setObject(4,temp.getDiscount());*//*
            boolean b = CrudUtil.executeUpdate("INSERT INTO `Order Detail` VALUES(?,?,?,?)", temp.getOrderId(), temp.getItemCode(), temp.getOrderQty(), temp.getDiscount());
            if (b==true){
                if (new OrderDAOImpl().updateOrderQty(temp.getItemCode(),temp.getOrderQty())){
                    continue;
                }else{
                 return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    //-----------------------delete item method-----------------------------

    public boolean removeItems(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<ItemDetailsDTO> orderItemDetailDTOS = getOrderItems(orderId);
       *//* PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `Order Detail` WHERE OrderId = ?");
        preparedStatement.setObject(1,orderId);*//*
        boolean b = CrudUtil.executeUpdate("DELETE FROM `Order Detail` WHERE OrderId = ?", orderId);

        if (b==true){
            for (ItemDetailsDTO temp: orderItemDetailDTOS
                 ) {
                if (new OrderDAOImpl().updateOrderQty(temp.getItemCode(),temp.getOrderQty()*-1)){
                    continue;
                }else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
*/
}
