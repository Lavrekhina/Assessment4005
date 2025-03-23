package com.prins.legal_system.configuration;

import com.prins.legal_system.exceptions.DBException;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

public class DBWorker {
    private static final DBWorker instance = new DBWorker();
    private static final String DROP_ALL_SQL = "select 'drop table ' || name || ';' from sqlite_master where type = 'table';";
    private final Properties properties;

    private Connection connection;

    private DBWorker() {
        properties = loadProperties();
    }

    public static DBWorker getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    connection = DriverManager.getConnection(properties.getProperty("url"), properties);
                } catch (SQLException e) {
                    throw new DBException(e.getMessage());
                }

            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return connection;
    }
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }

        }
    }
    public void resetDB() throws IOException, SQLException {
        File sqlFolder = new File("sql");
        if(!sqlFolder.exists()){
            throw new RuntimeException("sql folder does not exist");
        }

        clearDB();

        var conn = getConnection();

        Arrays.stream(sqlFolder.listFiles()).forEach(file -> {
            try {
                var statement = conn.createStatement();
                var result = statement.execute(Files.readString(file.toPath()));

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });

        conn.close();
    }
    public void clearDB() throws SQLException {
        var conn = getConnection();
        var dropStatement = conn.createStatement();
        var dropRs = dropStatement.executeQuery(DROP_ALL_SQL);

        while (dropRs.next()) {
            var query = dropRs.getString(1);
            var result = dropStatement.execute(query);
            System.out.printf("Dropping table '%s'%n", query);
        }
        dropRs.close();
    }

    private Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }
    }


}
