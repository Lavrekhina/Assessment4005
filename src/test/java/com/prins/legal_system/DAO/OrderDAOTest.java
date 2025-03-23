/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.prins.legal_system.DAO;

import com.prins.legal_system.configuration.DBWorker;
import com.prins.legal_system.model.Inventory;
import com.prins.legal_system.model.Order;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author soflavre
 */
public class OrderDAOTest {
    private OrderDAO dao = new OrderDAO();

    @BeforeEach
    void setUp() throws SQLException, IOException {
        DBWorker.getInstance().resetDB();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        DBWorker.getInstance().clearDB();
    }

    @Test
    void shouldSuccessfullyInsertOrder() throws SQLException {
        dao.insert(new Order("A", "2024-04-01", "London"));
        dao.insert(new Order("A2", "2024-04-05", "London2"));
        dao.insert(new Order("A3", "2024-04-10", "London3"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}