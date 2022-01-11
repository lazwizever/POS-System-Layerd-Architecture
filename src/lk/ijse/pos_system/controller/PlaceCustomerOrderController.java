package lk.ijse.pos_system.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.PurchaseOrderBO;
import lk.ijse.pos_system.business.custom.impl.PurchaseOrderBOImpl;
import lk.ijse.pos_system.dto.CustomerDTO;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.dto.ItemDetailsDTO;
import lk.ijse.pos_system.dto.OrderDTO;
import lk.ijse.pos_system.reports.CustomerOrderReport;
import lk.ijse.pos_system.repository.custom.CustomerDAO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.OrderDAO;
import lk.ijse.pos_system.repository.custom.OrderDetailsDAO;
import lk.ijse.pos_system.repository.custom.impl.CustomerDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.ItemDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDAOImpl;
import lk.ijse.pos_system.repository.custom.impl.OrderDetailsDAOImpl;
import lk.ijse.pos_system.validation.Validation;
import lk.ijse.pos_system.view.tm.CartTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class PlaceCustomerOrderController {
    public AnchorPane placeOrderContext;
    public Label lblDate;
    public Label lblTime;
    public JFXComboBox cmbCustomerIds;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtCity;
    public JFXTextField txtCode;
    public JFXTextField txtCustomerId;
    public JFXButton addListButton;
    public JFXButton placeOrderButton;
    public JFXComboBox cmbItemCode;
    public JFXTextField txtCustomerQty;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtDiscount;
    public TableView<CartTM> tblCart;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colCustomerQty;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public JFXTextField txtDescription;
    public JFXTextField txtUnitPrice;
    public JFXButton btnRemove;
    public Label txtGrossTotal;
    public Label txtTtlDiscount;
    public Label txtNetTtl;
    public Label txtOrderId;
    public Label txtDate1;
    public Label txtTime1;
    public JFXButton placeOrderButton1;
    public Label txtGrossAmount;
    public Label txtDiscountPayment;
    public Label txtNetAmount;
    public JFXTextField txtCash;
    public Label txtChange;
    public Label txtOrderIdPayment;
    public TableView<CartTM> tblPayments;
    public TableColumn colItemCodePayment;
    public TableColumn colDescriptionPayment;
    public TableColumn colUnitPricePayment;
    public TableColumn colQtyPayment;
    public TableColumn colAmount;
    public JFXComboBox<String> cmbTitle;
    public JFXComboBox<String> cmbProvince;

    int tblSelectRowRemove = -1;
    PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBOFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);
    LinkedHashMap<JFXTextField, Pattern> customerMap = new LinkedHashMap<>();
    LinkedHashMap<JFXTextField, Pattern> customerQtyMap = new LinkedHashMap<>();
    LinkedHashMap<JFXTextField, Pattern> cashMap = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {

        storeValidation();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCustomerQty.setCellValueFactory(new PropertyValueFactory<>("customerQty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        colItemCodePayment.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescriptionPayment.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyPayment.setCellValueFactory(new PropertyValueFactory<>("customerQty"));
        colUnitPricePayment.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("total"));

        {
            Thread clock=new Thread(){
                public void run(){
                    try {
                        while (true) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
                            Date date = new Date();
                            String myString = formatter.format(date);
                            String myString1 = formatter1.format(date);
                            Platform.runLater(() -> {
                                lblDate.setText(myString);
                                lblTime.setText(myString1);
                            });
                            sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            clock.start();

        }
            placeOrderButton.setDisable(true);
            addListButton.setDisable(true);
            btnRemove.setDisable(true);

        try {
            setOrderId();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadCustomerIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCustomerIds.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if(!newValue.equals("Customer id :")){
                    loadCustomerDetails((String) newValue);
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        try {
            loadItemIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.equals("Item Code")){
                    loadItemDetails((String) newValue);
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });


        tblCart.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tblSelectRowRemove = (int) newValue;
        });

        for (JFXTextField jfxTextField : customerMap.keySet()) {
            jfxTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                validateAllData();
            });
        }

        txtCustomerQty.textProperty().addListener((observable, oldValue, newValue) -> {
            validateCustomerQty();
        });

        txtCash.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Validation.validate(cashMap)){
                boolean cashGreaterThanTotal = Double.parseDouble(txtNetTtl.getText())<= Double.parseDouble(txtCash.getText());
                if (cashGreaterThanTotal){
                    txtCash.setStyle("-fx-border-color: green;"+"-fx-border-width: 1;");
                }else {
                    txtCash.setStyle("-fx-border-color: red;"+"-fx-border-width: 1;");
                }
            }
        });

        ObservableList<String> titles =FXCollections.observableArrayList("Mr","Mrs","Ms");
        cmbTitle.setItems(titles);
        ObservableList<String> provinces =FXCollections.observableArrayList("Southern","Western","Eastern","Northern","Central","North Central","North Western","Uva","Sabaragamuwa");
        cmbProvince.setItems(provinces);

        txtCustomerId.setText(purchaseOrderBO.generateCustomerId());
    }

    //---------Generate Order Id---------------
    private void setOrderId() throws SQLException, ClassNotFoundException {
        txtOrderId.setText(purchaseOrderBO.generateOrderId());
    }

    public void backToUserFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/UserForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) placeOrderContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    //---------------Customers-----------------------------------------
    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        cmbCustomerIds.setItems( purchaseOrderBO.getAllCustomerIDs());
    }

    private void loadCustomerDetails(String cid) throws SQLException, ClassNotFoundException {
        CustomerDTO customerDTO = purchaseOrderBO.searchCustomer(cid);
        txtCustomerId.setText(customerDTO.getCustomerId());
        cmbTitle.setValue(customerDTO.getCustomerTitle());
        txtName.setText(customerDTO.getCustomerName());
        txtAddress.setText(customerDTO.getCustomerAddress());
        txtCity.setText(customerDTO.getCity());
        cmbProvince.setValue(customerDTO.getProvince());
        txtCode.setText(customerDTO.getPostalCode());
    }

    public void addNewCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (validateAllData()) {

            if (validateCustomerId()) {
                CustomerDTO customerDTO = new CustomerDTO(
                        txtCustomerId.getText(), cmbTitle.getValue(), txtName.getText(), txtAddress.getText(), txtCity.getText(),
                        cmbProvince.getValue(), txtCode.getText()
                );
                if (purchaseOrderBO.addCustomer(customerDTO)) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Successfully Added.").show();
                    placeOrderButton.setDisable(false);
                   clearCustomerTextFields();
                } else {
                    new Alert(Alert.AlertType.WARNING, " 'Try Again' ").show();
                }
                loadCustomerIds();
            }
        }

    }

    public void txtCustomerQtyOnClickAction(MouseEvent mouseEvent) {
        addListButton.setDisable(false);
    }
    //----------------------------------------------------------------------


    //--------------Items----------------------------------------------------------
    private void loadItemIds() throws SQLException, ClassNotFoundException {
        List<String> itemIds = purchaseOrderBO.getAllItemIds();
        cmbItemCode.getItems().addAll(itemIds);
        String grossAmountText = txtGrossAmount.getText();
    }

    private void loadItemDetails(String itemId) throws SQLException, ClassNotFoundException {
        ItemDTO itemDTO =purchaseOrderBO.searchItem(itemId);
        txtQtyOnHand.setText(String.valueOf(itemDTO.getQyOnHand()));
        txtDiscount.setText(String.valueOf(itemDTO.getDiscount()));
        txtDescription.setText(itemDTO.getDescription());
        txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
    }

    //------------------------------------------------------------------------------------


    ObservableList<CartTM> observableList = FXCollections.observableArrayList();
    public void addListOnAction(ActionEvent actionEvent) {
        if (validateCustomerQty()){
            double total = Integer.parseInt(txtCustomerQty.getText())*Double.parseDouble(txtUnitPrice.getText());
            CartTM cartTM = new CartTM(
                    String.valueOf(cmbItemCode.getValue()),txtDescription.getText(),Integer.parseInt(txtCustomerQty.getText()),Double.parseDouble(txtUnitPrice.getText()),
                    Double.parseDouble(txtDiscount.getText()),total
            );

            if (Integer.parseInt(txtQtyOnHand.getText())>Integer.parseInt(txtCustomerQty.getText())){

                //---------------------Start Update customer Qty--------------------------------
                int rowIndex = isExists(cartTM);
                if (rowIndex==-1){
                    observableList.add(cartTM);

                }else {
                    //------------Update----------------------------------

                    CartTM cartTM1 = observableList.get(rowIndex);
                    CartTM newCartTm = new CartTM(
                            cartTM1.getItemCode(),
                            cartTM1.getDescription(),
                            cartTM1.getCustomerQty()+Integer.parseInt(txtCustomerQty.getText()),
                            cartTM1.getUnitPrice(),
                            cartTM1.getDiscount(),
                            total+cartTM1.getTotal()
                    );
                    observableList.remove(rowIndex);
                    observableList.add(newCartTm);

                    //-----------------End Update Customer Qty-------------------
                }
                tblCart.setItems(observableList);

            }else{
                new Alert(Alert.AlertType.WARNING,"Can't Be Execced The Limit Of Quantity On Hand.").show();
            }
            if (placeOrderButton.isDisable()){
                placeOrderButton.setDisable(false);
            }
            calculateNetTtl();
            clearItemTextFields();
        }

    }

    private void setData(CartTM cartTM){
        cmbItemCode.setValue(cartTM.getItemCode());
        txtDescription.setText(cartTM.getDescription());
        txtCustomerQty.setText(String.valueOf(cartTM.getCustomerQty()));
        txtUnitPrice.setText(String.valueOf(cartTM.getUnitPrice()));
        txtDiscount.setText(String.valueOf(cartTM.getDiscount()));
        btnRemove.setDisable(false);
    }

    //-----------Remove Item In Table------------------
    public void removeItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
            if (tblSelectRowRemove==-1){
                new Alert(Alert.AlertType.WARNING,"Please Select A Row").show();
            }else {
                observableList.remove(tblSelectRowRemove);
                if (observableList.isEmpty()){
                    placeOrderButton.setDisable(true);
                }
                calculateNetTtl();
                tblCart.refresh();
            }

    }

    private void calculateNetTtl(){
        double grossTtl = 0;
        double ttlDiscount =0;
        double netTtl =0;

        for (CartTM cartTM:observableList
             ) {
            grossTtl += cartTM.getTotal();
            ttlDiscount += cartTM.getDiscount();

        }
        netTtl += grossTtl-ttlDiscount;
        txtGrossTotal.setText(String.valueOf(grossTtl));
        txtTtlDiscount.setText(String.valueOf(ttlDiscount));
        txtNetTtl.setText(String.valueOf(netTtl));
    }

    //---------------IsExists----------------------
    private int isExists(CartTM cartTM){
        for (int i = 0; i < observableList.size(); i++) {
            if (cartTM.getItemCode().equals(observableList.get(i).getItemCode())){
                return i;
            }
        }
        return -1;
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ArrayList<ItemDetailsDTO> orderItemDetailDTOS = new ArrayList();
        for (CartTM temp: observableList
             ) {
           orderItemDetailDTOS.add(new ItemDetailsDTO(
                   txtOrderId.getText(),
                   temp.getItemCode(),
                   temp.getCustomerQty(),
                   temp.getDiscount()
           ));
        }
        OrderDTO orderDTO = new OrderDTO(
                txtOrderId.getText(),
                lblDate.getText(),
                txtCustomerId.getText(),
                orderItemDetailDTOS
        );

        if (purchaseOrderBO.addOrder(orderDTO)){
            new Alert(Alert.AlertType.INFORMATION,"Your Order Has Been Placed Successfully.").show();
            paymentInfo();
            setOrderId();
            clearCustomerTextFields();
            clearItemTextFields();
           // observableList.clear();
            tblCart.setItems(FXCollections.observableArrayList());
            txtGrossTotal.setText("0.00");
            txtTtlDiscount.setText("0.00");
            txtNetTtl.setText("0.00");
        }else {
            new Alert(Alert.AlertType.WARNING,"Try Again.").show();
        }
    }

    private void clearItemTextFields(){
        txtQtyOnHand.clear();
        txtDescription.clear();
        txtDiscount.clear();
        txtDiscount.clear();
        txtUnitPrice.clear();
        txtCustomerQty.clear();
        txtCustomerQty.setStyle("-fx-border-color: white;"+"-fx-border-width: 1;");
        addListButton.setDisable(true);
        cmbCustomerIds.setValue("Customer id :");
        cmbItemCode.setValue("Item Code");
    }

    private void clearCustomerTextFields() throws SQLException, ClassNotFoundException {
        txtCustomerId.setText(purchaseOrderBO.generateCustomerId());
        cmbProvince.getSelectionModel().clearSelection();
        cmbTitle.getSelectionModel().clearSelection();

        for (JFXTextField jfxTextField : customerMap.keySet()) {
            jfxTextField.clear();
            jfxTextField.setStyle("-fx-border-color: white;"+"-fx-border-width: 1;");
        }
    }


    public void cancelOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearCustomerTextFields();
        clearItemTextFields();
        observableList.clear();
        tblCart.setItems(observableList);
        txtGrossTotal.setText("0.00");
        txtTtlDiscount.setText("0.00");
        txtNetTtl.setText("0.00");
    }

    public void paymentInfo(){
        txtOrderIdPayment.setText(txtOrderId.getText());
        txtDate1.setText(lblDate.getText());
        txtTime1.setText(lblTime.getText());
        txtGrossAmount.setText(txtGrossTotal.getText());
        txtDiscountPayment.setText(txtTtlDiscount.getText());
        txtNetAmount.setText(txtNetTtl.getText());
        tblPayments.setItems(observableList);
    }

    public void cashOnAction(ActionEvent actionEvent) {
        if (Validation.validate(cashMap)){
            boolean cashGreaterThanTotal = Double.parseDouble(txtNetTtl.getText())<= Double.parseDouble(txtCash.getText());
            if (cashGreaterThanTotal){
                double change = Double.parseDouble(txtCash.getText())-Double.parseDouble(txtNetAmount.getText());
                txtChange.setText(String.valueOf(change));
            }else {
                txtCash.setStyle("-fx-border-color: red;"+"-fx-border-width: 1;");
            }
        }
    }

    public void printBillOnAction(ActionEvent actionEvent) {
            ObservableList<CartTM> items = tblPayments.getItems();

            HashMap map =new HashMap();
            map.put("GrossTotal",txtGrossAmount.getText());
            map.put("TotalDiscount",txtDiscountPayment.getText());
            map.put("NetAmount",txtNetAmount.getText());
            map.put("Cash",txtCash.getText());
            map.put("Change",txtChange.getText());
            map.put("OrderId",txtOrderId.getText());

            CustomerOrderReport.generateBill(items,map);

        txtGrossAmount.setText("0.00");
        txtDiscountPayment.setText("0.00");
        txtNetAmount.setText("0.00");
        txtCash.clear();
        txtCash.setStyle("-fx-border-color: white;"+"-fx-border-width: 1;");
        txtChange.setText("0.00");
        observableList.clear();
        txtOrderIdPayment.setText("");
        txtDate1.setText("");
        txtTime1.setText("");
    }


    private boolean validateCustomerId() throws SQLException, ClassNotFoundException {
        if (txtCustomerId.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Customer Id can't Be Empty").show();
            return false;

        }else {
            if (purchaseOrderBO.getAllCustomerIDs().contains(txtCustomerId.getText())){
                new Alert(Alert.AlertType.WARNING,"Customer Id Already Exists").show();
                return false;
            }else {
                return true;
            }
        }
    }

    public boolean validateAllData(){
        if (Validation.validate(customerMap)){
         if (!cmbTitle.getValue().isEmpty()){
            if (!cmbProvince.getValue().isEmpty()){
                return true;
            }else {
                new Alert(Alert.AlertType.WARNING,"Please Select A Province");
                return false;
            }
         }else {
             new Alert(Alert.AlertType.WARNING,"Please Select A Customer Title");
             return false;
         }
        }else {
            return false;
        }
    }

    public void clearErrorMessage(MouseEvent mouseEvent) {}

    public boolean validateCustomerQty(){
       return Validation.validate(customerQtyMap);
    }

    public void storeValidation(){
        customerMap.put(txtName, Validation.name);
        customerMap.put(txtAddress, Validation.text);
        customerMap.put(txtCity, Validation.name);
        customerMap.put(txtCode, Validation.number);

        customerQtyMap.put(txtCustomerQty,Validation.number);

        cashMap.put(txtCash,Validation.decimalNumber);
    }

}
