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
import main.java.abstraction.LoginData;
import main.java.agent.CustomBottomAgent;
import main.java.controller.LoginRequestController;
import main.java.message.LoginMessage;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;

public class LoginRequestControllerTest {

	@Test
	public void test() {
		CustomBottomAgent agent = Mockito.mock(CustomBottomAgent.class);
		LoginData data = new LoginData();
		LoginRequestController requestController = new LoginRequestController(data);
		requestController.setAgent(agent);
		StringProperty mail = new SimpleStringProperty();
		StringProperty password = new SimpleStringProperty();

		// mocking methods of CustomButtomAgent
		String messageInfo = "new message is send";
		List<String> spyMessageList = Mockito.spy(new LinkedList<String>());
		Mockito.doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				spyMessageList.add(messageInfo);
				return null;
			}

		}).when(agent).sendMessage(Mockito.anyString(), Mockito.anyString());

		// initialize test
		data.getNicknameProperty().bind(mail);
		data.getPasswordProperty().bind(password);

		// test receiveResult method
		LoginMessage loginMessage = new LoginMessage();
		Assert.assertEquals(1, requestController.receiveResult(loginMessage.getMessage()));
		ReportMessage registerReport = new ReportMessage();
		registerReport.setReferencedMessage(RegisterMessage.ID);
		Assert.assertEquals(1, requestController.receiveResult(registerReport.getMessage()));
		ReportMessage errorReport = new ReportMessage();
		errorReport.setReferencedMessage(LoginMessage.ID);
		errorReport.setResult(false);
		errorReport.setErrorCode("Test");
		Assert.assertEquals(2, requestController.receiveResult(errorReport.getMessage()));
		ReportMessage report = new ReportMessage();
		report.setReferencedMessage(LoginMessage.ID);
		report.setResult(true);
		Assert.assertEquals(0, requestController.receiveResult(report.getMessage()));

		// test sendRequest method
		mail.set("");
		password.set("");
		Assert.assertEquals(1, requestController.sendRequest());
		mail.set("test@mail.de");
		Assert.assertEquals(1, requestController.sendRequest());
		mail.set("");
		password.set("Uh_90!er45.");
		Assert.assertEquals(1, requestController.sendRequest());
		mail.set("test@mail.de");
		Assert.assertEquals(0, requestController.sendRequest());
		Assert.assertEquals(1, spyMessageList.size());
	}
}
