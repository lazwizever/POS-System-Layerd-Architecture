package lk.ijse.pos_system.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginFormController {
    public AnchorPane loginForm;
    public PasswordField txtPassword;
    public TextField txtUserName;
    public Circle crclImage;
    public ImageView userImage;
    public Label lblWarningUserName;
    public Label lblWarningPassword;
    public ImageView userImage1;


    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        String path = null;
        if (txtUserName.getText().equals("Admin") || txtUserName.getText().equals("User")){
            if (txtUserName.getText().equals("Admin")){
                if (txtPassword.getText().equals("000000")){
                    path = "../view/AdminForm.fxml";

                }else {
                    lblWarningPassword.setVisible(true);
                }
            }else {
                //---------User---------
                if (txtPassword.getText().equals("111111")){
                    path = "../view/UserForm.fxml";
                }else {
                    lblWarningPassword.setVisible(true);
                }
            }
            URL resource = getClass().getResource(path);
            Parent load = FXMLLoader.load(resource);
            Stage window = (Stage) loginForm.getScene().getWindow();
            window.setScene(new Scene(load));
        }else {
            lblWarningUserName.setVisible(true);
        }

    }

    public void onMouseClick(MouseEvent mouseEvent) {
        if(txtUserName.getText().equals("Admin")){
            userImage.setVisible(true);
            crclImage.setVisible(true);
        }else{
            userImage1.setVisible(true);
            crclImage.setVisible(true);
        }
    }
}
