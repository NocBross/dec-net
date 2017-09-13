package test.java.abstraction;

import org.junit.Assert;
import org.junit.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.abstraction.RegisterData;

public class RegisterDataTest {

	@Test
	public void test() {
		RegisterData data = new RegisterData();
		StringProperty mailProperty = new SimpleStringProperty();
		StringProperty passwordProperty = new SimpleStringProperty();
		StringProperty passwordRepeateProperty = new SimpleStringProperty();

		// initialize test
		data.getNicknameProperty().bind(mailProperty);
		data.getPasswordProperty().bind(passwordProperty);
		data.getRepeatedPasswordProperty().bind(passwordRepeateProperty);

		// test mail property binding
		mailProperty.set("test");
		Assert.assertEquals("test", data.getNickname());
		mailProperty.set("test@");
		Assert.assertEquals("test@", data.getNickname());
		mailProperty.set("test@mail");
		Assert.assertEquals("test@mail", data.getNickname());
		mailProperty.set("test_test@mail");
		Assert.assertEquals("test_test@mail", data.getNickname());

		// test password property binding
		passwordProperty.set("test");
		Assert.assertEquals("test", data.getPassword());
		passwordProperty.set("test123");
		Assert.assertEquals("test123", data.getPassword());
		passwordProperty.set("test_23!");
		Assert.assertEquals("test_23!", data.getPassword());
		passwordProperty.set("g?75");
		Assert.assertEquals("g?75", data.getPassword());

		// test repeated password property binding
		passwordRepeateProperty.set("test");
		Assert.assertEquals("test", data.getRepeatedPassword());
		passwordRepeateProperty.set("test123");
		Assert.assertEquals("test123", data.getRepeatedPassword());
		passwordRepeateProperty.set("test_23!");
		Assert.assertEquals("test_23!", data.getRepeatedPassword());
		passwordRepeateProperty.set("g?75");
		Assert.assertEquals("g?75", data.getRepeatedPassword());

		// test unbind
		data.getNicknameProperty().unbind();
		data.getPasswordProperty().unbind();
		data.getRepeatedPasswordProperty().unbind();
		Assert.assertEquals("test_test@mail", data.getNickname());
		Assert.assertEquals("g?75", data.getPassword());
		Assert.assertEquals("g?75", data.getRepeatedPassword());
	}
}
