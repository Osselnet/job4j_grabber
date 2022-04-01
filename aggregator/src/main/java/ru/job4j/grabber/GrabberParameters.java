package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GrabberParameters {
    private String url;
    private String urlTemplate;
    private LocalDateTime start;
    private LocalDateTime finish;

    public GrabberParameters(String url, String urlTemplate) {
        this.url = url;
        this.urlTemplate = urlTemplate;
    }

    public GrabberParameters(String url, String urlTemplate, LocalDateTime start, LocalDateTime finish) {
        new GrabberParameters(url, urlTemplate);
        this.start = start;
        this.finish = finish;
    }

    public boolean isReachLimit(LocalDateTime dateTime) {
        return dateTime.isBefore(start);
    }

    public boolean isInRange(LocalDateTime dateTime) {
        return finish == null || !dateTime.isAfter(finish);
    }

    public String getUrl() {
        return url;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }
}
