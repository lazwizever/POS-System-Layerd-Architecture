package lk.ijse.pos_system.repository;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.repository.custom.CustomerDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T,ID> extends SuperDAO{
    boolean add(T t) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    boolean update(T t) throws SQLException, ClassNotFoundException;

    T search(ID id) throws SQLException, ClassNotFoundException;

    ObservableList<T> getAll() throws SQLException, ClassNotFoundException;
}
