package com.space;

import com.space.connector.dbClient.DB;
import com.space.connector.dbClient.DBConnector;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgPool;

public class Main {
  public static void main(String[] args) {
    try {
      Vertx vertx = Vertx.vertx();
      DBConnector sqlConnector = new DBConnector();
      PgPool pgPool = sqlConnector.getPool(vertx);
      DB.setPool(pgPool);
      vertx.deployVerticle(MainVerticle.class.getName()).onSuccess(handler -> {
        System.out.println(handler);
      }).onFailure(handler -> {
        System.out.println(handler);
      });
    } catch (Exception e) {
      throw e;
    }
  }
}
