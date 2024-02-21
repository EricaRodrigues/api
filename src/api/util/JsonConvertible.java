package api.util;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public interface JsonConvertible<T> {
    JsonObjectBuilder toJson();
    T toObject(JsonObject json);
}
