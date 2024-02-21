package api.data;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class Base<T> implements IBase<T> {

    @Override
    public Base<T> setData(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String propertyName = entry.getKey();
            Object value = entry.getValue();

            try {
                Field field = getClass().getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(this, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Lide com exceções, se necessário
                e.printStackTrace();
            }
        }

        return this;
    }
    
}
