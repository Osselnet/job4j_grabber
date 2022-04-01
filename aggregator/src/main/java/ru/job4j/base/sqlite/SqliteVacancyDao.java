package ru.job4j.base.sqlite;

import ru.job4j.base.dao.AbstractJDBCDao;
import ru.job4j.base.dao.DaoFactory;
import ru.job4j.base.dao.PersistException;
import ru.job4j.domain.Vacancy;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SqliteVacancyDao extends AbstractJDBCDao<Vacancy, Integer> {
    private class PersistVacancy extends Vacancy {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, title, text, link, dateTime FROM test.Vacancy ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO test.Vacancy (title, text, link, dateTime) \n" +
                "VALUES (?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE test.Vacancy \n" +
                "SET title = ?, text  = ?, link = ?, dateTime = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM test.Vacancy WHERE id= ?;";
    }

    @Override
    public Vacancy create() throws PersistException {
        Vacancy s = new Vacancy();
        return persist(s);
    }

    public SqliteVacancyDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
    }

    @Override
    protected List<Vacancy> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Vacancy> result = new LinkedList<Vacancy>();
        try {
            while (rs.next()) {
                PersistVacancy vacancy = new PersistVacancy();
                vacancy.setId(rs.getInt("id"));
                vacancy.setTitle(rs.getString("title"));
                vacancy.setText(rs.getString("text"));
                vacancy.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
                result.add(vacancy);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Vacancy object) throws PersistException {
        try {
            Timestamp sqlDateTime = Timestamp.valueOf(object.getDateTime());
            statement.setString(1, object.getTitle());
            statement.setString(2, object.getLink());
            statement.setString(3, object.getText());
            statement.setTimestamp(4, sqlDateTime);
            statement.setInt(5, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Vacancy object) throws PersistException {
        try {
            Timestamp sqlDateTime = Timestamp.valueOf(object.getDateTime());
            statement.setString(1, object.getTitle());
            statement.setString(2, object.getLink());
            statement.setString(3, object.getText());
            statement.setTimestamp(4, sqlDateTime);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    protected java.sql.Date convert(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }
}