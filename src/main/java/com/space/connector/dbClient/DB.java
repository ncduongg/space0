package com.space.connector.dbClient;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.models.Category;
import com.space.models.ModelsMapping.CategoryMap;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class DB {
    private static final Logger logger = Logger.getLogger(DB.class.getName());
    private static PgPool pgPool;

    public static void setPool(PgPool pgPool) {
        DB.pgPool = pgPool;
    }

    public static void pRegister(String userid, String name, String username, String email, String phone,
            Handler<AsyncResult<Boolean>> handler) throws Exception {
        try {
            pgPool.getConnection().onSuccess(conn -> {
                conn.preparedQuery(
                        "INSERT INTO tb_user(user_id,name,username,email,phone) VALUES ($1,$2,$3,$4,$5)")
                        .execute(Tuple.of(userid, name, username, email, phone)).onComplete(ar -> {
                            handler.handle(Future.succeededFuture(true));
                        }).eventually(cls -> {
                            return conn.close();
                        }).onFailure(error -> {
                            handler.handle(Future.failedFuture(error));
                            logger.log(Level.SEVERE, "[ERROR] register : ", error);
                        });
            }).onFailure(error -> {
                handler.handle(Future.failedFuture(error));
                logger.log(Level.SEVERE, "[ERROR] register : ", error);
            });
        } catch (Exception e) {
            throw e;
        }
    }

    public static void gCategory(Handler<AsyncResult<List<Category>>> handler) throws Exception {
        try {
            pgPool.getConnection().onSuccess(conn -> {
                conn.query("SELECT * FROM tb_category").execute().onComplete(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rowSet = ar.result();
                        List<Category> result = CategoryMap.DBCategoryMap(rowSet);
                        handler.handle(Future.succeededFuture(result));
                    } else {
                        handler.handle(Future.failedFuture(ar.cause()));
                    }
                }).onFailure(error -> {
                    handler.handle(Future.failedFuture(error));
                    logger.log(Level.SEVERE, "[ERROR] querry : ", error);
                }).eventually(close -> {
                    logger.info("Dong connect :" + close);
                    return conn.close();
                });
            }).onFailure(error -> {
                logger.log(Level.SEVERE, "[ERROR] getConnection : ", error);
            });
        } catch (Exception e) {
            throw e;
        }
    }
}
