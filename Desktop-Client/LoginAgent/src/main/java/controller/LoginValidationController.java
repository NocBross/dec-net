package main.java.controller;

import main.java.abstraction.LoginData;
import main.java.validator.PasswordValidator;

/**
 * The ValidationController checks whether the stored data is valid in the sense
 * of the software.<br>
 * It is used by the LoginRequestController before the LoginMessage will be
 * generated.
 * 
 * @author developer
 *
 */

public class LoginValidationController {

	private LoginData data;
	private PasswordValidator passwordValidator;

	public LoginValidationController(LoginData data) {
		this.data = data;
		passwordValidator = new PasswordValidator();
	}

	/**
	 * Checks if the stored nickname is not empty.
	 * 
	 * @return true if the stored nickname is not empty
	 */
	public boolean validateNickname() {
		return !data.getNickname().isEmpty();
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
