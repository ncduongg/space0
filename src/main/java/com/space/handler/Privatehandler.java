package com.space.handler;

import com.space.util.Util;

import io.vertx.core.MultiMap;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import static com.space.util.Util.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.Main;
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

    public static void pUploadFile(RoutingContext rc) {
        try {
            List<FileUpload> filesUploads = rc.fileUploads();
            FileSystem fileSystem = Main.fileUpload.getFileSystem();
            JsonObject jRes = new JsonObject()
                    .put("response_code", "002")
                    .put("status", "400")

                    .put("content", new JsonObject());
            if (filesUploads.size() == 0) {
                jRes.put("message", "Invalid upload file");
                sendRespone(rc, 200, jRes);
                return;
            }
            for (FileUpload file : filesUploads) {
                if (!file.contentType().startsWith("image/")) {
                    jRes.put("message", "Upload Image Only not : " + file.contentType());
                    sendRespone(rc, 200, jRes);
                    return;
                }
            }
            pController.pUploadFile(rc, fileSystem, filesUploads);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            rc.fail(e);
        }
    }

    public static void pCreateProduct(RoutingContext rc) {
        // JsonObject jReq = rc.body().asJsonObject();
        try {
            MultiMap params = rc.request().formAttributes();
            logger.info("params : " + params);
            if (params.size() == 0) {
                JsonObject jRes = new JsonObject()
                        .put("response_code", "002")
                        .put("status", "400")
                        .put("message", "Invalid params")
                        .put("content", new JsonObject());
                sendRespone(rc, 200, jRes);
                return;
            }
            JsonObject jPrams = new JsonObject();
            for (Map.Entry<String, String> entry : params.entries()) {
                jPrams.put(entry.getKey(), stringToJsonObject(entry.getValue()) == null ? entry.getValue()
                        : stringToJsonObject(entry.getValue()));
            }
            pController.pCreateProduct(rc, jPrams);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            rc.fail(e);
        }
    }

    public static void pCreateDonate(RoutingContext rc) {
        try {
            logger.info("=====HANDLER pCreateDonate=====" + rc.body().asJsonObject());
            JsonObject jReq = rc.body().asJsonObject();
            String doId = jReq.getString("do_id");
            String userId = jReq.getString("user_id");
            double doAmount = Double.parseDouble(jReq.getString("do_amount"));
            String doState = "create";
            pController.pCreateDonate(rc, doId, userId, doAmount, "", doState);
        } catch (Exception e) {
            rc.fail(e);
            logger.log(Level.SEVERE, "", e);
        }

    }
    public static void pUpdateDonate(RoutingContext rc) {
        try {
            JsonObject jReq = rc.body().asJsonObject();
            String doId = jReq.getString("do_id");
            String doData = jReq.getString("do_data");
            String doState = jReq.getString("do_state");
            pController.pUpdateDonate(rc, doId, doData, doState);
        } catch (Exception e) {
            rc.fail(e);
            logger.log(Level.SEVERE, "", e);
        }

    }
}
