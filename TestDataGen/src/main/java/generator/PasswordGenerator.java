package main.java.generator;

import main.java.constants.Security;
import main.java.validator.PasswordValidator;
import main.java.validator.Validator;

public class PasswordGenerator {

    private int keyLength;
    private Validator<String> passwordValidator;

    public PasswordGenerator() {
        keyLength = Security.MIN_PASSWORD_LENGTH;

        passwordValidator = new PasswordValidator();
    }

    /**
     * Generates a random password for a new test user.
     * 
     * @return random password
     */
    public String generate() {
        String password = "";
        int randomPosition = 0;

        do {
            password = "";
            for (int i = 0; i < keyLength; i++) {
                randomPosition = (int) ((1 + Math.random() * Security.PASSWORD_CHARACTERS.length()) - 1);
                password += Security.PASSWORD_CHARACTERS.charAt(randomPosition);
            }
        } while (!passwordValidator.validate(password));

        return password;
    }

}
