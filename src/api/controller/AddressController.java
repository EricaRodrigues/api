package api.controller;

import api.dao.AddressDAO;
import api.dao.IDAO;
import api.data.Address;
import api.util.BaseController;

public class AddressController extends BaseController<Address> {
    private final AddressDAO addressDAO = new AddressDAO();

    @Override
    protected IDAO<Address> getDAO() {
        return addressDAO;
    }
}
