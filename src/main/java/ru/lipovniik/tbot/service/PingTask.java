package ru.lipovniik.tbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class PingTask {
    @Scheduled(fixedRate = 1620000)
    void pingMe(){
        try {
            URL url = new URL("https://amaty-cay.herokuapp.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            log.info("Ping {}, OK: response code {}", url.getHost(), connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {
            log.error("Ping FAILED");
            e.printStackTrace();
        }
    }
}
