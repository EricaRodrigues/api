package api.handle;

import com.sun.net.httpserver.HttpExchange;

import api.util.BaseController;
import api.util.IRouteHandler;
import api.util.JsonConvertible;
import api.util.RouterManager;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;

public abstract class BaseHandler<T extends JsonConvertible<T>> {
    private final BaseController<T> controller;
    private final Class<T> entityClass;

    public BaseHandler(BaseController<T> controller, Class<T> entityClass) {
        this.controller = controller;
        this.entityClass = entityClass;
    }

    public void registerRoutes(RouterManager routerManager) {
        List<String> routeNames = Arrays.asList("getAll", "getById", "getByKey", "add", "update", "delete");

        for (String routeName : routeNames) {
            String routePath = "/api/" + getEntityName().toLowerCase() + "/" + routeName;
            routerManager.addRoute(routePath, getRouteHandler(routeName));
        }
    }

    protected abstract BaseController<T> getController();

    protected String getEntityName() {
        // Obtém o tipo genérico da classe BaseHandler
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<?> entityType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

        return entityType.getSimpleName();
    }

    protected IRouteHandler getRouteHandler(String routeName) {
        try {
            Method method = getClass().getMethod(routeName, HttpExchange.class);
            return (exchange) -> {
                try {
                    method.invoke(this, exchange);
                } catch (Exception e) {
                    e.printStackTrace(); // Lide com exceções adequadamente
                    HttpHandlerUtils.sendErrorResponse(exchange, 500, "Internal Server Error");
                }
            };
        } catch (NoSuchMethodException e) {
            e.printStackTrace(); // Lide com exceções adequadamente
            return null;
        }
    }

    public void add(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            handleFormDataRequest(exchange, HttpMethod.POST);
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    public void update(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            handleFormDataRequest(exchange, HttpMethod.PUT);
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    public void getById(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetByIdRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    public void getByKey(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetByKeyRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    public void getAll(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetAllRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    public void delete(HttpExchange exchange) throws IOException {
        if ("DELETE".equals(exchange.getRequestMethod())) {
            handleDeleteRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    private void handleFormDataRequest(HttpExchange exchange, HttpMethod method) throws IOException {

        try {
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");

            if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
                // Ler o corpo da solicitação
                InputStream inputStream = exchange.getRequestBody();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String requestBody = bufferedReader.readLine();

                // Criar uma instância do tipo T usando reflexão
                T entity = createInstanceUsingReflection();

                // Se entity é do tipo JsonConvertible, então chame o método toObject
                if (entity instanceof JsonConvertible) {
                    entity = ((JsonConvertible<T>) entity)
                            .toObject(Json.createReader(new StringReader(requestBody)).readObject());
                }

                // Chamar o método correspondente na BaseController
                switch (method) {
                    case POST:
                        T nEntity = controller.add(entity);

                        if (nEntity != null) {
                            // Criar um JsonObject para representar a entidade
                            JsonObjectBuilder entityBuilder = convertToJson(nEntity);

                            JsonObject jsonObject = entityBuilder.build();

                            // Converter o JsonObject em uma string JSON
                            String response = jsonObject.toString();

                            // Configurar cabeçalhos de resposta
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            int responseLength = response.getBytes(StandardCharsets.UTF_8).length;

                            // Send the response headers with the correct content length
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseLength);

                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes(StandardCharsets.UTF_8));
                            }
                        } else {
                            // Falha
                            exchange.sendResponseHeaders(404, -1); // Not Found
                        }

                        break;
                    case PUT:
                        boolean result;
                        result = controller.update(entity);

                        // Preparar a resposta
                        if (result) {
                            // Sucesso
                            exchange.sendResponseHeaders(200, -1); // No Content
                        } else {
                            // Falha
                            exchange.sendResponseHeaders(404, -1); // Not Found
                        }

                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupported HTTP method");
                }
            } else {
                exchange.sendResponseHeaders(415, -1); // Tipo de mídia não suportado
            }
        } catch (Exception e) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = e.getMessage().getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, responseLength);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
            }
        }

    }

