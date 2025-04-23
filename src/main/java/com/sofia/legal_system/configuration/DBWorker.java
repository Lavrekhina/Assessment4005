package com.sofia.legal_system.configuration;

import com.sofia.legal_system.exceptions.DBException;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBWorker {
    private static final DBWorker instance = new DBWorker();
    private static final String DROP_ALL_SQL = "select 'drop table ' || name || ';' from sqlite_master where type = 'table';";
    private final int POOL_SIZE = 10;
    private final BlockingQueue<Connection> pool = new ArrayBlockingQueue<>(POOL_SIZE);
    private final Properties properties;

    private DBWorker() {
        properties = loadProperties();
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.add(DriverManager.getConnection(properties.getProperty("url"), properties));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize pool", e);
        }

        System.out.println("Database properties loaded");
        System.out.println("Database initializing");
        try {
            initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Database initialized");
    }

    public static DBWorker getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.setAutoCommit(true); // Reset connection state
            pool.offer(conn); // Return to pool
        } catch (SQLException e) {
            // If we can't reset the connection, close it permanently
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
            // And create a new one to maintain pool size
            try {
                pool.add(DriverManager.getConnection("jdbc:sqlite:sample.db"));
            } catch (SQLException ex) {
                // Log pool maintenance failure
            }
        }
    }

    public void resetDB() throws IOException, SQLException {
        File sqlFolder = new File("sql");
        if (!sqlFolder.exists()) {
            throw new RuntimeException("sql folder does not exist");
        }

        clearDB();

        var conn = getConnection();

        Arrays.stream(sqlFolder.listFiles()).forEach(file -> {
            try {
                var statement = conn.createStatement();
                var result = statement.execute(Files.readString(file.toPath()));
                statement.close();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });

        releaseConnection(conn);
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
        dropStatement.close();
        dropRs.close();

        releaseConnection(conn);
    }

    private void initialize() throws SQLException {
        var conn = getConnection();
        var stmt = conn.createStatement().executeQuery("select name from sqlite_master where type = 'table'");
        var tables = new ArrayList<String>();
        while (stmt.next()) {
            tables.add(stmt.getString(1));
        }

        if (tables.stream().noneMatch(t -> t.equalsIgnoreCase("inventory") || t.equalsIgnoreCase("orders") || t.equalsIgnoreCase("shipments"))) {
            try {
                resetDB();
            } catch (IOException e) {
                System.out.println("Error DB initialization");
                throw new RuntimeException(e);
            }
        }
        releaseConnection(conn);
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
