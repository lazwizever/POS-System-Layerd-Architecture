package lk.ijse.pos_system.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.SystemReportsBO;
import lk.ijse.pos_system.business.custom.impl.SystemReportsBOImpl;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.repository.custom.OrderDetailsDAO;
import lk.ijse.pos_system.repository.custom.impl.CustomerDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.ItemDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDetailsDAOImpl;
import lk.ijse.pos_system.view.tm.CustomerReportTm;
import lk.ijse.pos_system.view.tm.ReportTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class SystemReportsController {

    public JFXComboBox<String> cmbYear;
    public JFXComboBox<String> cmbMonth;
    public JFXComboBox cmbAnnualYear;
    public AnchorPane systemReports;
    public JFXDatePicker selectDate;
    public TableView tblReports;
    public TableColumn colItemCode;
    public TableColumn colLost;
    public TableColumn colProfit;
    public TableColumn colLeastMovable;
    public TableColumn colMostMovable;
    public JFXButton dailyIncome;
    public JFXButton monthlyIncome;
    public JFXButton annualIncome;
    public TableColumn colSales;
    public TableColumn colIncome;
    public Label lblIncome;
    public Label lblLost;
    public Label lblProfit;
    public JFXDatePicker selectDateTo;
    public JFXDatePicker selectDateFrom;
    public JFXRadioButton itemReport;
    public JFXRadioButton customerReport;
    public TableView tblCustomerReport;
    public TableColumn colCustomerName;
    public TableColumn colTotal;
    public TableColumn colCustomerId;
    public Label lblIncome1;
    public Label lblLost1;
    public Label lblProfit0;

    SystemReportsBO systemReportsBO = (SystemReportsBO) BOFactory.getBOFactory().getBO(BOFactory.BOTypes.SYSTEM_REPORTS);

    ObservableList<CustomerReportTm> customerIncome = FXCollections.observableArrayList();

    public void initialize(){

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        colLost.setCellValueFactory(new PropertyValueFactory<>("lost"));
        colSales.setCellValueFactory(new PropertyValueFactory<>("sales"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colSales.setSortType(TableColumn.SortType.DESCENDING);

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("income"));

        ObservableList<String> month = FXCollections.observableArrayList();
        month.addAll("01","02","03","04","05","06","07","08","09","10","11","12");
        cmbMonth.setItems(month);

        ObservableList<String> year = FXCollections.observableArrayList();
        year.addAll("2016","2017","2018","2019","2020","2021");
        cmbYear.setItems(year);
        cmbAnnualYear.setItems(year);
       // cmbYear.setDisable(false);
        /*monthlyIncome.setDisable(true);
        dailyIncome.setDisable(true);
        annualIncome.setDisable(true);
        cmbMonth.setDisable(true);*/

        customerReport.selectedProperty().addListener((observable, oldValue, newValue) -> {
            tblCustomerReport.setVisible(true);
            itemReport.setSelected(false);
            tblReports.setVisible(false);
            lblIncome1.setVisible(false);
            lblLost1.setVisible(false);
            lblProfit0.setVisible(false);
            lblProfit.setVisible(false);
            lblLost.setVisible(false);
            lblIncome.setVisible(false);
        });

        itemReport.selectedProperty().addListener((observable, oldValue, newValue) -> {
            tblCustomerReport.setVisible(false);
            tblReports.setVisible(true);
            lblIncome1.setVisible(true);
            lblLost1.setVisible(true);
            lblProfit0.setVisible(true);
            lblProfit.setVisible(true);
            lblLost.setVisible(true);
            lblIncome.setVisible(true);
            customerReport.setSelected(false);
        });

    }

    public void moveAdminFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/AdminForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) systemReports.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    ObservableList<ReportTM> reports = FXCollections.observableArrayList();

    //--------------------------Start Income reports---------------------------------------------------
    public void dailyIncomeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        playAnimation(actionEvent);
        disableAll();
        selectDate.setDisable(false);

    }

    public void monthlyIncomeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        disableAll();
        cmbYear.setDisable(false);
        cmbMonth.setDisable(false);
        playAnimation(actionEvent);

    }

    public void annualIncomeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        disableAll();
        cmbAnnualYear.setDisable(false);
        playAnimation(actionEvent);
        if (customerReport.isSelected()){
            reports.clear();
            int sales = 0;
            for (ItemDTO itemDTO : systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromYear(cmbYear.getValue())
                ) {
                    for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                ReportTM reportTM = new ReportTM(
                        itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
                );
                reports.add(reportTM);
            }
            tblReports.setItems(reports);
            setLabelTotals();
        }
        if (itemReport.isSelected()){
            reports.clear();
            int sales = 0;
            for (ItemDTO itemDTO : systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromYear(cmbYear.getValue())
                ) {
                    for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                ReportTM reportTM = new ReportTM(
                        itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
                );
                reports.add(reportTM);
            }
            tblReports.setItems(reports);
            setLabelTotals();
        }

    }
    //---------------------------------End-------------------------------------------------------------

    public void disableAll(){
        selectDate.setDisable(true);
        cmbMonth.setDisable(true);
        cmbYear.setDisable(true);
        cmbAnnualYear.setDisable(true);
        cmbMonth.getSelectionModel().clearSelection();
        cmbYear.getSelectionModel().clearSelection();
        cmbAnnualYear.getSelectionModel().clearSelection();
    }

    public void playAnimation(ActionEvent actionEvent){
        ObservableList<JFXButton> btn = FXCollections.observableArrayList(dailyIncome,monthlyIncome,annualIncome);
        if (actionEvent.getSource() instanceof JFXButton){
            JFXButton source = (JFXButton) actionEvent.getSource();

            for (JFXButton jfxButton : btn) {
                ScaleTransition scaleT = new ScaleTransition(Duration.millis(200),jfxButton);
                if (jfxButton.getId().equals(source.getId())){
                    scaleT.setToX(1.2);
                    scaleT.setToY(1.2);
                }else {
                    scaleT.setToX(1);
                    scaleT.setToY(1);
                }
                scaleT.play();
            }
        }
    }

    public void visibleDailyIncome(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (customerReport.isSelected()){
            customerIncome.clear();
            double income = 0;
            for (String custId: systemReportsBO.getAllCustomerIDs()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromDate(selectDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                ) {
                    if (orderDTO.getCustomerId().equals(custId)){
                        for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                        ) {
                            income+=orderItem.getOrderQty()*systemReportsBO.searchItem(orderItem.getItemCode()).getUnitPrice();

                        }
                    }
                }
                CustomerReportTm customerReportTm = new CustomerReportTm(
                        custId,systemReportsBO.searchCustomer(custId).getCustomerName(),income
                );
                customerIncome.add(customerReportTm);
            }
            tblCustomerReport.setItems(customerIncome);
        }
        if (itemReport.isSelected()){
            reports.clear();
            int sales = 0;
            for (ItemDTO itemDTO : systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromDate(selectDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                ) {
                    for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                ReportTM reportTM = new ReportTM(
                        itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
                );
                reports.add(reportTM);
            }
            tblReports.setItems(reports);
            setLabelTotals();
        }
    }

    public void visibleMonthlyIncome(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (itemReport.isSelected()){
            reports.clear();
            int sales = 0;
            for (ItemDTO itemDTO : systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromMonth(cmbYear.getValue(),cmbMonth.getValue())
                ) {
                    for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                ReportTM reportTM = new ReportTM(
                        itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
                );
                reports.add(reportTM);
            }
            tblReports.setItems(reports);
            setLabelTotals();
        }
        if (customerReport.isSelected()){
            customerIncome.clear();
            double income = 0;
            for (String custId: systemReportsBO.getAllCustomerIDs()
            ) {

                for (OrderDTO orderDTO : systemReportsBO.getOrderFromMonth(cmbYear.getValue(),cmbMonth.getValue())
                ) {

                    if (orderDTO.getCustomerId().equals(custId)){

                        for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                        ) {
                            income+=orderItem.getOrderQty()*systemReportsBO.searchItem(orderItem.getItemCode()).getUnitPrice();

                        }
                    }
                }
                CustomerReportTm customerReportTm = new CustomerReportTm(
                        custId,systemReportsBO.searchCustomer(custId).getCustomerName(),income
                );
                customerIncome.add(customerReportTm);
            }
            tblCustomerReport.setItems(customerIncome);
        }

    }

    public void visibleAnnualIncome(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (customerReport.isSelected()){
            reports.clear();
            int sales = 0;
            for (ItemDTO itemDTO : systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromYear(cmbYear.getValue())
                ) {
                    for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                ReportTM reportTM = new ReportTM(
                        itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
                );
                reports.add(reportTM);
            }
            tblReports.setItems(reports);
            setLabelTotals();
        }
        if (itemReport.isSelected()){
            reports.clear();
            int sales = 0;
            for (ItemDTO itemDTO : systemReportsBO.getAllItems()
            ) {
                for (OrderDTO orderDTO : systemReportsBO.getOrderFromYear(cmbYear.getValue())
                ) {
                    for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(orderDTO.getOrderId())
                    ) {
                        if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                            sales+=orderItem.getOrderQty();
                        }
                    }
                }
                ReportTM reportTM = new ReportTM(
                        itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
                );
                reports.add(reportTM);
            }
            tblReports.setItems(reports);
            setLabelTotals();
        }
    }

    //-----------------Set To Label Income,Profit,Loss--------------------------------------------------
    private void setLabelTotals(){
        double income = 0;
        double loss = 0;

        for (ReportTM setTotal: reports
             ) {
            income+=setTotal.getIncome();
            loss+=setTotal.getLost();
        }
        lblIncome.setText(String.valueOf(income));
        lblLost.setText(String.valueOf(loss));
        lblProfit.setText(String.valueOf(income-loss));
    }

    //-------------------View Seasonal Trends-----------------------------------------------------------
    public void viewOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        reports.clear();
        int sales = 0;
        for (ItemDTO itemDTO : systemReportsBO.getAllItems()
        ) {
            for (String order: systemReportsBO.getOrderSeason(selectDateFrom.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),selectDateTo.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            ) {
                for (ItemDetailsDTO orderItem:systemReportsBO.getOrderItems(order)
                ) {
                    if (orderItem.getItemCode().equals(itemDTO.getItemCode())){
                        sales+=orderItem.getOrderQty();
                    }
                }
            }
            ReportTM reportTM = new ReportTM(
                    itemDTO.getItemCode(),sales,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount()),0.00,sales*(itemDTO.getUnitPrice()- itemDTO.getDiscount())
            );
            reports.add(reportTM);
        }
        tblReports.setItems(reports);
        setLabelTotals();
    }


}
