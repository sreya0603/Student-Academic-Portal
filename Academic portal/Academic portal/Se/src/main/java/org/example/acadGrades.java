package org.example;
import java.io.*;
import java.sql.*;
public class acadGrades {

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
        public void viewGradesOfAllStudents() {

            String query = """
                    SELECT students.stud_nam, students.entry_number, stud_course_reg.course_code, stud_course_reg.grade
                    FROM students
                    LEFT JOIN stud_course_reg ON stud_course_reg.entry_number = students.entry_number AND stud_course_reg.statuss = 'c';
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("%-20s %-20s %-20s %-20s%n", "Name", "Entry Number", "Course Code", "Grade");
                while (rs.next()) {
                    System.out.printf("%-20s %-20s %-20s %-20d%n",
                            rs.getString("stud_nam"), rs.getString("entry_number"),
                            rs.getString("course_code"), rs.getInt("grade"));
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public void generateTranscript(String entryNumber) {
        STgrades st=new STgrades();
        String query = "SELECT s.stud_nam, s.batch_year, s.department, r.course_code, r.grade, r.statuss "
                + "FROM students s JOIN stud_course_reg r ON s.entry_number = r.entry_number "
                + "WHERE s.entry_number = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, entryNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String studentName = rs.getString("stud_nam");
                int year = rs.getInt("batch_year");
                String department = rs.getString("department");
                double cgpa=st.ComputeCgpa(entryNumber);
                StringBuilder transcript = new StringBuilder();
                transcript.append(String.format("Name: %s%n", studentName));
                transcript.append(String.format("Entry Number: %s%n", entryNumber));
                transcript.append(String.format("Batch Year: %d%n", year));
                transcript.append(String.format("Department: %s%n%n", department));
                transcript.append(String.format("CGPA: %.2f%n%n", cgpa));
                transcript.append(String.format("%-20s %-20s %-20s%n", "Course Code", "Grade", "Status"));
                do {
                    String courseCode = rs.getString("course_code");
                    int grade = rs.getInt("grade");
                    String status = rs.getString("statuss");
                    transcript.append(String.format("%-20s %-20d %-20s%n", courseCode, grade, status));
                } while (rs.next());

                File file = new File("transcript_" + entryNumber + ".txt");
                FileWriter writer = new FileWriter(file);
                writer.write(transcript.toString());
                writer.close();
                System.out.println("Transcript generated successfully.");
            } else {
                System.out.println("No student found with entry number " + entryNumber);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing transcript file: " + e.getMessage());
        }

    }

}
