package DataValidation;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class dataValidation {

    public static boolean checkValidation(TextField inputTextField, Label inputLabel, int validationNumber, String numberLength) {
        if (inputTextField.getText().isEmpty()) {
            inputLabel.setText("Field cannot be empty");
            return false;
        } else
        {
            switch (validationNumber) {
                case 1:
                    return textAlphabet(inputTextField, inputLabel, "Field may only contain letters");
                case 2:
                    Boolean numeric = textNumeric(inputTextField, inputLabel, "Field may only contain numbers");
                    Boolean length = dataLength(inputTextField, inputLabel, "Number must be 10 digits", numberLength);
                    if (numeric && length)
                        return true;
                    else return false;
                case 3:
                    return emailFormat(inputTextField, inputLabel, "Invalid email format, (Example.gmail.com)");
                default: {
                    inputLabel.setText("");
                    return true;
                }
            }
        }
    }

    //null form validation
    /*public static boolean textFieldIsNull(TextField inputTextField, Label inputLabel, String validationText){
            boolean isNull = false;
            String validationString = null;

            if (inputTextField.getText().isEmpty()){
                isNull = true;
                validationString = validationText;
            }

            inputLabel.setText(validationString);
            return isNull;
        }*/

//     data length form validation
    public static boolean dataLength(TextField inputTextField, Label inputLabel, String validationText, String requiredLength){
        boolean isDataLength = true;
        String validationString = null;

        if(!inputTextField.getText().matches("\\b\\w" + "{" + requiredLength + "}" + "\\b")){

            isDataLength = false;
            validationString = validationText;
        }
        inputLabel.setText(validationString);
        return isDataLength;
    }

    //only allows inputs with alphabet letters
    public static boolean textAlphabet(TextField inputTextField, Label inputLabel, String validationText){
        boolean isAlphabet = true;
        String validationString = null;

        if(!inputTextField.getText().matches("[a-z A-Z]+")){
            isAlphabet = false;
            validationString = validationText;
        }
        inputLabel.setText(validationString);
        System.out.println(inputTextField.getText().matches("[a-z A-Z]"));
        return isAlphabet;
    }

    //only allows inputs with numbers
    public static boolean textNumeric(TextField inputTextField, Label inputLabel, String validationText){
        boolean isNumeric = true;
        String validationString = null;

        if(!inputTextField.getText().matches("[0-9]+")){
            isNumeric = false;
            validationString = validationText;
        }
        inputLabel.setText(validationString);
        return isNumeric;
    }

    //only allows email format
    public static boolean emailFormat(TextField inputTextField, Label inputLabel, String validationText){
        boolean isEmail = true;
        String validationString = null;

        if(!inputTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com")){
            isEmail = false;
            validationString = validationText;
        }
        inputLabel.setText(validationString);
        return isEmail;
    }

}
