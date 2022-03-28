package ru.job4j.base.domain;

import ru.job4j.base.dao.Identified;
import java.util.Date;

/**
 * Объектное представление сущности Вакансия.
 */
public class Vacancy implements Identified<Integer> {

    private Integer id = null;
    private String text;
    private Date publicationDate;
    private Website website;

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

     public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Website getGroup() {
        return website;
    }

    public void setGroup(Website website) {
        this.website = website;
    }
}
