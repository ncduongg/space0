package com.space.connector.dbClient;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.models.Category;
import com.space.models.Product;
import com.space.models.User;
import com.space.models.ModelsMapping.CategoryMap;
import com.space.models.ModelsMapping.ProductMap;
import com.space.models.ModelsMapping.UserMap;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
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
                    conn.close();
                }).onFailure(error -> {
                    handler.handle(Future.failedFuture(error));
                    logger.log(Level.SEVERE, "[ERROR] querry : ", error);
                }).eventually(close -> {
                    logger.info("Dong connect :" + close);
                    return conn.close();
                });
            }).onFailure(error -> {
                logger.log(Level.SEVERE, "[ERROR] getConnection : ", error);
                handler.handle(Future.failedFuture(error));
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] getConnection : ", e);
            throw e;
        }
    }

    public static void pCreateOrUpdateCategory(Category category, Handler<AsyncResult<Boolean>> handler)
            throws Exception {
        try {
            pgPool.getConnection().onSuccess(conn -> {
                conn.preparedQuery("SELECT * FROM TB_CATEGORY WHERE cate_id = $1")
                        .execute(Tuple.of(category.getId()))
                        .onSuccess(ar -> {
                            if (ar.rowCount() > 0) {
                                conn.preparedQuery(
                                        "UPDATE TB_CATEGORY SET cate_name = $1, cate_des = $2, cate_date_update = NOW() WHERE cate_id = $3")
                                        .execute(Tuple.of(category.getName(), category.getDes(), category.getId()))
                                        .onSuccess(ar1 -> {
                                            handler.handle(Future.succeededFuture(true));
                                        })
                                        .onFailure(e -> {
                                            logger.log(Level.SEVERE,
                                                    "[ERROR] pCreateOrUpdateCategory Count by ID Cate : ", e);
                                            handler.handle(Future.failedFuture(e));
                                        }).eventually(res -> conn.close());
                            }
                            if (ar.rowCount() == 0) {
                                conn.preparedQuery(
                                        "INSERT INTO TB_CATEGORY (cate_id,cate_name,cate_note,cate_des,cate_date,cate_date_update,cate_sub) VALUES ($1,$2,$3,$4,NOW(),NOW(),1)")
                                        .execute(Tuple.of(category.getId(), category.getName(), category.getNote(),
                                                category.getDes()))
                                        .onSuccess(ar1 -> {
                                            handler.handle(Future.succeededFuture(true));
                                        })
                                        .onFailure(e -> {
                                            logger.log(Level.SEVERE,
                                                    "[ERROR] pCreateOrUpdateCategory Count by ID Cate : ", e);
                                            handler.handle(Future.failedFuture(e));
                                        }).eventually(res -> conn.close());
                            }
                        }).onFailure(e -> {
                            logger.log(Level.SEVERE, "[ERROR] pCreateOrUpdateCategory Count by ID Cate : ", e);
                            handler.handle(Future.failedFuture(e));
                        }).eventually(res -> conn.close());
            }).onFailure(e -> {
                logger.log(Level.SEVERE, "[ERROR] pCreateOrUpdateCategory get Conn: ", e);
                handler.handle(Future.failedFuture(e));
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[ERROR] pCreateOrUpdateCategory get Conn: ", e);
            handler.handle(Future.failedFuture(e));
        }
    }

    public static void pGetProfile(String username, Handler<AsyncResult<User>> handler) throws Exception {
        try {
            pgPool.getConnection().onSuccess(conn -> {
                conn.preparedQuery("SELECT * FROM TB_USER WHERE username = $1")
                        .execute(Tuple.of(username))
                        .onSuccess(ar -> {
                            User user = UserMap.DbUserMap(ar);
                            handler.handle(Future.succeededFuture(user));
                        }).onFailure(error -> {
                            logger.log(Level.SEVERE, "[ERROR] pGetProfile : ", error);
                            handler.handle(Future.failedFuture(error));
                        }).eventually(close -> {
                            return conn.close();
                        });
            }).onFailure(error -> {
                logger.log(Level.SEVERE, "[ERROR] getConnection : ", error);
                handler.handle(Future.failedFuture(error));
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            throw e;
        }
    }

    public static void pCreateProduct(JsonObject body, Handler<AsyncResult<Void>> handler) {
        logger.info("body : " + body);
        try {
            pgPool.getConnection().onComplete(ar -> {
                if (ar.succeeded()) {
                    SqlConnection conn = ar.result();
                    conn.preparedQuery(
                            "INSERT INTO TB_PRODUCT(prod_id,cate_id,prod_name,prod_des,prod_images,prod_url,prod_type,prod_notes,prod_date,prod_date_update,prod_data,prod_state,prod_active_auction,prod_amount,prod_amount_dis) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,NOW(),NOW(),$9,$10,'inactive',$11,$12)")
                            .execute( // ($1,$2,$3,$4,$5,$6,$7,$8,NOW(),NOW(),$9,$10,'inactive',$11,$12)
                                    Tuple.of(
                                            body.getString("prod_id"),
                                            body.getString("cate_id"),
                                            body.getString("prod_name"),
                                            body.getString("prod_des"),
                                            body.getJsonObject("file").getString("img_id"),
                                            body.getString("prod_url"),
                                            body.getString("prod_type"),
                                            body.getString("prod_notes"),
                                            body.getString("prod_data"),
                                            body.getString("prod_state"),
                                            Double.valueOf(body.getString("prod_amount")),
                                            Double.valueOf(body.getString("prod_amount_dis"))),
                                    handl -> {
                                        if (handl.succeeded()) {
                                            conn.preparedQuery(
                                                    "INSERT INTO TB_IMAGES (img_id,img_file_name,img_state) VALUES($1,$2,'active')")
                                                    .execute(Tuple.of(body.getJsonObject("file").getString("img_id"),
                                                            body.getJsonObject("file").getString("img_file_name")))
                                                    .onComplete(ar1 -> {
                                                        if (ar1.succeeded()) {
                                                            handler.handle(Future.succeededFuture());
                                                        }
                                                        if (ar1.failed()) {
                                                            logger.log(Level.SEVERE, "", ar1.cause());
                                                            handler.handle(Future.failedFuture(ar1.cause()));
                                                        }
                                                    });
                                        }
                                        if (handl.failed()) {
                                            handler.handle(Future.failedFuture(handl.cause()));
                                        }
                                    });
                    conn.close();
                }
                if (ar.failed()) {
                    handler.handle(Future.failedFuture(ar.cause()));
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            throw e;
        }
    }

    public static void gProduct(String category, String key, Handler<AsyncResult<List<Product>>> resultHandler) {
        logger.info("category : " + category + " key :" + key);
        pgPool.getConnection().onComplete(con -> {
            if (con.succeeded()) {
                SqlConnection connection = con.result();
                if (!"all".equals(category) && !"".equals(key)) {
                    connection.preparedQuery(
                            "SELECT p.*,i.img_file_name as prod_images_name FROM public.tb_product p inner join tb_images i on p.prod_images = img_id WHERE p.cate_id = $1 and lower(p.prod_name) like $2")
                            .execute(Tuple.of(category, "%" + key + "%"), result -> {
                                if (result.succeeded()) {
                                    resultHandler
                                            .handle(Future.succeededFuture(ProductMap.DBProductMap(result.result())));
                                    connection.close();
                                } else {
                                    resultHandler.handle(Future.failedFuture(result.cause()));
                                    connection.close();
                                }
                            });
                }
                if (!"all".equals(category) && "".equals(key)) {
                    connection.preparedQuery(
                            "SELECT p.*,i.img_file_name as prod_images_name FROM public.tb_product p inner join tb_images i on p.prod_images = img_id WHERE p.cate_id = $1")
                            .execute(Tuple.of(category), result -> {
                                if (result.succeeded()) {
                                    resultHandler
                                            .handle(Future.succeededFuture(ProductMap.DBProductMap(result.result())));
                                    connection.close();
                                } else {
                                    resultHandler.handle(Future.failedFuture(result.cause()));
                                    connection.close();
                                }
                            });
                }
                if ("all".equals(category) && "".equals(key)) {
                    connection.preparedQuery(
                            "SELECT p.*,i.img_file_name as prod_images_name FROM public.tb_product p inner join tb_images i on p.prod_images = img_id")
                            .execute().onComplete(result -> {
                                if (result.succeeded()) {
                                    resultHandler
                                            .handle(Future.succeededFuture(ProductMap.DBProductMap(result.result())));
                                    connection.close();
                                } else {
                                    resultHandler.handle(Future.failedFuture(result.cause()));
                                    connection.close();
                                }
                            });
                }
                if ("all".equals(category) && !"".equals(key)) {
                    connection.preparedQuery(
                            "SELECT p.*,i.img_file_name as prod_images_name FROM public.tb_product p inner join tb_images i on p.prod_images = img_id WHERE lower(p.prod_name) like $1 ")
                            .execute(Tuple.of("%" + key + "%")).onComplete(result -> {
                                if (result.succeeded()) {
                                    resultHandler
                                            .handle(Future.succeededFuture(ProductMap.DBProductMap(result.result())));
                                    connection.close();
                                } else {
                                    resultHandler.handle(Future.failedFuture(result.cause()));
                                    connection.close();
                                }
                            });
                }
            } else {
                resultHandler.handle(Future.failedFuture(con.cause()));
            }
        });
    }

    public static void gProductDetail(String id, Handler<AsyncResult<List<Product>>> resultHandler) {
        pgPool.getConnection().onComplete(con -> {
            if (con.succeeded()) {
                logger.info("id Product : " + id);
                SqlConnection connection = con.result();
                connection.preparedQuery(
                        "SELECT p.*,i.img_file_name as prod_images_name FROM public.tb_product p inner join tb_images i on p.prod_images = img_id WHERE p.prod_id = $1")
                        .execute(Tuple.of(id), result -> {
                            if (result.succeeded()) {
                                resultHandler.handle(Future.succeededFuture(ProductMap.DBProductMap(result.result())));
                                connection.close();
                            } else {
                                resultHandler.handle(Future.failedFuture(result.cause()));
                                connection.close();
                            }
                        });
            } else {
                resultHandler.handle(Future.failedFuture(con.cause()));
            }
        });
    }

    public static void pCreateDonate(String do_id, String user_id, double do_amount, String do_data,
            String do_state,
            Handler<AsyncResult<Void>> resultHandler) {
        pgPool.getConnection().onComplete(con -> {
            if (con.succeeded()) {
                SqlConnection connection = con.result();
                connection.preparedQuery(
                        "INSERT INTO tb_donate (do_id, user_id, do_amount, do_data, do_state) VALUES ($1, $2, $3, $4, $5)")
                        .execute(Tuple.of(do_id, user_id, do_amount, do_data, do_state), result -> {
                            if (result.succeeded()) {
                                resultHandler.handle(Future.succeededFuture());
                                connection.close();
                            } else {
                                resultHandler.handle(Future.failedFuture(result.cause()));
                                connection.close();
                            }
                        });
            } else {
                resultHandler.handle(Future.failedFuture(con.cause()));
            }
        });
    }

    public static void pUpdateDonate(String doId, String doData, String doState,
            Handler<AsyncResult<Void>> resultHandler) {
        pgPool.getConnection().onComplete(con -> {
            if (con.succeeded()) {
                SqlConnection connection = con.result();
                connection.preparedQuery(
                        "UPDATE tb_donate SET do_data = $2 WHERE do_state = 'create' and do_id = $1")
                        .execute(Tuple.of(doId, doData), result -> {
                            if (result.succeeded()) {
                                connection.preparedQuery(
                                        "UPDATE tb_user set amount = (tb_user.amount + d.do_amount) from tb_donate d  where tb_user.user_id = d.user_id and d.do_state = 'create' and d.do_id = $1")
                                        .execute(Tuple.of(doId), update -> {
                                            if (update.succeeded()) {
                                                connection.preparedQuery(
                                                        "UPDATE tb_donate SET do_state = $2 WHERE do_state = 'create' and do_id = $1")
                                                        .execute(Tuple.of(doId,doState), updateState -> {
                                                            if (updateState.succeeded()) {
                                                                resultHandler.handle(Future.succeededFuture());
                                                                connection.close();
                                                            } else {
                                                                resultHandler.handle(Future.failedFuture(updateState.cause()));
                                                                connection.close();
                                                            }
                                                        });

                                            } else {
                                                resultHandler.handle(Future.failedFuture(result.cause()));
                                                connection.close();
                                            }
                                        });

                            } else {
                                resultHandler.handle(Future.failedFuture(result.cause()));
                                connection.close();
                            }
                        });
            } else {
                resultHandler.handle(Future.failedFuture(con.cause()));
            }
        });
    }
}
