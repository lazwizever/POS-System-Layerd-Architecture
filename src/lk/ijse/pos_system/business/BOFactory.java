package lk.ijse.pos_system.business;

import lk.ijse.pos_system.business.custom.impl.ItemBOImpl;
import lk.ijse.pos_system.business.custom.impl.OrderBOImpl;
import lk.ijse.pos_system.business.custom.impl.PurchaseOrderBOImpl;
import lk.ijse.pos_system.business.custom.impl.SystemReportsBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getBOFactory(){
        if (boFactory==null){
            boFactory = new BOFactory();
        }
            return boFactory;
    }

    public SuperBO getBO(BOTypes types){
        switch (types){
            case ITEM:
                return new ItemBOImpl();
            case ORDER:
                return new OrderBOImpl();
            case PURCHASE_ORDER:
                return new PurchaseOrderBOImpl();
            case SYSTEM_REPORTS:
                return new SystemReportsBOImpl();
            default:
                return null;

        }
    }

    public enum BOTypes{
        ITEM,ORDER,PURCHASE_ORDER,SYSTEM_REPORTS
    }

}
