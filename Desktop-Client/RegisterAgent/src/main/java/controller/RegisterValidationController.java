package main.java.controller;

import main.java.abstraction.RegisterData;
import main.java.validator.PasswordValidator;
import main.java.validator.UserIDValidator;

public class RegisterValidationController {

    private RegisterData data;
    private PasswordValidator passwordValidator;
    private UserIDValidator userIDValidator;

    public RegisterValidationController(RegisterData data) {
        this.data = data;
        passwordValidator = new PasswordValidator();
        userIDValidator = new UserIDValidator();
    }

    /**
     * Checks if both passwords are equal.
     * 
     * @return true in case of equality, false otherwise
     */
    public boolean checkForPasswordEquality() {
        return data.getPassword().equals(data.getRepeatedPassword());
    }

    /**
     * Checks if the stored nickname is not empty.
     * 
     * @return true if the stored nickname is not empty
     */
    public boolean validateUserID() {
        return userIDValidator.validate(data.getUserID());
    }

    /**
     * Checks if the stored password is a valid password.
     * 
     * @return true in case of the stored password is a valid one, false otherwise
     */
    public boolean validatePassword() {
        return passwordValidator.validate(data.getPassword());
    }
}
