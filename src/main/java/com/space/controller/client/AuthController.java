package com.space.controller.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.Config;
import com.space.connector.dbClient.DB;
import com.space.connector.httpClient.httpClient;
import com.space.util.Util;

import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AuthController {
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private static Properties p = Config.gProperties();
    private static final String key = p.getProperty("auth.key");
    private static final String host = p.getProperty("auth.host");
    private static final String pathRegister = p.getProperty("auth.register");
    private static final String pathLogin = p.getProperty("auth.login");
    private static final String pathAuth = p.getProperty("auth.verify");

    public static void verifyAuth(RoutingContext rc, String token) throws Exception {
        logger.log(Level.INFO, "verifyAuth :" + token);
        String url = host + pathAuth;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JsonObject body = new JsonObject().put("access_token", token);
        httpClient.createWebClientRequest(HttpMethod.PUT, url, 60000, headers, "application/json", body.toBuffer())
                .onComplete(ar -> {
                    JsonObject bodyRes = ar.result().bodyAsJsonObject();
                    logger.log(Level.INFO, "verifyAuth : " + bodyRes);
                    String responseCode = bodyRes.getString("response_code");
                    if ("000".equals(responseCode)) {
                        rc.put("username", bodyRes.getJsonObject("content").getString("username"));
                        rc.next();
                        return;
                    } else {
                        Util.sendRespone(rc, 200, bodyRes);
                    }
                });
    }

    public static void Login(RoutingContext rc, String username, String password) throws Exception {
        logger.log(Level.INFO, "Login :" + username + "-" + password);
        String url = host + pathLogin;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JsonObject body = new JsonObject()
                .put("username", username)
                .put("password", password);
        httpClient.createWebClientRequest(HttpMethod.POST, url, 60000, headers, "application/json", body.toBuffer())
                .onComplete(ar -> {
                    JsonObject bodyRes = ar.result().bodyAsJsonObject();
                    logger.log(Level.INFO, "Auth Login : " + bodyRes);
                    String responseCode = bodyRes.getString("response_code");
                    if (responseCode.equals("000")) {
                        String token = bodyRes.getJsonObject("content").getString("access_token");
                        Cookie cookie = Cookie.cookie("Authorization", token);
                        cookie.setHttpOnly(true).setPath("/").setMaxAge(60000).encode();
                        rc.response().addCookie(cookie)
                                .putHeader("content-type", "application/json")
                                .putHeader("Authorization", token)
                                .end(bodyRes.toBuffer());
                    } else {
                        Util.sendRespone(rc, 200, bodyRes);
                    }
                });
    }

    public static void Register(RoutingContext rc, String id, String name, String username, String password,
            String email,
            String phone) throws Exception {
        logger.log(Level.INFO, "Register : " + username + "-" + password + "-" + email + "-" + phone);
        String url = host + pathRegister;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JsonObject body = new JsonObject()
                .put("username", username)
                .put("password", password);
        httpClient.createWebClientRequest(HttpMethod.POST, url, 60000, headers, "application/json", body.toBuffer())
                .onComplete(ar -> {
                    JsonObject bodyRes = ar.result().bodyAsJsonObject();
                    logger.log(Level.INFO, "Auth Register : " + bodyRes);
                    String responseCode = bodyRes.getString("response_code");
                    if (responseCode.equals("000")) {
                        try {
                            DB.pRegister(id, name, username, email, phone, ar1 -> {
                                if (ar1.succeeded()) {
                                    Util.sendRespone(rc, 200, bodyRes);
                                    return;
                                } else {
                                    bodyRes.put("status", "500")
                                            .put("response_code", "999")
                                            .put("message", ar1.cause().getMessage());
                                    Util.sendRespone(rc, 200, bodyRes);
                                    return;
                                }
                            });
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, e.getMessage());
                            bodyRes.put("status", "500")
                                    .put("response_code", "999")
                                    .put("message", e.getMessage());
                            Util.sendRespone(rc, 200, bodyRes);
                            return;
                        }
                    } else {
                        logger.info("=========" + bodyRes);
                        Util.sendRespone(rc, 200, bodyRes);
                        return;
                    }
                });
    }
}
