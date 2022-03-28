package ru.job4j.base.domain;

import ru.job4j.base.dao.Identified;

/**
 * Объектное представление сущности Сайт
 */
public class Website implements Identified<Integer> {

    private Integer id = null;
    private String title;

    public Integer getId() {
        return id;
    }

    protected void setId(int id) { this.id = id;  }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

