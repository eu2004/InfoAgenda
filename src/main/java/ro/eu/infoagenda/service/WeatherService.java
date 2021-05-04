package ro.eu.infoagenda.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;

import java.io.IOException;

public class WeatherService {
    private static final Logger logger = LogManager.getLogger(WeatherService.class);
    private static final String CURRENT_LOCATION_ACCUWEATHER_URL = "https://www.accuweather.com/ro/ro/bucharest/287430/current-weather/287430";
    private final LocalCurrentTemperatureCash localCurrentTemperature = new LocalCurrentTemperatureCash();

    public Info<String> getOutsideCurrentTemperature() throws IOException {
        InfoContent<String> infoContent = new InfoContent<>();

        infoContent.setContent(getLocalOutsideCurrentTemperature());
        return new Info<>(infoContent);
    }

    private String getLocalOutsideCurrentTemperature() throws IOException {
        if (localCurrentTemperature.getValue() == null) {
            synchronized (localCurrentTemperature) {
                if (localCurrentTemperature.getValue() == null) {
                    Document doc = Jsoup.connect(CURRENT_LOCATION_ACCUWEATHER_URL).get();
                    Element root = doc.select("div.two-column-page-content")
                            .select("div.page-column-1")
                            .select("div.content-module")
                            .select("div.current-weather-card.card-module.content-module.non-ad")
                            .select("div.card-content")
                            .select("div.temp")
                            .select("div.display-temp")
                            .first();
                    localCurrentTemperature.setValue(root.text());
                    logger.info("set localCurrentTemperature " + root.text());
                }
            }
        }

        return localCurrentTemperature.getValue();
    }

    private static class LocalCurrentTemperatureCash {
        private static final int evictionPeriod = 30 * 60 * 1000; //30 min

        private String localCurrentTemperature = null;
        private volatile long lastCall = -1;

        public String getValue() {
            if (lastCall == -1) {
                return localCurrentTemperature;
            }

            long timeSinceLastCall = System.currentTimeMillis() - lastCall;
            if (evictionPeriod <= timeSinceLastCall) {
                logger.info("localCurrentTemperature has expired! EvictionPeriod:"
                        + evictionPeriod
                        + " <= timeSinceLastCall:"
                        + timeSinceLastCall);
                localCurrentTemperature = null;
            }
            return localCurrentTemperature;
        }

        public void setValue(String value) {
            lastCall = System.currentTimeMillis();
            localCurrentTemperature = value;
        }
    }
}
