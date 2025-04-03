package com.sofia.legal_system.DAO;

import com.sofia.legal_system.configuration.DBWorker;

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
    private static final String RETRIEVE_ALL_QUERY = "select * from %s";
    private static final String RETRIEVE_WITH_FILTER_QUERY = "select * from %s where %s ";
    private static final String DELETE_BY_ID = "delete from %s where %s";

    /**
     * Constructs a new BaseCrudDAO instance.
     */
    public BaseCrudDAO() {
    }

    /**
     * Retrieves all entities from the database table.
     *
     * @return a list of all entities
     * @throws SQLException if a database access error occurs
     */
    public List<T> getAll() throws SQLException {
        var stmt = DBWorker.getInstance().getConnection().createStatement();
        var rs = stmt.executeQuery(String.format(RETRIEVE_ALL_QUERY, tableName()));

        var result = new ArrayList<T>();
        while (rs.next()) {
            result.add(map(rs));
        }
        rs.close();
        stmt.close();
        return result;
    }

    /**
     * Retrieves all entities that match the specified filter conditions.
     *
     * @param filter the WHERE clause conditions to apply (without the WHERE keyword)
     * @return a list of entities matching the filter
     * @throws SQLException if a database access error occurs
     */
    public List<T> getAll(String filter) throws SQLException {
        var stmt = DBWorker.getInstance().getConnection().createStatement();
        var rs = stmt.executeQuery(String.format(RETRIEVE_WITH_FILTER_QUERY, tableName(), filter));

        var result = new ArrayList<T>();
        while (rs.next()) {
            result.add(map(rs));
        }
        rs.close();
        stmt.close();
        return result;
    }

    /**
     * Deletes the specified entity from the database.
     *
     * @param entity the entity to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean delete(T entity) throws SQLException {
        return deleteById(getId(entity));
    }

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @return true if exactly one entity was deleted, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteById(U id) throws SQLException {
        var stmt = DBWorker.getInstance().getConnection().createStatement();
        stmt.execute(String.format(DELETE_BY_ID, tableName(), toIdFilter(id)));

        var result = stmt.getUpdateCount();
        stmt.close();
        return result == 1;
    }

    /**
     * Deletes all specified entities from the database (not implemented).
     *
     * @param entities the collection of entities to delete
     * @return false as this operation is not yet implemented
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteAll(Collection<T> entities) throws SQLException {
        return false;
    }

    /**
     * Inserts a new entity into the database.
     *
     * @param entity the entity to insert
     * @return the inserted entity with generated identifier (if applicable)
     * @throws SQLException if a database access error occurs
     */
    public T insert(T entity) throws SQLException {
        var stmt = DBWorker.getInstance().getConnection().createStatement();
        stmt.execute(String.format(INSERT_QUERY, tableName(), toInsertColumns(), toInsertValue(entity)));

        var result = getById(toIdValue(stmt.getGeneratedKeys()));
        stmt.close();
        return result;
    }


    public T update(T entity) throws SQLException {
        var stmt = DBWorker.getInstance().getConnection().createStatement();
        stmt.execute(String.format(UPDATE_QUERY, tableName(), toUpdateValue(entity), toIdFilter(getId(entity))));

        var result = getById(toIdValue(stmt.getGeneratedKeys()));
        stmt.close();
        return result;
    }

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the identifier of the entity to retrieve
     * @return the entity with the specified ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public T getById(U id) throws SQLException {
        var stmt = DBWorker.getInstance().getConnection().createStatement();
        var rs = stmt.executeQuery(String.format(RETRIEVE_WITH_FILTER_QUERY, tableName(), toIdFilter(id)));

        var res = map(rs);
        rs.close();
        stmt.close();
        return res;
    }

    /**
     * Maps a ResultSet row to an entity object.
     *
     * @param resultSet the ResultSet containing the database row
     * @return the mapped entity object
     * @throws SQLException if a database access error occurs
     */
    protected abstract T map(ResultSet resultSet) throws SQLException;

    /**
     * Specifies the name of the database table associated with the entity.
     *
     * @return the name of the database table
     */
    protected abstract String tableName();

    /**
     * Generates the VALUES part of the INSERT statement for the given entity.
     *
     * @param t the entity to insert
     * @return the VALUES clause string
     */
    protected abstract String toInsertValue(T t);

    /**
     * Generates the column names part of the INSERT statement for the given entity.
     *
     * @return the column names string (e.g., "(col1, col2)")
     */
    protected abstract String toInsertColumns();

    /**
     * Generates the WHERE clause condition to filter by ID.
     *
     * @param u the entity's identifier
     * @return the WHERE condition string (e.g., "id = 123")
     */
    protected abstract String toIdFilter(U u);

    /**
     * Extracts the identifier from an entity.
     *
     * @param t the entity
     * @return the entity's identifier
     */
    protected abstract U getId(T t);

    /**
     * Extracts the generated identifier from a ResultSet after insertion.
     *
     * @param resultSet the ResultSet containing generated keys
     * @return the generated identifier
     * @throws SQLException if a database access error occurs
     */
    protected abstract U toIdValue(ResultSet resultSet) throws SQLException;


    protected abstract String toUpdateValue(T t);
}