package api.controller;

import api.dao.ClientDAO;
import api.dao.IDAO;
import api.data.Client;
import api.util.BaseController;

public class ClientController extends BaseController<Client> {
    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    protected IDAO<Client> getDAO() {
        return clientDAO;
    }
}
