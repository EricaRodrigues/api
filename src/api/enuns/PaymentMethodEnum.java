package api.enuns;

public enum PaymentMethodEnum {
    PAYPAL("1", "PayPal");
    
    private final String key;
    private final String value;

    PaymentMethodEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMethodEnum fromKey(String key) {
        for (PaymentMethodEnum payment : PaymentMethodEnum.values()) {
            if (payment.getKey().equals(key)) {
                return payment;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + key);
    }

    public static PaymentMethodEnum fromValue(String value) {
        for (PaymentMethodEnum payment : PaymentMethodEnum.values()) {
            if (payment.getValue().equals(value)) {
                return payment;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + value);
    }
}
