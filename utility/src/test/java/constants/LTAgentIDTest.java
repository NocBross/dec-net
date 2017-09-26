package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.LTAgentID;

public class LTAgentIDTest {

    @Test
    public void test() {
        Assert.assertEquals(4, LTAgentID.values().length);
    }

}
