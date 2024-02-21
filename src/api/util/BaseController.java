package api.util;

import java.util.ArrayList;

import api.dao.IDAO;

public abstract class BaseController<T> implements IBaseController<T> {
    protected abstract IDAO<T> getDAO();

    @Override
    public T add(T entity) throws Exception {
        try {
            return getDAO().add(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean update(T entity) throws Exception {
        try {
            return getDAO().update(entity);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public T getById(int id) throws Exception {
        try {
            return getDAO().getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ArrayList<T> getBy(String key) throws Exception {
        try {
            return getDAO().getBy(key);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ArrayList<T> getAll() throws Exception {
        try {
            return getDAO().getAll();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        try {
            return getDAO().delete(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
