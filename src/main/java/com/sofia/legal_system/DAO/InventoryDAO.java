package com.sofia.legal_system.DAO;

import com.sofia.legal_system.model.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDAO extends BaseCrudDAO<Inventory, Integer> {

    @Override
    protected Inventory map(ResultSet resultSet) throws SQLException {
        return new Inventory(resultSet.getInt("item_id"),
                resultSet.getString("item_name"),
                resultSet.getInt("item_quantity"),
                resultSet.getString("item_location"));
    }

    @Override
    protected String tableName() {
        return "inventory";
    }

    @Override
    protected String toInsertValue(Inventory inventory) {
        return String.format("('%s', %s, '%s')", inventory.getName(), inventory.getQuantity(), inventory.getLocation());
    }

    @Override
    protected String toInsertColumns() {
        return String.format("(%s, %s, '%s')", "item_name", "item_quantity", "item_location");
    }

    @Override
    protected String toIdFilter(Integer integer) {
        return "item_id = " + integer;
    }

    @Override
    protected Integer getId(Inventory inventory) {
        return inventory.getId();
    }

    @Override
    protected Integer toIdValue(ResultSet resultSet) {
        try {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
