package api.controller;

import java.util.ArrayList;

import api.dao.IDAO;
import api.dao.InvoiceItemDAO;
import api.data.InvoiceItem;
import api.util.BaseController;

public class InvoiceItemController extends BaseController<InvoiceItem> {
    private final InvoiceItemDAO invoiceItemDAO = new InvoiceItemDAO();

    @Override
    protected IDAO<InvoiceItem> getDAO() {
        return invoiceItemDAO;
    }

    public ArrayList<InvoiceItem> getByInvoiceId(int idInvoice){
        return invoiceItemDAO.getByInvoiceId(idInvoice);
    }
}
