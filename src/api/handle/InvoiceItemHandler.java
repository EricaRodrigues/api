package api.handle;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sun.net.httpserver.HttpExchange;

import api.controller.InvoiceItemController;
import api.data.InvoiceItem;
import api.data.User;
import api.util.BaseController;
import api.util.RouterManager;


public class InvoiceItemHandler extends BaseHandler<InvoiceItem> {
    private final InvoiceItemController invoiceItemController;

    public InvoiceItemHandler(InvoiceItemController invoiceItemController) {
        super(invoiceItemController, InvoiceItem.class);
        this.invoiceItemController = invoiceItemController;
    }

    @Override
    protected BaseController<InvoiceItem> getController() {
        return invoiceItemController;
    }

    @Override
    public void registerRoutes(RouterManager routerManager) {
        super.registerRoutes(routerManager);

        // Adiciona a rota específica para getByEmailAndPassword
        routerManager.addRoute("/api/" + getEntityName().toLowerCase() + "/getByInvoiceId",
                getRouteHandler("getByInvoiceId"));
    }

    // Novo método específico para UserHandler
    public void getByInvoiceId(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {

            // Exemplo: obtendo parâmetros da requisição
            String query = exchange.getRequestURI().getQuery();
            int invoiceId = Integer.parseInt(HttpHandlerUtils.extractKeyFromQuery("invoiceId", query));

            // Exemplo: chamar o método específico do userController
            ArrayList<InvoiceItem> invoiceItems = invoiceItemController.getByInvoiceId(invoiceId);

            // Se o usuário for encontrado, retornar os dados
            if (invoiceItems != null) {
                JsonArrayBuilder jsonArrayBuilder = convertListToJson(invoiceItems);
                JsonArray jsonArray = jsonArrayBuilder.build();

                // Convert the JsonObject to a JSON string
                String response = jsonArray.toString();

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
