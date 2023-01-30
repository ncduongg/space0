package com.space.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.controller.client.UserController;

import io.vertx.ext.web.RoutingContext;

public class UserHandler {
    private static final Logger logger = Logger.getLogger(UserHandler.class.getName());
    private static UserController pUserController = new UserController();
    public static void gGetUserProfile(RoutingContext rc) {
        try {
            pUserController.gGetUserProfile(rc);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] gGetUserProfile :", e);
        }
    }
}
