package api.controller;

import api.dao.BrandDAO;
import api.dao.IDAO;
import api.data.Brand;
import api.util.BaseController;

public class BrandController extends BaseController<Brand> {
    private final BrandDAO brandDAO = new BrandDAO();

    @Override
    protected IDAO<Brand> getDAO() {
        return brandDAO;
    }
}
