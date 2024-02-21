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

import api.data.Client;
import api.data.Address;

public class ClientDAO implements IDAO<Client>{

    //Add Client
    public Client add(Client client) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false); // Inicia a transação
    
            // Insert Address
            Address address = client.getBillingAddress();
            address = insertAddress(connection, address);
    
            // Insert User
            client = insertUser(connection, client);

            // Insert Client
            client = insertClient(connection, client);
    
            connection.commit(); // Commit da transação
    
            return client;
    
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback em caso de exceção
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace(); // Lidar com exceção de rollback
                }
            }
            throw e; // Lança a exceção novamente
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restaura o modo de autocommit
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace(); // Lidar com exceção de fechamento de conexão
                }
            }
        }
    }

    private Address insertAddress(Connection connection, Address address) throws SQLException {

        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String addressSql = "INSERT INTO Address (`streetName`, `streetNumber`, `country`, `state`, `zipCode`, `createDate`, `updateDate`, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement addressStatement = connection.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS)) {
            
            addressStatement.setString(1, address.getStreetName());
            addressStatement.setString(2, address.getStreetNumber());
            addressStatement.setString(3, address.getCountry());
            addressStatement.setString(4, address.getState());
            addressStatement.setString(5, address.getZipCode());
            addressStatement.setDate(6, dataAtual);
            addressStatement.setDate(7, dataAtual);
            addressStatement.setString(8, address.getStatus().getKey());

            int rowsInserted = addressStatement.executeUpdate();
    
            if (rowsInserted > 0) {
                ResultSet generatedKeys = addressStatement.getGeneratedKeys();
                if (generatedKeys.next()) {

                    int generatedId = generatedKeys.getInt(1);
                    address.setIdAddress(generatedId);
                    return address;
                }
            }
        }
        throw new SQLException("Falha ao inserir Address");
    }

    private Client insertUser(Connection connection, Client client) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String userSql = "INSERT INTO User (`firstName`, `lastName`, `email`, `password`, `group`, `createDate`, `updateDate`, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement userStatement = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
            userStatement.setString(1, client.getFirstName());
            userStatement.setString(2, client.getLastName());
            userStatement.setString(3, client.getEmail());
            userStatement.setString(4, client.getPassword());               
            userStatement.setString(5, client.getGroup().getValue());
            userStatement.setDate(6, dataAtual);
            userStatement.setDate(7, dataAtual);
            userStatement.setString(8, client.getStatus().getKey());
                int rowsInserted = userStatement.executeUpdate();

                if (rowsInserted > 0) {
                    // Recupere o ID gerado
                    ResultSet generatedKeys = userStatement.getGeneratedKeys();
                    
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        client.setIdUser(generatedId);
                        return client;
                    }
                }
        }
        throw new SQLException("Falha ao inserir User");
    }

    private Client insertClient(Connection connection, Client client) throws SQLException {
        String clientSql = "INSERT INTO Client (`userId`, `billingAddressId`, `phone`) VALUES (?, ?, ?)";
        try (PreparedStatement clientStatement = connection.prepareStatement(clientSql, Statement.RETURN_GENERATED_KEYS)) {
            clientStatement.setInt(1, client.getIdUser());
            clientStatement.setInt(2, client.getBillingAddress().getIdAddress());
            clientStatement.setString(3, client.getPhone());
            int rowsInserted = clientStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Recupere o ID gerado
                ResultSet generatedKeys = clientStatement.getGeneratedKeys();
                
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    client.setIdClient(generatedId);
                    return client;
                }
            }
        }
        throw new SQLException("Falha ao inserir Client");
    }

    //Update Client
    public boolean update(Client client) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false); // Inicia a transação
    
            // Insert Address
            Address address = client.getBillingAddress();
            address = editAddress(connection, address);
    
            // Insert User
            client = editUser(connection, client);

            // Insert Client
            client = editClient(connection, client);
    
            connection.commit(); // Commit da transação
    
            return true;
    
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback em caso de exceção
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace(); // Lidar com exceção de rollback
                }
            }
            throw e; // Lança a exceção novamente
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restaura o modo de autocommit
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace(); // Lidar com exceção de fechamento de conexão
                }
            }
        }    
    }

    private Address editAddress(Connection connection, Address address) throws SQLException {

        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String addressSql = "UPDATE Address SET `streetName` = ?, `streetNumber` = ?, `country` = ?, `state` = ?, `zipCode` = ?, `updateDate` = ?, `status` = ? WHERE id = ?";
        try (PreparedStatement addressStatement = connection.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS)) {
            
            addressStatement.setString(1, address.getStreetName());
            addressStatement.setString(2, address.getStreetNumber());
            addressStatement.setString(3, address.getCountry());
            addressStatement.setString(4, address.getState());
            addressStatement.setString(5, address.getZipCode());
            addressStatement.setDate(6, dataAtual);
            addressStatement.setString(7, address.getStatus().getKey());
            addressStatement.setInt(8, address.getIdAddress());

            int rowsAffected = addressStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                return address;
            }
        }
        throw new SQLException("Falha ao editar Address");
    }

    private Client editUser(Connection connection, Client client) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        String userSql = "UPDATE User SET `firstName` = ?, `lastName` = ?, `email` = ?, `password` = ?, `group` = ?, `updateDate` = ?, `status` = ? WHERE id = ?";
        try (PreparedStatement userStatement = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
            userStatement.setString(1, client.getFirstName());
            userStatement.setString(2, client.getLastName());
            userStatement.setString(3, client.getEmail());
            userStatement.setString(4, client.getPassword());               
            userStatement.setString(5, client.getGroup().getValue());
            userStatement.setDate(6, dataAtual);
            userStatement.setString(7, client.getStatus().getKey());
            userStatement.setInt(8, client.getIdClient());
            int rowsAffected = userStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                return client;
            }
        }
        throw new SQLException("Falha ao editar User");
    }

    private Client editClient(Connection connection, Client client) throws SQLException {
        String clientSql = "UPDATE Client SET `userId` = ?, `billingAddressId` = ?, `phone` = ? WHERE id = ?";
        try (PreparedStatement clientStatement = connection.prepareStatement(clientSql, Statement.RETURN_GENERATED_KEYS)) {
            clientStatement.setInt(1, client.getIdUser());
            clientStatement.setInt(2, client.getBillingAddress().getIdAddress());
            clientStatement.setString(3, client.getPhone());
            clientStatement.setInt(4, client.getIdClient());
            int rowsAffected = clientStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                return client;
            }
        }
        throw new SQLException("Falha ao editar Client");
    }


    // Gets Client

    public Client getById(int clientId) {
        String sql = "select "
                    + "u.id as uId, u.firstName as uFirstName, u.lastName as uLastName, u.email as uEmail, u.password as uPassword, u.group as uGroup, u.createDate as uCreateDate, u.updateDate as uUpdateDate, u.status as uStatus, "
                    + "c.id as cId, c.phone as cPhone, "
                    + "a.id as aId, a.streetName as aStreetName, a.streetNumber as aStreetNumber, a.country as aCountry, a.state as aState, a.zipCode as aZipCode, a.createDate as aCreateDate, a.updateDate as aUpdateDate, a.status as aStatus "
                    + "from Client c "
                    + "inner join User u on u.id = c.userId "
                    + "inner join Address a on a.id = c.billingAddressId "
                    + "WHERE c.id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int aId = resultSet.getInt("aId");
                String aStreetName = resultSet.getString("aStreetName");
                String aStreetNumber = resultSet.getString("aStreetNumber");
                String aCountry = resultSet.getString("aCountry");
                String aState = resultSet.getString("aState");
                String aZipCode = resultSet.getString("aZipCode");
                Date aCreateDate = resultSet.getDate("aCreateDate");
                Date aUpdateDate = resultSet.getDate("aUpdateDate");
                String aStatus = resultSet.getString("aStatus");

                Address address = new Address(aId, aStreetName, aStreetNumber, aCountry, aState, aZipCode,  StatusEnum.fromKey(aStatus), aCreateDate, aUpdateDate);    

                int uId = resultSet.getInt("uId");
                String uFirstName = resultSet.getString("uFirstname");
                String uLastName = resultSet.getString("uLastname");
                String uEmail = resultSet.getString("uEmail");
                String uPassword = resultSet.getString("uPassword");
                String uGroup = resultSet.getString("uGroup");
                Date uDateCreate = resultSet.getDate("uCreateDate");
                Date uUpdateDate = resultSet.getDate("uUpdateDate");
                String uStatus = resultSet.getString("uStatus");

                int cId = resultSet.getInt("cId");
                String cPhone = resultSet.getString("cPhone");

                return new Client(uId, uEmail, uFirstName, uLastName,uPassword, GroupEnum.fromValue(uGroup), StatusEnum.fromKey(uStatus), uDateCreate, uUpdateDate, cId, address, cPhone);
            }
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Client> getBy(String key) {
        ArrayList<Client> clientList = new ArrayList<Client>();


        String sql = "select "
        + "u.id as uId, u.firstName as uFirstName, u.lastName as uLastName, u.email as uEmail, u.password as uPassword, u.group as uGroup, u.createDate as uCreateDate, u.updateDate as uUpdateDate, u.status as uStatus, "
        + "c.id as cId, c.phone as cPhone, "
        + "a.id as aId, a.streetName as aStreetName, a.streetNumber as aStreetNumber, a.country as aCountry, a.state as aState, a.zipCode as aZipCode, a.createDate as aCreateDate, a.updateDate as aUpdateDate, a.status as aStatus "
        + "from Client c "
        + "inner join User u on u.id = c.userId "
        + "inner join Address a on a.id = c.billingAddressId "
        + "WHERE u.Email LIKE ? OR c.id = ?";
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

                int aId = resultSet.getInt("aId");
                String aStreetName = resultSet.getString("aStreetName");
                String aStreetNumber = resultSet.getString("aStreetNumber");
                String aCountry = resultSet.getString("aCountry");
                String aState = resultSet.getString("aState");
                String aZipCode = resultSet.getString("aZipCode");
                Date aCreateDate = resultSet.getDate("aCreateDate");
                Date aUpdateDate = resultSet.getDate("aUpdateDate");
                String aStatus = resultSet.getString("aStatus");

                Address address = new Address(aId, aStreetName, aStreetNumber, aCountry, aState, aZipCode,  StatusEnum.fromKey(aStatus), aCreateDate, aUpdateDate);    

                int uId = resultSet.getInt("uId");
                String uFirstName = resultSet.getString("uFirstname");
                String uLastName = resultSet.getString("uLastname");
                String uEmail = resultSet.getString("uEmail");
                String uPassword = resultSet.getString("uPassword");
                String uGroup = resultSet.getString("uGroup");
                Date uDateCreate = resultSet.getDate("uCreateDate");
                Date uUpdateDate = resultSet.getDate("uUpdateDate");
                String uStatus = resultSet.getString("uStatus");
                
                int cId = resultSet.getInt("cId");
                String cPhone = resultSet.getString("cPhone");

                Client client = new Client(uId, uEmail, uFirstName, uLastName, uPassword, GroupEnum.fromValue(uGroup), StatusEnum.fromKey(uStatus), uDateCreate, uUpdateDate, cId, address, cPhone);
                clientList.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientList;
    }

    public ArrayList<Client> getAll() {
        ArrayList<Client> clientList = new ArrayList<>();
        String sql = "select "
                    + "u.id as uId, u.firstName as uFirstName, u.lastName as uLastName, u.email as uEmail, u.password as uPassword, u.group as uGroup, u.createDate as uCreateDate, u.updateDate as uUpdateDate, u.status as uStatus, "
                    + "c.id as cId, c.phone as cPhone, "
                    + "a.id as aId, a.streetName as aStreetName, a.streetNumber as aStreetNumber, a.country as aCountry, a.state as aState, a.zipCode as aZipCode, a.createDate as aCreateDate, a.updateDate as aUpdateDate, a.status as aStatus "
                    + "from Client c "
                    + "inner join User u on u.id = c.userId "
                    + "inner join Address a on a.id = c.billingAddressId ";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int aId = resultSet.getInt("aId");
                String aStreetName = resultSet.getString("aStreetName");
                String aStreetNumber = resultSet.getString("aStreetNumber");
                String aCountry = resultSet.getString("aCountry");
                String aState = resultSet.getString("aState");
                String aZipCode = resultSet.getString("aZipCode");
                Date aCreateDate = resultSet.getDate("aCreateDate");
                Date aUpdateDate = resultSet.getDate("aUpdateDate");
                String aStatus = resultSet.getString("aStatus");

                Address address = new Address(aId, aStreetName, aStreetNumber, aCountry, aState, aZipCode,  StatusEnum.fromKey(aStatus), aCreateDate, aUpdateDate);    


                int uId = resultSet.getInt("uId");
                String uFirstName = resultSet.getString("uFirstname");
                String uLastName = resultSet.getString("uLastname");
                String uEmail = resultSet.getString("uEmail");
                String uPassword = resultSet.getString("uPassword");
                String uGroup = resultSet.getString("uGroup");
                Date uDateCreate = resultSet.getDate("uCreateDate");
                Date uUpdateDate = resultSet.getDate("uUpdateDate");
                String uStatus = resultSet.getString("uStatus");
                
                int cId = resultSet.getInt("cId");
                String cPhone = resultSet.getString("cPhone");

                Client client = new Client(uId, uEmail, uFirstName, uLastName, uPassword, GroupEnum.fromValue(uGroup), StatusEnum.fromKey(uStatus), uDateCreate, uUpdateDate, cId, address, cPhone);
                clientList.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientList;
    }

    public boolean delete(int id) {
        String deleteQuery = "DELETE FROM Client WHERE id = ?";
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
