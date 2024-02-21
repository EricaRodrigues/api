package api.handle;

import api.controller.BrandController;
import api.data.Brand;
import api.util.BaseController;


public class BrandHandler extends BaseHandler<Brand> {
    private final BrandController brandController;

    public BrandHandler(BrandController brandController) {
        super(brandController, Brand.class);
        this.brandController = brandController;
    }

    @Override
    protected BaseController<Brand> getController() {
        return brandController;
    }
}
