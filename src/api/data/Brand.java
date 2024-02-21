package api.data;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.enuns.StatusEnum;
import api.util.JsonConvertible;

public class Brand extends Base<Brand> implements JsonConvertible<Brand> {
    private int idBrand;
    private String name;
    private Date createDate;
    private Date updateDate;
    private StatusEnum status;

    public Brand() {
    }

    public Brand(int idBrand, String name, Date createDate, Date updateDate, StatusEnum status) {
        this.idBrand = idBrand;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public void setIdBrand(int idBrand) {
        this.idBrand = idBrand;
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

        if (idBrand > 0) {
            brandBuilder.add("id", idBrand);
        }

        if (!name.isEmpty()) {
            brandBuilder.add("name", name);
        }

        if (status != null) {
            brandBuilder.add("status", name);
        }

        if (createDate != null) {
            brandBuilder.add("createDate", dateFormat.format(createDate));
        }

        if (updateDate != null) {
            brandBuilder.add("updateDate", dateFormat.format(updateDate));
        }

        return brandBuilder;
    }

    public Brand toObject(JsonObject json) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Brand brand = new Brand();

        if (json.containsKey("id")) {
            brand.setIdBrand(json.getInt("id"));
        }

        if (json.containsKey("name")) {
            brand.setName(json.getString("name"));
        }

        StatusEnum status = StatusEnum.ACTIVE;
        if (json.containsKey("status")) {
           
            try {
                status = StatusEnum.fromValue(json.getString("status"));
            } catch (Exception e) {
                status = StatusEnum.ACTIVE;
            }
        }
        brand.setStatus(status);

        if (json.containsKey("createDate")) {
            try {
                Date createDate = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("createDate")).getTime());
                brand.setCreateDate(createDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (json.containsKey("updateDate")) {
            try {
                Date updateDate = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("updateDate")).getTime());
                brand.setUpdateDate(updateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return brand;
    }

}
