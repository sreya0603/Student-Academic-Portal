package org.example.test;
import org.example.profGrade;
import org.junit.jupiter.api.*;

        import java.io.ByteArrayOutputStream;
        import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.junit.jupiter.api.Assertions.assertFalse;
        import static org.junit.jupiter.api.Assertions.assertTrue;

public class profGradeTest {

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
    private profGrade pg;


    @Test
    public void testViewGrades() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        pg.viewGrades("Prof1");
        String expectedOutput = "Name                 Entry Number        Code                 Grade     \n" +
                "Alice                EN101              CS101               85        \n" +
                "Bob                  EN102              CS101               70        \n" +
                "Charlie              EN103              CS102               90        \n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testIsCourseOfferedByFaculty() {
        assertTrue(pg.isCourseOfferedByFaculty("Prof1", "CS101", 1, 2022));
        assertFalse(pg.isCourseOfferedByFaculty("Prof2", "CS101", 1, 2022));
        assertFalse(pg.isCourseOfferedByFaculty("Prof1", "CS103", 1, 2022));
    }

    @Test
    public void testUpdateGrades() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO stud_course_reg (entry_number, course_code, semester, yearr, grade, statuss) " +
                "VALUES ('EN101', 'CS101', 1, 2022, null, null), " +
                "('EN102', 'CS101', 1, 2022, null, null), " +
                "('EN103', 'CS102', 1, 2022, null, null)");
        ps.executeUpdate();
        ps.close();
        conn.close();

        // run test
        pg.updateGrades("Prof1");

        // verify test result
        ps = conn.prepareStatement("SELECT grade FROM stud_course_reg WHERE course_code='CS101'");
        ResultSet rs = ps.executeQuery();
        assertTrue(rs.next());
        assertEquals(85, rs.getInt("grade"));
        assertTrue(rs.next());
        assertEquals(70, rs.getInt("grade"));
        assertFalse(rs.next());
        rs.close();
        ps.close();
        conn.close();
    }

}
