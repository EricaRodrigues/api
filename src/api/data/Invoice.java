package api.data;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import api.enuns.InvoiceStatusEnum;
import api.enuns.PaymentMethodEnum;
import api.enuns.ShippingMethodEnum;
import api.util.JsonConvertible;

public class Invoice extends Base<Invoice> implements JsonConvertible<Invoice> {

    private int idInvoice;
    private Client client;
    private Address shippingAddress;
    private ShippingMethodEnum shippingMethod;
    private DiscountCode discountCode;
    private Double totalPrice;
    private PaymentMethodEnum paymentMethod;
    private InvoiceStatusEnum status;
    private Date createDate;

    public Invoice() {

    }

    public Invoice(int idInvoice, Client client, Address shippingAddress, ShippingMethodEnum shippingMethod,
            DiscountCode discountCode, Double totalPrice, PaymentMethodEnum paymentMethod, InvoiceStatusEnum status,
            Date createDate) {
        this.idInvoice = idInvoice;
        this.client = client;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.discountCode = discountCode;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createDate = createDate;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public InvoiceStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatusEnum status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ShippingMethodEnum getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethodEnum shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public JsonObjectBuilder toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
                .add("id", idInvoice)
                .add("client", client.toJson())
                .add("shippingAddress", shippingAddress.toJson())
                .add("shippingMethod", shippingMethod.getName())
                .add("discountCode", discountCode.toJson())
                .add("totalPrice", totalPrice)
                .add("paymentMethod", paymentMethod.getValue())
                .add("status", status.getValue())
                .add("createDate", dateFormat.format(createDate));

        return brandBuilder;
    }

    @Override
    public Invoice toObject(JsonObject json) {
        int id = json.getInt("id");
        Client client = new Client().toObject(json.getJsonObject("client"));
        Address shippingAddress = new Address().toObject(json.getJsonObject("shippingAddress"));
        String shippingMethodName = json.getString("shippingMethod");
        ShippingMethodEnum shippingMethod = ShippingMethodEnum.fromValue(shippingMethodName); // Você precisa
                                                                                              // implementar este método
                                                                                              // em ShippingMethodEnum
        DiscountCode discountCode = new DiscountCode().toObject(json.getJsonObject("discountCode"));
        Double totalPrice = json.getJsonNumber("totalPrice").doubleValue();
        String paymentMethodName = json.getString("paymentMethod");
        PaymentMethodEnum paymentMethod = PaymentMethodEnum.fromValue(paymentMethodName); // Você precisa implementar
                                                                                          // este método em
                                                                                          // PaymentMethodEnum
        String statusName = json.getString("status");
        InvoiceStatusEnum status = InvoiceStatusEnum.fromValue(statusName); // Você precisa implementar este método em
                                                                            // InvoiceStatusEnum
        String createDateStr = json.getString("createDate");
        Date createDate = parseDate(createDateStr); // Você precisa implementar este método

        return new Invoice(id, client, shippingAddress, shippingMethod, discountCode, totalPrice, paymentMethod, status,
                createDate);
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Date(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
