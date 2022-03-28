package ru.job4j.base.sqlite;

import ru.job4j.base.dao.AbstractJDBCDao;
import ru.job4j.base.dao.DaoFactory;
import ru.job4j.base.dao.PersistException;
import ru.job4j.base.domain.Website;
import ru.job4j.base.domain.Vacancy;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        return "SELECT id, name, surname, enrolment_date, group_id FROM daotalk.Student ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO daotalk.Student (name, surname, enrolment_date, group_id) \n" +
                "VALUES (?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE daotalk.Student \n" +
                "SET name = ?, surname  = ?, enrolment_date = ?, group_id = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM daotalk.Student WHERE id= ?;";
    }

    @Override
    public Vacancy create() throws PersistException {
        Vacancy s = new Vacancy();
        return persist(s);
    }

    public SqliteVacancyDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
        addRelation(Vacancy.class, "group");
    }

    @Override
    protected List<Vacancy> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Vacancy> result = new LinkedList<Vacancy>();
        try {
            while (rs.next()) {
                PersistVacancy student = new PersistVacancy();
                student.setId(rs.getInt("id"));
                student.setText(rs.getString("name"));
                student.setPublicationDate(rs.getDate("enrolment_date"));
                student.setGroup((Website) getDependence(Website.class, rs.getInt("group_id")));
                result.add(student);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Vacancy object) throws PersistException {
        try {
            Date sqlDate = convert(object.getPublicationDate());
            int groupId = (object.getGroup() == null || object.getGroup().getId() == null) ? -1
                    : object.getGroup().getId();
            statement.setString(1, object.getText());
            statement.setDate(2, sqlDate);
            statement.setInt(3, groupId);
            statement.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Vacancy object) throws PersistException {
        try {
            Date sqlDate = convert(object.getPublicationDate());
            int groupId = (object.getGroup() == null || object.getGroup().getId() == null) ? -1
                    : object.getGroup().getId();
            statement.setString(1, object.getText());
            statement.setDate(2, sqlDate);
            statement.setInt(3, groupId);
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
