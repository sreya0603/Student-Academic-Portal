package org.example;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class profGrade {
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
    public void viewGrades(String professorName) {
        String query = "SELECT s.stud_nam, s.entry_number, c.code, sc.grade " +
                "FROM stud_course_reg sc " +
                "JOIN students s ON sc.entry_number = s.entry_number " +
                "JOIN CourseOfferings c ON sc.course_code = c.code " +
                "WHERE c.proffesor = ? " +
                "ORDER BY c.code, s.entry_number;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, professorName);
            ResultSet rs = ps.executeQuery();
            System.out.printf("%-20s %-20s %-20s %-10s\n", "Name", "Entry Number", "Code", "Grade");
            while (rs.next()) {
                String studentName = rs.getString("stud_nam");
                String entryNumber = rs.getString("entry_number");
                String courseCode = rs.getString("code");
                int grade = rs.getInt("grade");

                System.out.printf("%-20s %-20s %-20s %-10d%n", studentName, entryNumber, courseCode, grade);
            }

            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isCourseOfferedByFaculty(String professor, String courseCode, int semester, int year) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            String query = "SELECT * FROM CourseOfferings WHERE code=? AND semester=? AND yearr=? AND proffesor=?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, courseCode);
            stmt.setInt(2, semester);
            stmt.setInt(3, year);
            stmt.setString(4, professor);
            rs = stmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    private boolean isStudentTakingCourse(String entryNumber, String courseCode, int semester, int year) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            pstmt = conn.prepareStatement("SELECT entry_number FROM stud_course_reg WHERE entry_number = ? AND course_code = ? AND semester = ? AND yearr = ?");
            pstmt.setString(1, entryNumber);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, semester);
            pstmt.setInt(4, year);
            rs = pstmt.executeQuery();
            result=rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public void updateGrades(String professor) {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        System.out.println("Enter course code you want to give grade");
        Scanner myObj = new Scanner(System.in);

        String courseCode = myObj.nextLine();

        System.out.println("Enter semester (as an integer):");
        int semester = myObj.nextInt();

        System.out.println("Enter year (as an integer):");
        int year = myObj.nextInt();
        myObj.nextLine();
        System.out.println("Enter filepath");
        String filePath = myObj.nextLine();
        if (!isCourseOfferedByFaculty(professor, courseCode,semester,year)) {
            System.out.println("Course is not offered by the faculty.");
            return;
        }
        PreparedStatement pstmt=null;
        try{
            pstmt = conn.prepareStatement("UPDATE stud_course_reg SET grade = ?,statuss='c' WHERE entry_number = ? AND course_code = ? AND semester = ? AND yearr = ?");
            br = new BufferedReader(new FileReader(filePath));
            // Skip first line (header)
            br.readLine();
            // Loop through remaining lines
            while ((line = br.readLine()) != null) {
                // Split line into columns
                String[] data = line.split(cvsSplitBy);
                // Extract data from columns
                String entryNumber = data[0];
                int grade = Integer.parseInt(data[1]);
                if (!isStudentTakingCourse(entryNumber, courseCode, semester, year)) {
                    System.out.println("Student is not taking the course.");
                    continue;
                }
                // Set parameters for PreparedStatement
                pstmt.setInt(1, grade);
                pstmt.setString(2, entryNumber);
                pstmt.setString(3, courseCode);
                pstmt.setInt(4, semester);
                pstmt.setInt(5, year);
                // Execute update
                pstmt.executeUpdate();
            }
            System.out.println("Grades updated successfully.");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
        }

    }

}
