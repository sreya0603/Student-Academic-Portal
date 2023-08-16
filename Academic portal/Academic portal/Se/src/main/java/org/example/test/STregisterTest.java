package org.example.test;

import org.example.STregister;
import org.junit.jupiter.api.*;
import java.sql.*;

public class STregisterTest {

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
            stmt.execute("INSERT INTO courseofferings (code, semester, yearr, cgpa_constraint, statuss) VALUES ('CSE101', '1', '2022', 0, 'enrolling')");
            stmt.execute("INSERT INTO courses (code, title, credits, prerequisites) VALUES ('CSE101', 'Intro to Computer Science', 3, NULL)");
            stmt.execute("INSERT INTO stud_course_reg (entry_number, course_code, statuss, semester, yearr) VALUES ('1001', 'CSE101', 'c', 1, 2022)");
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
            stmt.execute("DELETE FROM courseofferings WHERE code = 'CSE101'");
            stmt.execute("DELETE FROM courses WHERE code = 'CSE101'");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCheckCourseExists() {
        STregister streg = new STregister();
        String result = streg.checkCourseExists("CSE101");
        Assertions.assertEquals("1 2022 2.5", result);
    }

    @Test
    void testCheckIfcompleted() {
        STregister streg = new STregister();
        boolean result = streg.checkIfcompleted("1001", "CSE101");
        Assertions.assertTrue(result);
    }

    @Test
    void testCheckIfRegistered() {
        STregister streg = new STregister();
        boolean result = streg.checkIfRegistered("1001", "CSE101");
        Assertions.assertFalse(result);
    }

    @Test
    void testInsertRegistrationRecord() {
        STregister streg = new STregister();
        streg.insertRegistrationRecord("1001", "CSE101", 2, 2022);
        boolean result = streg.checkIfRegistered("1001", "CSE101");
        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteRegistrationRecord() {
        STregister streg = new STregister();
        streg.deleteRegistrationRecord("1001", "CSE101");
        boolean result = streg.checkIfRegistered("1001", "CSE101");
        Assertions.assertFalse(result);
    }

    @Test
    void testHasClearedPrerequisites() {
        STregister streg = new STregister();
        boolean result = streg.hasClearedPrerequisites("1001", "CSE101");
        Assertions.assertTrue(result);
    }

    @Test
    void testTotalCredits() {
        STregister streg = new STregister();
        int result = streg.totalCredits(1, 2022, "1001", "c");
        Assertions.assertEquals(3, result);
    }
}