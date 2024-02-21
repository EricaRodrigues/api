package api.dao;

import api.db.DatabaseUtil;
import api.enuns.InvoiceStatusEnum;
import api.enuns.PaymentMethodEnum;
import api.enuns.ShippingMethodEnum;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import api.data.Invoice;
import api.data.Client;
import api.data.DiscountCode;
import api.data.Address;

public class InvoiceDAO implements IDAO<Invoice> {
    
    public ArrayList<Invoice> getAll() {
        ArrayList<Invoice> invoiceList = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int clientId = resultSet.getInt("clientId");
                Client client = new ClientDAO().getById(clientId);
                int discountCodeId = resultSet.getInt("discountCodeId");
                DiscountCode discountCode = new DiscountCodeDAO().getById(discountCodeId);
                int shippingAddressId = resultSet.getInt("shippingAddressId");
                Address shippingAddress = new AddressDAO().getById(shippingAddressId);
                String shippingMethod = resultSet.getString("shippingMethod");
                Double totalPrice = resultSet.getDouble("totalPrice");
                String paymentMethod = resultSet.getString("paymentMethod");
                String status = resultSet.getString("status");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());

                var invoice = new Invoice(id, client, shippingAddress, ShippingMethodEnum.fromKey(shippingMethod), discountCode, totalPrice, PaymentMethodEnum.fromKey(paymentMethod), InvoiceStatusEnum.fromKey(status), createDate);
                invoiceList.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceList;
    }


    public Invoice getById(int idInvoice) {
        String sql = "SELECT * FROM Invoice WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInvoice);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int clientId = resultSet.getInt("clientId");
                Client client = new ClientDAO().getById(clientId);
                int discountCodeId = resultSet.getInt("discountCodeId");
                DiscountCode discountCode = new DiscountCodeDAO().getById(discountCodeId);
                int shippingAddressId = resultSet.getInt("shippingAddressId");
                Address shippingAddress = new AddressDAO().getById(shippingAddressId);
                String shippingMethod = resultSet.getString("shippingMethod");
                Double totalPrice = resultSet.getDouble("totalPrice");
                String paymentMethod = resultSet.getString("paymentMethod");
                String status = resultSet.getString("status");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                
                return new Invoice(id, client, shippingAddress, ShippingMethodEnum.fromKey(shippingMethod), discountCode, totalPrice, PaymentMethodEnum.fromKey(paymentMethod), InvoiceStatusEnum.fromKey(status), createDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Invoice> getBy(String key) {
        ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
        String sql = "SELECT * FROM Invoice i "
                    + "inner join Client c on c.id = i.clientId "
                    + "inner join User u on u.id = c.userId "
                    + "where u.email LIKE ? OR u.firstName LIKE ? OR u.lastName LIKE ? OR i.id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Check if the key is a number (ID) or a string (stockname)
            int keyId;
            try {
                keyId = Integer.parseInt(key);
                statement.setString(1, ""); 
                statement.setString(2, "");
                statement.setString(3, "");
                statement.setInt(4, keyId);
            } catch (NumberFormatException e) {
                statement.setString(1, "%" + key + "%");
                statement.setString(2, "%" + key + "%");
                statement.setString(3, "%" + key + "%");
                statement.setInt(4, 0); 
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int clientId = resultSet.getInt("clientId");
                Client client = new ClientDAO().getById(clientId);
                int discountCodeId = resultSet.getInt("discountCodeId");
                DiscountCode discountCode = new DiscountCodeDAO().getById(discountCodeId);
                int shippingAddressId = resultSet.getInt("shippingAddressId");
                Address shippingAddress = new AddressDAO().getById(shippingAddressId);
                String shippingMethod = resultSet.getString("shippingMethod");
                Double totalPrice = resultSet.getDouble("totalPrice");
                String paymentMethod = resultSet.getString("paymentMethod");
                String status = resultSet.getString("status");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                
                var invoice = new Invoice(id, client, shippingAddress, ShippingMethodEnum.fromKey(shippingMethod), discountCode, totalPrice, PaymentMethodEnum.fromKey(paymentMethod), InvoiceStatusEnum.fromKey(status), createDate);
                invoiceList.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceList;
    }


    @Override
    public Invoice add(Invoice entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }


    @Override
    public boolean update(Invoice entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }


    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
