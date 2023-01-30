package com.space.controller.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.connector.dbClient.DB;
import com.space.models.Category;
import static com.space.util.Util.*;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class PrivateController {
    private static final Logger logger = Logger.getLogger(PrivateController.class.getName());

    public void pCreateOrUpdateCategory(RoutingContext rc, Category category) {
        try {
            Promise<Boolean> p = Promise.promise();
            DB.pCreateOrUpdateCategory(category, p);
            p.future().onSuccess(ar -> {
                JsonObject jRes = new JsonObject();
                if (ar) {
                    jRes.put("response_code", "000")
                            .put("status", "200")
                            .put("message", "Thêm mới/ Cập nhật thành công CateID: " + category.getId())
                            .put("content", new JsonObject());
                } else {
                    jRes.put("response_code", "999")
                            .put("status", "500")
                            .put("message", "Thêm mới/ Cập nhật THẤT BẠI CateID: " + category.getId())
                            .put("content", new JsonObject());
                }
                sendRespone(rc, 200, jRes);
            }).onFailure(e -> {
                logger.log(Level.SEVERE, "", e);
                rc.fail(e);
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            rc.fail(e);
        }
    }
}
