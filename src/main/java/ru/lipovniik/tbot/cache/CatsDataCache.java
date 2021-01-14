package ru.lipovniik.tbot.cache;

import org.springframework.stereotype.Component;
import ru.lipovniik.tbot.botapi.parsers.amatycay.AmatyCayParser;

import java.util.Map;

@Component
public class CatsDataCache {
    private Map<String, String> maleCatsPhoto;
    private Map<String, String> femaleCatsPhoto;
    private Map<String, Map<String, String>> kittens;
    private final AmatyCayParser parser;

    public CatsDataCache(AmatyCayParser parser) {
        this.parser = parser;
    }

    public void updateCatsMaps(){
        kittens = parser.getParserKidData();
        maleCatsPhoto = parser.getParserAdultData("koty");
        femaleCatsPhoto = parser.getParserAdultData("koshki");
    }

    public Map<String, String> getMaleCatsPhoto() {
        if (maleCatsPhoto == null)
            updateCatsMaps();
        return maleCatsPhoto;
    }

    public Map<String, String> getFemaleCatsPhoto() {
        if (femaleCatsPhoto == null)
            updateCatsMaps();
        return femaleCatsPhoto;
    }

    public Map<String, Map<String, String>> getKittens() {
        if (kittens == null)
            updateCatsMaps();
        return kittens;
    }
}
