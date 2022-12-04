package com.space.controller.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.connector.dbClient.DB;
import com.space.models.Category;
import com.space.models.ModelsMapping.CategoryMap;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import com.space.util.*;

public class PublicController {
    private static final Logger logger = Logger.getLogger(PublicController.class.getName());

    public void gCategory(RoutingContext rc) throws Exception {
        Promise<List<Category>> p = Promise.promise();
        try {
            DB.gCategory(p);
            p.future().onSuccess(ar -> {
                JsonArray jCategories = new JsonArray();
                for (Category category : ar) {
                    JsonObject jCategory = new JsonObject()
                            .put("cate_id", category.getId())
                            .put("cate_name", category.getName())
                            .put("cate_note", category.getNote())
                            .put("cate_des", category.getDes())
                            .put("cate_date", Util.iso8601(category.getDate()))
                            .put("cate_date_update", Util.iso8601(category.getDateUpdate()))
                            .put("cate_sub", category.getSub());
                    jCategories.add(jCategory);
                }
                logger.info("=======" + jCategories);
                Util.sendRespone(rc, 200, jCategories.toBuffer());
            }).onFailure(error -> {
                logger.log(Level.SEVERE, "[ERROR] gCategory : ", error);
                Util.sendRespone(rc, 200, new JsonObject().toBuffer());
            });
        } catch (Exception e) {
            throw e;
        }
    }

    public void gCategory(RoutingContext rc, String id) throws Exception {
        Promise<List<Category>> p = Promise.promise();
        DB.gCategory(p);
        p.future().onSuccess(ar -> {
            JsonArray jCategories = new JsonArray();
            for (Category category : ar) {
                if (id.equals(category.getId())) {
                    JsonObject jCategory = new JsonObject()
                            .put("cate_id", category.getId())
                            .put("cate_name", category.getName())
                            .put("cate_note", category.getNote())
                            .put("cate_des", category.getDes())
                            .put("cate_date", Util.iso8601(category.getDate()))
                            .put("cate_date_update", Util.iso8601(category.getDateUpdate()))
                            .put("cate_sub", category.getSub());
                    jCategories.add(jCategory);
                }
            }
            logger.info("=======" + jCategories);
            Util.sendRespone(rc, 200, jCategories.toBuffer());
        }).onFailure(error -> {
            logger.log(Level.SEVERE, "[ERROR] gCategory : ", error);
            Util.sendRespone(rc, 200, new JsonObject().toBuffer());
        });
    }
}
