package com.space.handler;

import java.util.logging.Logger;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ServiceHandler {
  private static final Logger logger = Logger.getLogger(ServiceHandler.class.getName());

  public static void getListItemHome(RoutingContext ctx) {

    JsonObject bodyRes = new JsonObject().put("name", "duong");
    ctx.response().putHeader("content-type", "application/json;charset=UTF-8").end(bodyRes.toBuffer());
  }

  public static void testPost(RoutingContext ctx) {
    JsonObject bodyRes = ctx.body().asJsonObject();
    ctx.response().putHeader("content-type", "application/json;charset=UTF-8").end(bodyRes.toBuffer());
  }
}
