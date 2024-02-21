package api.data;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.enuns.StatusEnum;
import api.util.JsonConvertible;

public class Category extends Base<Category> implements JsonConvertible<Category> {

    private int idCategory;
    private String name;
    private Date createDate;
    private Date updateDate;
    private StatusEnum status;

    public Category() {
    }

    public Category(int idCategory, String name, Date createDate, Date updateDate, StatusEnum status) {
        this.idCategory = idCategory;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public JsonObjectBuilder toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObjectBuilder brandBuilder = Json.createObjectBuilder();
        if (idCategory > 0) {
            brandBuilder.add("id", idCategory);
        }

        if (!name.isEmpty()) {
            brandBuilder.add("name", name);
        }

        if (status != null) {
            brandBuilder.add("status", status.getValue());
        }

        if (createDate != null) {
            brandBuilder.add("createDate", dateFormat.format(createDate));
        }

        if (updateDate != null) {
            brandBuilder.add("updateDate", dateFormat.format(updateDate));
        }

        return brandBuilder;
    }

    @Override
    public Category toObject(JsonObject json) {
        Category category = new Category();

        if (json.containsKey("id")) {
            category.setIdCategory(json.getInt("id"));
        }

        if (json.containsKey("name")) {
            category.setName(json.getString("name"));
        }

        if (json.containsKey("status")) {
            StatusEnum status;
            try {
                status = StatusEnum.fromValue(json.getString("status"));
            } catch (Exception e) {
                status = StatusEnum.ACTIVE;
            }

            category.setStatus(status);
        }

        if (json.containsKey("createDate")) {
            String createDateStr = json.getString("createDate");
            Date createDate = parseDate(createDateStr);
            category.setCreateDate(createDate);
        }

        if (json.containsKey("updateDate")) {
            String updateDateStr = json.getString("updateDate");
            Date updateDate = parseDate(updateDateStr);
            category.setUpdateDate(updateDate);
        }

        return category;
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Date(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
            return null;
        }
    }

}
