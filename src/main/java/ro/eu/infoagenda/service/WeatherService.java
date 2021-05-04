package ro.eu.infoagenda.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;

import java.io.IOException;

public class WeatherService {
    private LocalCurrentTemperatureCash localCurrentTemperature = new LocalCurrentTemperatureCash();
    private String getCurrentLocationAccuweatherURL() {
        return "https://www.accuweather.com/ro/ro/bucharest/287430/current-weather/287430";
    }

    public Info<String> getOutsideCurrentTemperature() throws IOException {
        InfoContent<String> infoContent = new InfoContent<>();

        infoContent.setContent(getLocalOutsideCurrentTemperature());
        return new Info<>(infoContent);
    }

    private String getLocalOutsideCurrentTemperature() throws IOException {
        if (localCurrentTemperature.getValue() == null) {
            Document doc = Jsoup.connect(getCurrentLocationAccuweatherURL()).get();
            Element root = doc.select("div.two-column-page-content")
                    .select("div.page-column-1")
                    .select("div.content-module")
                    .select("div.current-weather-card.card-module.content-module.non-ad")
                    .select("div.card-content")
                    .select("div.temp")
                    .select("div.display-temp")
                    .first();
            localCurrentTemperature.setValue(root.text());
            System.out.println("get localCurrentTemperature " + root.text());
        }

        return localCurrentTemperature.getValue();
    }

    private class LocalCurrentTemperatureCash {
        private final int evictionPeriod = 30 * 60 * 1000; //30 min

        private String localCurrentTemperature = null;
        private long lastCall = -1;

        public String getValue() {
            long timeSinceLastCall = System.currentTimeMillis() - lastCall;
            if (evictionPeriod <= timeSinceLastCall) {
                System.out.println("evictionPeriod:" + evictionPeriod + " <= timeSinceLastCall:" + timeSinceLastCall);
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
