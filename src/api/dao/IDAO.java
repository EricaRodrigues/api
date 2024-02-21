package api.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDAO<T> {
    T add(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    T getById(int id);
    ArrayList<T> getBy(String key);
    ArrayList<T> getAll();
    boolean delete(int id) throws SQLException;
}
