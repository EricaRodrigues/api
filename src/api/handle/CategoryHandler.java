package api.handle;

import api.controller.CategoryController;
import api.data.Category;
import api.util.BaseController;


public class CategoryHandler extends BaseHandler<Category> {
    private final CategoryController categoryController;

    public CategoryHandler(CategoryController categoryController) {
        super(categoryController, Category.class);
        this.categoryController = categoryController;
    }

    @Override
    protected BaseController<Category> getController() {
        return categoryController;
    }
}
