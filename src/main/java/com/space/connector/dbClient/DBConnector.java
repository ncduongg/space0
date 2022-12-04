package com.space.connector.dbClient;

import java.util.Properties;

import com.space.Config;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class DBConnector {
    private static Properties p = Config.gProperties();
    private static int DB_Port = Integer.parseInt(p.getProperty("DB_port"));
    private static String DB_Host = p.getProperty("DB_Host");
    private static String DB_Database = p.getProperty("DB_Database");
    private static String DB_User = p.getProperty("DB_User");
    private static String DB_Password = p.getProperty("DB_Password");
    private static int DB_Timeout = Integer.parseInt(p.getProperty("DB_port"));
    private static int DB_Poolsize = Integer.parseInt(p.getProperty("DB_Poolsize"));
    private static PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(DB_Port)
            .setHost(DB_Host)
            .setDatabase(DB_Database)
            .setUser(DB_User)
            .setPassword(DB_Password);
    // set Poool Connection
    private static PoolOptions poolOptions = new PoolOptions()
            .setConnectionTimeout(DB_Timeout)
            .setMaxSize(DB_Poolsize);

    public PgPool getPool(Vertx vertx) {
        PgPool pgPool = PgPool.pool(vertx, connectOptions, poolOptions);
        return pgPool;
    }

}
