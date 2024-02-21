package api.dao;

import api.db.DatabaseUtil;
import api.enuns.StatusEnum;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import api.data.Brand;
import api.data.Stock;
import api.data.Category;

public class StockDAO implements IDAO<Stock> {

    public Stock add(Stock stock) throws SQLException {
        Connection connection = null;
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        stock.setCreateDate(dataAtual);
        stock.setUpdateDate(dataAtual);

        String sql = "INSERT INTO Stock (`brandId`, `sku`, `name`, `description`, `onHand`, `price`, `createDate`, `updateDate`, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false); // Inicia a transação
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, stock.getBrand().getIdBrand());
            statement.setString(2, stock.getSku());
            statement.setString(3, stock.getName());
            statement.setString(4, stock.getDescription());
            statement.setString(5, stock.getOnHand());
            statement.setDouble(6, stock.getPrice());
            statement.setDate(7, stock.getCreateDate());
            statement.setDate(8, stock.getUpdateDate());
            statement.setString(9, stock.getStatus().getKey());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Recupere o ID gerado
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    stock.setIdStock(generatedId); // Defina o ID no objeto Stock

                    // Save the colection StockCategory
                    this.saveStockCategories(connection, stock, stock.getColCategory());

                    connection.commit(); // Commit da transação
                    return stock;
                }
            }

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

        return null;
    }

    private void saveStockCategories(Connection connection, Stock stock, ArrayList<Category> categories)
            throws SQLException {

        if (categories != null && !categories.isEmpty()) {
            // Remove todas as associações de categorias antigas para este estoque
            deleteStockCategories(connection, stock.getIdStock());

            // Adiciona as novas associações de categorias
            for (Category category : categories) {
                addStockCategory(connection, stock, category);
            }
        }
    }

    private void deleteStockCategories(Connection connection, int stockId) throws SQLException {
        String deleteQuery = "DELETE FROM StockCategory WHERE stockId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, stockId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStockCategory(Connection connection, Stock stock, Category category) throws SQLException {
        String insertQuery = "INSERT INTO StockCategory (stockId, categoryId) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setInt(1, stock.getIdStock());
            pstmt.setInt(2, category.getIdCategory());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean update(Stock stock) throws SQLException {
        Connection connection = null;
        Calendar calendar = Calendar.getInstance();
        Date dataAtual = new Date(calendar.getTimeInMillis());

        stock.setUpdateDate(dataAtual);

        String updateQuery = "UPDATE Stock SET `brandId` = ?, `sku` = ?, `name` = ?, `description` = ?, `onHand` = ?, `price` = ?, `updateDate` = ?, `status` = ? WHERE id = ?";
        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false); // Inicia a transação
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, stock.getBrand().getIdBrand());
            statement.setString(2, stock.getSku());
            statement.setString(3, stock.getName());
            statement.setString(4, stock.getDescription());
            statement.setString(5, stock.getOnHand());
            statement.setDouble(6, stock.getPrice());
            statement.setDate(7, stock.getUpdateDate());
            statement.setString(8, stock.getStatus().getKey());
            statement.setInt(9, stock.getIdStock());

            int rowsAffected = statement.executeUpdate();

            // Save the colection StockCategory
            this.saveStockCategories(connection, stock, stock.getColCategory());

            connection.commit(); // Commit da transação
            return rowsAffected > 0;

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

    public Stock getById(int stockId) {
        String sql = "SELECT * FROM Stock WHERE id = ?";
        String stockCategorySql = "SELECT c.* FROM StockCategory sc INNER JOIN Category c ON c.id = sc.categoryId WHERE stockId = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                PreparedStatement stockCategoryStatement = connection.prepareStatement(stockCategorySql)) {
            statement.setInt(1, stockId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int brandId = resultSet.getInt("brandId");
                String sku = resultSet.getString("sku");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String onHand = resultSet.getString("onHand");
                Double price = resultSet.getDouble("price");
                Date createDate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                Brand brand = new BrandDAO().getById(brandId);

                // Obter informações das categorias associadas
                stockCategoryStatement.setInt(1, stockId);
                ResultSet categoryResultSet = stockCategoryStatement.executeQuery();
                ArrayList<Category> categories = new ArrayList<>();

                while (categoryResultSet.next()) {
                    int categoryId = categoryResultSet.getInt("id");
                    String categoryName = categoryResultSet.getString("name");
                    Date categoryDateCreate = new Date(categoryResultSet.getTimestamp("createDate").getTime());
                    Date categoryUpdateDate = new Date(categoryResultSet.getTimestamp("updateDate").getTime());
                    String categoryStatus = categoryResultSet.getString("status");
                    Category category = new Category(categoryId, categoryName, categoryDateCreate, categoryUpdateDate,
                            StatusEnum.fromKey(categoryStatus));
                    if (category != null) {
                        categories.add(category);
                    }
                }

                return new Stock(id, brand, sku, name, description, onHand, price, StatusEnum.fromKey(status),
                        createDate, updateDate, categories);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Stock> getBy(String key) {
        ArrayList<Stock> stockList = new ArrayList<Stock>();
        String sql = "SELECT * FROM Stock WHERE name LIKE ? OR id = ?";
        String stockCategorySql = "SELECT c.* FROM StockCategory sc INNER JOIN Category c ON c.id = sc.categoryId WHERE stockId = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                PreparedStatement stockCategoryStatement = connection.prepareStatement(stockCategorySql)) {

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
                int brandId = resultSet.getInt("brandId");
                String name = resultSet.getString("name");
                String sku = resultSet.getString("sku");
                String description = resultSet.getString("description");
                String onHand = resultSet.getString("onHand");
                Double price = resultSet.getDouble("price");
                Date dateCreate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                Brand brand = new BrandDAO().getById(brandId);

                // Obter informações das categorias associadas
                stockCategoryStatement.setInt(1, id);
                ResultSet categoryResultSet = stockCategoryStatement.executeQuery();
                ArrayList<Category> categories = new ArrayList<>();

                while (categoryResultSet.next()) {
                    int categoryId = categoryResultSet.getInt("id");
                    String categoryName = categoryResultSet.getString("name");
                    Date categoryDateCreate = new Date(categoryResultSet.getTimestamp("createDate").getTime());
                    Date categoryUpdateDate = new Date(categoryResultSet.getTimestamp("updateDate").getTime());
                    String categoryStatus = categoryResultSet.getString("status");
                    Category category = new Category(categoryId, categoryName, categoryDateCreate, categoryUpdateDate,
                            StatusEnum.fromKey(categoryStatus));
                    if (category != null) {
                        categories.add(category);
                    }
                }

                var stock = new Stock(id, brand, sku, name, description, onHand, price, StatusEnum.fromKey(status),
                        dateCreate, updateDate, categories);
                stockList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    public ArrayList<Stock> getAll() {
        ArrayList<Stock> stockList = new ArrayList<>();
        String sql = "SELECT * FROM Stock";
        String stockCategorySql = "SELECT c.* FROM StockCategory sc INNER JOIN Category c ON c.id = sc.categoryId WHERE stockId = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                PreparedStatement stockCategoryStatement = connection.prepareStatement(stockCategorySql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int brandId = resultSet.getInt("brandId");
                String sku = resultSet.getString("sku");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String onHand = resultSet.getString("onHand");
                Double price = resultSet.getDouble("price");
                Date dateCreate = new Date(resultSet.getTimestamp("createDate").getTime());
                Date updateDate = new Date(resultSet.getTimestamp("updateDate").getTime());
                String status = resultSet.getString("status");
                Brand brand = new BrandDAO().getById(brandId);

                // Obter informações das categorias associadas
                stockCategoryStatement.setInt(1, id);
                ResultSet categoryResultSet = stockCategoryStatement.executeQuery();
                ArrayList<Category> categories = new ArrayList<>();

                while (categoryResultSet.next()) {
                    int categoryId = categoryResultSet.getInt("id");
                    String categoryName = categoryResultSet.getString("name");
                    Date categoryDateCreate = new Date(categoryResultSet.getTimestamp("createDate").getTime());
                    Date categoryUpdateDate = new Date(categoryResultSet.getTimestamp("updateDate").getTime());
                    String categoryStatus = categoryResultSet.getString("status");
                    Category category = new Category(categoryId, categoryName, categoryDateCreate, categoryUpdateDate,
                            StatusEnum.fromKey(categoryStatus));
                    if (category != null) {
                        categories.add(category);
                    }
                }

                var stock = new Stock(id, brand, sku, name, description, onHand, price, StatusEnum.fromKey(status),
                        dateCreate, updateDate, categories);
                stockList.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockList;
    }

    public boolean delete(int id) throws SQLException {
        Connection connection = null;

        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);

            // Save the colection StockCategory
            this.deleteStockCategories(connection, id);

            String deleteQuery = "DELETE FROM Stock WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            connection.commit(); // Commit da transação
            return rowsAffected > 0;

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
}
