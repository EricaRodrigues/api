package api.data;

import api.enuns.StatusEnum;
import api.util.JsonConvertible;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class DiscountCode extends Base<DiscountCode> implements JsonConvertible<DiscountCode> {
    private int idDiscountCode;
    private String name;
    private Double value;
    private Date createDate;
    private Date updateDate;
    private StatusEnum status;

    public DiscountCode(int idDiscountCode, String name, Double value, Date createDate, Date updateDate,
            StatusEnum status) {
        this.idDiscountCode = idDiscountCode;
        this.name = name;
        this.value = value;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public DiscountCode() {
        //TODO Auto-generated constructor stub
    }

    public int getIdDiscountCode() {
        return idDiscountCode;
    }

    public DiscountCode setIdDiscountCode(int idDiscountCode) {
        this.idDiscountCode = idDiscountCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiscountCode setName(String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public DiscountCode setValue(Double value) {
        this.value = value;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public DiscountCode setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public DiscountCode setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public DiscountCode setStatus(StatusEnum status) {
        this.status = status;
        return this;
    }

    public JsonObjectBuilder toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
                .add("id", idDiscountCode)
                .add("name", name)
                .add("value", value)
                .add("status", status.getValue())
                .add("createDate", dateFormat.format(createDate))
                .add("updateDate", dateFormat.format(updateDate));

        return brandBuilder;
    }

    @Override
    public DiscountCode toObject(JsonObject json) {
        int id = json.getInt("id");
        String name = json.getString("name");
        double value = json.getJsonNumber("value").doubleValue();
        StatusEnum status = StatusEnum.fromValue(json.getString("status"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createDate;
        Date updateDate;

        try {
            createDate = dateFormat.parse(json.getString("createDate"));
            updateDate = dateFormat.parse(json.getString("updateDate"));
        } catch (ParseException e) {
            // Lide com a exceção de parse conforme necessário
            e.printStackTrace();
            return null;
        }

        return new DiscountCode(id, name, value, createDate, updateDate, status);
    }
}
