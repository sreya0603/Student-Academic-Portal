package org.example.test;

import org.example.student;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {

    @Test
    void testDashboard() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create a new student and call the dashboard method
        student student = new student();
        student.dashboard("john.doe@example.com");

        // Convert the output stream to a string and compare with expected output
        String expectedOutput = "enter \n 1: to register a course \n 2: to deregister \n 3: to view grades \n 4: to compute current cgpa \n 5: for graduation checking \n 6:to logout\n" +
                "register a course\n" +
                "deregister\n" +
                "view grades\n" +
                "compute current cgpa\n" +
                "CGPA for student john.doe is: 3.50\n" +
                "graduation check\n" +
                "eligible for graduation\n" +
                "logout\n";
        assertEquals(expectedOutput, outputStream.toString());

        // Restore the standard output
        System.setOut(System.out);
    }
}
