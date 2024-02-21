package api.handle;

import api.controller.StockController;
import api.data.Stock;
import api.util.BaseController;


public class StockHandler extends BaseHandler<Stock> {
    private final StockController stockController;

    public StockHandler(StockController stockController) {
        super(stockController, Stock.class);
        this.stockController = stockController;
    }

    @Override
    protected BaseController<Stock> getController() {
        return stockController;
    }
}
