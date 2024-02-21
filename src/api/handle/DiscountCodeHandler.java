package api.handle;

import api.controller.DiscountCodeController;
import api.data.DiscountCode;
import api.util.BaseController;


public class DiscountCodeHandler extends BaseHandler<DiscountCode> {
    private final DiscountCodeController discountCodeController;

    public DiscountCodeHandler(DiscountCodeController discountCodeController) {
        super(discountCodeController, DiscountCode.class);
        this.discountCodeController = discountCodeController;
    }

    @Override
    protected BaseController<DiscountCode> getController() {
        return discountCodeController;
    }
}
