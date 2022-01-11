package lk.ijse.pos_system.repository;

import lk.ijse.pos_system.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    private static PreparedStatement getPrepareStatement(String sql,Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i+1,args[i]);
        }
        return preparedStatement;
    }

    public static boolean executeUpdate(String sql,Object... args) throws SQLException, ClassNotFoundException {
        return getPrepareStatement(sql,args).executeUpdate()>0;
    }

    public static ResultSet executeQuery(String sql, Object... args) throws SQLException, ClassNotFoundException {
        return getPrepareStatement(sql,args).executeQuery();
    }


}
