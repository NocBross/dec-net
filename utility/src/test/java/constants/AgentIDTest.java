package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.AgentID;

public class AgentIDTest {

    @Test
    public void test() {
        Assert.assertEquals(7, AgentID.values().length);
    }

}
