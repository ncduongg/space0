package com.space.connector.httpClient;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.space.MainVerticle;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class httpClient {
    private static final Logger logger = Logger.getLogger(httpClient.class.getName());

    public static Future<HttpResponse<Buffer>> createWebClientRequest(HttpMethod method, String url,
            Map<String, String> headers, Buffer content) {
        return createWebClientRequest(method, url, 60000, headers, null, content);
    }

    public static Future<HttpResponse<Buffer>> createWebClientRequest(HttpMethod method, String url, long timeout,
            Map<String, String> headers, String contentType,
            Buffer content) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            throw new RuntimeException(e);
        }

        WebClient webClient = MainVerticle.webClient;

        String contentTypeHeader = contentType != null && !contentType.isEmpty() ? contentType
                : APPLICATION_JSON.toString();
        logger.info("uri.getHost(): " + uri.getHost());
        logger.info("url: " + url);
        MultiMap form = MultiMap.caseInsensitiveMultiMap();
        if (headers != null) {
            form.addAll(headers);
        }

        return webClient.requestAbs(method, url).timeout(timeout)
                .putHeader("Host", uri.getHost())
                .putHeader("Content-Type", contentTypeHeader)
                .putHeader("Content-Length", (content != null ? content.length() : 0) + "")
                .putHeaders(form)
                .sendJson(content);
    }

}
