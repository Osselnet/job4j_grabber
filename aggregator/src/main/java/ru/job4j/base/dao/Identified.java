package ru.job4j.base.dao;

import java.io.Serializable;

public interface Identified<PK extends Serializable> {

    /** Возвращает идентификатор объекта */
    public PK getId();
}