/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.DAO;

import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.configuration.DBWorker;
import com.sofia.legal_system.model.Shipment;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author soflavre
 */
public class ShipmentDAOTest {
    private ShipmentDAO dao = new ShipmentDAO();

    @BeforeEach
    void setUp() throws SQLException, IOException {
        DBWorker.getInstance().resetDB();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        DBWorker.getInstance().clearDB();
    }

    @Test
    void shouldSuccessfullyInsertShipment() throws SQLException {
        dao.insert(new Shipment("London", "2024-04-01","Delivered"));
        dao.insert(new Shipment("Wimbledon", "2024-04-05", "Dispached"));
        dao.insert(new Shipment("Oval", "2024-04-10", "In transt"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    void shouldSuccessfullyFilterShipment() throws SQLException {
        dao.insert(new Shipment("London", "2024-04-01","Delivered"));
        var check = dao.insert(new Shipment("Wimbledon", "2024-04-05", "Dispached"));
        dao.insert(new Shipment("Oval", "2024-04-10", "In transt"));


        var result = dao.getAll("destination = 'London'");
        assertNotNull(result);
        assertFalse(result.isEmpty());

        var saveEntity = result.get(0);
        assertEquals("2024-04-01", saveEntity.getDate());
        assertEquals(check.getId(), saveEntity.getId());
    }

    @Test
    void shouldSuccessfullyDelete() throws SQLException {
        dao.insert(new Shipment("London", "2024-04-01","Delivered"));
        dao.insert(new Shipment("Wimbledon", "2024-04-05", "Dispached"));
        dao.insert(new Shipment("Oval", "2024-04-10", "In transt"));


        var result = dao.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());

        dao.deleteById(result.get(0).getId());

        var resultAfterDelete = dao.getAll();
        assertTrue(result.size() - resultAfterDelete.size() > 0);
    }
}
