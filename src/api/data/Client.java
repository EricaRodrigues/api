package api.data;

import java.sql.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.enuns.GroupEnum;
import api.enuns.StatusEnum;

public class Client extends User {

    private int idClient;
    private Address billingAddress;
    private String phone;

    public Client(int idUser, String userName, String firstName, String lastName, String password, GroupEnum group,
            StatusEnum status, Date createDate, Date updateDate, int idClient, Address billingAddress,
            String phone) {
        super(idUser, userName, firstName, lastName, password, group, status, createDate, updateDate);
        this.idClient = idClient;
        this.billingAddress = billingAddress;
        this.phone = phone;
    }

    public Client() {
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public JsonObjectBuilder toJson() {

        JsonObjectBuilder clientBuilder = Json.createObjectBuilder()
                .add("id", idClient)
                .add("billingAddress", billingAddress.toJson())
                .add("phone", phone);

        // Chame o método toJson da classe base (User) para incluir os campos herdados
        JsonObjectBuilder userBuilder = super.toJson();

        // Adicione todos os campos do userBuilder ao clientBuilder
        userBuilder.build().forEach(clientBuilder::add);
        return clientBuilder;
    }

    public Client toObject(JsonObject json) {
        Client client = new Client();

        if (json.containsKey("id")) {
            client.setIdClient(json.getInt("id"));
        }

        if (json.containsKey("idClient")) {
            client.setIdClient(json.getInt("idClient"));
        }

        if (json.containsKey("email")) {
            client.setEmail(json.getString("email"));
        }

        if (json.containsKey("idUser")) {
            client.setIdUser(json.getInt("idUser"));
        }

        if (json.containsKey("firstName")) {
            client.setFirstName(json.getString("firstName"));
        }

        if (json.containsKey("lastName")) {
            client.setLastName(json.getString("lastName"));
        }

        if (json.containsKey("password")) {
            client.setPassword(json.getString("password"));
        }

        if (json.containsKey("group")) {
            GroupEnum group;
            try {
                group = GroupEnum.fromValue(json.getString("group"));
            } catch (Exception e) {
                group = GroupEnum.CLIENT;
            }

            client.setGroup(group);
        }

        if (json.containsKey("status")) {
            StatusEnum status;
            try {
                status = StatusEnum.fromValue(json.getString("status"));
            } catch (Exception e) {
                status = StatusEnum.ACTIVE;
            }

            client.setStatus(status);
        }

        if (json.containsKey("billingAddress")) {
            JsonObject billingAddressJson = json.getJsonObject("billingAddress");
            client.setBillingAddress(new Address().toObject(billingAddressJson));
        }

        if (json.containsKey("phone")) {
            client.setPhone(json.getString("phone"));
        }

        return client;
    }
}
