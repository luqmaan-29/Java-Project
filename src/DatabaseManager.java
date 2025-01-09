import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection connection;

    // Constructor to establish the database connection
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/medical_store_db", "root", "salt@20293");
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Method to add a new medicine to the database
    public void addMedicine(String name, String manufacturer, double price, int stock) {
        String query = "INSERT INTO medicines (name, manufacturer, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, manufacturer);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stock);
            pstmt.executeUpdate();
            System.out.println("Medicine added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding medicine: " + e.getMessage());
        }
    }

    // Method to update the stock of a medicine
    public void updateMedicineStock(int id, int stock) {
        String query = "UPDATE medicines SET stock = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, stock);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Medicine stock updated successfully.");
            } else {
                System.out.println("Medicine with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating medicine stock: " + e.getMessage());
        }
    }

    // Method to retrieve a specific medicine by ID
    public Map<String, Object> getMedicineById(int id) {
        String query = "SELECT * FROM medicines WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> med = new HashMap<>();
                med.put("id", rs.getInt("id"));
                med.put("name", rs.getString("name"));
                med.put("manufacturer", rs.getString("manufacturer"));
                med.put("price", rs.getDouble("price"));
                med.put("stock", rs.getInt("stock"));
                return med;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving medicine: " + e.getMessage());
        }
        return null;
    }

    // Method to delete a medicine by name
    public void deleteMedicineByName(String name) {
        String query = "DELETE FROM medicines WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Medicine deleted successfully.");
            } else {
                System.out.println("No medicine found with the name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting medicine: " + e.getMessage());
        }
    }

    // Method to retrieve all medicines
    public List<Map<String, Object>> getAllMedicines() {
        String query = "SELECT * FROM medicines";
        List<Map<String, Object>> medicines = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Map<String, Object> med = new HashMap<>();
                med.put("id", rs.getInt("id"));
                med.put("name", rs.getString("name"));
                med.put("manufacturer", rs.getString("manufacturer"));
                med.put("price", rs.getDouble("price"));
                med.put("stock", rs.getInt("stock"));
                medicines.add(med);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving medicines: " + e.getMessage());
        }
        return medicines;
    }

    // Method to check if a medicine exists by ID
    public boolean medicineExists(int id) {
        String query = "SELECT COUNT(*) AS count FROM medicines WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking medicine existence: " + e.getMessage());
        }
        return false;
    }

    // Method to close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error closing the database connection: " + e.getMessage());
        }
    }
}
