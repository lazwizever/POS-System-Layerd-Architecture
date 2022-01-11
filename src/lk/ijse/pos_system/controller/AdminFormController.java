package lk.ijse.pos_system.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class AdminFormController {
    public AnchorPane adminContext;
    public ImageView iconSystemReports;
    public ImageView iconManageItems;

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/LoginForm.fxml",450,350);
    }

    public void manageItemsOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/ManageItemsForm.fxml",300,100);
    }

    public void moveToSystemReportsOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/SystemReports.fxml",350,60);
    }


    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);
        }
    }

    public void navigate(String path,int x, int y) throws IOException {
        URL resource = getClass().getResource(path);
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) adminContext.getScene().getWindow();
        window.setX(x);
        window.setY(y);
        window.setScene(new Scene(load));
    }
}
