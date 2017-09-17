package test.java.validator;

import org.junit.Assert;
import org.junit.Test;

import main.java.validator.PasswordValidator;
import main.java.validator.Validator;

public class PasswordValidatorTest {

    @Test
    public void test() {
        Validator validator = new PasswordValidator();

        Assert.assertFalse(validator.validate("1"));
        Assert.assertFalse(validator.validate("a"));
        Assert.assertFalse(validator.validate("."));
        Assert.assertFalse(validator.validate("D"));
        Assert.assertFalse(validator.validate("cdnrg856"));
        Assert.assertFalse(validator.validate("fvIFt485"));
        Assert.assertFalse(validator.validate("vfgb85494O   IQEGH895    "));
        Assert.assertFalse(validator.validate("HVDK87jvr854kdji"));
        Assert.assertFalse(validator.validate("nu_8?"));
        Assert.assertFalse(validator.validate("a9_oB!j5."));

        Assert.assertFalse(validator.validate("System.exit(0)"));
        Assert.assertFalse(validator.validate("SELECT * FROM user1"));
        Assert.assertFalse(validator.validate("1=1"));
        Assert.assertFalse(validator.validate("Übergang5!"));
        Assert.assertFalse(validator.validate("Test-Test"));
        Assert.assertFalse(validator.validate("42;UPDATE+USER+SET+TYPE=\"admin\"+WHERE+ID=23"));
        Assert.assertFalse(validator.validate("42;UPDATE USER SET TYPE=\"admin\" WHERE ID=23"));
        Assert.assertFalse(validator.validate("sql'+;GO+EXEC+cmdshell('shutdown+/s')+--"));
        Assert.assertFalse(validator.validate("sql' ;GO EXEC cmdshell('shutdown /s') --"));

        Assert.assertTrue(validator.validate("G8_j?H4%i9.6p"));
        Assert.assertTrue(validator.validate("a9_oB!j5.Z"));
        Assert.assertTrue(validator.validate("Test1...Test2..."));
    }
}
