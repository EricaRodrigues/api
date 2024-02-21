package api.handle;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class HttpHandlerUtils {

    
    public static void handleRequest(HttpExchange exchange, Object controller, String methodName) throws IOException {
        try {
            Method method = controller.getClass().getMethod(methodName, HttpExchange.class);
            method.invoke(controller, exchange);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal Server Error");
        }
    }

    public static Map<String, Object> parseFormData(String formData) {
        Map<String, Object> data = new HashMap<>();

        // Remova caracteres especiais do JSON
        formData = formData.replaceAll("[{}\"]", "");

        // Divida a string em pares chave-valor
        String[] pairs = formData.split(",");
    
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                data.put(key, value);
            }
        }
    
        return data;
    }

    public static void sendJsonResponse(HttpExchange exchange, String jsonResponse) throws IOException {
        // Configurar cabeçalhos de resposta
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.length());

        // Enviar a resposta ao cliente
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        // Configurar cabeçalhos de resposta
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, errorMessage.length());

        // Enviar a resposta ao cliente
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void sendBooleanResponse(HttpExchange exchange, boolean value) throws IOException {
        // Configurar cabeçalhos de resposta
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        // Enviar a resposta ao cliente
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(Boolean.toString(value).getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String extractKeyFromQuery(String key, String query) {
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (key.equals(keyValue[0])) {
                return keyValue[1];
            }
        }
        return null;
    }
}
