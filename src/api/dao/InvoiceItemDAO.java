package api.dao;

import api.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import api.data.InvoiceItem;
import api.data.Invoice;

public class InvoiceItemDAO implements IDAO<InvoiceItem>{

    public ArrayList<InvoiceItem> getAll() {
        ArrayList<InvoiceItem> invoiceItemList = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceItem";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int invoiceId = resultSet.getInt("invoiceId");
                Invoice invoice = new InvoiceDAO().getById(invoiceId);
                int stockId = resultSet.getInt("stockId");
                String stockSku = resultSet.getString("stockSku");
                String stockName = resultSet.getString("stockName");
                Double stockPrice = resultSet.getDouble("stockPrice");
                int qty = resultSet.getInt("qty");
                
                var invoiceItem = new InvoiceItem(id, invoice,stockId, stockSku ,stockName, stockPrice, qty);
                invoiceItemList.add(invoiceItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceItemList;
    }


    public InvoiceItem getById(int idInvoiceItem) {
        String sql = "SELECT * FROM InvoiceItem WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInvoiceItem);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int invoiceId = resultSet.getInt("invoiceId");
                Invoice invoice = new InvoiceDAO().getById(invoiceId);
                int stockId = resultSet.getInt("stockId");
                String stockSku = resultSet.getString("stockSku");
                String stockName = resultSet.getString("stockName");
                Double stockPrice = resultSet.getDouble("stockPrice");
                int qty = resultSet.getInt("qty");
                
                return new InvoiceItem(id, invoice, stockId, stockSku, stockName, stockPrice, qty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<InvoiceItem> getBy(String key) {
        ArrayList<InvoiceItem> invoiceItemList = new ArrayList<InvoiceItem>();
        String sql = "SELECT * FROM InvoiceItem WHERE stockName LIKE ? OR id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Check if the key is a number (ID) or a string (stockname)
            int keyId;
            try {
                keyId = Integer.parseInt(key);
                statement.setString(1, ""); // Leave the first placeholder empty
                statement.setInt(2, keyId);
            } catch (NumberFormatException e) {
                // If the key is not a number, consider it as a stockname
                statement.setString(1, "%" + key + "%");
                statement.setInt(2, 0); // Leave the second placeholder as zero
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int invoiceId = resultSet.getInt("invoiceId");
                Invoice invoice = new InvoiceDAO().getById(invoiceId);
                int stockId = resultSet.getInt("stockId");
                String stockSku = resultSet.getString("stockSku");
                String stockName = resultSet.getString("stockName");
                Double stockPrice = resultSet.getDouble("stockPrice");
                int qty = resultSet.getInt("qty");
                
                var invoiceItem =  new InvoiceItem(id, invoice, stockId, stockSku, stockName, stockPrice, qty);
                invoiceItemList.add(invoiceItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceItemList;
    }



    public ArrayList<InvoiceItem> getByInvoiceId(int idInvoice) {
        ArrayList<InvoiceItem> invoiceItemList = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceItem WHERE invoiceId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInvoice);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int invoiceId = resultSet.getInt("invoiceId");
                Invoice invoice = new InvoiceDAO().getById(invoiceId);
                int stockId = resultSet.getInt("stockId");
                String stockSku = resultSet.getString("stockSku");
                String stockName = resultSet.getString("stockName");
                Double stockPrice = resultSet.getDouble("stockPrice");
                int qty = resultSet.getInt("qty");
                
                var invoiceItem = new InvoiceItem(id, invoice, stockId, stockSku, stockName, stockPrice, qty);
                invoiceItemList.add(invoiceItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceItemList;
    }

    public ArrayList<InvoiceItem> getByInvoiceIdAndKey(int idInvoice, String key) {
        ArrayList<InvoiceItem> invoiceItemList = new ArrayList<InvoiceItem>();
        String sql = "SELECT * FROM InvoiceItem WHERE invoiceId = ? and (stockName LIKE ? OR id = ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Check if the key is a number (ID) or a string (stockname)
            int keyId;
            try {
                keyId = Integer.parseInt(key);
                statement.setInt(1, idInvoice);
                statement.setString(2, "");
                statement.setInt(3, keyId);
            } catch (NumberFormatException e) {
                statement.setInt(1, idInvoice);
                statement.setString(2, "%" + key + "%");
                statement.setInt(3, 0);
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int invoiceId = resultSet.getInt("invoiceId");
                Invoice invoice = new InvoiceDAO().getById(invoiceId);
                int stockId = resultSet.getInt("stockId");
                String stockSku = resultSet.getString("stockSku");
                String stockName = resultSet.getString("stockName");
                Double stockPrice = resultSet.getDouble("stockPrice");
                int qty = resultSet.getInt("qty");
                
                var invoiceItem =  new InvoiceItem(id, invoice, stockId, stockSku, stockName, stockPrice, qty);
                invoiceItemList.add(invoiceItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceItemList;
    }


    @Override
    public InvoiceItem add(InvoiceItem entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }


    @Override
    public boolean update(InvoiceItem entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }


    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}



