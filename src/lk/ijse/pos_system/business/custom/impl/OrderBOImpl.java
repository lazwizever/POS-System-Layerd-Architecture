package lk.ijse.pos_system.business.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.OrderBO;
import lk.ijse.pos_system.db.DbConnection;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.entity.OrderDetail;
import lk.ijse.pos_system.repository.DAOFactory;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.repository.custom.OrderDetailsDAO;
import lk.ijse.pos_system.repository.custom.impl.CustomerDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.ItemDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDetailsDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<ItemDetailsDTO> getOrderItems(String oId) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> orders = orderDetailsDAO.getOrderItems(oId);
        ArrayList<ItemDetailsDTO> orderItems = new ArrayList<>();
        for (OrderDetail order : orders) {
            orderItems.add(new ItemDetailsDTO(order.getOrderId(),order.getItemCode(),order.getOderQTY(),order.getDiscount()));
        }
        return orderItems;
    }

    @Override
    public ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(itemCode);
        ItemDTO itemDTO = new ItemDTO(item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand());
        return itemDTO;
    }

    @Override
    public List<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIDs();
    }

    @Override
    public ObservableList<String> getOrderIdForCustomer(String customerId) throws SQLException, ClassNotFoundException {
       return orderDAO.getOrderIdForCustomer(customerId);
    }

    @Override
    public boolean deleteOrder(String id) throws SQLException, ClassNotFoundException {
        return orderDAO.delete(id);
    }

    public boolean editOrder(String oId,ArrayList<ItemDetailsDTO> newOrderItems){
        Connection connection = null;

        //---------------Start Transaction---------------------------//
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<OrderDetail> oldOrderItems = orderDetailsDAO.getOrderItems(oId);

            //-------------------Delete Old order Items----------------------
            if (orderDetailsDAO.deleteItemsOfAnOrder(oId)){
                L1:for (OrderDetail temp : oldOrderItems) {
                    if (itemDAO.updateStock(temp.getItemCode(),temp.getOderQTY()*-1)){
                      continue L1;
                    }else {
                        connection.rollback();
                        return false;
                    }
                }

                //-------------------Add new order Items----------------------
                L2:for (ItemDetailsDTO orderItem : newOrderItems) {
                    OrderDetail orderDetail = new OrderDetail(orderItem.getOrderId(),orderItem.getItemCode(),orderItem.getOrderQty(),orderItem.getDiscount());

                    if ( orderDetailsDAO.add(orderDetail)){
                        if (itemDAO.updateStock(orderItem.getItemCode(),orderItem.getOrderQty())){
                            continue L2;
                        }else {
                            connection.rollback();
                            return false;
                        }

                    }else{
                        connection.rollback();
                        return false;
                    }
                }
                connection.commit();
                return true;
            }else {
                connection.rollback();
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }
}
