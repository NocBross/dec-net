package test.java.controller;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.abstraction.RegisterData;
import main.java.agent.CustomBottomAgent;
import main.java.constants.ServerStatusCodes;
import main.java.controller.RegisterRequestController;
import main.java.controller.RegisterValidationController;
import main.java.message.LoginMessage;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;

public class RegisterRequestControllerTest {

    @Test
    public void test() {
        CustomBottomAgent agent = Mockito.mock(CustomBottomAgent.class);
        RegisterData data = new RegisterData();
        RegisterRequestController requestController = new RegisterRequestController(data,
                new RegisterValidationController(data));
        requestController.setAgent(agent);
        StringProperty nicknameProperty = new SimpleStringProperty();
        StringProperty addressProperty = new SimpleStringProperty();
        StringProperty passwordProperty = new SimpleStringProperty();
        StringProperty passwordRepeateProperty = new SimpleStringProperty();

        // mocking methods of CustomButtomAgent
        String messageInfo = "new message is send";
        String scatterInfo = "scatter message";
        List<String> spyMessageList = Mockito.spy(new LinkedList<String>());
        List<String> spyScatterList = Mockito.spy(new LinkedList<String>());
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                spyMessageList.add(messageInfo);
                return null;
            }

        }).when(agent).sendMessage(Mockito.anyString(), Mockito.anyString());
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                spyScatterList.add(scatterInfo);
                return null;
            }

        }).when(agent).scatterMessage(Mockito.anyString());

        // initialize test
        data.getNicknameProperty().bind(nicknameProperty);
        data.getPasswordProperty().bind(passwordProperty);
        data.getRepeatedPasswordProperty().bind(passwordRepeateProperty);

        // test receiveResult method
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertEquals(1, requestController.receiveResult(registerMessage.getMessage()));
        ReportMessage loginReport = new ReportMessage();
        loginReport.setReferencedMessage(LoginMessage.ID);
        Assert.assertEquals(1, requestController.receiveResult(loginReport.getMessage()));
        ReportMessage errorReport = new ReportMessage();
        errorReport.setReferencedMessage(RegisterMessage.ID);
        errorReport.setResult(false);
        errorReport.setStatusCode(ServerStatusCodes.REGISTER_CORRECT);
        Assert.assertEquals(ServerStatusCodes.REGISTER_CORRECT,
                requestController.receiveResult(errorReport.getMessage()));
        ReportMessage report = new ReportMessage();
        report.setReferencedMessage(RegisterMessage.ID);
        report.setResult(true);
        report.setStatusCode(ServerStatusCodes.REGISTER_CORRECT);
        Assert.assertEquals(ServerStatusCodes.REGISTER_CORRECT, requestController.receiveResult(report.getMessage()));

        // test sendRequest method
        nicknameProperty.set("");
        passwordProperty.set("");
        passwordRepeateProperty.set("");
        Assert.assertEquals(1, requestController.sendRequest());
        nicknameProperty.set("test");
        addressProperty.set("mail.de");
        Assert.assertEquals(2, requestController.sendRequest());
        passwordProperty.set("Uh_90!er45.");
        Assert.assertEquals(3, requestController.sendRequest());
        passwordRepeateProperty.set("Uh_90!er45.");
        Assert.assertEquals(0, requestController.sendRequest());
        Assert.assertEquals(1, spyMessageList.size());
        Assert.assertEquals(1, spyScatterList.size());
    }
}
