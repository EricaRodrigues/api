package api.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import api.data.Category;
import api.db.DatabaseUtil;
import api.enuns.StatusEnum;

public class CategoryDAO implements IDAO<Category>{
    
        public Category add(Category category) {

        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String sql = "INSERT INTO Category (`name`, `createDate`, `updateDate`, `status`) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); 
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setDate(2, dataAtual);
            statement.setDate(3, dataAtual);
            statement.setString(4, category.getStatus().getKey());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Recupere o ID gerado
                ResultSet generatedKeys = statement.getGeneratedKeys();
                
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    category.setIdCategory(generatedId); 
                    return category;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(Category category) {
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String updateQuery = "UPDATE Category SET `name` = ?, `status` = ?, `updateDate` = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getStatus().getKey());
            statement.setDate(3, dataAtual);
            statement.setInt(4, category.getIdCategory());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Category getById(int categoryId) {
        String sql = "SELECT * FROM Category WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status") ;
                return new Category(id, name, createDate, updateDate, StatusEnum.fromKey(status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Category> getBy(String key) {
        ArrayList<Category> categoryList = new ArrayList<Category>();
        String sql = "SELECT * FROM Category WHERE name LIKE ? OR id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Check if the key is a number (ID) or a string (categoryname)
            int keyId;
            try {
                keyId = Integer.parseInt(key);
                statement.setString(1, ""); // Leave the first placeholder empty
                statement.setInt(2, keyId);
            } catch (NumberFormatException e) {
                // If the key is not a number, consider it as a categoryname
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
                var category = new Category(id, name, createDate, updateDate, StatusEnum.fromKey(status));
                categoryList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public ArrayList<Category> getAll() {
        ArrayList<Category> categoryList = new ArrayList<>();
        String sql = "SELECT * FROM Category";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status") ;
                var category = new Category(id, name, createDate, updateDate, StatusEnum.fromKey(status));
                categoryList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public boolean delete(int id) {
        String deleteQuery = "DELETE FROM Category WHERE id = ?";
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
