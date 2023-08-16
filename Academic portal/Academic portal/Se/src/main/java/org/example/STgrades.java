package org.example;

import java.sql.*;

public class STgrades {
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
    public void viewgrade(String studentId) {
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = conn.prepareStatement("SELECT courses.title,courses.credits, stud_course_reg.grade FROM courses INNER JOIN stud_course_reg ON courses.code=stud_course_reg.course_code WHERE stud_course_reg.entry_number = ?");
            ps.setString(1, studentId);
            rs = ps.executeQuery();

            System.out.println("Course\t\tGrade\t\tcredits");
            while(rs.next()) {
                String course = rs.getString("title");
                int grade = rs.getInt("grade");
                int credits = rs.getInt("credits");
                System.out.println(course + "\t\t\t" + grade + "\t\t\t"+ credits);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double ComputeCgpa(String studentId) {
        PreparedStatement ps;
        ResultSet rs;
        double cgpa=0;
        try {
            ps = conn.prepareStatement("SELECT SUM(courses.credits * stud_course_reg.grade) AS total_credits, SUM(courses.credits) AS total_credits_taken FROM courses INNER JOIN stud_course_reg ON courses.code=stud_course_reg.course_code WHERE stud_course_reg.entry_number = ? and stud_course_reg.statuss='c'");
            ps.setString(1, studentId);
            rs = ps.executeQuery();

            if (rs.next()) {
                double total_credits = rs.getDouble("total_credits");
                double total_credits_taken = rs.getDouble("total_credits_taken");

                if (total_credits_taken > 0) {
                    cgpa = total_credits / total_credits_taken;
                } else {
                    System.out.println("Student has not taken any courses.");
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cgpa;
    }


}
