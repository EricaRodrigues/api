package api.controller;

import api.dao.CategoryDAO;
import api.dao.IDAO;
import api.data.Category;
import api.util.BaseController;

public class CategoryController extends BaseController<Category> {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected IDAO<Category> getDAO() {
        return categoryDAO;
    }
}
