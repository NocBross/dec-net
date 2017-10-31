package main.java.validator;

public class PortValidator implements Validator<Integer> {

    private final int MIN_PORT_NUMBER;
    private final int MAX_PORT_NUMBER;

    public PortValidator() {
        MIN_PORT_NUMBER = 0;
        MAX_PORT_NUMBER = 65535;
    }

    @Override
    public boolean validate(Integer intToValidate) {
        if (MIN_PORT_NUMBER <= intToValidate && intToValidate <= MAX_PORT_NUMBER) {
            return true;
        } else {
            return false;
        }
    }

}
