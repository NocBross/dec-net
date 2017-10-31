package test.java.validator;

import org.junit.Assert;
import org.junit.Test;

import main.java.validator.PortValidator;
import main.java.validator.Validator;

public class PortValidatorTest {

    @Test
    public void test() {
        Validator<Integer> portValidator = new PortValidator();

        Assert.assertFalse(portValidator.validate(-36));
        Assert.assertTrue(portValidator.validate(16755));
        Assert.assertFalse(portValidator.validate(18798498));
    }

}
