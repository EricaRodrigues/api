package api.util;

import java.util.ArrayList;

public interface IBaseController<T> {
    T add(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    T getById(int id) throws Exception;
    ArrayList<T> getBy(String key) throws Exception;
    ArrayList<T> getAll() throws Exception;
    boolean delete(int id) throws Exception;
}
