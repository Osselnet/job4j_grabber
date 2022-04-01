package ru.job4j.base.sqlite;

import ru.job4j.base.dao.*;
import ru.job4j.domain.Vacancy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SqliteDaoFactory implements DaoFactory<Connection> {
    private final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private final String url = "jdbc:sqlite:" + path + "test.db";
    private final String driver = "org.sqlite.JDBC";//РРјСЏ РґСЂР°Р№РІРµСЂР°
    private final Map<Class, DaoCreator> creators;

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
            Class.forName(driver);//Р РµРіРёСЃС‚СЂРёСЂСѓРµРј РґСЂР°Р№РІРµСЂ
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        creators = new HashMap<Class, DaoCreator>();
        creators.put(Vacancy.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new SqliteVacancyDao(SqliteDaoFactory.this, connection);
            }
        });
    }
}
