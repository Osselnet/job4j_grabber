package ru.job4j.util;

import ru.job4j.grabber.GrabberParameters;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;

public class Config {
    private final int maximumNumberSites = 10;
    private Properties properties;
    List<GrabberParameters> params = new ArrayList<>();
    boolean parallelWebScraping;
    int launchPeriod;
    int port;

    public Config() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "app.properties"));
        for (int i = 1; i <= maximumNumberSites; i++) {
            String url = "";
            String urlTemplate = "";
            for (String s : properties.stringPropertyNames()) {
                if (s.contains(format("url%d", i))) {
                    if (s.contains("template")) {
                        urlTemplate = properties.getProperty(s);
                    }
                    if (s.contains("website")) {
                        url = properties.getProperty(s);
                    }
                }
            }
            if (!url.equals("") && !urlTemplate.equals("")) {
                GrabberParameters param = new GrabberParameters(url, urlTemplate);
                params.add(param);
            }
        }
        if (params == null) {
            throw new IOException();
        }
        try {
            launchPeriod = Integer.parseInt(properties.getProperty("launchPeriod"));
            parallelWebScraping = Boolean.parseBoolean(properties.getProperty("parallelWebScraping"));
            port = Integer.parseInt(properties.getProperty("port"));
        } catch (Exception e) {
            throw new IOException();
        }
    }

    public List<GrabberParameters> getParams() {
        return params;
    }

    public boolean isParallelWebScraping() {
        return parallelWebScraping;
    }

    public int getLaunchPeriod() {
        return launchPeriod;
    }

    public int getPort() {
        return port;
    }
}
