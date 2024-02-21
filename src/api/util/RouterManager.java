package api.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RouterManager {
    private Map<String, HttpHandler> routeHandlers = new HashMap<>();

    public void addRoute(String route, HttpHandler handler) {
        routeHandlers.put(route, handler);
    }

    public void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        HttpHandler handler = routeHandlers.get(path);

        if (handler != null) {
            handler.handle(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1); // Not Found
        }
    }
}
