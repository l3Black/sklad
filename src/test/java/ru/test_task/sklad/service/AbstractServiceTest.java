package ru.test_task.sklad.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.test_task.sklad.util.SqlUtil.initDb;
import static ru.test_task.sklad.util.SqlUtil.populateDb;

public abstract class AbstractServiceTest {
    private static final Logger log = getLogger("result");

    private static StringBuilder results = new StringBuilder();

    public AbstractServiceTest() {
        initDb();
    }

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
    public void populateDbBeforeTest() {
        populateDb();
    }
}
