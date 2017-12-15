package test.java.agent;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.message.RDFMessage;

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
        String url = "http://localhost/test";
        String model = "some model";
        CustomAgent parent = Mockito.mock(CustomAgent.class);
        RootController controller = Mockito.mock(RootController.class);
        TestAgent agent = new TestAgent(parent, AgentID.CLIENT_AGENT);
        RDFMessage rdfMessage = new RDFMessage(url, model);

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

        agent.receiveMessage(rdfMessage.getMessage());
        Assert.assertEquals(1, receiveMessageListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), receiveMessageListSpy.get(0));

        agent.scatterMessage(rdfMessage.getMessage());
        Assert.assertEquals(1, scatterMessageListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), scatterMessageListSpy.get(0));

        agent.sendMessage(url, rdfMessage);
        Assert.assertEquals(1, urlListSpy.size());
        Assert.assertEquals(url, urlListSpy.get(0));
        Assert.assertEquals(1, messageListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), messageListSpy.get(0));

        agent.storeRDFModel(rdfMessage);
        Assert.assertEquals(1, modelListSpy.size());
        Assert.assertEquals(rdfMessage.getMessage(), modelListSpy.get(0));

        agent.switchAgent(AgentID.LOGIN_AGENT);
        Assert.assertEquals(1, switchAgentSpyList.size());
        Assert.assertEquals(AgentID.LOGIN_AGENT, switchAgentSpyList.get(0));
    }

}
