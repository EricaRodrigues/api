package api;

import com.sun.net.httpserver.HttpServer;

import api.controller.AddressController;
import api.controller.BrandController;
import api.controller.CategoryController;
import api.controller.DiscountCodeController;
import api.controller.InvoiceController;
import api.controller.InvoiceItemController;
import api.controller.StockController;
import api.controller.UserController;
import api.handle.AddressHandler;
import api.handle.BrandHandler;
import api.handle.CategoryHandler;
import api.handle.DiscountCodeHandler;
import api.handle.InvoiceHandler;
import api.handle.InvoiceItemHandler;
import api.handle.StockHandler;
import api.handle.UserHandler;
import api.util.RouterManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LPAApiManager {
public static void main(String[] args) {
        int port = 8083;

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            RouterManager routerManager = new RouterManager();
            registerHandlers(routerManager);

            server.createContext("/", exchange -> routerManager.handleRequest(exchange));

            server.setExecutor(null); // default executor
            server.start();

            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente de produção
        }
    }

    private static void registerHandlers(RouterManager routerManager) {
        AddressHandler addressHandler = new AddressHandler(new AddressController());
        BrandHandler brandHandler = new BrandHandler(new BrandController());
        CategoryHandler categoryHandler = new CategoryHandler(new CategoryController());
        // ClientHandler clientHandler = new ClientHandler(new ClientController());
        DiscountCodeHandler discountCodeHandler = new DiscountCodeHandler(new DiscountCodeController());
        InvoiceHandler invoiceHandler = new InvoiceHandler(new InvoiceController());
        InvoiceItemHandler invoiceItemHandler = new InvoiceItemHandler(new InvoiceItemController());
        StockHandler stockHandler = new StockHandler(new StockController());
        UserHandler userHandler = new UserHandler(new UserController());

        addressHandler.registerRoutes(routerManager);
        brandHandler.registerRoutes(routerManager);
        categoryHandler.registerRoutes(routerManager);
        // clientHandler.registerRoutes(routerManager);
        discountCodeHandler.registerRoutes(routerManager);
        invoiceHandler.registerRoutes(routerManager);
        invoiceItemHandler.registerRoutes(routerManager);
        stockHandler.registerRoutes(routerManager);
        userHandler.registerRoutes(routerManager);
    }
}










// package api;

// import com.sun.net.httpserver.HttpServer;

// import api.controller.BrandController;
// import api.data.Brand;

// import com.sun.net.httpserver.HttpHandler;
// import com.sun.net.httpserver.HttpExchange;

// import java.io.IOException;
// import java.io.OutputStream;
// import java.net.InetSocketAddress;
// import java.nio.charset.StandardCharsets;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Map;

// import javax.json.Json;
// import javax.json.JsonArray;
// import javax.json.JsonObject;
// import javax.json.JsonArrayBuilder;
// import javax.json.JsonObjectBuilder;

// public class LPAApiManager {

//     public static void main(String[] args) {
//         int port = 8083;

//         try {
//             HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

//             // Mapeamento de caminhos para manipuladores
//             Map<String, HttpHandler> contextMap = new HashMap<>();
//             contextMap.put("/api/brand/add", new AddBrandHandler());
//             contextMap.put("/api/brand/update", new UpdateBrandHandler());
//             contextMap.put("/api/brand/getById", new GetBrandByIdHandler());
//             contextMap.put("/api/brand/getByName", new GetBrandsByNameHandler());
//             contextMap.put("/api/brand/getAll", new GetAllBrandsHandler());
//             contextMap.put("/api/brand/delete", new DeleteBrandHandler());

//             // Adicionar manipuladores ao servidor
//             for (Map.Entry<String, HttpHandler> entry : contextMap.entrySet()) {
//                 server.createContext(entry.getKey(), entry.getValue());
//             }

//             server.setExecutor(null); // default executor
//             server.start();

//             System.out.println("Server started on port " + port);
//         } catch (IOException e) {
//             e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente de produção
//         }

//     }

//     // Implementação básica de manipuladores para cada endpoint
//     static class AddBrandHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Implementa a lógica para o endpoint /api/brand/add
//             if ("POST".equals(exchange.getRequestMethod())) {
//                 // Extrair o corpo da requisição, analisar o JSON e chamar o método
//                 // correspondente na BrandController
//             } else {
//                 exchange.sendResponseHeaders(405, -1); // Método não permitido
//             }
//         }
//     }

//     static class UpdateBrandHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Implementa a lógica para o endpoint /api/brand/update
//             if ("PUT".equals(exchange.getRequestMethod())) {
//                 // Extrair o corpo da requisição, analisar o JSON e chamar o método
//                 // correspondente na BrandController
//             } else {
//                 exchange.sendResponseHeaders(405, -1); // Método não permitido
//             }
//         }
//     }

//     static class GetBrandByIdHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Implementa a lógica para o endpoint /api/brand/getById
//             if ("GET".equals(exchange.getRequestMethod())) {
//                 // Extrair o parâmetro da requisição
//                 String query = exchange.getRequestURI().getQuery();
//                 int brandId = extractBrandIdFromQuery(query);

