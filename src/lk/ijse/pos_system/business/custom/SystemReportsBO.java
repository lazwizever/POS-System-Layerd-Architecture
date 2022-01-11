package lk.ijse.pos_system.business.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.view.tm.CustomerReportTm;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SystemReportsBO extends SuperBO {
    ObservableList<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException;

    ObservableList<OrderDTO> getOrderFromDate(String date) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDetailsDTO> getOrderItems(String orderId) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemCode) throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String custId) throws SQLException, ClassNotFoundException;

    ObservableList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

    ObservableList<OrderDTO> getOrderFromMonth(String year, String month) throws SQLException, ClassNotFoundException;

    ObservableList<OrderDTO> getOrderFromYear(String year) throws SQLException, ClassNotFoundException;

    ObservableList<String> getOrderSeason(String from, String to) throws SQLException, ClassNotFoundException;
}
