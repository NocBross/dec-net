package test.java.agent;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;

public class CustomBottomAgentTest {

    private class TestAgent extends CustomBottomAgent {

        public TestAgent(CustomAgent parent, AgentID ID) {
            super(parent, ID);
        }

        public void setRootController(RootController controller) {
            agentSceneController = controller;
        }

    }

    @Test
    public void test() {
        String destination = "test destination";
        String resource = "/search?nickname=test";
        String message = "test message";
        CustomAgent parent = Mockito.mock(CustomAgent.class);
        RootController controller = Mockito.mock(RootController.class);
        TestAgent agent = new TestAgent(parent, AgentID.CLIENT_AGENT);

        List<String> receiveMessageListSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                receiveMessageListSpy.add((String) invocation.getArguments()[0]);
                return null;
            }

        }).when(controller).receiveResult(Mockito.anyString());
        agent.setRootController(controller);

        List<String> scatterMessageListSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                scatterMessageListSpy.add((String) invocation.getArguments()[0]);
                return null;
            }

        }).when(parent).scatterMessage(Mockito.anyString());

        List<String> destinationListSpy = new LinkedList<String>();
        List<String> resourceListSpy = new LinkedList<String>();
        List<String> messageListSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                destinationListSpy.add((String) invocation.getArguments()[0]);
                resourceListSpy.add((String) invocation.getArguments()[1]);
                messageListSpy.add((String) invocation.getArguments()[2]);
                return null;
            }

        }).when(parent).sendMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        List<Model> modelListSpy = new LinkedList<Model>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                resourceListSpy.add((String) invocation.getArguments()[0]);
                modelListSpy.add((Model) invocation.getArguments()[1]);
                return null;
            }

        }).when(parent).storeRDFModel(Mockito.anyString(), Mockito.any());

        List<AgentID> switchAgentSpyList = new LinkedList<AgentID>();
        Mockito.doAnswer(new Answer<AgentID>() {

            @Override
            public AgentID answer(InvocationOnMock invocation) throws Throwable {
                switchAgentSpyList.add((AgentID) invocation.getArguments()[0]);
                return null;
            }

        }).when(parent).switchAgent(Mockito.any());

        Assert.assertEquals(AgentID.CLIENT_AGENT, agent.getID());
        Assert.assertNull(agent.getScene());

        agent.receiveMessage(message);
        Assert.assertEquals(1, receiveMessageListSpy.size());
        Assert.assertEquals(message, receiveMessageListSpy.get(0));

        agent.scatterMessage(message);
        Assert.assertEquals(1, scatterMessageListSpy.size());
        Assert.assertEquals(message, scatterMessageListSpy.get(0));

        agent.sendMessage(destination, resource, message);
        Assert.assertEquals(1, destinationListSpy.size());
        Assert.assertEquals(destination, destinationListSpy.get(0));
        Assert.assertEquals(1, resourceListSpy.size());
        Assert.assertEquals(resource, resourceListSpy.get(0));
        Assert.assertEquals(1, messageListSpy.size());
        Assert.assertEquals(message, messageListSpy.get(0));

        agent.storeRDFModel(destination, null);
        Assert.assertEquals(2, resourceListSpy.size());
        Assert.assertEquals(destination, resourceListSpy.get(1));
        Assert.assertEquals(1, modelListSpy.size());
        Assert.assertNull(modelListSpy.get(0));

        agent.switchAgent(AgentID.LOGIN_AGENT);
        Assert.assertEquals(1, switchAgentSpyList.size());
        Assert.assertEquals(AgentID.LOGIN_AGENT, switchAgentSpyList.get(0));
    }

}
