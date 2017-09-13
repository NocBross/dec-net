package test.java.abstraction;

import org.junit.Assert;
import org.junit.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.abstraction.LoginData;

public class LoginDataTest {

	@Test
	public void test() {
		LoginData data = new LoginData();
		StringProperty mail = new SimpleStringProperty();
		StringProperty password = new SimpleStringProperty();

		// initialize test
		data.getNicknameProperty().bind(mail);
		data.getPasswordProperty().bind(password);

		// test mail property binding
		mail.set("test");
		Assert.assertEquals("test", data.getNickname());
		mail.set("test@");
		Assert.assertEquals("test@", data.getNickname());
		mail.set("test@mail");
		Assert.assertEquals("test@mail", data.getNickname());
		mail.set("test_test@mail");
		Assert.assertEquals("test_test@mail", data.getNickname());

		// test password property binding
		password.set("test");
		Assert.assertEquals("test", data.getPassword());
		password.set("test123");
		Assert.assertEquals("test123", data.getPassword());
		password.set("test_23!");
		Assert.assertEquals("test_23!", data.getPassword());
		password.set("g?75");
		Assert.assertEquals("g?75", data.getPassword());

		// test unbind
		data.getNicknameProperty().unbind();
		data.getPasswordProperty().unbind();
		Assert.assertEquals("test_test@mail", data.getNickname());
		Assert.assertEquals("g?75", data.getPassword());
	}
}
