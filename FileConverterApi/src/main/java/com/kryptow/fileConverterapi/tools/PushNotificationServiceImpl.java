package com.kryptow.fileConverterapi.tools;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class PushNotificationServiceImpl {
    private final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private final String FIREBASE_SERVER_KEY = "AAAAUUps8vw:APA91bFIfywanUfQTWtApzHfaGy23ZgRhz4E9hWqRWXs0gHZgRDfboDpHrbAsDw_KELHgduj4MDJOS_8ipj5m8rDduwB0mjIEBHCApEaEwPzUo9o2GMgqTFbzDQyAhse9SwRhkrMHXpB";
    private final String messageTitle = "";
    private final String message = "";
    
    public void sendPushNotification(String key) {


        JSONObject msg = new JSONObject();

        msg.put("title", messageTitle);
        msg.put("body", message);
        msg.put("click_action", "FLUTTER_NOTIFICATION_CLICK");

  
        String response = callToFcmServer(msg, key);

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