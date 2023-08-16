package org.example.test;

import org.example.STgrades;
import org.junit.jupiter.api.*;
import java.sql.*;
public class STgradesTest {

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

    @BeforeAll
    void setUp() {
        // Set up the database connection

        // Add test data to the database
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO courses (code, title, credits, prerequisites) VALUES ('CSE101', 'Intro to Computer Science', 3, NULL)");
            stmt.execute("INSERT INTO stud_course_reg (entry_number, course_code, statuss, semester, yearr, grade) VALUES ('1001', 'CSE101', 'c', 1, 2022, 9)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDown() {
        // Remove test data from the database
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM stud_course_reg WHERE entry_number = '1001'");
            stmt.execute("DELETE FROM courses WHERE code = 'CSE101'");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testViewGrade() {
        STgrades stgrades = new STgrades();
        stgrades.viewgrade("1001");
        // Check output manually
    }

    @Test
    void testComputeCgpa() {
        STgrades stgrades = new STgrades();
        double result = stgrades.ComputeCgpa("1001");
        Assertions.assertEquals(0.0, result, 0.001);
    }

}