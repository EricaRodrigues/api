package api.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import api.data.DiscountCode;
import api.db.DatabaseUtil;
import api.enuns.StatusEnum;

public class DiscountCodeDAO implements IDAO<DiscountCode>{

    public DiscountCode getById(int discountCodeId) {
        String sql = "SELECT * FROM DiscountCode WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, discountCodeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Double value = resultSet.getDouble("value");
                String status = resultSet.getString("status");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());        
                return new DiscountCode(id, name, value, createDate, updateDate, StatusEnum.fromKey(status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DiscountCode add(DiscountCode entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public boolean update(DiscountCode entity) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public ArrayList<DiscountCode> getBy(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBy'");
    }

    @Override
    public ArrayList<DiscountCode> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
