
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class ProductDAO {

    // LOAD PRODUCTS FROM DATABASE
    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> productList = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) return productList;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product product = new MakeupProduct(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("image_path")
                );

                productList.add(product);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Error loading products!");
            e.printStackTrace();
        }

        return productList;
    }

    // ADD PRODUCT TO DATABASE
    public boolean addProduct(Product product) {

        String sql = "INSERT INTO products " +
                "(id, name, category, price, stock, image_path) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) return false;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, product.getId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getCategory());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getImagePath());

            ps.executeUpdate();
            ps.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Error adding product!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE PRODUCT FROM DATABASE
    public boolean deleteProduct(int id) {

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) return false;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            ps.close();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting product!");
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE PRODUCT STOCK
    public boolean updateStock(int id, int stock) {

        String sql = "UPDATE products SET stock = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) return false;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, stock);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            ps.close();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating product stock!");
            e.printStackTrace();
            return false;
        }
    }

    // SAVE CHECKOUT DETAILS AND UPDATE STOCK
    public boolean saveCheckout(
            String customerName,
            Map<Integer, Integer> cart) {

        String selectSQL =
                "SELECT name, price, stock FROM products WHERE id = ? FOR UPDATE";

        String insertSQL =
                "INSERT INTO orders " +
                "(customer_name, product_name, price, quantity, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        String updateSQL =
                "UPDATE products SET stock = stock - ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (con == null) return false;

            con.setAutoCommit(false);

            try (
                PreparedStatement selectPS = con.prepareStatement(selectSQL);
                PreparedStatement insertPS = con.prepareStatement(insertSQL);
                PreparedStatement updatePS = con.prepareStatement(updateSQL)
            ) {

                // Process every product in the cart
                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {

                    int productId = entry.getKey();
                    int quantity = entry.getValue();

                    selectPS.setInt(1, productId);

                    try (ResultSet rs = selectPS.executeQuery()) {

                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }

                        String productName = rs.getString("name");
                        double price = rs.getDouble("price");
                        int stock = rs.getInt("stock");

                        if (quantity <= 0 || quantity > stock) {
                            con.rollback();
                            return false;
                        }

                        double subtotal = price * quantity;

                        // Save customer and purchased product
                        insertPS.setString(1, customerName);
                        insertPS.setString(2, productName);
                        insertPS.setDouble(3, price);
                        insertPS.setInt(4, quantity);
                        insertPS.setDouble(5, subtotal);

                        insertPS.executeUpdate();

                        // Reduce product stock
                        updatePS.setInt(1, quantity);
                        updatePS.setInt(2, productId);

                        updatePS.executeUpdate();
                    }
                }

                con.commit();
                return true;

            } catch (SQLException e) {

                con.rollback();
                throw e;

            } finally {

                con.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println("Checkout database error!");
            e.printStackTrace();

            return false;
        }
    }
}
