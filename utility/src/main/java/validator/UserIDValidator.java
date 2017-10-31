package main.java.validator;

public class UserIDValidator implements Validator<String> {

    @Override
    public boolean validate(String stringToValidate) {
        String[] userID = stringToValidate.split("@");
        if (userID.length != 2) {
            return false;
        }

        if (userID[0].equals("") || userID[0].indexOf(" ") != -1) {
            return false;
        }

        if (userID == null || userID[1].indexOf(" ") != -1) {
            return false;
        }

        return true;
    }

}
