package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.UserStatus;

public class StatusTest {

	@Test
	public void test() {
		Assert.assertEquals(3, UserStatus.values().length);
	}

}
