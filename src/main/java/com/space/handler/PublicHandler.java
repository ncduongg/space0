package com.space.handler;

import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.controller.client.PublicController;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

public class PublicHandler {
    private static final Logger logger = Logger.getLogger(PublicHandler.class.getName());
    private static PublicController pController = new PublicController();

    public static void getListCategory(RoutingContext rc) {
        try {
            pController.gCategory(rc);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] getListCategory :", e);
            rc.fail(e);
        }
    }

    public static void getCategory(RoutingContext rc) {
        try {
            String id = rc.request().getParam("id");
            pController.gCategory(rc, id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] getListCategory :", e);
            rc.fail(e);
        }
    }

    public static void getListProduct() {

    }

    public static void gProduct(RoutingContext rc) {
        try {
            String cateid = rc.request().getParam("cate_id");
            String key = rc.request().getParam("key").toLowerCase();
            logger.info("category1 : " + cateid  + " key :" + key);
            key = URLDecoder.decode(key, "UTF-8");
            logger.info("category2 : " + cateid  + " key :" + key);
            pController.gProduct(rc, cateid, key);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] gProduct :", e);
            rc.fail(e);
        }
    }
    public static void gProductDetail(RoutingContext rc) {
        try {
            String id = rc.request().getParam("id");
            pController.gProductDetail(rc, id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] gProduct :", e);
            rc.fail(e);
        }
    }
}
