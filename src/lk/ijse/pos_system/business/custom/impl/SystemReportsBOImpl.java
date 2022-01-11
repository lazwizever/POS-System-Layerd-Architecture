package lk.ijse.pos_system.business.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.custom.SystemReportsBO;
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
import lk.ijse.pos_system.view.tm.CustomerReportTm;

import java.sql.SQLException;
import java.util.ArrayList;

public class SystemReportsBOImpl implements SystemReportsBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);


    @Override
    public ObservableList<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIDs();
    }

    @Override
    public ObservableList<OrderDTO> getOrderFromDate(String date) throws SQLException, ClassNotFoundException {
        ObservableList<Order> orders = orderDAO.getOrderFromDate(date);
        ObservableList<OrderDTO> orderFromDate = FXCollections.observableArrayList();

        for (Order order : orders) {

            ArrayList<OrderDetail> orderItems = orderDetailsDAO.getOrderItems(order.getOrderId());
            ArrayList<ItemDetailsDTO> orderItemDetail = new ArrayList<>();

            //----------------------Add To ItemDetailsDTO type Array-------------------
            for (OrderDetail orderItem : orderItems) {
                orderItemDetail.add(new ItemDetailsDTO(orderItem.getOrderId(),orderItem.getItemCode(),orderItem.getOderQTY(),orderItem.getDiscount()));
            }
            orderFromDate.add(new OrderDTO(order.getOrderId(),order.getOrderDate(),order.getCustId(),orderItemDetail));
        }
        return orderFromDate;
    }

    @Override
    public ArrayList<ItemDetailsDTO> getOrderItems(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> orderItems = orderDetailsDAO.getOrderItems(orderId);
        ArrayList<ItemDetailsDTO> orders = new ArrayList<>();
        for (OrderDetail orderItem : orderItems) {
            orders.add(new ItemDetailsDTO(orderItem.getOrderId(),orderItem.getItemCode(),orderItem.getOderQTY(),orderItem.getDiscount()));
        }
        return orders;
    }

    @Override
    public ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(itemCode);
        ItemDTO itemDTO = new ItemDTO(item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand());
        return itemDTO;
    }

    @Override
    public CustomerDTO searchCustomer(String custId) throws SQLException, ClassNotFoundException {
        Customer customer = customerDAO.search(custId);
        CustomerDTO customerDTO = new CustomerDTO(customer.getCustId(),customer.getCustTitle(),customer.getCustName(),customer.getCustAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode());
        return customerDTO;
    }

    @Override
    public ObservableList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ObservableList<Item> all = itemDAO.getAll();
        ObservableList<ItemDTO> allItem = FXCollections.observableArrayList();

        for (Item item : all) {
            allItem.add(new ItemDTO(item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getDiscount(),item.getQtyOnHand()));
        }
        return allItem;
    }

    @Override
    public ObservableList<OrderDTO> getOrderFromMonth(String year, String month) throws SQLException, ClassNotFoundException {
        ObservableList<Order> orderMonth = orderDAO.getOrderFromMonth(year, month);
        ObservableList<OrderDTO> orderFromMonth = FXCollections.observableArrayList();

        for (Order order : orderMonth) {
            ArrayList<ItemDetailsDTO> items = getOrderItems(order.getOrderId());
            orderFromMonth.add(new OrderDTO(order.getOrderId(),order.getOrderDate(),order.getCustId(),items));
        }
        return orderFromMonth;
    }

    @Override
    public ObservableList<OrderDTO> getOrderFromYear(String year) throws SQLException, ClassNotFoundException {
        ObservableList<Order> orderYear = orderDAO.getOrderFromYear(year);
        ObservableList<OrderDTO> orderFromYear = FXCollections.observableArrayList();
        for (Order temp : orderYear) {
            ArrayList<ItemDetailsDTO> items = getOrderItems(temp.getOrderId());
            orderFromYear.add(new OrderDTO(temp.getOrderId(),temp.getOrderDate(),temp.getCustId(),items));
        }
        return orderFromYear;
    }

    @Override
    public ObservableList<String> getOrderSeason(String from, String to) throws SQLException, ClassNotFoundException {
       return orderDAO.getOrderSeason(from,to);
    }
}
