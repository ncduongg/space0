package com.space.controller.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.Main;
import com.space.connector.dbClient.DB;
import com.space.controller.file.FileUploadController;
import com.space.models.Category;
import static com.space.util.Util.*;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
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

    public void pUploadFile(RoutingContext rc, FileSystem fileSystem, List<FileUpload> fileUploads) {
        logger.log(Level.INFO, "===========UPLOAD FILE==========");
        FileUploadController fileUploadC = new FileUploadController();
        Promise<JsonObject> p = Promise.promise();
        fileUploadC.pUploadImagesHandler(fileSystem, fileUploads, p);
        p.future().onComplete(ar -> {
            if (ar.succeeded()) {
                JsonObject file = ar.result();
                JsonObject jRes = new JsonObject()
                        .put("status", "200")
                        .put("response_code", "000")
                        .put("content", file);
                sendRespone(rc, 200, jRes);
            }
            if (ar.failed()) {
                rc.fail(ar.cause());
            }
        });
    }

    public void pCreateProduct(RoutingContext rc, JsonObject body) {
        List<FileUpload> filesUploads = rc.fileUploads();
        FileSystem fileSystem = Main.fileUpload.getFileSystem();
        FileUploadController fileUploadC = new FileUploadController();
        fileUploadC.pUploadImagesHandler(fileSystem, filesUploads, handlerUpfile ->{
            if(handlerUpfile.succeeded()){
                body.put("file", handlerUpfile.result());
                DB.pCreateProduct(body, ar ->{
                    if(ar.succeeded()){
                        JsonObject jRes = new JsonObject()
                        .put("response_code", "000")
                        .put("status", "400")
                        .put("message", "Tạo mơi thành công sản phẩm  " + body.getString("prod_name"))
                        .put("content", new JsonObject());
                        sendRespone(rc, 200, jRes);
                    }
                    if(ar.failed()) {
                        logger.log(Level.SEVERE, "" + ar.cause().getMessage());
                        rc.fail(ar.cause());
                    }
                });
            }
            if(handlerUpfile.failed()){
                logger.log(Level.SEVERE, "" + handlerUpfile.cause().getMessage());
                rc.fail(handlerUpfile.cause());
            }
        });
    }
}
