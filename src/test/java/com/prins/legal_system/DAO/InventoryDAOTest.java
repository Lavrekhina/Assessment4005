package com.prins.legal_system.DAO;

import com.prins.legal_system.configuration.DBWorker;
import com.prins.legal_system.model.Inventory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class InventoryDAOTest {

    private InventoryDAO dao = new InventoryDAO();

    @BeforeEach
    void setUp() throws SQLException, IOException {
        DBWorker.getInstance().resetDB();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        DBWorker.getInstance().clearDB();
    }

    @Test
    void shouldSuccessfullyInsertInventory() throws SQLException {
        dao.insert(new Inventory("A", 1, "London"));
        dao.insert(new Inventory("A2", 2, "London2"));
        dao.insert(new Inventory("A3", 3, "London3"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldSuccessfullyFilterInventory() throws SQLException {
        dao.insert(new Inventory("A", 1, "London"));
        var check = dao.insert(new Inventory("A2", 2, "London2"));
        dao.insert(new Inventory("A3", 3, "London3"));


        var result = dao.getAll("item_name = 'A2'");
        assertNotNull(result);
        assertFalse(result.isEmpty());

        var saveEntity = result.get(0);
        assertEquals("A2", saveEntity.getName());
        assertEquals(check.getId(), saveEntity.getId());
    }

    @Test
    void shouldSuccessfullyDelete() throws SQLException {
        dao.insert(new Inventory("A", 1, "London"));
        dao.insert(new Inventory("A2", 2, "London2"));
        dao.insert(new Inventory("A3", 3, "London3"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());

        dao.deleteById(result.get(0).getId());

        var resultAfterDelete = dao.getAll();
        assertTrue(result.size() - resultAfterDelete.size() > 0);
    }
}