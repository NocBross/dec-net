package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.RegisterMessage;
import main.java.message.SearchMessage;

public class SearchMessageTest {

	@Test
	public void test() {
		String testNickname = "myID";
		SearchMessage message = new SearchMessage();

		// test initial values
		Assert.assertEquals("search", message.getType());
		Assert.assertNotNull(message.getNicknames());

		// test nickname methods
		Assert.assertTrue(message.addNickname(testNickname));
		Assert.assertFalse(message.addNickname(testNickname));
		Assert.assertEquals(1, message.getNicknames().size());
		Assert.assertEquals(testNickname, message.getNicknames().get(0));

		// test convert
		String testString = null;
		Assert.assertNull(SearchMessage.parse(testString));
		testString = "Test123";
		Assert.assertNull(SearchMessage.parse(testString));
		RegisterMessage registerMessage = new RegisterMessage();
		Assert.assertNull(SearchMessage.parse(registerMessage.toString()));
		SearchMessage testMessage = SearchMessage.parse(message.getMessage());
		Assert.assertNotNull(testMessage);
		Assert.assertEquals(message.getMessage(), testMessage.getMessage());

	}

}
