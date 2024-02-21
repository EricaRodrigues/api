package api.enuns;

public enum ShippingMethodEnum {
    AUSTRALIA_POST("1", "Australia Post Delivery - eParcel (10-15 days)", "20"),
    EXPRESS_POST("2", "Express Post - Australia Post (4-9 days)", "30");

    private final String key;
    private final String name;
    private final String value;

    private ShippingMethodEnum(String key, String name, String value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static ShippingMethodEnum fromKey(String key) {
        for (ShippingMethodEnum value : ShippingMethodEnum.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + key);
    }

    public static ShippingMethodEnum fromValue(String value) {
        for (ShippingMethodEnum status : ShippingMethodEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + value);
    }
}
