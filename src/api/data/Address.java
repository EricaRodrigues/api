package api.data;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.enuns.StatusEnum;
import api.util.JsonConvertible;

public class Address extends Base<Address> implements JsonConvertible<Address> {
    
    private int idAddress;
    private String streetName;
    private String streetNumber;
    private String country;
    private String state;
    private String zipCode;
    private StatusEnum status;
    private Date createDate;
    private Date updateDate;

    public Address() {
    }

    public Address(int idAddress, String streetName, String streetNumber, String country, String state, String zipCode, StatusEnum status, Date createDate, Date updateDate) {
        this.idAddress = idAddress;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public int getIdAddress() {
        return idAddress;
    }
    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }
    public String getStreetName() {
        return streetName;
    }
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    public String getStreetNumber() {
        return streetNumber;
    }
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public JsonObjectBuilder toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
                .add("id", idAddress)
                .add("streetName", streetName)
                .add("streetNumber", streetNumber)
                .add("country", country)
                .add("state", state)
                .add("zipCode", zipCode)
                .add("status", status.getValue())
                .add("createDate", dateFormat.format(createDate))
                .add("updateDate", dateFormat.format(updateDate));

        return brandBuilder;
    }

    @Override
    public Address toObject(JsonObject json) {
        Address address = new Address();

        if (json.containsKey("id")) {
            address.setIdAddress(json.getInt("id"));
        }

        if (json.containsKey("streetName")) {
            address.setStreetName(json.getString("streetName"));
        }

        if (json.containsKey("streetNumber")) {
            address.setStreetNumber(json.getString("streetNumber"));
        }

        if (json.containsKey("country")) {
            address.setCountry(json.getString("country"));
        }

        if (json.containsKey("state")) {
            address.setState(json.getString("state"));
        }

        if (json.containsKey("zipCode")) {
            address.setZipCode(json.getString("zipCode"));
        }

        if (json.containsKey("status")) {
            StatusEnum status;
            try {
                status = StatusEnum.fromValue(json.getString("status"));
            } catch (Exception e) {
                status = StatusEnum.ACTIVE;
            }

            address.setStatus(status);
        }

        if (json.containsKey("createDate")) {
            try {
                Date createDate = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("createDate")).getTime());
                address.setCreateDate(createDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (json.containsKey("updateDate")) {
            try {
                Date updateDate = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("updateDate")).getTime());
                address.setUpdateDate(updateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return address;
    }

}
