package api.controller;

import api.dao.IDAO;
import api.dao.StockDAO;
import api.data.Stock;
import api.util.BaseController;

public class StockController extends BaseController<Stock> {
    private final StockDAO stockDAO = new StockDAO();

    @Override
    protected IDAO<Stock> getDAO() {
        return stockDAO;
    }
}
