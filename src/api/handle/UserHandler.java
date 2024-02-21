package api.handle;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.controller.UserController;
import api.data.User;
import api.util.BaseController;
import api.util.RouterManager;
import com.sun.net.httpserver.HttpExchange;

public class UserHandler extends BaseHandler<User> {
    private final UserController userController;

    public UserHandler(UserController userController) {
        super(userController, User.class);
        this.userController = userController;
    }

    @Override
    protected BaseController<User> getController() {
        return userController;
    }

    @Override
    public void registerRoutes(RouterManager routerManager) {
        super.registerRoutes(routerManager); // Chama o método da classe base para registrar as rotas padrão

        // Adiciona a rota específica para getByEmailAndPassword
        routerManager.addRoute("/api/" + getEntityName().toLowerCase() + "/getByEmailAndPassword",
                getRouteHandler("getByEmailAndPassword"));
    }

    // Novo método específico para UserHandler
    public void getByEmailAndPassword(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {

            // Exemplo: obtendo parâmetros da requisição
            String query = exchange.getRequestURI().getQuery();
            String email = HttpHandlerUtils.extractKeyFromQuery("email", query);
            String password = HttpHandlerUtils.extractKeyFromQuery("password", query);

            // Exemplo: chamar o método específico do userController
            User user = userController.getByEmailAndPassword(email, password);

            // Se o usuário for encontrado, retornar os dados
            if (user != null) {
                JsonObjectBuilder entityBuilder = convertToJson(user);
                JsonObject jsonObject = entityBuilder.build();

                // Convert the JsonObject to a JSON string
                String response = jsonObject.toString();

                // Set the content type and calculate the correct content length
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                int responseLength = response.getBytes(StandardCharsets.UTF_8).length;

                // Send the response headers with the correct content length
                exchange.sendResponseHeaders(200, responseLength);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                // Usuário não encontrado, retornar 404 Not Found
                exchange.sendResponseHeaders(404, -1);
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }
}
