package api.enuns;

public enum InvoiceStatusEnum {
    PENDING_PAYMENT("1", "Pending Payment"),
    PROCESSING("2", "Processing"),
    SHIPPED("3", "Shipped"),
    DELIVERED("4", "Delivered");

    private final String key;
    private final String value;

    InvoiceStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static InvoiceStatusEnum fromKey(String key) {
        for (InvoiceStatusEnum invoiceStatus : InvoiceStatusEnum.values()) {
            if (invoiceStatus.getKey().equals(key)) {
                return invoiceStatus;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + key);
    }

    public static InvoiceStatusEnum fromValue(String value) {
        for (InvoiceStatusEnum invoiceStatus : InvoiceStatusEnum.values()) {
            if (invoiceStatus.getValue().equals(value)) {
                return invoiceStatus;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + value);
    }
}
