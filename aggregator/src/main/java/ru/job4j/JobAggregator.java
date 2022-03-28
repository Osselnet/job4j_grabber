package ru.job4j;

import ru.job4j.base.dao.DaoFactory;
import ru.job4j.base.dao.GenericDao;
import ru.job4j.base.dao.PersistException;
import ru.job4j.base.domain.Website;
import ru.job4j.base.sqlite.SqliteDaoFactory;

import java.sql.Connection;

public class JobAggregator {
    private static final DaoFactory<Connection> factory = new SqliteDaoFactory();
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = factory.getContext();
            GenericDao dao = factory.getDao(connection, Website.class);
        } catch (PersistException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
