package main.java.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressValidator implements Validator<String> {

    private final String ipv4Pattern;
    private final String ipv6Pattern;
    private Pattern ipv4Validator;
    private Pattern ipv6Validator;

    public IPAddressValidator() {
        ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";

        ipv4Validator = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
        ipv6Validator = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean validate(String stringToValidate) {
        Matcher m1 = ipv4Validator.matcher(stringToValidate);
        if (m1.matches()) {
            return true;
        }
        Matcher m2 = ipv6Validator.matcher(stringToValidate);
        return m2.matches();
    }

}
