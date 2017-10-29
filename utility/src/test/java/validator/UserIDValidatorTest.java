package test.java.validator;

import org.junit.Assert;
import org.junit.Test;

import main.java.validator.UserIDValidator;

public class UserIDValidatorTest {

    @Test
    public void test() {
        UserIDValidator validator = new UserIDValidator();

        Assert.assertFalse(validator.validate("test@"));
        Assert.assertFalse(validator.validate("@mail.de"));
        Assert.assertFalse(validator.validate(" @mail.de"));
        Assert.assertFalse(validator.validate("@"));
        Assert.assertFalse(validator.validate("@mail de"));
        Assert.assertTrue(validator.validate("test@mail.de"));
    }

}
