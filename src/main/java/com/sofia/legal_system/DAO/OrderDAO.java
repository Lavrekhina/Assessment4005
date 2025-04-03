/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.DAO;

import com.sofia.legal_system.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author soflavre
 */
public class OrderDAO extends BaseCrudDAO<Order, Integer> {

    @Override
    protected Order map(ResultSet resultSet) throws SQLException {
        return new Order(resultSet.getInt("order_id"),
                resultSet.getString("order_date"),
                resultSet.getString("customer_name"),
                resultSet.getString("order_status"));
    }

    @Override
    protected String tableName() {
        return "orders";
    }

    @Override
    protected String toInsertValue(Order order) {
        return String.format("('%s', '%s', '%s')", order.getDate(), order.getCustomerName(), order.getStatus());
    }

    @Override
    protected String toInsertColumns() {
        return String.format("('%s', '%s', '%s')", "order_date", "customer_name", "order_status");
    }

    @Override
    protected String toIdFilter(Integer integer) {
        return "order_id = " + integer;
    }

    @Override
    protected Integer getId(Order order) {
        return order.getId();
    }

    @Override
    protected Integer toIdValue(ResultSet resultSet) throws SQLException {
        try {
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toUpdateValue(Order order) {
        return String.format("order_date = '%s', order_status = '%s', customer_name = '%s'", order.getDate(), order.getStatus(), order.getCustomerName());
    }

}
