package ro.eu.infoagenda.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;
import ro.eu.infoagenda.model.WeatherDetails;

import java.io.IOException;

public class WeatherService {
    private static final Logger logger = LogManager.getLogger(WeatherService.class);
    private static final String CURRENT_LOCATION_ACCUWEATHER_URL = "https://www.accuweather.com/ro/ro/bucharest/287430/current-weather/287430";
    private final LocalCash<WeatherDetails> localWeather = new LocalCash<>(15 * 60 * 1000);

    public Info<WeatherDetails> getLocalWeatherInfo() throws IOException {
        InfoContent<WeatherDetails> infoContent = new InfoContent<>();
        infoContent.setContent(getLocalWeather());
        return new Info<>(infoContent);
    }

    private WeatherDetails getLocalWeather() throws IOException {
        if (localWeather.getValue() == null) {
            synchronized (localWeather) {
                if (localWeather.getValue() == null) {
                    setWeatherDetails();
                }
            }
        }

        return localWeather.getValue();
    }

    private void setWeatherDetails() throws IOException {
        String localCurrentTemperature = null;
        String realFeelTemperature = null;
        String windKmPerH = null;
        String overAll = null;
        String pressure = null;

        Document doc = Jsoup.connect(CURRENT_LOCATION_ACCUWEATHER_URL).get();
        Element root = getLocalCurrentTemperatureElement(doc);
        localCurrentTemperature = root != null ? root.text() : "Failed to load localCurrentTemperature!";

        root = getRealFeelTemperatureElement(doc);
        realFeelTemperature = root != null ? root.text() : "Failed to load realFeelTemperature!";

        root = getWindKmPerHElement(doc);
        windKmPerH = root != null ? root.text() : "Failed to load windKmPerH!";

        root = getOverAllElement(doc);
        overAll = root != null ? root.text() : "Failed to load overAll!";

        root = getPressureElement(doc);
        pressure = root != null ? root.text() : "Failed to load pressure!";

        localWeather.setValue(createLocalWeather(localCurrentTemperature, realFeelTemperature, windKmPerH, overAll, pressure));
        logger.info("set WeatherDetails " + localWeather.getValue());
    }

    private Element getPressureElement(Document doc) {
        Element root = doc.select("div.two-column-page-content")
                .select("div.page-column-1")
                .select("div.page-content.content-module")
                .select("div.current-weather-card.card-module.content-module")
                .select("div.current-weather-details")
                .select("div.right")
                .select("div.detail-item.spaced-content")
                .select("div")
                .first();
        return root;
    }

    private Element getOverAllElement(Document doc) {
        Element root = doc.select("div.two-column-page-content")
                .select("div.page-column-1")
                .select("div.page-content.content-module")
                .select("div.current-weather-card.card-module.content-module")
                .select("div.card-content")
                .select("div.current-weather")
                .select("div.phrase")
                .first();
        return root;
    }

    private Element getWindKmPerHElement(Document doc) {
        Element root = doc.select("div.two-column-page-content")
                .select("div.page-column-1")
                .select("div.page-content.content-module")
                .select("div.current-weather-card.card-module.content-module")
                .select("div.current-weather-details")
                .select("div.left")
                .select("div.detail-item.spaced-content")
                .get(1)
                .selectFirst("div");
        return root;
    }

    private Element getRealFeelTemperatureElement(Document doc) {
        Element root = doc.select("div.two-column-page-content")
                .select("div.page-column-1")
                .select("div.page-content.content-module")
                .select("div.current-weather-card.card-module.content-module")
                .select("div.card-content")
                .select("div.current-weather-extra")
                .select("div.realfeel-shade-details")
                .first();
        return root;
    }

    private Element getLocalCurrentTemperatureElement(Document doc) {
        Element root = doc.select("div.two-column-page-content")
                .select("div.page-column-1")
                .select("div.page-content.content-module")
                .select("div.current-weather-card.card-module.content-module")
                .select("div.card-content")
                .select("div.current-weather")
                .select("div.current-weather-info")
                .select("div.temp")
                .select("div.display-temp")
                .first();
        return root;
    }

    private WeatherDetails createLocalWeather(String localCurrentTemperature, String realFeelTemperature, String windKmPerH, String overAll, String pressure) {
        return new WeatherDetails(localCurrentTemperature, realFeelTemperature, windKmPerH, overAll, pressure);
    }

}
