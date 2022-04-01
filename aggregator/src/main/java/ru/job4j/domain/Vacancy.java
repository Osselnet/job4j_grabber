package ru.job4j.domain;

import ru.job4j.base.dao.Identified;

import java.time.LocalDateTime;

public class Vacancy implements Identified<Integer> {

    private Integer id = null;
    private  String title;
    private String link;
    private String text;
    private  LocalDateTime dateTime;

    public Vacancy (String title, String link, String text, LocalDateTime dateTime) {
        this.title = title;
        this.link = link;
        this.text = text;
        this.dateTime = dateTime;
    }

    public Vacancy() {}

    public Integer getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}