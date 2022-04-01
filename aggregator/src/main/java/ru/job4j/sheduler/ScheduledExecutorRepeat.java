package ru.job4j.scheduler;

import ru.job4j.base.dao.DaoFactory;
import ru.job4j.base.dao.GenericDao;
import ru.job4j.base.dao.PersistException;
import ru.job4j.base.sqlite.SqliteDaoFactory;
import ru.job4j.domain.Vacancy;
import ru.job4j.grabber.GrabberParameters;
import ru.job4j.grabber.GrabberTask;
import ru.job4j.grabber.ImplGrabberTask;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorRepeat {
    private final ScheduledExecutorService executorService;
    private static final DaoFactory<Connection> factory = new SqliteDaoFactory();
    private static Connection connection;
    private static GenericDao dao;
    private LocalDateTime lastDateTime = null;
    private List<GrabberParameters> params;

    public ScheduledExecutorRepeat(boolean parallelWebScraping, int launchPeriod, List<GrabberParameters> params) throws PersistException {
        this.params = params;
        connection = factory.getContext();
        dao = factory.getDao(connection, Vacancy.class);

        executorService = parallelWebScraping && params.size() > 1 ? Executors.newScheduledThreadPool(params.size())
                : Executors.newSingleThreadScheduledExecutor();
        for (GrabberParameters param : this.params) {
            executorService.scheduleAtFixedRate(() -> {
                try {
                    GrabberTask p = new ImplGrabberTask();
                    param.setStart(LocalDateTime.now());
                    param.setFinish(lastDateTime);
                    List<Vacancy> vacancies = p.parseVacancies(param);
                    lastDateTime = LocalDateTime.now();
                    for (Vacancy vacancy : vacancies) {
                        dao.persist(vacancy);
                    }
                } catch (IOException | PersistException e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
            }, 0, launchPeriod, TimeUnit.SECONDS);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Performing some shutdown cleanup...");
                executorService.shutdown();
                while (true) {
                    try {
                        System.out.println("Waiting for the service to terminate...");
                        if (executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                            break;
                        }
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println("Done cleaning");
            }
        }));
    }
}
