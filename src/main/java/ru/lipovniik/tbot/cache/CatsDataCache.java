package ru.lipovniik.tbot.cache;

import org.springframework.stereotype.Component;
import ru.lipovniik.tbot.botapi.parsers.amatycay.AmatyCayParser;

import java.util.Map;

@Component
public class CatsDataCache {
    private Map<String, String> maleCatsPhoto;
    private Map<String, String> femaleCatsPhoto;
    private AmatyCayParser parser;

    public CatsDataCache(AmatyCayParser parser) {
        this.parser = parser;
    }

    public void updateCatsMaps(){
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
}