//                 // Chamar o método correspondente na BrandController
//                 BrandController brandController = new BrandController();
//                 Brand brand = brandController.getBrandById(brandId);

                

//                 // Verificar se a marca foi encontrada
//                 if (brand != null) {
//                     // Criar um JsonObject para representar a marca
//                     JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
//                             .add("id", brand.getIdBrand())
//                             .add("name", brand.getName());

//                     JsonObject jsonBrand = brandBuilder.build();

//                     // Converter o JsonObject em uma string JSON
//                     String response = jsonBrand.toString();

//                     // Configurar cabeçalhos de resposta
//                     exchange.getResponseHeaders().set("Content-Type", "application/json");
//                     exchange.sendResponseHeaders(200, response.length());

//                     try (OutputStream os = exchange.getResponseBody()) {
//                         os.write(response.getBytes(StandardCharsets.UTF_8));
//                     }
//                 } else {
//                     // Marca não encontrada, retornar 404 Not Found
//                     exchange.sendResponseHeaders(404, -1);
//                 }
//             } else {
//                 // Método não permitido
//                 exchange.sendResponseHeaders(405, -1);
//             }
//         }

//         private int extractBrandIdFromQuery(String query) {
//             String[] params = query.split("&");
//             for (String param : params) {
//                 String[] keyValue = param.split("=");
//                 if ("brandId".equals(keyValue[0])) {
//                     return Integer.parseInt(keyValue[1]);
//                 }
//             }
//             return -1; // Retorna um valor padrão ou lida com o erro de alguma maneira
//         }
//     }

//     static class GetBrandsByNameHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Implementa a lógica para o endpoint /api/brand/getById
//             if ("GET".equals(exchange.getRequestMethod())) {
//                 // Extrair o parâmetro da requisição
//                 String query = exchange.getRequestURI().getQuery();
//                 String brandName = extractBrandNameFromQuery(query);

//                 // Chamar o método correspondente na BrandController
//                 BrandController brandController = new BrandController();
//                 ArrayList<Brand> brands = brandController.getBrandsByName(brandName);

//                 // Converter a lista de marcas para uma representação JSON
//                 JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

//                 for (Brand brand : brands) {
//                     JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
//                             .add("id", brand.getIdBrand())
//                             .add("name", brand.getName());
//                     arrayBuilder.add(brandBuilder);
//                 }

//                 JsonArray jsonArray = arrayBuilder.build();
//                 String response = jsonArray.toString();

//                 // Configurar cabeçalhos de resposta
//                 exchange.getResponseHeaders().set("Content-Type", "application/json");
//                 exchange.sendResponseHeaders(200, response.length());

//                 // Enviar a resposta ao cliente
//                 try (OutputStream os = exchange.getResponseBody()) {
//                     os.write(response.getBytes(StandardCharsets.UTF_8));
//                 }
//             } else {
//                 // Método não permitido
//                 exchange.sendResponseHeaders(405, -1);
//             }
//         }

//         private String extractBrandNameFromQuery(String query) {
//             String[] params = query.split("&");
//             for (String param : params) {
//                 String[] keyValue = param.split("=");
//                 if ("brandName".equals(keyValue[0])) {
//                     return keyValue[1];
//                 }
//             }
//             return null;
//         }
//     }

//     // Implementação básica de manipuladores para cada endpoint
//     static class GetAllBrandsHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Implementa a lógica para o endpoint /api/brand/getAll
//             if ("GET".equals(exchange.getRequestMethod())) {
//                 // Chamar o método correspondente na BrandController
//                 BrandController brandController = new BrandController();
//                 ArrayList<Brand> brands = brandController.getAllBrands();

//                 // Converter a lista de marcas para uma representação JSON
//                 JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

//                 for (Brand brand : brands) {
//                     JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
//                             .add("id", brand.getIdBrand())
//                             .add("name", brand.getName());
//                     arrayBuilder.add(brandBuilder);
//                 }

//                 JsonArray jsonArray = arrayBuilder.build();
//                 String response = jsonArray.toString();

//                 // Configurar cabeçalhos de resposta
//                 exchange.getResponseHeaders().set("Content-Type", "application/json");
//                 exchange.sendResponseHeaders(200, response.length());

//                 // Enviar a resposta ao cliente
//                 try (OutputStream os = exchange.getResponseBody()) {
//                     os.write(response.getBytes(StandardCharsets.UTF_8));
//                 }
//             } else {
//                 // Método não permitido
//                 exchange.sendResponseHeaders(405, -1);
//             }
//         }
//     }

//     static class DeleteBrandHandler implements HttpHandler {
//         @Override
//         public void handle(HttpExchange exchange) throws IOException {
//             // Implementa a lógica para o endpoint /api/brand/delete
//             if ("DELETE".equals(exchange.getRequestMethod())) {
//                 // Extrair o parâmetro da requisição, converter para int e chamar o método
//                 // correspondente na BrandController
//             } else {
//                 exchange.sendResponseHeaders(405, -1); // Método não permitido
//             }
//         }
//     }
// }