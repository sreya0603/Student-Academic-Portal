package org.example.test;
import org.example.system;

import org.junit.Test;
import java.sql.*;
import static org.junit.Assert.assertEquals;

public class systemTest {

    @Test
    public void testAuthenticate() throws SQLException, ClassNotFoundException {
        int expectedRole = 3;
        String email = "test@example.com";
        String password = "testpassword";

        // Insert test data into the database
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + "se", "postgres", "123");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email_id,username, passwordd, rolee) VALUES (?,'sre', ?, ?)");
        stmt.setString(1, email);
        stmt.setString(2, password);
        stmt.setInt(3, expectedRole);
        stmt.executeUpdate();

        // Call the method being tested
        int actualRole = system.authenticate(email, password);

        // Check the result
        assertEquals(expectedRole, actualRole);

        // Clean up the test data
        stmt = conn.prepareStatement("DELETE FROM users WHERE email_id = ?");
        stmt.setString(1, email);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Test
    public void testAuthenticateInvalidEmail() throws SQLException, ClassNotFoundException {
        String email = "nonexistent@example.com";
        String password = "testpassword";

        // Call the method being tested
        int actualRole = system.authenticate(email, password);

        // Check the result
        assertEquals(0, actualRole);
    }

    @Test
    public void testAuthenticateInvalidPassword() throws SQLException, ClassNotFoundException {
        String email = "test@example.com";
        String password = "wrongpassword";

        // Insert test data into the database
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + "se", "postgres", "123");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email_id,username, passwordd, rolee) VALUES (?,'sre', ?, ?)");
        stmt.setString(1, email);
        stmt.setString(2, "testpassword");
        stmt.setInt(3, 3);
        stmt.executeUpdate();

        // Call the method being tested
        int actualRole = system.authenticate(email, password);

        // Check the result
        assertEquals(0, actualRole);

        // Clean up the test data
        stmt = conn.prepareStatement("DELETE FROM users WHERE email_id = ?");
        stmt.setString(1, email);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

}
