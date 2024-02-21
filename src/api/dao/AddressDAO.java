package api.dao;

import api.db.DatabaseUtil;
import api.enuns.StatusEnum;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import api.data.Address;

public class AddressDAO implements IDAO<Address> {

    public Address getById(int addressId) {
        String sql = "SELECT * FROM Address WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, addressId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");

                String streetName = resultSet.getString("streetName");
                String streetNumber = resultSet.getString("streetNumber");
                String country = resultSet.getString("country");
                String state = resultSet.getString("state");
                String zipCode = resultSet.getString("zipCode");
                String status = resultSet.getString("status");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                
                return new Address(id, streetName, streetNumber, country, state, zipCode, StatusEnum.fromKey(status), createDate, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Address add(Address entity) throws SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public boolean update(Address entity) throws SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public ArrayList<Address> getBy(String key) {
        throw new UnsupportedOperationException("Unimplemented method 'getBy'");
    }

    @Override
    public ArrayList<Address> getAll() {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
