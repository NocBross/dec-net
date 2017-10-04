package test.java.agent;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import main.java.agent.CustomAgent;
import main.java.agent.CustomMiddleAgent;
import main.java.constants.AgentID;

public class CustomMiddleAgentTest {

	@Test
	public void test() {
		String destination = "test destination";
		String message = "test message";
		CustomAgent parent = Mockito.mock(CustomAgent.class);
		CustomMiddleAgent agent = new CustomMiddleAgent(parent, AgentID.CLIENT_AGENT, null);

		List<String> destinationListSpy = new LinkedList<String>();
		List<String> sendMessageListSpy = new LinkedList<String>();
		Mockito.doAnswer(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				destinationListSpy.add((String) invocation.getArguments()[0]);
				sendMessageListSpy.add((String) invocation.getArguments()[1]);
				return null;
			}

		}).when(parent).sendMessage(Mockito.anyString(), Mockito.anyString());

		Assert.assertEquals(AgentID.CLIENT_AGENT, agent.getID());
		Assert.assertNull(agent.getScene());
		agent.scatterMessage(message);
		agent.receiveMessage(message);

		agent.sendMessage(destination, message);
		Assert.assertEquals(1, destinationListSpy.size());
		Assert.assertEquals(destination, destinationListSpy.get(0));
		Assert.assertEquals(1, sendMessageListSpy.size());
		Assert.assertEquals(message, sendMessageListSpy.get(0));

		try {
			agent.switchAgent(AgentID.LOGIN_AGENT);
			throw new AssertionError();
		} catch (Exception e) {

		}
	}

}
