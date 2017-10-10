package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.AgentID;

public class LTAgentIDTest {

    @Test
    public void test() {
        Assert.assertEquals(6, AgentID.values().length);
    }

}
