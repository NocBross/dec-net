package test.java.controller;

import org.junit.Assert;
import org.junit.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.abstraction.RegisterData;
import main.java.controller.RegisterValidationController;

public class RegisterValidationControllerTest {

    @Test
    public void test() {
        RegisterData data = new RegisterData();
        RegisterValidationController validController = new RegisterValidationController(data);
        StringProperty nicknameProperty = new SimpleStringProperty();
        StringProperty resourceHubAddressProperty = new SimpleStringProperty();
        StringProperty passwordProperty = new SimpleStringProperty();
        StringProperty passwordRepeateProperty = new SimpleStringProperty();

        // initialize test
        data.getNicknameProperty().bind(nicknameProperty);
        data.getResourceHubAddressProperty().bind(resourceHubAddressProperty);
        data.getPasswordProperty().bind(passwordProperty);
        data.getRepeatedPasswordProperty().bind(passwordRepeateProperty);

        // test mail validation
        nicknameProperty.set("");
        resourceHubAddressProperty.set("");
        Assert.assertFalse(validController.validateUserID());
        nicknameProperty.set("test");
        resourceHubAddressProperty.set("mail");
        Assert.assertTrue(validController.validateUserID());

        // test password validation
        passwordProperty.set("4t_");
        Assert.assertFalse(validController.validatePassword());
        passwordProperty.set("4t_Ku");
        Assert.assertFalse(validController.validatePassword());
        passwordProperty.set("G8_!j?H5i9.6p?z");
        Assert.assertTrue(validController.validatePassword());

        // test password validation
        passwordRepeateProperty.set("4t_");
        Assert.assertFalse(validController.checkForPasswordEquality());
        passwordRepeateProperty.set("4t_Ku");
        Assert.assertFalse(validController.checkForPasswordEquality());
        passwordRepeateProperty.set("G8_!j?H5i9.6p?z");
        Assert.assertTrue(validController.checkForPasswordEquality());
    }
}
