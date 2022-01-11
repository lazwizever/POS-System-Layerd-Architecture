package lk.ijse.pos_system.validation;

import com.jfoenix.controls.JFXTextField;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Validation {
    public static Pattern number = Pattern.compile("^([0-9]{1,})$");
    public static Pattern decimalNumber = Pattern.compile("^([0-9.]{1,})$");
    public static Pattern name = Pattern.compile("^([A-z\\s. ]{3,80})$");
    public static Pattern text = Pattern.compile("^([A-z0-9/,\\s]{3,})$");
    public static Pattern city = Pattern.compile("^([A-z]{3,80})$");
    public static Pattern postalCode = Pattern.compile("^([0-9]{5})$");

    public static boolean validate(HashMap<JFXTextField,Pattern> map){
        for (JFXTextField jfxTextField : map.keySet()) {
            Pattern pattern = map.get(jfxTextField);
            if (!pattern.matcher(jfxTextField.getText()).matches()){
                jfxTextField.setStyle("-fx-border-color: red;"+"-fx-border-width: 1;");
                return false;
            }else {
                jfxTextField.setStyle("-fx-border-color: green;"+"-fx-border-width: 1;");
            }
        }
        return true;
    }
}
