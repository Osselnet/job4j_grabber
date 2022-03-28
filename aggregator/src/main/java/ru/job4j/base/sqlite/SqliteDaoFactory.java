package ru.job4j.base.sqlite;

import ru.job4j.base.dao.*;
import ru.job4j.base.domain.Website;
import ru.job4j.base.domain.Vacancy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SqliteDaoFactory implements DaoFactory<Connection> {
    private String url = "jdbc:sqlite:test.db";//URL адрес
    private String driver = "org.sqlite.JDBC";//Имя драйвера
    private Map<Class, DaoCreator> creators;

    public Connection getContext() throws PersistException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return  connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public SqliteDaoFactory() {
        try {
            Class.forName(driver);//Регистрируем драйвер
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        creators = new HashMap<Class, DaoCreator>();
        creators.put(Website.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new SqliteWebsiteDao(SqliteDaoFactory.this, connection);
            }
        });
        creators.put(Vacancy.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new SqliteVacancyDao(SqliteDaoFactory.this, connection);
            }
        });
    }
}

