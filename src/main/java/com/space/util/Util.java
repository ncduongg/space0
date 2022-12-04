package com.space.util;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.MainVerticle;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;

public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static final DateFormat yyyyMMddTHHmmssZ = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    private static final ZoneId defaultZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    static {
        // rfc1123DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        yyyyMMddTHHmmssZ.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void sendRespone(RoutingContext rc, int status, Buffer body) {
        rc.response().setStatusCode(status).putHeader("Content-Type", "application/json").end(body);
    }

    public static void sendRespone(RoutingContext rc, int status, JsonObject body) {
        rc.response().setStatusCode(
                status)
                .putHeader("content-type", "application/json")
                .end(body.toBuffer());
    }

    public interface ExecBlockingHandler {
        void handle() throws Exception;
    }

    public static void execBlocking(RoutingContext rc, ExecBlockingHandler handler) {
        rc.vertx().executeBlocking(promise -> {
            try {
                handler.handle();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "", e);
                rc.fail(e);
            }
        }, false, ar -> {
            if (ar.failed()) {
                logger.log(Level.SEVERE, "", ar.cause());
                rc.fail(ar.cause());
            }
        });
    }

    public static Date getDate(LocalDate date) {
        return Date.from(date.atStartOfDay(defaultZoneId).toInstant());
    }

    public synchronized static Date iso8601(String s) {
        try {
            if (s != null && !"".equals(s))
                return yyyyMMddTHHmmssZ.parse(s.replaceAll("[-:]", ""));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public synchronized static String iso8601(Date d) {
        if (d == null)
            return null;
        return yyyyMMddTHHmmssZ.format(d);
    }

    public static byte[] parseHexBinary(String s) {
        final int len = s.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0)
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));
            if (h == -1 || l == -1)
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);

            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }

    private static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9')
            return ch - '0';
        if ('A' <= ch && ch <= 'F')
            return ch - 'A' + 10;
        if ('a' <= ch && ch <= 'f')
            return ch - 'a' + 10;
        return -1;
    }

    public static void failureResponse(RoutingContext rc) {
        if (rc == null || rc.response().closed() || rc.response().ended())
            return;
        Throwable e = rc.failure();
        JsonObject error = new JsonObject()
                .put("status", "500")
                .put("response_code", "999")
                .put("message", e.getMessage());

        rc.response().setStatusCode(500).putHeader("Content-Type", "application/json")
                .end(error.toBuffer());
    }
}
