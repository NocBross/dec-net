package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.LogFiles;

public class LogFilesTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        LogFiles testClass = new LogFiles();

        Assert.assertEquals("logs/log_", LogFiles.LOG_PREFIX);
        Assert.assertEquals(".txt", LogFiles.LOG_POSTFIX);
        Assert.assertEquals(LogFiles.LOG_PREFIX + "login" + LogFiles.LOG_POSTFIX, LogFiles.LOGIN_LOG);
        Assert.assertEquals(LogFiles.LOG_PREFIX + "register" + LogFiles.LOG_POSTFIX, LogFiles.REGISTER_LOG);

        Assert.assertEquals(LogFiles.LOG_PREFIX, testClass.LOG_PREFIX);
        Assert.assertEquals(LogFiles.LOG_POSTFIX, testClass.LOG_POSTFIX);
        Assert.assertEquals(LogFiles.LOGIN_LOG, testClass.LOGIN_LOG);
        Assert.assertEquals(LogFiles.REGISTER_LOG, testClass.REGISTER_LOG);
    }

}
