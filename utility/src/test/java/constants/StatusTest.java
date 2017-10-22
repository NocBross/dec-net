package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Status;

public class StatusTest {

	@Test
	public void test() {
		Assert.assertEquals(3, Status.values().length);
	}

}
