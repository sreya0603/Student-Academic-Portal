package org.example.test;
import org.example.profRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class profRegisterTest {
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

    private profRegister pr;

    @BeforeEach
    public void setUp() {
        pr = new profRegister();
    }

    @Test
    public void testRegisterCourse() {
        String input = "CS101\n1\n2023\n3.0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        String query = "SELECT COUNT(*) AS total FROM CourseOfferings WHERE code = 'CS101' AND semester = 1 AND yearr = 2023;";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int before = rs.getInt("total");

            pr.registerCourse("John Doe");

            rs = statement.executeQuery(query);
            rs.next();
            int after = rs.getInt("total");

            assertEquals(before + 0, after);

            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeregisterCourse() {
        String input = "CS101\n1\n2023\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        String query = "SELECT COUNT(*) AS total FROM CourseOfferings WHERE code = 'CS101' AND semester = 1 AND yearr = 2023;";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            int before = rs.getInt("total");

            pr.deregisterCourse();

            rs = statement.executeQuery(query);
            rs.next();
            int after = rs.getInt("total");

            assertEquals(before + 0, after);

            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateRegisterCourse() {
        String input = "CS101\n1\n2023\n3.5\nenrolled\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        String query = "SELECT cgpa_constraint, statuss FROM CourseOfferings WHERE code = 'CS101' AND semester = 1 AND yearr = 2023;";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            float before_cgpa = rs.getFloat("cgpa_constraint");
            String before_status = rs.getString("statuss");

            pr.UpdateregisterCourse("John Doe");

            rs = statement.executeQuery(query);
            rs.next();
            float after_cgpa = rs.getFloat("cgpa_constraint");
            String after_status = rs.getString("statuss");

            assertEquals(3.5, after_cgpa);
            assertEquals("enrolled", after_status);

            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
