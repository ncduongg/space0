package com.space;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.handler.AuthHandler;
import com.space.handler.PublicHandler;
import com.space.handler.ServiceHandler;
import com.space.util.Util;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import io.vertx.ext.web.handler.TimeoutHandler;

public class MainVerticle extends AbstractVerticle {
  public static Properties p = Config.gProperties();
  public static final int port = Integer.parseInt(p.getProperty("port"));
  private static Logger logger = Logger.getLogger(MainVerticle.class.getName());
  public static WebClient webClient;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    try {
      Router router = Router.router(vertx);
      WebClientOptions options = new WebClientOptions().setIdleTimeout(120).setSsl(true).setTrustAll(true)
          .setMaxPoolSize(300);
      webClient = WebClient.create(vertx, options);
      KeyStoreOptions keyStore = new KeyStoreOptions().setType("jceks")
          .setPath("src/main/java/com/space/auth/keys/onecomm.jceks").setPassword("");
      JWTOptions jwtOptions = new JWTOptions().setAlgorithm("RSA");
      JWTAuthOptions jwtAuthOptions = new JWTAuthOptions().setKeyStore(keyStore).setJWTOptions(jwtOptions);
      JWTAuth jwt = JWTAuth.create(vertx, jwtAuthOptions);
      Set<String> allowedHeaders = new HashSet<>();
      allowedHeaders.add("x-requested-with");
      allowedHeaders.add("Access-Control-Allow-Origin");
      allowedHeaders.add("origin");
      allowedHeaders.add("Content-Type");
      allowedHeaders.add("accept");
      allowedHeaders.add("X-PINGARUNER");

      Set<HttpMethod> allowedMethods = new HashSet<>();
      allowedMethods.add(HttpMethod.GET);
      allowedMethods.add(HttpMethod.POST);
      allowedMethods.add(HttpMethod.DELETE);
      allowedMethods.add(HttpMethod.PATCH);
      allowedMethods.add(HttpMethod.OPTIONS);
      allowedMethods.add(HttpMethod.PUT);
      router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
      router.route().handler(BodyHandler.create());
      router.route().handler(ResponseTimeHandler.create());
      router.route().handler(TimeoutHandler.create(60000));
      router.route().failureHandler(Util::failureResponse);

      // API Public
      /* Category */
      router.get("/api/public/categories").produces("application/json")
          .handler(PublicHandler::getListCategory);
      router.get("/api/public/category/:id").produces("application/json")
          .handler(PublicHandler::getCategory);
      /* Author */
      router.post("/api/public/register").produces("application/json").consumes("application/json")
          .handler(AuthHandler::register);
      router.post("/api/public/login").produces("application/json").consumes("application/json")
          .handler(AuthHandler::login);

      // API Private
      router.get("/api/getItems").produces("application/json").handler(ServiceHandler::getListItemHome);
      router.post("/api/post").produces("application/json").consumes("application/json")
          .handler(ServiceHandler::testPost);
      vertx.createHttpServer().requestHandler(router).listen(port, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port " + port);
        } else {
          startPromise.fail(http.cause());
        }
      });
    } catch (

    Exception e) {
      logger.log(Level.SEVERE, "", e);
    }
  }

  private void handleFailure(RoutingContext rc) {
    rc.response()
        .putHeader("Content-type", "application/json; charset=utf-8")
        .setStatusCode(500)
        .end(rc.failure().getMessage() == null ? "ok" : "internal server error");
  }
}
