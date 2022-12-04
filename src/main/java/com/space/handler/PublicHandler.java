package com.space.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.controller.client.PublicController;

import io.vertx.ext.web.RoutingContext;

public class PublicHandler {
    private static final Logger logger = Logger.getLogger(PublicHandler.class.getName());
    private static PublicController pController = new PublicController();

    public static void getListCategory(RoutingContext rc) {
        try {
            pController.gCategory(rc);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] getListCategory :", e);
        }
    }

    public static void getCategory(RoutingContext rc) {
        try {
            String id = rc.request().getParam("id");
            pController.gCategory(rc, id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] getListCategory :", e);
        }
    }

    public static void createCategory() {

    }

    public static void getListProduct() {

    }

    public static void createProduct() {

    }
}
