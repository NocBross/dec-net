package test.java.agent;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javafx.stage.Stage;
import main.java.agent.CustomAgent;
import main.java.agent.CustomMiddleAgent;
import main.java.constants.AgentID;
import main.java.message.RDFMessage;

public class CustomMiddleAgentTest {

    private CustomAgent child = null;

    private class TestAgent extends CustomMiddleAgent {

        public TestAgent(CustomAgent parent, AgentID ID, Stage primaryStage) {
            super(parent, ID, primaryStage);
            addChild(child);
            activeAgent = child;
        }

    }

    @Test
    public void test() {
        testWithChildren();
        testWithoutChildren();
    }

    private void testWithChildren() {
        String url = "http://localhost/test";
        String model = "some model";
        CustomAgent parent = Mockito.mock(CustomAgent.class);
        child = Mockito.mock(CustomAgent.class);
        CustomMiddleAgent agent = new TestAgent(parent, AgentID.AUTHENTICATION_AGENT, null);
        RDFMessage rdfMessage = new RDFMessage(url, model);

        List<Integer> sceneSpy = new LinkedList<Integer>();
        Mockito.doAnswer(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                sceneSpy.add(new Integer(1));
                return null;
            }

        }).when(child).getScene();

        List<String> receiveMessageListSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                receiveMessageListSpy.add((String) invocation.getArguments()[0]);
                return null;
            }

        }).when(child).receiveMessage(Mockito.anyString());

        List<String> urlListSpy = new LinkedList<String>();
        List<String> messageListSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                urlListSpy.add((String) invocation.getArguments()[0]);
                messageListSpy.add(((RDFMessage) invocation.getArguments()[1]).getMessage());
                return null;
            }

        }).when(parent).sendMessage(Mockito.anyString(), Mockito.any());

        List<String> modelListSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                modelListSpy.add(((RDFMessage) invocation.getArguments()[0]).getMessage());
                return null;
            }

        }).when(parent).storeRDFModel(Mockito.any());

        Assert.assertEquals(AgentID.AUTHENTICATION_AGENT, agent.getID());

        Assert.assertNull(agent.getScene());
        Assert.assertEquals(1, sceneSpy.size());

        agent.receiveMessage(rdfMessage.getMessage());
        Assert.assertEquals(1, receiveMessageListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), receiveMessageListSpy.get(0));

        agent.scatterMessage(rdfMessage.getMessage());
        Assert.assertEquals(2, receiveMessageListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), receiveMessageListSpy.get(0));
        Assert.assertEquals(rdfMessage.getMessage(), receiveMessageListSpy.get(1));

        agent.sendMessage(url, rdfMessage);
        Assert.assertEquals(1, urlListSpy.size());
        Assert.assertEquals(url, urlListSpy.get(0));
        Assert.assertEquals(1, messageListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), messageListSpy.get(0));

        agent.storeRDFModel(rdfMessage);
        Assert.assertEquals(1, modelListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), modelListSpy.get(0));

        try {
            agent.switchAgent(AgentID.LOGIN_AGENT);
            throw new AssertionError();
        } catch (Exception e) {

        }
    }

    private void testWithoutChildren() {
        CustomMiddleAgent agent = new TestAgent(null, AgentID.AUTHENTICATION_AGENT, null);
        Assert.assertNull(agent.getScene());
    }

}
