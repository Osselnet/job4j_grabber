package ru.job4j.server;

import ru.job4j.base.dao.DaoFactory;
import ru.job4j.base.dao.GenericDao;
import ru.job4j.base.dao.PersistException;
import ru.job4j.base.sqlite.SqliteDaoFactory;
import ru.job4j.domain.Vacancy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.Connection;

public class ServiceThread extends Thread {
    private int port;
    private static final DaoFactory<Connection> factory = new SqliteDaoFactory();
    private static Connection connection;
    private static GenericDao dao;

    public ServiceThread(int port) throws PersistException {
        this.port = port;
        connection = factory.getContext();
        dao = factory.getDao(connection, Vacancy.class);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream()) {
                    out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                    for (Object vacancy : dao.getAll()) {
                        out.write(vacancy.toString().getBytes(Charset.forName("Windows-1251")));
                        out.write(System.lineSeparator().getBytes());
                    }
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
