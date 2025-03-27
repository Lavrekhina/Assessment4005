package com.prins.legal_system.configuration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBWorkerTest {
    @BeforeEach
    void setUp() throws SQLException, IOException {
        DBWorker.getInstance().resetDB();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        DBWorker.getInstance().clearDB();
    }

    @Test
    void shouldSuccessfullyCreateDBWorker() {
        assertNotNull(DBWorker.getInstance());
    }

    @Test
    void shouldSuccessfullyOpenAndCloseConnection() throws SQLException {
        var connection = DBWorker.getInstance().getConnection();
        DBWorker.getInstance().closeConnection();
        assertTrue(connection.isClosed());
    }

    @Test
    void shouldSuccessfullyInitDB() throws SQLException, IOException {
        var connection = DBWorker.getInstance().getConnection();
        var statement = connection.createStatement();

        statement.execute("INSERT INTO inventory (item_name, item_quantity, item_location)\n" +
                "VALUES ('Widget A', 100, 'Aisle 1');");
        statement.execute("INSERT INTO orders (order_date, customer_name, order_status)\n" +
                "VALUES ('2024-04-01', 'Customer One', 'Pending');");
        statement.execute("INSERT INTO shipments (destination, shipment_date, shipment_status)\n" +
                "VALUES ('Warehouse A', '2024-04-05', 'In Transit');");

        checkColumns(statement.executeQuery("select * from inventory"), "item_name", "Widget A");
        checkColumns(statement.executeQuery("select * from orders"), "order_date", "2024-04-01");
        checkColumns(statement.executeQuery("select * from shipments"), "destination", "Warehouse A");
        connection.close();
    }

    @Test
    void shouldProperlyClearDB() throws SQLException {
        var connection = DBWorker.getInstance().getConnection();
        var statement = connection.createStatement();

        statement.execute("INSERT INTO inventory (item_name, item_quantity, item_location)\n" +
                "VALUES ('Widget A', 100, 'Aisle 1');");
        statement.execute("INSERT INTO orders (order_date, customer_name, order_status)\n" +
                "VALUES ('2024-04-01', 'Customer One', 'Pending');");
        statement.execute("INSERT INTO shipments (destination, shipment_date, shipment_status)\n" +
                "VALUES ('Warehouse A', '2024-04-05', 'In Transit');");

        checkColumns(statement.executeQuery("select * from inventory"), "item_name", "Widget A");
        checkColumns(statement.executeQuery("select * from orders"), "order_date", "2024-04-01");
        checkColumns(statement.executeQuery("select * from shipments"), "destination", "Warehouse A");
        connection.close();

        DBWorker.getInstance().clearDB();


        assertThrows(SQLException.class, () ->{
            statement.execute("INSERT INTO inventory (item_name, item_quantity, item_location)\n" +
                    "VALUES ('Widget A', 100, 'Aisle 1');");
        });

        assertThrows(SQLException.class, () ->{
            statement.execute("INSERT INTO orders (order_date, customer_name, order_status)\n" +
                    "VALUES ('2024-04-01', 'Customer One', 'Pending');");
        });

        assertThrows(SQLException.class, () ->{
            statement.execute("INSERT INTO shipments (destination, shipment_date, shipment_status)\n" +
                    "VALUES ('Warehouse A', '2024-04-05', 'In Transit');");
        });
    }

    private void checkColumns(ResultSet rs, String columnName, String expected) {
        try {
            while (rs.next()) {
                var value = rs.getString(columnName);
                assertEquals(expected, value);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}