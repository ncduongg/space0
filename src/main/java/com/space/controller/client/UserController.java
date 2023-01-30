package com.space.controller.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.connector.dbClient.DB;
import com.space.models.User;
import static com.space.util.Util.sendRespone;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public void gGetUserProfile(RoutingContext rc) throws Exception {
        Promise<User> p = Promise.promise();
        JsonObject jResponse = new JsonObject();
        String username = rc.get("username");
        DB.pGetProfile(username, p);
        p.future().onSuccess(user -> {
            JsonObject jUser = new JsonObject();
            jUser.put("user_id", user.getUserId());
            jUser.put("username", user.getUserName());
            jUser.put("name", user.getName());
            jUser.put("phone", user.getPhone());
            jUser.put("email", user.getEmail());
            jResponse.put("message", "Success").put("response_code", "000").put("content", jUser);
            sendRespone(rc, 200, jResponse);
        }).onFailure(rc::fail);
    }
}
