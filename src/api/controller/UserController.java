package api.controller;

import api.dao.IDAO;
import api.dao.UserDAO;
import api.data.User;
import api.util.BaseController;

public class UserController extends BaseController<User> {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected IDAO<User> getDAO() {
        return userDAO;
    }

    public User getByEmailAndPassword(String email, String password) {
        return userDAO.getByEmailAndPassword(email, password);
    }
}
