package api.data;

import java.sql.Date;
import java.text.SimpleDateFormat;

import api.enuns.StatusEnum;
import api.util.JsonConvertible;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Stock extends Base<Stock> implements JsonConvertible<Stock> {

    private int idStock;
    private Brand brand;
    private String sku;
    private String name;
    private String description;
    private String onHand;
    private double price;
    private StatusEnum status;
    private Date createDate;
    private Date updateDate;
    private ArrayList<Category> colCategory;

    public Stock() {
    }

    public Stock(int idStock, Brand brand, String sku, String name, String description, String onHand, double price,
            StatusEnum status, Date createDate, Date updateDate, ArrayList<Category> colCategory) {
        this.idStock = idStock;
        this.brand = brand;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.onHand = onHand;
        this.price = price;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.colCategory = colCategory;
    }

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOnHand() {
        return onHand;
    }

    public void setOnHand(String onHand) {
        this.onHand = onHand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public ArrayList<Category> getColCategory() {
        return colCategory;
    }

    public void setColCategory(ArrayList<Category> colCategory) {
        this.colCategory = colCategory;
    }

    public JsonObjectBuilder toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObjectBuilder stockBuilder = Json.createObjectBuilder()
                .add("brand", brand.toJson())
                .add("price", price);
        // .add("colCategory", colCategory)

        if (idStock > 0) {
            stockBuilder.add("id", idStock);
        }

        if (!sku.isEmpty()) {
            stockBuilder.add("sku", sku);
        }

        if (!name.isEmpty()) {
            stockBuilder.add("name", name);
        }

        if (!description.isEmpty()) {
            stockBuilder.add("description", description);
        }

        if (!onHand.isEmpty()) {
            stockBuilder.add("onHand", onHand);
        }

        if (status != null) {
            stockBuilder.add("status", status.getValue());
        }

        if (createDate != null) {
            stockBuilder.add("createDate", dateFormat.format(createDate));
        }

        if (updateDate != null) {
            stockBuilder.add("updateDate", dateFormat.format(updateDate));
        }

        if (colCategory != null && !colCategory.isEmpty()) {
            JsonArrayBuilder categoryArrayBuilder = Json.createArrayBuilder();
            for (Category category : colCategory) {
                categoryArrayBuilder.add(category.toJson());
            }
            stockBuilder.add("colCategory", categoryArrayBuilder);
        }

        return stockBuilder;
    }

    public Stock toObject(JsonObject json) {
        Stock stock = new Stock();

        if (json.containsKey("id")) {
            stock.setIdStock(json.getInt("id"));
        }

        if (json.containsKey("sku")) {
            stock.setSku(json.getString("sku"));
        }

        if (json.containsKey("name")) {
            stock.setName(json.getString("name"));
        }

        if (json.containsKey("description")) {
            stock.setDescription(json.getString("description"));
        }

        if (json.containsKey("onHand")) {
            stock.setOnHand(json.getString("onHand"));
        }

        if (json.containsKey("price")) {
            JsonValue priceValue = json.get("price");

            if (priceValue.getValueType() == JsonValue.ValueType.NUMBER) {
                stock.setPrice(json.getJsonNumber("price").doubleValue());
            }
        }

        if (json.containsKey("status")) {
            StatusEnum status;
            try {
                status = StatusEnum.fromValue(json.getString("status"));
            } catch (Exception e) {
                status = StatusEnum.ACTIVE;
            }

            stock.setStatus(status);
        }

        if (json.containsKey("brand")) {
            JsonObject brandJson = json.getJsonObject("brand");
            stock.setBrand(new Brand().toObject(brandJson));
        }

        if (json.containsKey("colCategory")) {
            JsonArray categoryArray = json.getJsonArray("colCategory");
            ArrayList<Category> categories = new ArrayList<>();
            for (JsonValue categoryValue : categoryArray) {
                JsonObject categoryJson = categoryValue.asJsonObject();
                categories.add(new Category().toObject(categoryJson));
            }
            stock.setColCategory(categories);
        }

        return stock;
    }
}
