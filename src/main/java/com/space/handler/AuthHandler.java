package com.space.handler;

import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.controller.client.AuthController;
import com.space.util.Util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AuthHandler {
  private static final Logger logger = Logger.getLogger(AuthHandler.class.getName());

  public static void verifyAuth(RoutingContext rc) {
    String token = rc.request().getHeader("Authorization");
    JsonObject jResponse = new JsonObject();
    if (token == null || token.isBlank()) {
      jResponse.put("response_code", "002")
          .put("status", "400")
          .put("message", "Thông tin gửi lên không hợp lệ");
      Util.sendRespone(rc, 400, jResponse);
      return;
    }
    try {
      AuthController.verifyAuth(rc, token);
    } catch (Exception e) {
      rc.fail(e);
    }
  }

  public static void register(RoutingContext rc) {
    try {
      JsonObject body = rc.body().asJsonObject();
      JsonObject jResponse = new JsonObject();
      logger.log(Level.INFO, "register body : " + body);
      String username = body.getString("username");
      String email = body.getString("email");
      String phone = body.getString("phone");
      String password = body.getString("password");
      String name = body.getString("name");
      if (username == null || password == null || email == null || phone == null || name == null) {
        jResponse.put("response_code", "002")
            .put("status", "400")
            .put("message", "Thông tin gửi lên không hợp lệ");
        Util.sendRespone(rc, 200, jResponse);
        return;
      }
      if (username.isBlank() || password.isBlank() || email.isBlank() || phone.isBlank() || name.isBlank()) {
        jResponse.put("response_code", "002")
            .put("status", "400")
            .put("message", "Thông tin gửi lên không hợp lệ");
        Util.sendRespone(rc, 200, jResponse);
        return;
      }

      String userid = "USER_" + Base64.getUrlEncoder().withoutPadding()
          .encodeToString(Util.parseHexBinary(UUID.randomUUID().toString().replaceAll("-", "")));

      AuthController.Register(rc, userid, name, username, password, email, phone);
    } catch (

    Exception ex) {
      rc.fail(ex);
    }
  }

  public static void login(RoutingContext ctx) {
    try {
      JsonObject body = ctx.body().asJsonObject();
      String username = body.getString("username");
      String password = body.getString("password");
      if (username == null || password == null || username.isBlank() || password.isBlank()) {
        JsonObject bodyRes = new JsonObject().put("status", "400")
            .put("response_code", "002")
            .put("message", "Thông tin gửi lên không hợp lệ");
        Util.sendRespone(ctx, 200, bodyRes);
      }
      AuthController.Login(ctx, username, password);
    } catch (Exception ex) {
      ctx.fail(ex);
    }
  }
}
