package ru.lipovniik.tbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;


@Service
@Slf4j
public class SendMediaService {

    public SendMediaService() {
    }

    public void sendPhoto(long chatId, String fileId, String imageCaption){
        String rec = String.format("https://api.telegram.org/bot1275133484:AAHuPiA0j7883NUuctkLWDELUyeRDgW_K8E/sendPhoto?chat_id=%s&photo=%s&caption=%s",
                chatId, fileId, imageCaption);
        try{
            URL url = new URL(rec);
            BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
