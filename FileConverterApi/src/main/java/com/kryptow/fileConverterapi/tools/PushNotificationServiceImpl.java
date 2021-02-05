package com.kryptow.fileConverterapi.tools;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class PushNotificationServiceImpl {
    private final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private final String FIREBASE_SERVER_KEY = "AAAAttKIJSQ:APA91bHBYRk2WHzzp-ygbQ8pT4FnNijnWeXMJJvw4xARTvTeLOJ8hsRtS3Ech3oipvRgqAOAfduHjH2JOqJyBH4PvXgX25lCrH5BXZdec_fuA1sotuPgNy20vdOcd1RwlBp83qHaPt9r";
    
    public void sendPushNotification(String key,String url,String languageCode) {

    	String message[] = initLanguage(languageCode);
        JSONObject msg = new JSONObject();

        msg.put("title", message[0]);
        msg.put("body", message[1]);
        msg.put("url", url);
        msg.put("click_action", "FLUTTER_NOTIFICATION_CLICK");

  
        String response = callToFcmServer(msg, key);
        //System.out.println(response);

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
    
    private String[] initLanguage(String languageCode) {
    	String[] message = new String[2];
    	switch (languageCode) {
		case "tr":
			message[0] = "Dosya başarıyla dönüştürüldü";
			message[1] = "İndirmek için tıkla";
			break;
		case "es":
			message[0] = "El archivo se ha convertido correctamente";
			message[1] = "Haga clic para descargar";
			break;
		default:
			message[0] = "The file has been successfully converted";
			message[1] = "Click to download";
			break;
		}
    	return message;
    }
}