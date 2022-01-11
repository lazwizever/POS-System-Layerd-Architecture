package lk.ijse.pos_system.business.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.PurchaseOrderBO;
import lk.ijse.pos_system.db.DbConnection;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.entity.Customer;
import lk.ijse.pos_system.entity.Item;
import lk.ijse.pos_system.entity.Order;
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

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);


    @Override
    public String generateOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateOrderId();
    }

    @Override
    public ObservableList<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIDs();
    }

    @Override
    public CustomerDTO searchCustomer(String cid) throws SQLException, ClassNotFoundException {
        Customer customer = customerDAO.search(cid);
        CustomerDTO customerDTO = new CustomerDTO(customer.getCustId(),customer.getCustTitle(),customer.getCustName(),customer.getCustAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode());
        return customerDTO;
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(customerDTO.getCustomerId(),customerDTO.getCustomerTitle(),customerDTO.getCustomerName(),customerDTO.getCustomerAddress(),customerDTO.getCity(),customerDTO.getProvince(),customerDTO.getPostalCode());
        return customerDAO.add(customer);
    }

    @Override
    public List<String> getAllItemIds() throws SQLException, ClassNotFoundException {
       return itemDAO.getAllItemIds();
    }

    @Override
    public ItemDTO searchItem(String itemId) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(itemId);
        ItemDTO itemDTO = new ItemDTO(item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand());
        return itemDTO;
    }

    @Override
    public boolean addOrder(OrderDTO orderDTO) {
        Order order = new Order(orderDTO.getOrderId(),orderDTO.getOrderDate(),orderDTO.getCustomerId());
        ArrayList<ItemDetailsDTO> orders = orderDTO.getOrders();
        ArrayList<OrderDetail> orderItems = new ArrayList<>();

        for (ItemDetailsDTO itemDetailsDTO : orders) {
            orderItems.add(new OrderDetail(itemDetailsDTO.getOrderId(),itemDetailsDTO.getItemCode(),itemDetailsDTO.getOrderQty(),itemDetailsDTO.getDiscount()));
        }

        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            if (orderDAO.add(order)){
                L1:for (OrderDetail orderItem : orderItems) {
                    if (orderDetailsDAO.add(orderItem)){
                        if (itemDAO.updateStock(orderItem.getItemCode(),orderItem.getOderQTY())){
                            continue L1;
                        }else {
                            connection.rollback();
                            return false;
                        }
                    }else {
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

    @Override
    public String generateCustomerId() throws SQLException, ClassNotFoundException {
        return customerDAO.generateCustomerId();
    }
}
