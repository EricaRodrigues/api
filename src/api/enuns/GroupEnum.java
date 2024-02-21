package api.enuns;

public enum GroupEnum {
    CLIENT("0", "Client"),
    USER("1", "User"),
    ADMIN("2", "Admin");
    
    private final String key;
    private final String value;

    GroupEnum(String key, String value) {
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

    public static GroupEnum fromKey(String key) {
        for (GroupEnum group : GroupEnum.values()) {
            if (group.getKey().equals(key)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + key);
    }

    public static GroupEnum fromValue(String value) {
        for (GroupEnum group : GroupEnum.values()) {
            if (group.getValue().equals(value)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + value);
    }
}
