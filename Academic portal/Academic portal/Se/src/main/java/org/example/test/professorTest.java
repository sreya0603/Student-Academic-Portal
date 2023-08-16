package org.example.test;

import org.example.professor;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class professorTest {
    private static professor prof;
    private static String emailID;

    @BeforeAll
    static void setUp() {
        prof = new professor();
        emailID = "johndoe@example.com";
    }

    @AfterAll
    static void tearDown() {
        // Clean up any resources used by the professor object
    }

    @Test
    @DisplayName("Test professor dashboard")
    void testDashboard() throws SQLException {
        // Execute the professor dashboard method and verify that it runs without exceptions
        assertDoesNotThrow(() -> prof.dashboard(emailID));
    }
}
