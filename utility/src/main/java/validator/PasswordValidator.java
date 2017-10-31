package main.java.validator;

import java.util.regex.Pattern;

import main.java.constants.Security;

/**
 * This class checks if the given string is a correct password.<br>
 * A correct password has at least one small letter, one big letter, one number,
 * one special symbol like ! or ? and at least 10 characters.<br>
 * <br>
 * Available characters are a-z, A-Z, 0-9 and %!#$*+.,-/:;=?@_<>
 * 
 * @author Kay Oliver Szesny
 *
 */

public class PasswordValidator implements Validator<String> {

    private final String PASSWORD_PATTERN;
    private final Pattern validatorPattern;

    public PasswordValidator() {
        PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[%!#$*+.,-/:;=?@_<>]).{" + Security.MIN_PASSWORD_LENGTH
                + ",})";
        validatorPattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public boolean validate(String stringToValidate) {
        boolean isValid = validatorPattern.matcher(stringToValidate).matches();

        if (isValid) {
            for (int i = 0; i < stringToValidate.length(); i++) {
                if (Security.PASSWORD_CHARACTERS.indexOf(stringToValidate.charAt(i)) == -1) {
                    isValid = false;
                    break;
                }
            }
        }

        return isValid;
    }
}
