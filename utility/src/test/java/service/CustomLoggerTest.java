package test.java.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import main.java.service.CustomLogger;

public class CustomLoggerTest {

    @Test
    public void test() {
        try {
            String line = "";
            String testMessage = "a test message";
            String path = "logs/test_log.txt";
            File testFile = new File(path);
            CustomLogger testLogger = new CustomLogger(path);
            BufferedReader reader = new BufferedReader(new FileReader(testFile));

            // test writeLog
            testLogger.writeLog(testMessage, null);
            line = reader.readLine();
            String date = line.substring(1, line.indexOf(" "));
            String time = line.substring(line.indexOf(" ") + 1, line.lastIndexOf("]"));
            String message = line.substring(line.lastIndexOf("]") + 2);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Assert.assertTrue(DateUtils.isSameDay(new Date(), dateFormat.parse(date)));

            dateFormat = new SimpleDateFormat("HH:mm");
            String dateTime = dateFormat.format(new Date());
            Assert.assertEquals(dateTime, time);

            Assert.assertEquals(testMessage, message);

            // close logger and open it again
            testLogger.close();
            reader.close();
            testLogger = new CustomLogger(path);
            testLogger.writeLog(testMessage, new IOException());
            reader = new BufferedReader(new FileReader(testFile));
            int lines = 0;
            while (reader.readLine() != null) {
                lines++;
            }
            Assert.assertEquals(27, lines);

            // test thread save
            testFile.delete();
            final int seconds = 5;
            final CustomLogger logger = new CustomLogger(path);
            Thread t1 = new Thread() {

                public void run() {
                    long dead = System.currentTimeMillis() + seconds * 1000;
                    while (System.currentTimeMillis() <= dead) {
                        logger.writeLog("Thread-1", null);
                    }
                }
            };
            Thread t2 = new Thread() {

                public void run() {
                    long dead = System.currentTimeMillis() + seconds * 1000;
                    while (System.currentTimeMillis() <= dead) {
                        logger.writeLog("Thread-2", null);
                    }
                }
            };
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            reader.close();
            reader = new BufferedReader(new FileReader(testFile));
            dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            while ((line = reader.readLine()) != null) {
                Assert.assertNotNull(dateFormat.parse(line.substring(1, line.lastIndexOf("]"))));
                Assert.assertEquals("Thread", line.substring(line.lastIndexOf("]") + 2, line.lastIndexOf("-")));
                Assert.assertEquals(27, line.length());
            }

            // clean up
            reader.close();
            testLogger.close();
            logger.close();
            testFile.delete();
            testFile.getParentFile().delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}
