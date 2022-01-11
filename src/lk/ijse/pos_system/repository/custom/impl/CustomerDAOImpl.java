package lk.ijse.pos_system.repository.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.entity.Customer;
import lk.ijse.pos_system.repository.CrudUtil;
import lk.ijse.pos_system.repository.custom.CustomerDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAOImpl implements CustomerDAO {

    public ObservableList<String> getAllCustomerIDs() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM Customer");
        ObservableList<String> cIds = FXCollections.observableArrayList();
        while (resultSet.next()){
            cIds.add(resultSet.getString(1));
        }
        return cIds;
    }

    @Override
    public String generateCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT CustId FROM Customer ORDER BY CustId DESC LIMIT 1");
        if (resultSet.next()) {
            int tempId = Integer.
                    parseInt(resultSet.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId < 10) {
                return "C-00" + tempId;
            } else if (tempId < 100) {
                return "C-0" + tempId;
            } else {
                return "C-" + tempId;
            }
        } else {
            return "C-001";
        }
    }

    public Customer search(String cid) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM Customer WHERE CustId = ?", cid);
        if (resultSet.next()){
            Customer customerDTO = new Customer(
                    resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)
            );
            return customerDTO;
        }else {
            return null;
        }
    }

    @Override
    public ObservableList<Customer> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    public boolean add(Customer dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Customer VALUES (?,?,?,?,?,?,?) ",dto.getCustId(),dto.getCustTitle(),dto.getCustName(),dto.getCustAddress(),dto.getCity(),dto.getProvince(),dto.getPostalCode());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public boolean update(Customer customerDTO) throws SQLException, ClassNotFoundException {
        throw  new UnsupportedOperationException("Not Supported Yet");
    }

}

