package ro.eu.infoagenda.service;

import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeInfoService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public Info<String> getCurrentTime() {
        LocalTime localTime = LocalTime.now();
        InfoContent<String> infoContent = new InfoContent<>();
        infoContent.setContent(localTime.format(getTimeFormatter()));
        return new Info<>(infoContent);
    }

    private DateTimeFormatter getTimeFormatter() {
        return timeFormatter;
    }
}
