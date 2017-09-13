package main.java.abstraction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class stores all data which is used by the login scene and there
 * controllers.
 * 
 * @author developer
 *
 */

public class LoginData {

	private StringProperty nickname;
	private StringProperty password;

	public LoginData() {
		nickname = new SimpleStringProperty();
		password = new SimpleStringProperty();
	}

	/**
	 * Returns the current stored mail address.
	 * 
	 * @return mail address
	 */
	public String getNickname() {
		return nickname.get();
	}

	/**
	 * Returns the mail property which is the wrapper for the stored mail address.
	 * 
	 * @return mail property
	 */
	public StringProperty getNicknameProperty() {
		return nickname;
	}

	/**
	 * Returns the current stored password.
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password.get();
	}

	/**
	 * Returns the password property which is the wrapper for the stored password.
	 * 
	 * @return password property
	 */
	public StringProperty getPasswordProperty() {
		return password;
	}
}
