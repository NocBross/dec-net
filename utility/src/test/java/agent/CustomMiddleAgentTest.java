package test.java.agent;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javafx.stage.Stage;
import main.java.agent.CustomAgent;
import main.java.agent.CustomMiddleAgent;
import main.java.constants.AgentID;

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
        String destination = "test destination";
        String message = "test message";
        CustomAgent parent = Mockito.mock(CustomAgent.class);
        child = Mockito.mock(CustomAgent.class);
        CustomMiddleAgent agent = new TestAgent(parent, AgentID.AUTHENTICATION_AGENT, null);

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

        List<String> resourceListSpy = new LinkedList<String>();
        List<Model> modelListSpy = new LinkedList<Model>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                resourceListSpy.add((String) invocation.getArguments()[0]);
                modelListSpy.add((Model) invocation.getArguments()[1]);
                return null;
            }

        }).when(parent).storeRDFModel(Mockito.anyString(), Mockito.any());

        Assert.assertEquals(AgentID.AUTHENTICATION_AGENT, agent.getID());

        Assert.assertNull(agent.getScene());
        Assert.assertEquals(1, sceneSpy.size());

        agent.receiveMessage(message);
        Assert.assertEquals(1, receiveMessageListSpy.size());
        Assert.assertEquals(message, receiveMessageListSpy.get(0));

        agent.scatterMessage(message);
        Assert.assertEquals(2, receiveMessageListSpy.size());
        Assert.assertEquals(message, receiveMessageListSpy.get(0));
        Assert.assertEquals(message, receiveMessageListSpy.get(1));

        agent.sendMessage(destination, message);
        Assert.assertEquals(1, destinationListSpy.size());
        Assert.assertEquals(destination, destinationListSpy.get(0));
        Assert.assertEquals(1, sendMessageListSpy.size());
        Assert.assertEquals(message, sendMessageListSpy.get(0));

        agent.storeRDFModel(destination, null);
        Assert.assertEquals(1, resourceListSpy.size());
        Assert.assertEquals(destination, resourceListSpy.get(0));
        Assert.assertEquals(1, modelListSpy.size());
        Assert.assertNull(modelListSpy.get(0));

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
