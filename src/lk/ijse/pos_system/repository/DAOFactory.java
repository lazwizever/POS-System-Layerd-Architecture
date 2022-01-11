package lk.ijse.pos_system.repository;

import lk.ijse.pos_system.repository.custom.impl.CustomerDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.ItemDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDetailsDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory(){
    }

    public static DAOFactory getDaoFactory(){
        if (daoFactory==null){
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public SuperDAO getDAO(DAOTypes types){
        switch (types){
            case ORDER:
                return new OrderDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ORDER_DETAIL:
                return new OrderDetailsDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOTypes{
        ITEM,ORDER,CUSTOMER,ORDER_DETAIL
    }
}
