package ru.job4j.base.sqlite;

import ru.job4j.base.dao.AbstractJDBCDao;
import ru.job4j.base.dao.DaoFactory;
import ru.job4j.base.dao.PersistException;
import ru.job4j.base.domain.Website;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class SqliteWebsiteDao extends AbstractJDBCDao<Website, Integer> {

    private class PersistWebsite extends Website {
        public void setId(int id) {
            super.setId(id);
        }
    }


    @Override
    public String getSelectQuery() {
        return "SELECT id, number, department FROM daotalk.Group";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO daotalk.Group (number, department) \n" +
                "VALUES (?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE daotalk.Group SET number= ?, department = ? WHERE id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM daotalk.Group WHERE id= ?;";
    }

    @Override
    public Website create() throws PersistException {
        Website g = new Website();
        return persist(g);
    }

    public SqliteWebsiteDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
    }

    @Override
    protected List<Website> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Website> result = new LinkedList<Website>();
        try {
            while (rs.next()) {
                PersistWebsite website = new PersistWebsite();
                website.setId(rs.getInt("id"));
                website.setTitle(rs.getString("department"));
                result.add(website);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Website object) throws PersistException {
        try {
            statement.setString(2, object.getTitle());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Website object) throws PersistException {
        try {
            statement.setString(2, object.getTitle());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
