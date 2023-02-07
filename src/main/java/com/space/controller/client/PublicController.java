package com.space.controller.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.connector.dbClient.DB;
import com.space.models.Category;
import com.space.models.Product;
import com.space.util.Util;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class PublicController {
    private static final Logger logger = Logger.getLogger(PublicController.class.getName());

    // Get ALL Category
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

    public void gProduct(RoutingContext rc, String category, String key) throws Exception {
        Promise<List<Product>> p = Promise.promise();
        DB.gProduct(category, key, p);
        p.future().onSuccess(ar -> {
            JsonArray jProducts = new JsonArray();
            for (Product product : ar) {
                JsonObject jProduct = new JsonObject()
                        .put("prod_id", product.getProdId())
                        .put("cate_id", product.getCateId())
                        .put("prod_name", product.getProdName())
                        .put("prod_des", product.getProdDes())
                        .put("prod_image", product.getProdImage())
                        .put("prod_url", product.getProdUrl())
                        .put("prod_type", product.getProdType())
                        .put("prod_note", product.getProdNote())
                        .put("prod_date", Util.iso8601(product.getProdDate()))
                        .put("prod_date_update", Util.iso8601(product.getProdDateUpdate()))
                        .put("prod_quantity", product.getProdQuantity())
                        .put("prod_data", product.getProdData())
                        .put("prod_state", product.getProdState())
                        .put("prod_active_auction", product.getProdActiveAuction())
                        .put("prod_amount", product.getProdAmount())
                        .put("prod_amount_dis", product.getProdAmountDis());
                jProducts.add(jProduct);
            }
            Util.sendRespone(rc, 200, jProducts.toBuffer());
        }).onFailure(error -> {
            logger.log(Level.SEVERE, "[ERROR] gProduct : ", error);
            rc.fail(error);
        });
    }
    public void gProductDetail(RoutingContext rc, String id) throws Exception {
        Promise<List<Product>> p = Promise.promise();
        DB.gProductDetail(id, p);
        p.future().onSuccess(ar -> {
            Product product = ar.get(0);
            JsonObject jProduct = new JsonObject()
                        .put("prod_id", product.getProdId())
                        .put("cate_id", product.getCateId())
                        .put("prod_name", product.getProdName())
                        .put("prod_des", product.getProdDes())
                        .put("prod_image", product.getProdImage())
                        .put("prod_url", product.getProdUrl())
                        .put("prod_type", product.getProdType())
                        .put("prod_note", product.getProdNote())
                        .put("prod_date", Util.iso8601(product.getProdDate()))
                        .put("prod_date_update", Util.iso8601(product.getProdDateUpdate()))
                        .put("prod_quantity", product.getProdQuantity())
                        .put("prod_data", product.getProdData())
                        .put("prod_state", product.getProdState())
                        .put("prod_active_auction", product.getProdActiveAuction())
                        .put("prod_amount", product.getProdAmount())
                        .put("prod_amount_dis", product.getProdAmountDis());
            Util.sendRespone(rc, 200, jProduct.toBuffer());
        }).onFailure(error -> {
            logger.log(Level.SEVERE, "[ERROR] gProduct : ", error);
            rc.fail(error);
        });
    }
}
