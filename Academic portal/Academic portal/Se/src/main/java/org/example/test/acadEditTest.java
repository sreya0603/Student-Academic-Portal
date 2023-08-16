package org.example.test;
import org.example.acadEdit;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class acadEditTest {

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
    @Test
    void testEditCourseCatalog() throws SQLException {
        // Create a connection to the test database

        // Clear the courses table
        PreparedStatement clearStatement = con.prepareStatement("DELETE FROM courses");
        clearStatement.executeUpdate();

        // Create a new acadEdit object and set the input stream to simulate user input
        acadEdit edit = new acadEdit();
        String input = "Staff Dean's office\n1\nTestCode\nTestTitle\nTestLTP\n3\nTest1,Test2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Call the editCourseCatalog method and assert that a new course was added
        edit.editCourseCatalog();
        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM courses");
        ResultSet rs = ps.executeQuery();
        rs.next();
        assertEquals(1, rs.getInt(1));

        // Update the same course
        input = "Staff Dean's office\n2\nTestCode\nUpdatedTitle\nUpdatedLTP\n4\nTest1,Test2,Test3\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Call the editCourseCatalog method and assert that the course was updated
        edit.editCourseCatalog();
        ps = con.prepareStatement("SELECT title, LTP, credits, prerequisites FROM courses WHERE code = ?");
        ps.setString(1, "TestCode");
        rs = ps.executeQuery();
        rs.next();
        assertEquals("UpdatedTitle", rs.getString("title"));
        assertEquals("UpdatedLTP", rs.getString("LTP"));
        assertEquals(4, rs.getInt("credits"));
        assertEquals("Test1,Test2,Test3", String.join(",", (String[])rs.getArray("prerequisites").getArray()));

        // Close resources
        rs.close();
        ps.close();
        clearStatement.close();
        con.close();
    }
}
