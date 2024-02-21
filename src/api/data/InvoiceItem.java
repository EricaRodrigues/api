package api.data;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.util.JsonConvertible;

public class InvoiceItem extends Base<InvoiceItem> implements JsonConvertible<InvoiceItem> {

    private int idInvoiceItem;
    private Invoice invoice;
    private int stockId;
    private String stockSku;
    private String stockName;
    private Double stockPrice;
    private int qty;

    public InvoiceItem(int idInvoiceItem, Invoice invoice, int stockId, String stockSku, String stockName,
            Double stockPrice, int qty) {
        this.idInvoiceItem = idInvoiceItem;
        this.invoice = invoice;
        this.stockId = stockId;
        this.stockSku = stockSku;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.qty = qty;
    }

    public int getIdInvoiceItem() {
        return idInvoiceItem;
    }

    public void setId(int idInvoiceItem) {
        this.idInvoiceItem = idInvoiceItem;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getStockSku() {
        return stockSku;
    }

    public void setStockSku(String stockSku) {
        this.stockSku = stockSku;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public JsonObjectBuilder toJson() {

        JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
                .add("id", idInvoiceItem)
                .add("invoice", invoice.toJson())
                .add("stockId", stockId)
                .add("stockSku", stockSku)
                .add("stockName", stockName)
                .add("stockPrice", stockPrice)
                .add("qty", qty);

        return brandBuilder;
    }

    @Override
    public InvoiceItem toObject(JsonObject json) {
        int idInvoiceItem = json.getInt("idInvoiceItem");
        Invoice invoice = new Invoice().toObject(json.getJsonObject("invoice"));
        int stockId = json.getInt("stockId");
        String stockSku = json.getString("stockSku");
        String stockName = json.getString("stockName");
        double stockPrice = json.getJsonNumber("stockPrice").doubleValue();
        int qty = json.getInt("qty");

        return new InvoiceItem(idInvoiceItem, invoice, stockId, stockSku, stockName, stockPrice, qty);
    }

}
