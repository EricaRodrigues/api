package api.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import api.data.Brand;
import api.db.DatabaseUtil;
import api.enuns.StatusEnum;

public class BrandDAO implements IDAO<Brand>{
    
    public Brand add(Brand brand) {

        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        brand.setCreateDate(dataAtual);
        brand.setUpdateDate(dataAtual);
        brand.setStatus(StatusEnum.ACTIVE);
        
        String sql = "INSERT INTO Brand (`name`, `createDate`, `updateDate`, `status`) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); 
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, brand.getName());
            statement.setDate(2, brand.getCreateDate());
            statement.setDate(3, brand.getUpdateDate());
            statement.setString(4, brand.getStatus().getKey());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Recupere o ID gerado
                ResultSet generatedKeys = statement.getGeneratedKeys();
                
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    brand.setIdBrand(generatedId); 
                    return brand;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(Brand brand) {
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        brand.setUpdateDate(dataAtual);

        String updateQuery = "UPDATE Brand SET `name` = ?, `status` = ?, `updateDate` = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, brand.getName());
            statement.setString(2, brand.getStatus().getKey());
            statement.setDate(3, brand.getUpdateDate());
            statement.setInt(4, brand.getIdBrand());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Brand getById(int brandId) {
        String sql = "SELECT * FROM Brand WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, brandId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status") ;
                return new Brand(id, name, createDate, updateDate, StatusEnum.fromKey(status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Brand> getBy(String key) {
        ArrayList<Brand> brandList = new ArrayList<Brand>();
        String sql = "SELECT * FROM Brand WHERE name LIKE ? OR id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Check if the key is a number (ID) or a string (brandname)
            int keyId;
            try {
                keyId = Integer.parseInt(key);
                statement.setString(1, ""); // Leave the first placeholder empty
                statement.setInt(2, keyId);
            } catch (NumberFormatException e) {
                // If the key is not a number, consider it as a brandname
                statement.setString(1, "%" + key + "%");
                statement.setInt(2, 0); // Leave the second placeholder as zero
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                var brand = new Brand(id, name, createDate, updateDate, StatusEnum.fromKey(status));
                brandList.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brandList;
    }

    public ArrayList<Brand> getAll() {
        ArrayList<Brand> brandList = new ArrayList<>();
        String sql = "SELECT * FROM Brand";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status") ;
                var brand = new Brand(id, name, createDate, updateDate, StatusEnum.fromKey(status));
                brandList.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brandList;
    }

    public boolean delete(int id) {
        String deleteQuery = "DELETE FROM Brand WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}