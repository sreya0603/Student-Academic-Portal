package org.example;
import java.sql.*;
import java.util.Scanner;

public class profRegister {
    Connection conn=null;
    {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + "se", "postgres", "123");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void registerCourse(String professor){
        System.out.println("Enter course code for the course you want to offer:");
        Scanner myObj = new Scanner(System.in);

        String courseCode = myObj.nextLine();

        System.out.println("Enter semester (as an integer):");
        int semester = myObj.nextInt();

        System.out.println("Enter year (as an integer):");
        int year = myObj.nextInt();

        System.out.println("Enter CGPA constraint (as a decimal number):");
        float cgpa_constraint = myObj.nextFloat();

        myObj.nextLine(); // consume the newline character left by nextFloat()
        String query = "INSERT INTO CourseOfferings (code,  proffesor, semester, yearr, cgpa_constraint, statuss) " +
                "VALUES (?, ?, ?, ?, ?, 'enrolling');";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, courseCode);
            statement.setString(2, professor);
            statement.setInt(3, semester);
            statement.setInt(4, year);
            statement.setFloat(5, cgpa_constraint);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("Course " + courseCode + " has been registered for " + semester + "sem of year" + year);
            } else {
                System.out.println("Course registration failed.");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deregisterCourse(){
        System.out.println("Enter course code for the course you want to derigester:");
        Scanner myObj = new Scanner(System.in);

        String courseCode = myObj.nextLine();

        System.out.println("Enter semester (as an integer):");
        int semester = myObj.nextInt();

        System.out.println("Enter year (as an integer):");
        int year = myObj.nextInt();
        myObj.nextLine();
        String query = "DELETE FROM CourseOfferings WHERE code = ? AND semester = ? AND yearr = ?;";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, courseCode);
            statement.setInt(2, semester);
            statement.setInt(3, year);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("Course " + courseCode + " has been deregistered for " + semester + "sem of year" + year);
            } else {
                System.out.println("Course deregistration failed.");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void UpdateregisterCourse(String professor){
        System.out.println("Enter course code for the course you want to edit:");
        Scanner myObj = new Scanner(System.in);
        String courseCode = myObj.nextLine();

        System.out.println("Enter semester (as an integer):");
        int semester = myObj.nextInt();

        System.out.println("Enter year (as an integer):");
        int year = myObj.nextInt();

        System.out.println("Enter new CGPA constraint (as a decimal number):");
        float cgpa_constraint = myObj.nextFloat();
        myObj.nextLine();
        System.out.println("Enter new course stauss (as an integer):");
        String status = myObj.nextLine();// consume the newline character left by nextFloat()

        String query = "UPDATE CourseOfferings" +
                "SET cgpa_constraint = ?,proffesor = ?, statuss = ?" +
                "WHERE code = ? AND semester = ? AND yearr = ?;";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setFloat(1, cgpa_constraint);
            statement.setString(2, professor);
            statement.setString(3, status);
            statement.setString(4, courseCode);
            statement.setInt(5, semester);
            statement.setInt(6, year);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("Course " + courseCode + " has been registered for " + semester + "sem of year" + year);
            } else {
                System.out.println("Course registration failed.");
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
