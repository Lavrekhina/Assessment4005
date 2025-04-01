/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.DAO;

import com.sofia.legal_system.model.Shipment;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author soflavre
 */
public class ShipmentDAO extends BaseCrudDAO<Shipment, Integer>{

    @Override
    protected Shipment map(ResultSet resultSet) throws SQLException {
        return new Shipment(resultSet.getInt("shipment_id"),
            resultSet.getString("destination"),
            resultSet.getString("shipment_date"),
            resultSet.getString("shipment_status"));
    }

    @Override
    protected String tableName() {
        return "shipments";
    }

    @Override
    protected String toInsertValue(Shipment shipment) {
        return String.format("('%s', '%s', '%s')", shipment.getDestination(), shipment.getDate(), shipment.getShipmentStatus());
     }

    @Override
    protected String toInsertColumns() {
       return String.format("('%s', '%s', '%s')", "destination", "shipment_date", "shipment_status"); 
    }

    @Override
    protected String toIdFilter(Integer integer) {
        return "shipment_id = " + integer;
    }

    @Override
    protected Integer getId(Shipment shipment) {
        return shipment.getId();
    }

    @Override
    protected Integer toIdValue(ResultSet resultSet) throws SQLException {
     try{
        return resultSet.getInt(1);
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
    }
    
    
}
