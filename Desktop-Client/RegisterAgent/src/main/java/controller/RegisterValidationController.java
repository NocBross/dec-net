package main.java.controller;

import main.java.abstraction.RegisterData;
import main.java.validator.PasswordValidator;

public class RegisterValidationController {

	private RegisterData data;
	private PasswordValidator passwordValidator;

	public RegisterValidationController(RegisterData data) {
		this.data = data;
		passwordValidator = new PasswordValidator();
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
