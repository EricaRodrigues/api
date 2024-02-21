package api.controller;

import api.dao.DiscountCodeDAO;
import api.dao.IDAO;
import api.data.DiscountCode;
import api.util.BaseController;

public class DiscountCodeController extends BaseController<DiscountCode> {
    private final DiscountCodeDAO discountCodeDAO = new DiscountCodeDAO();

    @Override
    protected IDAO<DiscountCode> getDAO() {
        return discountCodeDAO;
    }
}
