package com.kryptow.fileConverterapi.tools;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class PushNotificationServiceImpl {
    private final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private final String FIREBASE_SERVER_KEY = "AAAAttKIJSQ:APA91bHBYRk2WHzzp-ygbQ8pT4FnNijnWeXMJJvw4xARTvTeLOJ8hsRtS3Ech3oipvRgqAOAfduHjH2JOqJyBH4PvXgX25lCrH5BXZdec_fuA1sotuPgNy20vdOcd1RwlBp83qHaPt9r";
    private final String messageTitle = "Dosya başarıyla dönüştürüldü";
    private final String message = "İndirmek için tıkla";
    
    public void sendPushNotification(String key,String url) {


        JSONObject msg = new JSONObject();

        msg.put("title", messageTitle);
        msg.put("body", message);
        msg.put("url", url);
        msg.put("click_action", "FLUTTER_NOTIFICATION_CLICK");

  
        String response = callToFcmServer(msg, key);
        System.out.println(response);

    }

    private String callToFcmServer(JSONObject message, String receiverFcmKey) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("data", message);
        json.put("notification", message);
        json.put("to", receiverFcmKey);

     //   System.out.println("Sending :" + json.toString());

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        return restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
    }
}