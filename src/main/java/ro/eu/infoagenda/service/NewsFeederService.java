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
    private final LocalCash<List<String>> localNewsCash = new LocalCash<>(60 * 60 * 1000);

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
                    if (articles == null) {
                        localNewsCash.setValue(Collections.emptyList());
                        logger.error("set localNews failed! please check " + NEWS_URL);
                    }else {
                        List<String> newsArticles = new ArrayList<>();
                        for (Element article : articles) {
                            newsArticles.add(article.text());
                        }
                        localNewsCash.setValue(newsArticles);
                        logger.info("set localNews " + localNewsCash);
                    }
                }
            }
        }

        return localNewsCash.getValue();
    }
}
