package api.util;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@FunctionalInterface
public interface IRouteHandler extends HttpHandler {
    void handle(HttpExchange exchange) throws IOException;
}
