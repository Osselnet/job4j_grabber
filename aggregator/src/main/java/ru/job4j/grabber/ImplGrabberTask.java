package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.domain.Vacancy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ImplGrabberTask implements GrabberTask {

    private static final String USER_AGENT = "Mozilla/5.0 (jsoup)";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.forLanguageTag("ru"));

    String makePageLink(int page, GrabberParameters params) {
        return format(params.getUrlTemplate(), page);
    }

    Elements getAllVacancyRowsOnPage(Document doc) {
        return doc.getElementsByClass("vacancy-card");
    }

    String grabTitleIfValid(Element row) {
        return row.getElementsByClass("vacancy-card__title").text();
    }

    String grabDateTime(Element row) {
        Element first = row.getElementsByClass("basic-date").first();
        return first.attr("datetime");
    }

    LocalDateTime parseDateTime(String dateLine) {
        return LocalDateTime.of(LocalDate.parse(dateLine.substring(0, dateLine.indexOf("T")), FORMATTER), LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    String grabLink(Element row, GrabberParameters params) {
        return row.getElementsByClass("vacancy-card__icon").attr("src");
    }

    String grabText(Element row) {
        return row.getElementsByClass("vacancy-card__skills")
                .stream()
                .map(Element::text)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<Vacancy> parseVacancies(GrabberParameters params) throws IOException {
        List<Vacancy> vacancies = new ArrayList<>();
        mainLoop(vacancies, params);
        return vacancies;
    }

    void mainLoop(List<Vacancy> buffer, GrabberParameters params) throws IOException {
        int i = 0;
        do {
            i++;
        } while (processPage(buffer, i, params));
    }

    boolean processPage(List<Vacancy> buffer, int pageNumber, GrabberParameters params) throws IOException {
        String link = makePageLink(pageNumber, params);
        Document doc = makeDocument(link);
        Elements rows = getAllVacancyRowsOnPage(doc);
        boolean result = !rows.isEmpty();
        for (Element row : rows) {
            if (!processRow(buffer, row, params)) {
                result = false;
                break;
            }
        }
        return result;
    }

    Document makeDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .referrer("")
                .get();
    }

    boolean processRow(List<Vacancy> buffer, Element row, GrabberParameters params) throws IOException {
        LocalDateTime dateTime = parseDateTime(grabDateTime(row));
        if (!params.isReachLimit(dateTime)) {
            return false;
        }
        if (params.isInRange(dateTime)) {
            grabRow(row, dateTime, params).ifPresent(buffer::add);
        }
        return true;
    }

    Optional<Vacancy> grabRow(Element row, LocalDateTime dateTime, GrabberParameters params) throws IOException {
        Vacancy result = null;
        String title = grabTitleIfValid(row);
        if (title != null) {
            String link = grabLink(row, params);
            String text = grabText(row);
            result = new Vacancy(title, link, text, dateTime);
        }
        return Optional.ofNullable(result);
    }
}