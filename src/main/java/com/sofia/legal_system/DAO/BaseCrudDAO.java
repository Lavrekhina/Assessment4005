package com.sofia.legal_system.DAO;

import com.sofia.legal_system.configuration.DBWorker;
import com.sofia.legal_system.model.Page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract base class providing CRUD (Create, Read, Update, Delete) operations for entities.
 *
 * @param <T> the type of the entity
 * @param <U> the type of the entity's identifier
 */
public abstract class BaseCrudDAO<T, U> {

    private static final String UPDATE_QUERY = "UPDATE %s SET %s WHERE %s";
    private static final String INSERT_QUERY = " insert into %s %s values %s";

    private static final String RETRIEVE_ALL_QUERY = "select * from %s %s";
    private static final String RETRIEVE_ALL_WITH_PAGE_QUERY = "select * from %s %s limit %s offset %s";
    private static final String RETRIEVE_WITH_FILTER_QUERY = "select * from %s where %s %s";
    private static final String RETRIEVE_WITH_FILTER_PAGE_QUERY = "select * from %s where %s %s limit %s offset %s";

    private static final String RETRIEVE_COUNT_FILTER_QUERY = "select count() as count from %s where %s ";
    private static final String RETRIEVE_ALL_COUNT_QUERY = "select count() as count from %s";

    private static final String DELETE_BY_ID = "delete from %s where %s";
    private static final DBWorker dbWorker = DBWorker.getInstance();


    public BaseCrudDAO() {
    }

    public List<T> getAll() throws SQLException {
        var connection = dbWorker.getConnection();

        var stmt = connection.createStatement();
        var rs = stmt.executeQuery(String.format(RETRIEVE_ALL_QUERY, tableName(), ""));

        var result = new ArrayList<T>();
        while (rs.next()) {
            result.add(map(rs));
        }
        rs.close();
        stmt.close();
        dbWorker.releaseConnection(connection);
        return result;
    }

    public Page<T> getAll(int page, int size, String sort) {
        var connection = dbWorker.getConnection();
        try (var stmt = connection.createStatement()) {
            var countRs = stmt.executeQuery(RETRIEVE_ALL_COUNT_QUERY.formatted(tableName()));
            var count = countRs.getInt(1);
            countRs.close();

            var rs = stmt.executeQuery(RETRIEVE_ALL_WITH_PAGE_QUERY.formatted(tableName(), sort, size, (page - 1) * size));

            var result = new ArrayList<T>();
            while (rs.next()) {
                result.add(map(rs));
            }
            rs.close();
            return new Page<T>(result, page, size, count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbWorker.releaseConnection(connection);
        }
    }

    public Page<T> getAll(int page, int size) {
        return getAll(page, size, "");
    }

    public List<T> getAll(String filter) throws SQLException {
        return getAll(filter, "");
    }

    public List<T> getAll(String filter, String sort) throws SQLException {
        var connection = dbWorker.getConnection();

        var stmt = connection.createStatement();
        var rs = stmt.executeQuery(String.format(RETRIEVE_WITH_FILTER_QUERY, tableName(), filter, sort));

        var result = new ArrayList<T>();
        while (rs.next()) {
            result.add(map(rs));
        }
        rs.close();
        stmt.close();
        dbWorker.releaseConnection(connection);
        return result;
    }

    public Page<T> getAll(String filter, int page, int size) {
        return getAll(filter, page, size, "");
    }

    public Page<T> getAll(String filter, int page, int size, String sort) {
        var connection = dbWorker.getConnection();
        try (var stmt = connection.createStatement()) {
            var countRs = stmt.executeQuery(RETRIEVE_COUNT_FILTER_QUERY.formatted(tableName(), filter));
            var count = countRs.getInt(1);
            countRs.close();

            var rs = stmt.executeQuery(RETRIEVE_WITH_FILTER_PAGE_QUERY.formatted(tableName(), filter, sort, size, (page - 1) * size));

            var result = new ArrayList<T>();
            while (rs.next()) {
                result.add(map(rs));
            }
            rs.close();
            return new Page<T>(result, page, size, count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbWorker.releaseConnection(connection);
        }
    }


    public synchronized boolean delete(T entity) throws SQLException {
        return deleteById(getId(entity));
    }

    public synchronized boolean deleteById(U id) throws SQLException {
        var connection = dbWorker.getConnection();

        var stmt = connection.createStatement();
        stmt.execute(String.format(DELETE_BY_ID, tableName(), toIdFilter(id)));

        var result = stmt.getUpdateCount();
        stmt.close();
        dbWorker.releaseConnection(connection);
        return result == 1;
    }

    public synchronized boolean deleteAll(Collection<T> entities) {
        entities.forEach(entity -> {
            try {
                delete(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public synchronized T insert(T entity) throws SQLException {
        var connection = dbWorker.getConnection();
        var stmt = connection.createStatement();
        stmt.execute(String.format(INSERT_QUERY, tableName(), toInsertColumns(), toInsertValue(entity)));

        var result = getById(toIdValue(stmt.getGeneratedKeys()));
        stmt.close();
        dbWorker.releaseConnection(connection);
        return result;
    }

    public synchronized T update(T entity) throws SQLException {
        var connection = dbWorker.getConnection();
        var stmt = connection.createStatement();
        stmt.execute(String.format(UPDATE_QUERY, tableName(), toUpdateValue(entity), toIdFilter(getId(entity))));

        var result = getById(toIdValue(stmt.getGeneratedKeys()));
        stmt.close();
        dbWorker.releaseConnection(connection);
        return result;
    }

    public T getById(U id) throws SQLException {
        var connection = dbWorker.getConnection();

        var stmt = connection.createStatement();
        var rs = stmt.executeQuery(String.format(RETRIEVE_WITH_FILTER_QUERY, tableName(), toIdFilter(id), ""));

        var res = map(rs);
        rs.close();
        stmt.close();
        dbWorker.releaseConnection(connection);
        return res;
    }

    protected abstract T map(ResultSet resultSet) throws SQLException;

    protected abstract String tableName();

    protected abstract String toInsertValue(T t);

    protected abstract String toInsertColumns();

    protected abstract String toIdFilter(U u);

    protected abstract U getId(T t);

    protected abstract U toIdValue(ResultSet resultSet) throws SQLException;

    protected abstract String toUpdateValue(T t);
}