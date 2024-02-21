package api.data;

import java.util.Map;

public interface IBase<T> {
    Base<T> setData(Map<String, Object> data);
}
