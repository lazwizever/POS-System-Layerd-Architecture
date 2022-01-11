package lk.ijse.pos_system.repository.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.entity.Order;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.OrderDAO;

import java.sql.*;
import java.util.ArrayList;


public class OrderDAOImpl implements OrderDAO {
    @Override
    public boolean add(Order orderDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO `Order` VALUES(?,?,?)",orderDTO.getOrderId(),orderDTO.getOrderDate(),orderDTO.getCustId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("DELETE FROM `Order` WHERE OrderId = ?",id);
    }

    @Override
    public boolean update(Order orderDTO) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public Order search(String orderId) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ObservableList<Order> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ObservableList<String> getOrderIdForCustomer(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE CustId = ?", customerId);
        ObservableList<String> orderIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            orderIds.add(resultSet.getString(1));
        }
        return orderIds;
    }

    public String generateOrderId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT OrderId FROM `Order` ORDER BY OrderId DESC LIMIT 1");
        if (resultSet.next()) {
            int tempId = Integer.
                    parseInt(resultSet.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId < 10) {
                return "O-00" + tempId;
            } else if (tempId < 100) {
                return "O-0" + tempId;
            } else {
                return "O-" + tempId;
            }
        } else {
            return "O-001";
        }
    }

    //----------------For Income Reports---------------------------------------

    public ObservableList<Order> getOrderFromDate(String date) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE OrderDate = ?", date);
        ObservableList<Order> orderDTOIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Order orderDTO = new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            orderDTOIds.add(orderDTO);
        }
        return orderDTOIds;
    }

    public ObservableList<Order> getOrderFromMonth(String year, String month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE OrderDate BETWEEN ? AND ?", year+"-"+month+"-01",year+"-"+month+"-31");
        ObservableList<Order> orderDTOIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Order orderDTO = new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            orderDTOIds.add(orderDTO);
        }
        return orderDTOIds;
    }

    public ObservableList<Order> getOrderFromYear(String year) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE OrderDate BETWEEN ? AND ?", year+"-01-01",year+"-12-31");
        ObservableList<Order> orderDTOIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            Order orderDTO = new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            orderDTOIds.add(orderDTO);
        }
        return orderDTOIds;
    }

    public ObservableList<String> getOrderSeason(String dateFrom, String dateTo) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM `Order` WHERE OrderDate BETWEEN ? AND ?", dateFrom,dateTo);
        ObservableList<String> orderIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            orderIds.add(resultSet.getString(1));
        }
        return orderIds;
    }


    /*public boolean addOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Order` VALUES(?,?,?)");
        preparedStatement.setObject(1, orderDTO.getOrderId());
        preparedStatement.setObject(2, orderDTO.getOrderDate());
        preparedStatement.setObject(3, orderDTO.getCustomerId());

        if (preparedStatement.executeUpdate() > 0) {
            if (addOrderItemDetails(orderDTO.getOrders())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //-------------Use Transaction---------------------------

    private boolean addOrderItemDetails(ArrayList<ItemDetailsDTO> itemDetailDTOS) throws SQLException, ClassNotFoundException {
        for (ItemDetailsDTO temp : itemDetailDTOS
        ) {
            Connection connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `Order Detail` VALUES (?,?,?,?) ");
            preparedStatement.setObject(1, temp.getOrderId());
            preparedStatement.setObject(2, temp.getItemCode());
            preparedStatement.setObject(3, temp.getOrderQty());
            preparedStatement.setObject(4, temp.getDiscount());

            try {
                if (preparedStatement.executeUpdate() > 0) {
                    if (updateOrderQty(temp.getItemCode(), temp.getOrderQty())) {
                        connection.commit();
                        continue;
                    } else {
                        connection.rollback();
                        return false;
                    }
                } else {
                    connection.rollback();
                    return false;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        }
        return true;
    }
    //----------------------------------------------------------------------------------

    public boolean updateOrderQty(String id, int orderQty) throws SQLException, ClassNotFoundException {
        int qyOnHand = new ItemDAOImpl().search(id).getQyOnHand();

       *//* PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Item SET QtyOnHand = ? WHERE ItemCode = ?");
        preparedStatement.setObject(1, qyOnHand - orderQty);
        preparedStatement.setObject(2, id);
        return preparedStatement.executeUpdate() > 0;*//*
        return CrudUtil.executeUpdate("UPDATE Item SET QtyOnHand = ? WHERE ItemCode = ?",id,orderQty);
    }

    public boolean removeOrder(String oId) throws SQLException, ClassNotFoundException {
        ArrayList<ItemDetailsDTO> orderItemDetailDTOS = new OrderDetailsDAOImpl().getOrderItems(oId);

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `Order` WHERE OrderId = ?");
        preparedStatement.setObject(1, oId);

        if (preparedStatement.executeUpdate() > 0) {
            for (ItemDetailsDTO temp : orderItemDetailDTOS
            ) {
                if (updateOrderQty(temp.getItemCode(), (temp.getOrderQty() * -1))) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }*/
}

