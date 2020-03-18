package ru.test_task.sklad.util;

import com.ibatis.common.jdbc.ScriptRunner;
import ru.test_task.sklad.Sklad;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class SqlUtil {

    private static void scriptRun(InputStream stream) {
        try {
            Properties properties = Sklad.properties;
            Class.forName(properties.getProperty("driver"));
            Connection con = DriverManager.getConnection(
                    properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
            ScriptRunner runner = new ScriptRunner(con, Boolean.parseBoolean(properties.getProperty("autoCommit")),
                    Boolean.parseBoolean(properties.getProperty("stopOnError")));
            Reader reader = new BufferedReader(new InputStreamReader(stream));
            runner.runScript(reader);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initDb() {
        InputStream stream = SqlUtil.class.getClassLoader().getResourceAsStream("db/initDB.sql");
        scriptRun(stream);
    }

    public static void populateDb() {
        InputStream stream = SqlUtil.class.getClassLoader().getResourceAsStream("db/populateDB.sql");
        scriptRun(stream);
    }
}