    private void handleGetByIdRequest(HttpExchange exchange) throws IOException {
        try {
            // Extrair o parâmetro da requisição
            String query = exchange.getRequestURI().getQuery();
            int entityId = Integer.parseInt(HttpHandlerUtils.extractKeyFromQuery("id", query));

            T entity = controller.getById(entityId);

            // Verificar se a entidade foi encontrada
            if (entity != null) {
                // Criar um JsonObject para representar a entidade
                JsonObjectBuilder entityBuilder = convertToJson(entity);

                JsonObject jsonObject = entityBuilder.build();

                // Converter o JsonObject em uma string JSON
                String response = jsonObject.toString();

                // Configurar cabeçalhos de resposta
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                int responseLength = response.getBytes(StandardCharsets.UTF_8).length;

                // Send the response headers with the correct content length
                exchange.sendResponseHeaders(200, responseLength);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                // Entidade não encontrada, retornar 404 Not Found
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = e.getMessage().getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, responseLength);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void handleGetByKeyRequest(HttpExchange exchange) throws IOException {
        try {
            // Extrair o parâmetro da requisição
            String query = exchange.getRequestURI().getQuery();
            String key = HttpHandlerUtils.extractKeyFromQuery("key", query);

            ArrayList<T> entities = controller.getBy(key);

            // Converter a lista de entidades para uma representação JSON
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (T entity : entities) {
                JsonObjectBuilder entityBuilder = convertToJson(entity);
                arrayBuilder.add(entityBuilder);
            }

            JsonArray jsonArray = arrayBuilder.build();
            String response = jsonArray.toString();

            // Configurar cabeçalhos de resposta
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = response.getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(200, responseLength);

            // Enviar a resposta ao cliente
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = e.getMessage().getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, responseLength);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void handleGetAllRequest(HttpExchange exchange) throws IOException {
        try {
            ArrayList<T> entities = controller.getAll();

            // Converter a lista de entidades para uma representação JSON
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (T entity : entities) {
                JsonObjectBuilder entityBuilder = convertToJson(entity);
                arrayBuilder.add(entityBuilder);
            }

            JsonArray jsonArray = arrayBuilder.build();
            String response = jsonArray.toString();

            // Configurar cabeçalhos de resposta
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = response.getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(200, responseLength);

            // Enviar a resposta ao cliente
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = e.getMessage().getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, responseLength);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        try {
            // Extrair o parâmetro da requisição
            String query = exchange.getRequestURI().getQuery();
            int entityId = Integer.parseInt(HttpHandlerUtils.extractKeyFromQuery("id", query));

            boolean result;
            result = controller.delete(entityId);

            // Preparar a resposta
            if (result) {
                // Sucesso
                exchange.sendResponseHeaders(204, -1); // No Content
            } else {
                // Falha
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        } catch (Exception e) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int responseLength = e.getMessage().getBytes(StandardCharsets.UTF_8).length;

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, responseLength);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private T createInstance(Map<String, Object> data) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String propertyName = entry.getKey();
                Object value = entry.getValue();

                try {
                    Field field = entityClass.getDeclaredField(propertyName);
                    field.setAccessible(true);
                    field.set(entity, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Lide com exceções, se necessário
                    e.printStackTrace();
                }
            }

            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating instance of entity.");
        }
    }

    protected JsonObjectBuilder convertToJson(T entity) {
        return entity.toJson();
    }

    protected JsonArrayBuilder convertListToJson(List<? extends JsonConvertible<?>> entityList) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (JsonConvertible<?> entity : entityList) {
            JsonObjectBuilder entityBuilder = entity.toJson();
            jsonArrayBuilder.add(entityBuilder);
        }

        return jsonArrayBuilder;
    }

    // Enum para representar os métodos HTTP
    private enum HttpMethod {
        POST, PUT
    }

    private T createInstanceUsingReflection() {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating instance of entity using reflection.");
        }
    }
}
