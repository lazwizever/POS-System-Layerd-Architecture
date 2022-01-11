package lk.ijse.pos_system.reports;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.controller.PlaceCustomerOrderController;
import lk.ijse.pos_system.view.tm.CartTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;

public class CustomerOrderReport {
    public static void generateBill(ObservableList<CartTM> paymentReport,HashMap hashMap){

        try {

            JasperDesign design = JRXmlLoader.load(CustomerOrderReport.class.getResourceAsStream("../view/PlaceOrderReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, hashMap, new JRBeanArrayDataSource(paymentReport.toArray()));
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
