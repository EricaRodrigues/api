package api.handle;

import api.controller.AddressController;
import api.data.Address;
import api.util.BaseController;


public class AddressHandler extends BaseHandler<Address> {
    private final AddressController addressController;

    public AddressHandler(AddressController addressController) {
        super(addressController, Address.class);
        this.addressController = addressController;
    }

    @Override
    protected BaseController<Address> getController() {
        return addressController;
    }
}
