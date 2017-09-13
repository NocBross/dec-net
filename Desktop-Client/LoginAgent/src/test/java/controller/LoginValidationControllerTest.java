package test.java.controller;

import org.junit.Assert;
import org.junit.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.abstraction.LoginData;
import main.java.controller.LoginValidationController;

public class LoginValidationControllerTest {

	@Test
	public void test() {
		LoginData data = new LoginData();
		LoginValidationController validController = new LoginValidationController(data);
		StringProperty password = new SimpleStringProperty();

		// initialize test
		data.getPasswordProperty().bind(password);

		// test password validation
		password.set("4t_");
		Assert.assertFalse(validController.validatePassword());
		password.set("4t_Ku");
		Assert.assertFalse(validController.validatePassword());
		password.set("G8_!j?H5i9.6p?z");
		Assert.assertTrue(validController.validatePassword());
	}
}
