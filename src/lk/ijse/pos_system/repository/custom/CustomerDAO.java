package lk.ijse.pos_system.repository.custom;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.entity.Customer;
import lk.ijse.pos_system.repository.CrudDAO;

import java.sql.SQLException;

public interface CustomerDAO extends CrudDAO<Customer,String> {
    public ObservableList<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException;

    String generateCustomerId() throws SQLException, ClassNotFoundException;
}
