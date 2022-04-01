package ru.job4j;

import ru.job4j.base.dao.PersistException;
import ru.job4j.scheduler.ScheduledExecutorRepeat;
import ru.job4j.server.ServiceThread;
import ru.job4j.util.Config;

import java.io.IOException;

public class JobAggregator {
    private static Config config;

    public static void main(String[] args) {
        try {
            config = new Config();
            new ScheduledExecutorRepeat(config.isParallelWebScraping(), config.getLaunchPeriod(), config.getParams());
            new ServiceThread(config.getPort());
        } catch (PersistException | IOException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}