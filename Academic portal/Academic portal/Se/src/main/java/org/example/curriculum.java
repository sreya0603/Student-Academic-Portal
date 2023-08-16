package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class curriculum {
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
    public boolean checkGraduationEligibility(String student_id) {
            int MIN_ELECTIVE_COURSES = 2;
            Scanner myObj = new Scanner(System.in);
            System.out.println("enter an curriculum id");
            int curriculum_id = myObj.nextInt();
            PreparedStatement stmt;
            ResultSet rs;
            try {
                // Establish the database connection
                // Get all courses taken by the student
                String query = "SELECT * FROM stud_course_reg WHERE entry_number = ?";
                stmt = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, student_id);
                rs = stmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();

                // Get all program core courses for the student's curriculum
                query = "SELECT code FROM program_core WHERE curriculum_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, curriculum_id);
                ResultSet core_courses_rs = stmt.executeQuery();

                // Get all program elective courses for the student's curriculum
                query = "SELECT code FROM program_elective WHERE curriculum_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, curriculum_id);
                ResultSet elective_courses_rs = stmt.executeQuery();

                // Get the BTP Capstone course for the student's curriculum
                query = "SELECT code FROM btp_capstone WHERE curriculum_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, curriculum_id);
                ResultSet btp_capstone_rs = stmt.executeQuery();

                // Initialize counters
                int core_courses_completed = 0;
                int elective_courses_completed = 0;
                boolean btp_capstone_completed = false;

                // Check if the student has completed all program core courses

                // Check if the student has completed all program core courses
                List<String> core_course_codes = new ArrayList<>();
                while (core_courses_rs.next()) {
                    core_course_codes.add(core_courses_rs.getString("code"));
                }

                rs.beforeFirst();
                while (rs.next()) {
                    if (core_course_codes.contains(rs.getString("course_code")) && rs.getString("statuss").equals("c")) {
                        core_courses_completed++;
                    }
                }

                if (core_courses_completed < core_course_codes.size()) {
                    return false; // The student has not completed all program core courses
                }

                // Check if the student has completed the minimum number of program elective courses
                rs.beforeFirst();
                while (rs.next()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        if (elective_courses_rs.next() && elective_courses_rs.getString("course_code").equals(rs.getString("code")) && rs.getString("statuss").equals("c")) {
                            elective_courses_completed++;
                        }
                    }
                }

                if (elective_courses_completed < MIN_ELECTIVE_COURSES) {
                    return false; // The student has not completed the minimum number of program elective courses
                }

                // Check if the student has completed the BTP Capstone course
                rs.beforeFirst();
                while (rs.next()) {
                    if (btp_capstone_rs.next() && btp_capstone_rs.getString("course_code").equals(rs.getString("code")) && rs.getString("statuss").equals("c")) {
                        btp_capstone_completed = true;
                    }
                }
                if(!btp_capstone_completed){
                    return false;
                }
                stmt.close();
                rs.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }

}
