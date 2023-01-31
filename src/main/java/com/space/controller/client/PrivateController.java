package com.space.controller.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.connector.dbClient.DB;
import com.space.models.Category;
import static com.space.util.Util.*;

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
    public void pUploadFile(RoutingContext rc,FileSystem fileSystem,List<FileUpload> fileUploads) {
        logger.log(Level.INFO, "===========UPLOAD FILE==========");
        for (FileUpload fileUpload : fileUploads) {
            // Doc file vua Upload
            fileSystem.readFile(fileUpload.uploadedFileName(), handlerReadfile ->{
                if(handlerReadfile.succeeded()) {
                    fileSystem.writeFile("public/images/" + System.currentTimeMillis() + fileUpload.fileName(), handlerReadfile.result()).onComplete(handlerWriteFile ->{
                        if(handlerWriteFile.succeeded()){
                            sendRespone(rc, 200, new JsonObject());
                        }
                        if(handlerWriteFile.failed()){
                            logger.log(Level.SEVERE, handlerReadfile.cause().getMessage());
                            rc.fail(handlerWriteFile.cause());
                        }
                    });
                }
                if(handlerReadfile.failed()){
                    logger.log(Level.SEVERE, handlerReadfile.cause().getMessage());
                    rc.fail(handlerReadfile.cause());
                }
            });
        }
    }
}
