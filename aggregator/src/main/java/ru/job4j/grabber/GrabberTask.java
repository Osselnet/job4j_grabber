package ru.job4j.grabber;

import ru.job4j.domain.Vacancy;

import java.io.IOException;
import java.util.List;

public interface GrabberTask {
    List<Vacancy> parseVacancies(GrabberParameters params) throws IOException;

    default String getSourceTitle() {
        return null;
    }
}
