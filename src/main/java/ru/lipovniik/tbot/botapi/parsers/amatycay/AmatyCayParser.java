package ru.lipovniik.tbot.botapi.parsers.amatycay;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class AmatyCayParser {
    private final String index = "https://amatycay154.ru";

    public Map<String, String> getParserData(String sex){
        return getCatsData(sex);
    }

    private Map<String, String> getCatsData(String sex) {
        Map<String, String> catsMap = new LinkedHashMap<>();
        Document site;
        try {
            site = Jsoup.connect("https://amatycay154.ru/koty-i-koshki/" + sex + "/")
                    .userAgent("Chrome/4.0.249.0")
                    .referrer("https://yandex.ru/")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        for (Element catData: site.select("div.row")){
            boolean isSterilized = sterilized(catData);
            if (!isSterilized) {
                String photo = catData.select("img[src$=.jpg]")
                        .attr("src");

                String name = catData.select("div.res-margin")
                        .select("a")
                        .first().text();
                catsMap.put(name, index + photo);
            }
        }
        
        return catsMap;
    }

    private boolean sterilized(Element data) {
        String urlCatPage = data.select("a.btn").attr("href");
        Document page;
        try {
            page = Jsoup.connect(index + urlCatPage)
                    .userAgent("Chrome/4.0.249.0")
                    .referrer("https://yandex.ru/")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String sterilized = page.select("div.pet-adopt-info")
                .select("b").get(1).text().toLowerCase(Locale.ROOT);

        return sterilized.equals("да");
    }
}
