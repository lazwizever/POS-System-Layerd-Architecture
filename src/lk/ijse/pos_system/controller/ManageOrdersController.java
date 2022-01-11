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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.OrderBO;
import lk.ijse.pos_system.business.custom.impl.OrderBOImpl;
import lk.ijse.pos_system.db.DbConnection;
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
import lk.ijse.pos_system.view.tm.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ManageOrdersController {
    public AnchorPane manageOrdersContext;
    public Label lblDate;
    public Label lblTime;
    public JFXComboBox <String>cmbCustomerId;
    public JFXComboBox<String> cmbOrderId;
    public JFXTextField txtOrderQty;
    public TableView<OrderTM> tblOrderItemDetails;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colOrderQty;
    public TableColumn colUnitPrice;
    public JFXTextField txtItemCode;
    public TableView<CartTM> tblPayment;
    public TableColumn colItemCodePayment;
    public TableColumn colDescriptionPayment;
    public TableColumn colUnitPricePayment;
    public TableColumn colQty;
    public TableColumn colAmount;
    public Label txtDate1;
    public Label txtTime1;
    public Label txtOrderIdPayment;
    public Label txtGrossTotal;
    public Label txtTtlDiscount;
    public Label txtNetTtl;
    public JFXTextField txtCash;
    public Label txtChange;
    public Label txtGrossTtl;
    public Label txtTtlDscnt;
    public Label txtNetTotal;
    public Label txtDate11;
    public JFXButton removeBtn;

    OrderBO orderBO = (OrderBO) BOFactory.getBOFactory().getBO(BOFactory.BOTypes.ORDER);

    ObservableList<OrderTM> orderItemDetails = FXCollections.observableArrayList();
    LinkedHashMap<JFXTextField, Pattern> updateMap = new LinkedHashMap<>();
    LinkedHashMap<JFXTextField, Pattern> generateBillMap = new LinkedHashMap<>();

    public void initialize(){

        storeValidation();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        colItemCodePayment.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("customerQty"));
        colDescriptionPayment.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPricePayment.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("total"));

        {
            Thread clock=new Thread(){
                public void run(){
                    try {
                        while (true) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
                            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm a");
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

        try {
            loadCustomerIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        removeBtn.setDisable(true);

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadOrderIds(newValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        cmbOrderId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                orderItemDetails.clear();

                loadItemDetails(newValue);
                paymentInfo();
                System.out.println(txtGrossTtl.getText());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblOrderItemDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeBtn.setDisable(false);
            try {
                if (newValue!= null){
                    setOrderQty(newValue);
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        txtOrderQty.textProperty().addListener((observable, oldValue, newValue) -> {
            validateCustomerQty();
        });

        txtCash.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Validation.validate(generateBillMap)){
                boolean cashGreaterThanTotal = Double.parseDouble(txtNetTtl.getText()) <= Double.parseDouble(txtCash.getText());
                if (cashGreaterThanTotal){
                    txtCash.setStyle("-fx-border-color: green;"+"-fx-border-width: 1;");
                }else {
                    txtCash.setStyle("-fx-border-color: red;"+"-fx-border-width: 1;");
                }
            }

        });

    }

    private void setOrderQty(OrderTM newValue) throws SQLException, ClassNotFoundException {
        for (int i = 0; i < orderItemDetails.size(); i++) {
            if (newValue.getItemCode().equals(orderItemDetails.get(i).getItemCode())){
                txtOrderQty.setText(String.valueOf(orderItemDetails.get(i).getOrderQty()));
                txtItemCode.setText(orderItemDetails.get(i).getItemCode());
            }
        }
    }

    private void loadItemDetails(String oId) throws SQLException, ClassNotFoundException {

        for (ItemDetailsDTO temp: orderBO.getOrderItems(oId)
             ) {
            ItemDTO itemDTO = orderBO.searchItem(temp.getItemCode());

            OrderTM orderTM = new OrderTM(
                    temp.getItemCode(), itemDTO.getDescription(),temp.getOrderQty(), itemDTO.getUnitPrice()
            );
            orderItemDetails.add(orderTM);
        }

        tblOrderItemDetails.setItems(orderItemDetails);
        calculate();

    }

    public void backToUserFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/UserForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) manageOrdersContext.getScene().getWindow();
        window.setX(550);
        window.setY(300);
        window.setScene(new Scene(load));
    }

    public void loadCustomerIds() throws SQLException, ClassNotFoundException {
        List<String> customerIds = orderBO.getAllCustomerIDs();
        cmbCustomerId.getItems().setAll(customerIds);
    }

    public void loadOrderIds(String customerId) throws SQLException, ClassNotFoundException {
        cmbOrderId.setItems(orderBO.getOrderIdForCustomer(customerId));
    }

    public void removeItemFromTableOnAction(ActionEvent actionEvent) {
        if (cmbOrderId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select an order").show();
            return;
        }
        for (int i = 0; i < orderItemDetails.size(); i++) {
            if (txtItemCode.getText().equals(orderItemDetails.get(i).getItemCode())){
                orderItemDetails.remove(i);
            }
        }
        tblOrderItemDetails.setItems(orderItemDetails);
    }

    public void updateDetailsOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbOrderId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select an order").show();
            return;
        }
            if (validateCustomerQty()){
                ItemDTO itemDTO = orderBO.searchItem(txtItemCode.getText());

                OrderTM orderTM =new OrderTM(
                        txtItemCode.getText(), itemDTO.getDescription(),Integer.parseInt(txtOrderQty.getText()), itemDTO.getUnitPrice()
                );

                for (int i = 0; i < orderItemDetails.size(); i++) {
                    if (txtItemCode.getText().equals(orderItemDetails.get(i).getItemCode())){
                        orderItemDetails.set(i,orderTM);
                    }
                }
                tblOrderItemDetails.setItems(orderItemDetails);
            }
            txtItemCode.clear();
            txtOrderQty.clear();
        txtOrderQty.setStyle("-fx-border-color: white;"+"-fx-border-width: 1;");
    }

    public void editOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbOrderId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select an order").show();
            return;
        }
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to edit this item ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        //-------------------When press yes----------------------
        if(result.orElse(no)==yes) {

            if(editOrder()){
                new Alert(Alert.AlertType.INFORMATION,"Successfully Ordered.").show();
                paymentInfo();
        }else {
                new Alert(Alert.AlertType.WARNING,"try Again").show();
        }
        }
    }

    private boolean editOrder() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDetailsDTO> newOrderItems = new ArrayList();

        for (OrderTM temp: orderItemDetails
        ) {
            ItemDetailsDTO itemDetailsDTO = new ItemDetailsDTO(
                    cmbOrderId.getValue(),temp.getItemCode(),temp.getOrderQty(),orderBO.searchItem(temp.getItemCode()).getDiscount()
            );
            newOrderItems.add(itemDetailsDTO);
        }
        return orderBO.editOrder(cmbOrderId.getValue(),newOrderItems);
    }

    public void cancelOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cmbOrderId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select an order").show();
            return;
        }
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to cancel this order ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        //-------------------When press yes----------------------
        if(result.orElse(no)==yes){
            if (orderBO.deleteOrder(cmbOrderId.getValue())){
                new Alert(Alert.AlertType.INFORMATION,"Successfully remove.").show();
            }else{
                new Alert(Alert.AlertType.WARNING,"'Try Again'").show();
            }
        }
    }

    public void paymentInfo() throws SQLException, ClassNotFoundException {
        ObservableList<CartTM> paymentDetails = FXCollections.observableArrayList();
        txtOrderIdPayment.setText(cmbOrderId.getValue());
        txtDate1.setText(lblDate.getText());
        txtTime1.setText(lblTime.getText());
        txtGrossTotal.setText(txtGrossTtl.getText());
        txtTtlDiscount.setText(txtTtlDscnt.getText());
        txtNetTtl.setText(txtNetTotal.getText());


        for (OrderTM temp: orderItemDetails
             ) {
            double total = temp.getOrderQty()*temp.getUnitPrice();
            CartTM cartTM = new CartTM(

                    temp.getItemCode(),temp.getDescription(),temp.getOrderQty(),temp.getUnitPrice(),
                    orderBO.searchItem(temp.getItemCode()).getDiscount(),total
            );
            paymentDetails.add(cartTM);
        }

        tblPayment.setItems(paymentDetails);
    }

    public void printBillOnAction(ActionEvent actionEvent) {
        if (cmbCustomerId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select An customer id").show();
            return;
        }
        if (cmbOrderId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select an order").show();
            return;
        }

        ObservableList<CartTM> items = tblPayment.getItems();

        HashMap map =new HashMap();
        map.put("GrossTotal",txtGrossTotal.getText());
        map.put("TotalDiscount",txtTtlDiscount.getText());
        map.put("NetAmount",txtNetTtl.getText());
        map.put("Cash",txtCash.getText());
        map.put("Change",txtChange.getText());
        map.put("OrderId",txtOrderIdPayment.getText());

        CustomerOrderReport.generateBill(items,map);

        txtGrossTotal.setText("0.00");
        txtTtlDiscount.setText("0.00");
        txtNetTtl.setText("0.00");
        txtCash.clear();
        txtCash.setStyle("-fx-border-color: white;"+"-fx-border-width: 1;");
        txtChange.setText("0.00");
        tblPayment.setItems(FXCollections.observableArrayList());
        txtOrderIdPayment.setText("");
        txtDate1.setText("");
        txtTime1.setText("");
    }

    public void cashOnAction(ActionEvent actionEvent) {
        if (Validation.validate(generateBillMap)){
            boolean cashGreaterThanTotal = Double.parseDouble(txtNetTtl.getText())<= Double.parseDouble(txtCash.getText());
            if (cashGreaterThanTotal){
                double change = Double.parseDouble(txtCash.getText())-Double.parseDouble(txtNetTtl.getText());
                txtChange.setText(String.valueOf(change));
            }else {
                txtCash.setStyle("-fx-border-color: red;"+"-fx-border-width: 1;");
            }
        }

    }

    public void calculate() throws SQLException, ClassNotFoundException {
        double grossTotal=0;
        double totalDiscount=0;

        for (OrderTM temp:orderItemDetails
             ) {
            grossTotal += temp.getOrderQty()*temp.getUnitPrice();
            totalDiscount+=orderBO.searchItem(temp.getItemCode()).getDiscount()*temp.getOrderQty();
        }
        txtGrossTtl.setText(String.valueOf(grossTotal));
        txtTtlDscnt.setText(String.valueOf(totalDiscount));
        txtNetTotal.setText(String.valueOf(grossTotal-totalDiscount));
    }

    public void selectOrderId(MouseEvent mouseEvent) {
        if (cmbCustomerId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please select An customer id").show();
        }
    }

    public boolean validateCustomerQty(){
        /*if (!txtOrderQty.getText().matches("^([1-9]{1,})$")){
            new Alert(Alert.AlertType.WARNING,"Invalid Customer Quantity").show();
            return false;
        }else {
            return true;
        }*/
        return Validation.validate(updateMap);
    }

    public void storeValidation(){
        updateMap.put(txtOrderQty, Validation.number);
        generateBillMap.put(txtCash,Validation.decimalNumber);
    }

}
