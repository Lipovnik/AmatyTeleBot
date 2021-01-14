package ru.lipovniik.tbot.botapi.parsers.amatycay;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class AmatyCayParser {
    private final String index = "https://amatycay154.ru";

    public Map<String, String> getParserAdultData(String sex) {
        String url = "https://amatycay154.ru/koty-i-koshki/" + sex + "/";
        return getCatsAdultData(url);
    }

    public Map<String, Map<String, String>> getParserKidData() {
        return getCatsKidData();
    }

    private Map<String, String> getCatsAdultData(String url) {
        Map<String, String> catsMap = new LinkedHashMap<>();
        Document site = getSiteData(url);

        if (site != null)
            for (Element catData : site.select("div.row")) {
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

    private Map<String, Map<String, String>> getCatsKidData() {
        Map<String, Map<String, String>> catsMap = new LinkedHashMap<>();
        Document site = getSiteData("https://amatycay154.ru/svobodnye-kotyata/");

        if (site != null) {
            ArrayList<String[]> dataKittens = getDataKitten(site.select("div.row"));
            String lastDofB = dataKittens.get(0)[0];
            Map<String, String> siblings = new HashMap<>();
            for (String[] kit: dataKittens){
                if (!lastDofB.equals(kit[0])){
                    catsMap.put(lastDofB, siblings);
                    lastDofB = kit[0];
                    siblings = new HashMap<>();
                }
                siblings.put(kit[1], kit[2]);
            }
        }

        return catsMap;
    }

    private Document getSiteData(String url) {
        Document site;
        try {
            site = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0")
                    .referrer("https://yandex.ru/")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return site;
    }

    private boolean sterilized(Element data) {
        String urlCatPage = data.select("a.btn").attr("href");
        String url = index + urlCatPage;
        Document page = getSiteData(url);
        String sterilized = "нет";

        if (page != null)
            sterilized = page.select("div.pet-adopt-info")
                    .select("b").get(1).text().toLowerCase(Locale.ROOT);

        return sterilized.equals("да");
    }

    private ArrayList<String[]> getDataKitten(Elements elements) {
        ArrayList<String[]> dataKittens = new ArrayList<>();
        for (Element catData : elements) {
            String[] kit = new String[3];
            kit[0] = getDOfB(catData);
            kit[1] = index + catData.select("img[src$=.jpg]")
                    .attr("src");

            kit[2] = catData.select("div.res-margin")
                    .select("a")
                    .first().text();
            System.out.println(Arrays.toString(kit));
            dataKittens.add(kit);
        }
        return dataKittens;
    }

    private String getDOfB(Element data){
        String urlCatPage = data.select("a.btn").attr("href");
        String url = index + urlCatPage;
        Document page = getSiteData(url);
        String dateOfBirth = "01.01.2020";

        if (page != null) {
            dateOfBirth = page.select("div.col-md-7").select("ul").last()
                    .select("li").first()
                    .text().substring(15);
        }
        return dateOfBirth;
    }
}
