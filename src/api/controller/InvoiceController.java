package api.controller;

import api.dao.IDAO;
import api.dao.InvoiceDAO;
import api.data.Invoice;
import api.util.BaseController;

public class InvoiceController extends BaseController<Invoice> {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    @Override
    protected IDAO<Invoice> getDAO() {
        return invoiceDAO;
    }
}