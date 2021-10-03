package DataValidation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class dataValidation {

    //null form validation
    public static boolean textFieldIsNull(TextField inputTextField, Label inputLabel, String validationText){
        boolean isNull = false;
        String validationString = null;

        if (inputTextField.getText().isEmpty()){
            isNull = true;
            validationString = validationText;
        }

        inputLabel.setText(validationString);
        return isNull;
    }

    // data length form validation

//    public static boolean dataLength(TextField inputTextField, Label inputLabel, String validationText, String requiredLength){
//        boolean isDataLength = true;
//        String validationString = null;
//
//        if(!inputTextField.getText().matches("\\b\\w" + "{" + requiredLength + "}" + "\\b")){
//
//        }
//    }

}
