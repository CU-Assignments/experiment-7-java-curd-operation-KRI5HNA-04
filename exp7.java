/*Create Java applications with JDBC for database connectivity, CRUD operations, and MVC architecture. 
Easy Level: 
Problem Statement: 
Create a Java program to connect to a MySQL database and fetch data from a single table. The program should: 
Use DriverManager and Connection objects. 
Retrieve and display all records from a table named Employee with columns EmpID, Name, and Salary.*/

import java.sql.*;

public class EmployeeFetcher {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";
        
        String query = "SELECT * FROM Employee";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("EmpID\tName\tSalary");
            while (rs.next()) {
                int empId = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                System.out.println(empId + "\t" + name + "\t" + salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


/*Build a program to perform CRUD operations (Create, Read, Update, Delete) on a database table Product with 
columns: 
ProductID, ProductName, Price, and Quantity. 
The program should include: 
Menu-driven options for each operation. Transaction handling to ensure data integrity.*/

import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1 -> createProduct(conn, scanner);
                    case 2 -> readProducts(conn);
                    case 3 -> updateProduct(conn, scanner);
                    case 4 -> deleteProduct(conn, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void createProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        }
    }
    
    private static void readProducts(Connection conn) throws SQLException {
        String query = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("ProductID | ProductName | Price | Quantity");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + " | " + rs.getString("ProductName") + " | " + rs.getDouble("Price") + " | " + rs.getInt("Quantity"));
            }
        }
    }
    
    private static void updateProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ProductID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        String query = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, price);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Product updated successfully." : "Product not found.");
        }
    }
    
    private static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ProductID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String query = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Product deleted successfully." : "Product not found.");
        }
    }
}


/*Develop a Java application using JDBC and MVC architecture to manage student data. The application should: 
Use a Student class as the model with fields like StudentID, Name, Department, and Marks. 
Include a database table to store student data. 
Allow the user to perform CRUD operations through a simple menu-driven view. 
Implement database operations in a separate controller class*/

import java.sql.*;
import java.util.Scanner;

// Model Class
class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}

// Controller Class
class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    
    public void createStudent(Student student) throws SQLException {
        String query = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, student.getStudentID());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setDouble(4, student.getMarks());
            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        }
    }

    public void readStudents() throws SQLException {
        String query = "SELECT * FROM Student";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("StudentID | Name | Department | Marks");
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " | " + rs.getString("Name") + " | " +
                        rs.getString("Department") + " | " + rs.getDouble("Marks"));
            }
        }
    }

    public void updateStudent(int studentID, double marks) throws SQLException {
        String query = "UPDATE Student SET Marks = ? WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, marks);
            pstmt.setInt(2, studentID);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Student updated successfully." : "Student not found.");
        }
    }

    public void deleteStudent(int studentID) throws SQLException {
        String query = "DELETE FROM Student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentID);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Student deleted successfully." : "Student not found.");
        }
    }
}

// View & Main Application
public class StudentManagementApp {
    public static void main(String[] args) {
        StudentController controller = new StudentController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Department: ");
                        String department = scanner.nextLine();
                        System.out.print("Enter Marks: ");
                        double marks = scanner.nextDouble();
                        scanner.nextLine();
                        controller.createStudent(new Student(id, name, department, marks));
                    }
                    case 2 -> controller.readStudents();
                    case 3 -> {
                        System.out.print("Enter Student ID to update: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter new Marks: ");
                        double marks = scanner.nextDouble();
                        scanner.nextLine();
                        controller.updateStudent(id, marks);
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        controller.deleteStudent(id);
                    }
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}



