package main.java.abstraction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RegisterData {

	private StringProperty nickname;
	private StringProperty password;
	private StringProperty repeatedPassword;

	public RegisterData() {
		nickname = new SimpleStringProperty();
		password = new SimpleStringProperty();
		repeatedPassword = new SimpleStringProperty();
	}

	/**
	 * Returns the current mail.
	 * 
	 * @return current mail
	 */
	public String getNickname() {
		return nickname.get();
	}

	/**
	 * Returns the mail property.
	 * 
	 * @return
	 */
	public StringProperty getNicknameProperty() {
		return nickname;
	}

	/**
	 * Returns the current password.
	 * 
	 * @return current password
	 */
	public String getPassword() {
		return password.get();
	}

	/**
	 * Returns the password property.
	 * 
	 * @return password property
	 */
	public StringProperty getPasswordProperty() {
		return password;
	}

	/**
	 * Returns the current repeat password
	 * 
	 * @return current repeat password
	 */
	public String getRepeatedPassword() {
		return repeatedPassword.get();
	}

	/**
	 * Returns the repeat password property.
	 * 
	 * @return repeat password property
	 */
	public StringProperty getRepeatedPasswordProperty() {
		return repeatedPassword;
	}
}
