package ro.eu.infoagenda.service;

import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateInfoService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd, EEEE");

    public Info<String> getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        InfoContent<String> infoContent = new InfoContent<>();
        infoContent.setContent(localDate.format(getDateTimeFormatter()));
        return new Info<>(infoContent);
    }

    private DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
}
