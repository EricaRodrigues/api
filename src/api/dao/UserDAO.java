package api.dao;

import api.db.DatabaseUtil;
import api.enuns.GroupEnum;
import api.enuns.StatusEnum;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import api.data.User;

public class UserDAO implements IDAO<User>{
       
    @Override
    public User add(User user) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String sql = "INSERT INTO User (`firstName`, `lastName`, `email`, `password`, `group`, `createDate`, `updateDate`, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); 
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());               
                statement.setString(5, user.getGroup().getValue());
                statement.setDate(6, dataAtual);
                statement.setDate(7, dataAtual);
                statement.setString(8, user.getStatus().getKey());
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    // Recupere o ID gerado
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        user.setIdUser(generatedId);
                        return user;
                    }
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(User user) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String updateQuery = "UPDATE User SET `firstname` = ?, `lastName` = ?, `email` = ?, `password` = ?, `group` = ?, `updateDate` = ?, `status` = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setString(5, user.getGroup().getValue());
                preparedStatement.setDate(6, dataAtual);
                preparedStatement.setString(7, user.getStatus().getKey());
                preparedStatement.setInt(8, user.getIdUser());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getById(int userId) {
        String sql = "SELECT * FROM User WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String group = resultSet.getString("group");
                Date dateCreate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                
                return new User(id, email, firstName, lastName, password, GroupEnum.fromValue(group), StatusEnum.fromKey(status), dateCreate, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<User> getBy(String key) {
        ArrayList<User> userList = new ArrayList<User>();
        String sql = "SELECT * FROM User WHERE email LIKE ? OR id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Check if the key is a number (ID) or a string (email)
            int keyId;
            try {
                keyId = Integer.parseInt(key);
                statement.setString(1, ""); // Leave the first placeholder empty
                statement.setInt(2, keyId);
            } catch (NumberFormatException e) {
                // If the key is not a number, consider it as a email
                statement.setString(1, "%" + key + "%");
                statement.setInt(2, 0); // Leave the second placeholder as zero
            }
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String group = resultSet.getString("group");
                Date dateCreate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                User user = new User(id, email, firstName, lastName, password, GroupEnum.fromValue(group), StatusEnum.fromKey(status), dateCreate, updateDate);

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User getByEmailAndPassword(String pEmail, String pPassword) {
        String sql = "SELECT * FROM User WHERE email = ? and password = ? and `group` = ? LIMIT 1";
        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, pEmail);
            statement.setString(2, pPassword);
            statement.setString(3, GroupEnum.ADMIN.getValue());
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String group = resultSet.getString("group");
                Date dateCreate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                return new User(id, email, firstName, lastName, password, GroupEnum.fromValue(group), StatusEnum.fromKey(status), dateCreate, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<User> getAll() {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String group = resultSet.getString("group");
                Date dateCreate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");

                User user = new User(id, email, firstName, lastName, password, GroupEnum.fromValue(group), StatusEnum.fromKey(status), dateCreate, updateDate);

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean delete(int id) {
        String deleteQuery = "DELETE FROM User WHERE id = ?";
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
