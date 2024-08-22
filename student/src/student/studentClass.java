package student;

import java.sql.*;
import java.util.Scanner;

public class studentClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection con = null;
        try {
            // Establish connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "Abirami@0202");
            System.out.println("Connected to the database!");

            // Create table if not exists
            createTable(con);

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert");
                System.out.println("2. Read");
                System.out.println("3. Update");
                System.out.println("4. Delete");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        insertData(con, scanner);
                        break;
                    case 2:
                        readData(con);
                        break;
                    case 3:
                        updateData(con, scanner);
                        break;
                    case 4:
                        deleteData(con, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close(); // Close connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }

    // Method to create student table if not exists
    private static void createTable(Connection con) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS student (" +
                                  "roll_number INT PRIMARY KEY," +
                                  "name VARCHAR(100)," +
                                  "cgpa DOUBLE," +
                                  "email VARCHAR(100))";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(createTableQuery);
        System.out.println("Student table created (if not exists).");
    }

    // Method to insert data into student table
    private static void insertData(Connection con, Scanner scanner) throws SQLException {
        System.out.print("Enter Roll Number: ");
        int rollNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter CGPA: ");
        double cgpa = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        String insertQuery = "INSERT INTO student (roll_number, name, cgpa, email) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStatement = con.prepareStatement(insertQuery);
        insertStatement.setInt(1, rollNumber);
        insertStatement.setString(2, name);
        insertStatement.setDouble(3, cgpa);
        insertStatement.setString(4, email);
        insertStatement.executeUpdate();
        System.out.println("\nData inserted into student table.");
    }

    // Method to read data from student table
    private static void readData(Connection con) throws SQLException {
        String selectQuery = "SELECT * FROM student";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(selectQuery);
        System.out.println("\nStudent table data:\n");
        System.out.println("Roll No.\tName    \tCGPA    \tEmail");
        while (rs.next()) {
        	System.out.println(rs.getInt("roll_number")+"\t\t"+rs.getString("name")+"\t\t"+rs.getDouble("cgpa")+"\t\t"+rs.getString("email"));
   
        }
        System.out.println("\n");
    }

    // Method to update data in student table
    private static void updateData(Connection con, Scanner scanner) throws SQLException {
        System.out.print("Enter Roll Number to Update: ");
        int rollNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter New Name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter current CGPA: ");
        String newCGPA = scanner.nextLine();
        String updateQuery = "UPDATE student SET name = ?, cgpa = ? WHERE roll_number = ?";
        PreparedStatement updateStatement = con.prepareStatement(updateQuery);
        updateStatement.setString(1, newName);
        updateStatement.setInt(3, rollNumber);
        updateStatement.setString(2, newCGPA);
        int rowsUpdated = updateStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("\nData updated in student table.");
        } else {
            System.out.println("\nNo data found with roll number: " + rollNumber);
        }
    }

    // Method to delete data from student table
    private static void deleteData(Connection con, Scanner scanner) throws SQLException {
        System.out.print("Enter Roll Number to Delete: ");
        int rollNumber = scanner.nextInt();

        String deleteQuery = "DELETE FROM student WHERE roll_number = ?";
        PreparedStatement deleteStatement = con.prepareStatement(deleteQuery);
        deleteStatement.setInt(1, rollNumber);
        int rowsDeleted = deleteStatement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("\nData deleted from student table.");
        } else {
            System.out.println("\nNo data found withroll number: " + rollNumber);
           }
    }
}
