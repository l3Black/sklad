package ru.test_task.sklad.service;

import com.ibatis.common.jdbc.ScriptRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import ru.test_task.sklad.dao.EmFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractServiceTest {
    private static final Logger log = getLogger("result");

    private static StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @BeforeClass
    public static void clearStringBuilder() {
        results.setLength(0);
    }

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Before
    public void evictCache() {

    }

    @Before
    public void populateDb() {
        EmFactory.factory.getCache().evictAll();

        String aSQLScriptFilePath = "src/main/resources/db/populateDB.sql";
        String pathToProp = "src/test/resources/db.config.properties";
        File prop = new File(pathToProp);
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(prop));
            Class.forName(properties.getProperty("driver"));
            Connection con = DriverManager.getConnection(
                    properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
            ScriptRunner sr = new ScriptRunner(con, Boolean.parseBoolean(properties.getProperty("autoCommit")),
                    Boolean.parseBoolean(properties.getProperty("stopOnError")));
            Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }
}
