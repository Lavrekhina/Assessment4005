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
        dao.insert(new Order("2024-04-01", "Ann Robs", "Recieved"));
        dao.insert(new Order("2024-04-05", "Will Bank", "Paid"));
        dao.insert(new Order("2024-04-10", "Robert Smith", "Recieved"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void shouldSuccessfullyFilterOrder() throws SQLException {
        dao.insert(new Order("2024-04-01", "Ann Robs", "Recieved"));
        var check = dao.insert(new Order("2024-04-05", "Will Bank", "Paid"));
        dao.insert(new Order("2024-04-10", "Robert Smith", "Recieved"));


        var result = dao.getAll("order_date = '2024-04-01'");
        assertNotNull(result);
        assertFalse(result.isEmpty());

        var saveEntity = result.get(0);
        assertEquals("2024-04-01", saveEntity.getDate());
        assertEquals(check.getId(), saveEntity.getId());
    }

    @Test
    void shouldSuccessfullyDelete() throws SQLException {
        dao.insert(new Order("2024-04-01", "Ann Robs", "Recieved"));
        dao.insert(new Order("2024-04-05", "Will Bank", "Paid"));
        dao.insert(new Order("2024-04-10", "Robert Smith", "Recieved"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());

        dao.deleteById(result.get(0).getId());

        var resultAfterDelete = dao.getAll();
        assertTrue(result.size() - resultAfterDelete.size() > 0);
    }
}