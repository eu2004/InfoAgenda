package ro.eu.infoagenda.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ro.eu.infoagenda.model.Info;
import ro.eu.infoagenda.model.InfoContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFeederService {
    private static final String NEWS_URL = "https://www.biziday.ro/";
    private static final Logger logger = LogManager.getLogger(NewsFeederService.class);
    private volatile LocalNewsCash localNewsCash = new LocalNewsCash();

    public Info<List<String>> getLocalNews() throws IOException {
        InfoContent<List<String>> infoContent = new InfoContent<>();
        infoContent.setContent(getLocalNewsInfoContent());
        return new Info<>(infoContent);
    }

    private List<String> getLocalNewsInfoContent() throws IOException {
        if (localNewsCash.getValue() == null) {
            synchronized (localNewsCash) {
                if (localNewsCash.getValue() == null) {
                    Document doc = Jsoup.connect(NEWS_URL).get();
                    Elements articles = doc.select("li.article");
                    List<String> newsArticles = new ArrayList<>();
                    for (Element article : articles) {
                        newsArticles.add(article.text());
                    }
                    localNewsCash.setValue(newsArticles);
                    logger.info("set localNews " + localNewsCash);
                }
            }
        }

        return localNewsCash.getValue();
    }

    private static class LocalNewsCash {
        private static final int evictionPeriod = 60 * 60 * 1000; //1 h

        private List<String> localNews = null;
        private volatile long lastCall = -1;

        public List<String> getValue() {
            if (lastCall == -1) {
                localNews = null;
                return null;
            }

            long timeSinceLastCall = System.currentTimeMillis() - lastCall;
            if (evictionPeriod <= timeSinceLastCall) {
                logger.info("localNews has expired! EvictionPeriod:"
                        + evictionPeriod
                        + " <= timeSinceLastCall:"
                        + timeSinceLastCall);
                localNews = null;
            }

            if (localNews != null) {
                return Collections.unmodifiableList(localNews);
            }

            return null;
        }

        public void setValue(List<String> value) {
            lastCall = System.currentTimeMillis();
            localNews = new ArrayList<>(value);
        }

        @Override
        public String toString() {
            return localNews.toString();
        }
    }
}
