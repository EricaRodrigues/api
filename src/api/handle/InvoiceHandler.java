package api.handle;

import api.controller.InvoiceController;
import api.data.Invoice;
import api.util.BaseController;


public class InvoiceHandler extends BaseHandler<Invoice> {
    private final InvoiceController invoiceController;

    public InvoiceHandler(InvoiceController invoiceController) {
        super(invoiceController, Invoice.class);
        this.invoiceController = invoiceController;
    }

    @Override
    protected BaseController<Invoice> getController() {
        return invoiceController;
    }
}
