package com.space.connector.imgur;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.space.Config;
import com.space.connector.httpClient.httpClient;

import io.vertx.core.http.HttpMethod;

public class imgurClient {
    private static Properties p = Config.gProperties();
    private static final String keyAuthImgur = p.getProperty("imgur.key");
    private static final String host = p.getProperty("imgur.host");
    private static final String pathUpload = p.getProperty("imgur.upload");

    public static void uploadImg() {
        String url = host + pathUpload;

        httpClient.createWebClientRequest(HttpMethod.POST, url, 60000, headerImgur(), null, null).onSuccess(ar -> {

        }).onFailure(error -> {

        });
    }

    private static Map<String, String> headerImgur() {
        Map<String, String> header = new HashMap<>();
        header.put("Access-Control-Allow-Headers",
                "Authorization, Content-Type, Accept, X-Mashape-Authorization, IMGURPLATFORM, IMGURUIDJAFO, SESSIONCOUNT, IMGURMWBETA, IMGURMWBETAOPTIN");
        header.put("Authorization", "Client-ID " + keyAuthImgur);
        header.put("Content-Type", "application/json");
        return header;
    }
}
