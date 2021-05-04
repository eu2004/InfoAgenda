package ro.eu.infoagenda.service;

import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateInfoService {

    public Info<String> getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        InfoContent<String> infoContent = new InfoContent<>();
        infoContent.setContent(localDate.format(DateTimeFormatter.ofPattern("yyyy MM dd, EEEE")));
        return new Info<>(infoContent);
    }
}
