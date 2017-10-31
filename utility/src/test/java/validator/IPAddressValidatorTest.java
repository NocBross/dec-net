package test.java.validator;

import org.junit.Assert;
import org.junit.Test;

import main.java.validator.IPAddressValidator;
import main.java.validator.Validator;

public class IPAddressValidatorTest {

    @Test
    public void test() {
        Validator<String> ipValidator = new IPAddressValidator();

        // test IPv4
        Assert.assertFalse(ipValidator.validate("1.0.5"));
        Assert.assertFalse(ipValidator.validate("test"));
        Assert.assertTrue(ipValidator.validate("192.168.0.0"));

        // test IPv6
        Assert.assertFalse(ipValidator.validate("jewifgero"));
        Assert.assertFalse(ipValidator.validate("2001:0db8:85a3:08d3::0370:7344"));
        Assert.assertTrue(ipValidator.validate("2001:0db8:85a3:08d3:1319:8a2e:0370:7344"));
    }

}
