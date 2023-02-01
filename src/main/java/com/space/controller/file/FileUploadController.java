package com.space.controller.file;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.util.Util;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;

public class FileUploadController {
    private static final Logger logger = Logger.getLogger(FileUploadController.class.getName());
    public void pUploadImagesHandler(FileSystem fileSystem,List<FileUpload> fileUploads,Handler<AsyncResult<JsonObject>> handle) {
        pUploadFileHandler("images/", fileSystem, fileUploads, handle);
    }
    private void pUploadFileHandler (String typeFile,FileSystem fileSystem,List<FileUpload> fileUploads,Handler<AsyncResult<JsonObject>> handle) { 
        
        for (FileUpload fileUpload : fileUploads) {
    
            fileSystem.readFile(fileUpload.uploadedFileName(), handlerReadfile -> {
                if (handlerReadfile.succeeded()) {
                    String imgId = Util.idRandom("IMG");
                    String fileName  = System.currentTimeMillis() + fileUpload.fileName();
                    fileSystem.writeFile("public/" + fileName,
                            handlerReadfile.result()).onComplete(handlerWriteFile -> {
                                if (handlerWriteFile.succeeded()) {
                                    JsonObject result = new JsonObject()
                                                .put("img_id", imgId)
                                                .put("img_file_name", fileName)
                                                .put("img_statle", "active");
                                    fileSystem.delete(fileUpload.uploadedFileName());
                                    handle.handle(Future.succeededFuture(result));
                                }
                                if (handlerWriteFile.failed()) {
                                    logger.log(Level.SEVERE, handlerReadfile.cause().getMessage());
                                    handle.handle(Future.failedFuture(handlerWriteFile.cause()));;
                                }
                            });
                }
                if (handlerReadfile.failed()) {
                    logger.log(Level.SEVERE, handlerReadfile.cause().getMessage());
                    handle.handle(Future.failedFuture(handlerReadfile.cause()));;
                }
            });
        }
    }
}
