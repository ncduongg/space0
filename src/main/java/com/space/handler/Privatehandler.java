package com.space.handler;

import com.space.util.Util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import static com.space.util.Util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.controller.client.PrivateController;
import com.space.models.Category;

public class Privatehandler {
    private static final Logger logger = Logger.getLogger(Privatehandler.class.getName());
    public static PrivateController pController = new PrivateController();

    public static void pCreateOrUpdateCategory(RoutingContext rc) {
        logger.log(Level.INFO, "============pCreateOrUpdateCategory START==========");
        try {
            JsonObject jCategory = rc.body().asJsonObject();
            String cateId = jCategory.getString("cate_id");
            String cateName = jCategory.getString("cate_name");
            String cateDes = jCategory.getString("cate_des");
            String mess = checkNullOrEmpty(cateId, cateName, cateDes);
            if (!"".equals(mess)) {
                JsonObject jRes = new JsonObject()
                        .put("response_code", "002")
                        .put("status", "400")
                        .put("message", mess)
                        .put("content", new JsonObject());
                sendRespone(rc, 200, jRes);
                return;
            }
            Category category = new Category();
            category.setId(cateId);
            category.setName(cateName);
            category.setDes(cateDes);
            pController.pCreateOrUpdateCategory(rc, category);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            rc.fail(e);
        }
    }
}
