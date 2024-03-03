package studentCurd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class curd {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentCURD";
    private static final String USER = "shoot";
    private static final String PASS = "shoot";

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            // Create table if not exists
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255), last VARCHAR(255), email VARCHAR(255), number VARCHAR(255), PRIMARY KEY (id))";
            stmt.executeUpdate(createTableQuery);

            Scanner scan = new Scanner(System.in);

            System.out.println("1. Insert");
            System.out.println("2. Update");
            System.out.println("3. Read");
            System.out.println("4. Delete");
            System.out.print("Choose an option: ");
            int choice = scan.nextInt();

            switch (choice) {
                case 1:
                    insertData(conn, scan);
                    break;
                case 2:
                    updateData(conn, scan);
                    break;
                case 3:
                    readData(conn);
                    break;
                case 4:
                    deleteData(conn, scan);
                    break;
                default:
                    System.out.println("Invalid choice");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void insertData(Connection conn, Scanner scan) throws Exception {
        System.out.print("Enter First name: ");
        String name = scan.next();

        System.out.print("Enter Last name: ");
        String last = scan.next();

        System.out.print("Enter Email: ");
        String email = scan.next();

        System.out.print("Enter Contact no: ");
        String number = scan.next();

        String insertDataQuery = "INSERT INTO users (name, last, email, number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertDataQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, last);
            pstmt.setString(3, email);
            pstmt.setString(4, number);
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        }
    }

    private static void updateData(Connection conn, Scanner scan) throws Exception {
        System.out.print("Enter user ID to update: ");
        int userId = scan.nextInt();

        System.out.print("Enter new Contact no: ");
        String newNumber = scan.next();

        String updateDataQuery = "UPDATE users SET number = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateDataQuery)) {
            pstmt.setString(1, newNumber);
            pstmt.setInt(2, userId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data updated successfully");
            } else {
                System.out.println("User with ID " + userId + " not found");
            }
        }
    }

    private static void readData(Connection conn) throws Exception {
        String readDataQuery = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(readDataQuery)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String last = resultSet.getString("last");
                String email = resultSet.getString("email");
                String number = resultSet.getString("number");

                System.out.println("ID: " + id + ", Name: " + name + " " + last + ", Email: " + email + ", Contact No: " + number);
            }
        }
    }

    private static void deleteData(Connection conn, Scanner scan) throws Exception {
        System.out.print("Enter user ID to delete: ");
        int userId = scan.nextInt();

        String deleteDataQuery = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteDataQuery)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data deleted successfully");
            } else {
                System.out.println("User with ID " + userId + " not found");
            }
        }
    }
}
