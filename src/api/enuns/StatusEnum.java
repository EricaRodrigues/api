package api.enuns;

public enum StatusEnum {
    INACTIVE("0", "Inactive"),
    ACTIVE("1", "Active");
    
    private final String key;
    private final String value;

    StatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toJsonValue() {
        return getValue();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static StatusEnum fromKey(String key) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + key);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + value);
    }
}
