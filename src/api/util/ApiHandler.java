package api.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class ApiHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Lógica comum para manipulação de requisições
        if (isMethodAllowed(exchange.getRequestMethod())) {
            // Lógica comum para processamento da requisição
            processRequest(exchange);
        } else {
            // Método não permitido
            exchange.sendResponseHeaders(405, -1);
        }
    }

    protected abstract boolean isMethodAllowed(String method);

    protected abstract void processRequest(HttpExchange exchange) throws IOException;

    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
