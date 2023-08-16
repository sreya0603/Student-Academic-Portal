package org.example.test;

import org.example.acadGrades;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class acadGradesTest {

    // A helper method to create a test database connection
    private Connection createTestConnection() throws SQLException {

        Connection con=null;
        {
            try {
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + "se", "postgres", "123");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    @Test
    public void testViewGradesOfAllStudents() {
        acadGrades ag = new acadGrades();

        // Insert test data into the database
        try (Connection conn = createTestConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students VALUES (?, ?, ?)");
        ) {
            // Insert two test students
            pstmt.setString(1, "John Doe");
            pstmt.setString(2, "001");
            pstmt.setInt(3, 2022);
            pstmt.executeUpdate();

            pstmt.setString(1, "Jane Smith");
            pstmt.setString(2, "002");
            pstmt.setInt(3, 2022);
            pstmt.executeUpdate();

            // Insert test course registrations
            pstmt.clearParameters();
            pstmt.setString(1, "001");
            pstmt.setString(2, "CSE101");
            pstmt.setInt(3, 80);
            pstmt.setString(4, "c");
            pstmt.executeUpdate();

            pstmt.clearParameters();
            pstmt.setString(1, "001");
            pstmt.setString(2, "CSE102");
            pstmt.setInt(3, 70);
            pstmt.setString(4, "c");
            pstmt.executeUpdate();

            pstmt.clearParameters();
            pstmt.setString(1, "002");
            pstmt.setString(2, "CSE101");
            pstmt.setInt(3, 90);
            pstmt.setString(4, "c");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Redirect System.out to a buffer
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the viewGradesOfAllStudents method
        try {
            ag.viewGradesOfAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Restore System.out
        System.setOut(System.out);

        // Verify the output
        String expectedOutput = "Name                 Entry Number         Course Code         Grade               \n" +
                "John Doe             001                 CSE101              80                  \n" +
                "John Doe             001                 CSE102              70                  \n" +
                "Jane Smith           002                 CSE101              90                  \n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testGenerateTranscript() {
        acadGrades ag = new acadGrades();

        // Insert test data into the database
        try (Connection conn = createTestConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students VALUES (?, ?, ?)");) {
            // Insert a test student
            pstmt.setString(1, "John Doe");
            pstmt.setString(2, "001");
            pstmt.setInt(3, 2022);
            pstmt.executeUpdate();

            // Insert test course registrations
            pstmt.clearParameters();
            pstmt.setString(1, "001");
            pstmt.setString(2, "CSE102");
            pstmt.setInt(3, 70);
            pstmt.setString(4, "c");
            pstmt.executeUpdate();

            pstmt.clearParameters();
            pstmt.setString(1, "001");
            pstmt.setString(2, "MTH101");
            pstmt.setInt(3, 85);
            pstmt.setString(4, "b");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Redirect System.out to a buffer
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the generateTranscript method
        try {
            ag.generateTranscript("001");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Restore System.out
        System.setOut(System.out);

        // Verify the output
        String expectedOutput = "Name: John Doe\n" +
                "Entry Number: 001\n" +
                "\n" +
                "Course Code: CSE101     Grade: 80       Grade Point: 2.0     \n" +
                "Course Code: CSE102     Grade: 70       Grade Point: 1.7     \n" +
                "Course Code: MTH101     Grade: 85       Grade Point: 3.0     \n" +
                "\n" +
                "CGPA: 2.2\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}