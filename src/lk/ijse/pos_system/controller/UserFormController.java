package lk.ijse.pos_system.controller;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFormController {
    public AnchorPane userFormContext;
    public Label lblTime;
    public Label lblDate;


    public void initialize(){
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
    }

    public void CustomerPlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/PlaceCustomerOrder.fxml",340,50);
    }

    public void manageOrderOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/ManageOrders.fxml",350,60);
    }

    public void backToLoginFormOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/LoginForm.fxml",350,100);

    }

    public void navigate(String path,int x, int y) throws IOException {
        URL resource = getClass().getResource(path);
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) userFormContext.getScene().getWindow();
        window.setX(x);
        window.setY(y);
        window.setScene(new Scene(load));
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

    public void playMouseExistAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);
        }
    }
}
