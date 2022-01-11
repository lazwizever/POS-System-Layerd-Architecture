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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.custom.ItemBO;
import lk.ijse.pos_system.business.custom.impl.ItemBOImpl;
import lk.ijse.pos_system.dto.ItemDTO;
import lk.ijse.pos_system.repository.custom.ItemDAO;
import lk.ijse.pos_system.repository.custom.impl.ItemDAOImpl;
import lk.ijse.pos_system.validation.Validation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageItemsFormController {
    public Label lblDate;
    public Label lblTime;
    public AnchorPane manageItemContext;
    public JFXTextField txtItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtDiscount;
    public TableView<ItemDTO> tblItemDetails;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public JFXButton btnAddAndUpdate;
    public TableColumn colDiscount;
    public JFXButton removeBtn;
    public JFXComboBox cmbGenerateItemIds;
    public JFXTextField txtGenerateItemCode;


    ItemBO itemBO = (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BOTypes.ITEM);
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    {
        Thread clock=new Thread(){
            public void run(){
                try {
                    while (true) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy ");
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

    public void initialize(){

        storeValidation();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qyOnHand"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        try {
            loadItemIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        removeBtn.setDisable(true);

        try {
            loadAllItemDetails();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeBtn.setDisable(false);
            try {
                if (newValue!=null){
                    setItemDetails(newValue);
                    btnAddAndUpdate.setText("Update");
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ItemDTO selectRow = newValue;

        });

        for (JFXTextField jfxTextField : map.keySet()) {
            jfxTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                checkAllFields();
            });
        }

        try {
            txtItemCode.setText(itemBO.generateItemId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemDetails(ItemDTO newValue) throws SQLException, ClassNotFoundException {
        ItemDTO itemDTO = itemBO.searchItem(newValue.getItemCode());
        txtItemCode.setText(itemDTO.getItemCode());
        txtDescription.setText(itemDTO.getDescription());
        txtDiscount.setText(String.valueOf(itemDTO.getDiscount()));
        txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
        txtPackSize.setText(itemDTO.getPackSize());
        txtQtyOnHand.setText(String.valueOf(itemDTO.getQyOnHand()));

    }

    private void loadAllItemDetails() throws SQLException, ClassNotFoundException {
        ObservableList<ItemDTO> itemDTOS  = itemBO.getAllItems();
        tblItemDetails.setItems(itemDTOS);
    }

    private void loadItemIds() throws SQLException, ClassNotFoundException {
        List<String> itemIds = itemBO.getAllItemIds();
    }

    public void backToAdminFormOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/AdminForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) manageItemContext.getScene().getWindow();
        window.setX(350);
        window.setY(100);
        window.setScene(new Scene(load));
    }

    public void addNewItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
            //-----------------Update----------------------------
        if (checkAllFields()){
            if (btnAddAndUpdate.getText().equals("Update")){
                ItemDTO items = new ItemDTO(
                        txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Double.parseDouble(txtUnitPrice.getText()),
                        Double.parseDouble(txtDiscount.getText()),
                        Integer.parseInt(txtQtyOnHand.getText())
                );
                if(itemBO.updateItem(items)){
                    clearFields();
                    tblItemDetails.setItems(itemBO.getAllItems());

                }
                //-------------------- Add new item----------------------
            }else {
                ItemDTO itemDTO = new ItemDTO(
                        txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Double.parseDouble(txtUnitPrice.getText()),
                        Double.parseDouble(txtDiscount.getText()),Integer.parseInt(txtQtyOnHand.getText())
                );

                if(itemBO.addItem(itemDTO)){
                    new Alert(Alert.AlertType.INFORMATION,"Item Has Been Successfully Added.").show();
                    clearFields();
                }else{
                    new Alert(Alert.AlertType.WARNING,"'Try Again.'").show();
                }

                List<String> itemIds = itemBO.getAllItemIds();
                itemIds.add(txtItemCode.getText());
                tblItemDetails.setItems(itemBO.getAllItems());
            }
            btnAddAndUpdate.setText("Add New Item");
        }

    }

    public void removeItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes =new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no =new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to remove this item ?",yes,no);
        alert.setTitle("Confirmation Alert");
        Optional<ButtonType> result = alert.showAndWait();

        //-------------------When press yes----------------------
        if(result.orElse(no)==yes){
            if (itemBO.deleteItem(txtItemCode.getText())){
                tblItemDetails.setItems(itemBO.getAllItems());
                clearFields();
            }else{
                new Alert(Alert.AlertType.WARNING,"'Try Again.'").show();
            }

        }
    }

    public void clearFields() throws SQLException, ClassNotFoundException {
        for (JFXTextField jfxTextField : map.keySet()) {
            jfxTextField.clear();
            jfxTextField.setStyle("-fx-border-color: white;"+"-fx-border-width: 1");
        }
        txtItemCode.setText(itemBO.generateItemId());
    }

    private boolean checkAllFields(){
        /*if (txtItemCode.getText().isEmpty() || txtDescription.getText().isEmpty() || txtPackSize.getText().isEmpty() || txtUnitPrice.getText().isEmpty() || txtQtyOnHand.getText().isEmpty() || txtDiscount.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please Check All The Fields").show();
            return false;
        }else {
            if (itemBO.getAllItemIds().contains(txtItemCode.getText()) && btnAddAndUpdate.getText().equals("Add New Item")){
                new Alert(Alert.AlertType.WARNING,"Item Id Already Exists").show();
                return false;
            }else {
                if(txtPackSize.getText().matches("^([0-9]{1,}[.][0-9]{1,})|([0-9]{1,})$")){
                    if(txtUnitPrice.getText().matches("^([1-9][0-9]{1,}[.][0-9]{1,})|([1-9][0-9]{1,})|([1-9]{1,}[.][0-9]{1,})|([1-9]{1,})$")){
                            if(txtQtyOnHand.getText().matches("^([0-9]{1,}[.][0-9]{1,})|([0-9]{1,})$")){
                                if(txtDiscount.getText().matches("^([1-9][0-9]{1,}[.][0-9]{1,})|([1-9][0-9]{1,})|([1-9]{1,}[.][0-9]{1,})|([1-9]{1,})$")){
                                return true;

                            }else{
                                new Alert(Alert.AlertType.WARNING,"Invalid Discount").show();
                                return false;
                            }
                        }else{
                            new Alert(Alert.AlertType.WARNING,"Invalid Quantity On Hand").show();
                            return false;
                        }
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Invalid Unit Price").show();
                        return false;
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid Pack Size").show();
                    return false;
                }
            }
        }*/
        return Validation.validate(map);
    }

    public void storeValidation(){
        map.put(txtDescription, Validation.text);
        map.put(txtPackSize, Validation.number);
        map.put(txtUnitPrice, Validation.decimalNumber);
        map.put(txtQtyOnHand, Validation.number);
        map.put(txtDiscount, Validation.decimalNumber);
    }

}
