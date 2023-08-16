package org.example;

import java.sql.*;
import java.util.Scanner;

public class STregister {
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

    public String checkCourseExists(String courseId) {
        PreparedStatement ps;
        ResultSet rs;
        String courseInfo = null;


        try {
            ps = conn.prepareStatement("SELECT semester, yearr,cgpa_constraint FROM courseofferings WHERE code = ? and statuss='enrolling'");
            ps.setString(1, courseId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String semester = rs.getString("semester");
                String year = rs.getString("yearr");
                double cgpa = rs.getDouble("cgpa_constraint");
                courseInfo = semester + " " + year+ " " +cgpa;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseInfo;
    }
    public boolean checkIfcompleted(String studentId, String courseId) {
        PreparedStatement ps;
        ResultSet rs;
        boolean completed = false;

        try {
            ps = conn.prepareStatement("SELECT * FROM stud_course_reg WHERE entry_number = ? AND course_code = ? and statuss='c'");
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            rs = ps.executeQuery();
            completed = rs.next();
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completed;
    }
    public boolean checkIfRegistered(String studentId, String courseId) {
        PreparedStatement ps;
        ResultSet rs;
        boolean registered = false;
        try {
            ps = conn.prepareStatement("SELECT * FROM stud_course_reg WHERE entry_number = ? AND course_code = ? and statuss='R'");
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            rs = ps.executeQuery();
            registered = rs.next();
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registered;
    }
    public void insertRegistrationRecord(String studentId, String courseId, int semester, int year) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO stud_course_reg (entry_number,course_code,statuss,semester,yearr) VALUES (?, ?,'R',?,?)");
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.setInt(3, semester);
            ps.setInt(4, year);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void deleteRegistrationRecord(String studentId, String courseId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM stud_course_reg WHERE entry_number = ? AND course_code = ? and statuss='R'");
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean hasClearedPrerequisites(String studentId, String courseId) {
        PreparedStatement ps;
        ResultSet rs;
        boolean pre = true;
        try {
            ps = conn.prepareStatement("SELECT prerequisites FROM courses WHERE  code = ?");
            ps.setString(1, courseId);
            rs = ps.executeQuery();
            rs.next();
            Array a= rs.getArray("prerequisites");
            if (a != null) { // check if prerequisites are present
                String[] arr = (String[]) a.getArray();
                for (String s : arr) {
                    pre = pre && checkIfcompleted(studentId, s);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pre;
    }
    public int totalCredits(int semester,int year,String studentId,String status){
        PreparedStatement ps;
        ResultSet rs;
        int total_credits=0;
        try {
            ps = conn.prepareStatement("SELECT SUM(c.credits) AS total_credits" +
                    "        FROM courses c" +
                    "        INNER JOIN stud_course_reg scr ON c.code = scr.course_code" +
                    "        WHERE scr.entry_number=? AND scr.semester = ? AND scr.yearr = ? AND statuss=?");
            ps.setString(1, studentId);
            ps.setInt(2, semester);
            ps.setInt(3, year);
            ps.setString(4, status);
            rs = ps.executeQuery();

            if (rs.next()) {
                total_credits = rs.getInt("total_credits");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total_credits;
    }

    public boolean checkCreditLimit(String studentId,int semester,int year){
        boolean exceeded=false;
        int previousSemester = semester == 1 ? 2 : 1;
        int previousYear = semester == 1 ? year - 1 : year;
        int secondPreviousSemester = previousSemester == 1 ? 2 : 1;
        int secondPreviousYear = previousSemester == 1 ? previousYear - 1 : previousYear;
        int x=totalCredits(secondPreviousSemester,secondPreviousYear ,studentId,"c");
        int y=totalCredits(previousSemester,previousYear,studentId,"c");
        int z=totalCredits(semester,year,studentId,"R");
        if(z>1.25*((x+y)/2)) exceeded = true;
        return exceeded;
    }
    public void updateRegister(String studentId, boolean isRegister){
        STgrades st=new STgrades();
        System.out.println("enter courseId:__________");
        Scanner myObj = new Scanner(System.in);
        String courseId = myObj.nextLine();
        String courseExists = checkCourseExists(courseId);
        int semester,year;
        double requiredCGPA;
        if (courseExists==null) {
            System.out.println("course does not exist.");
            return;
        }
        else {
            String[] parts = courseExists.split(" ");
            semester =Integer.parseInt(parts[0]);
            year = Integer.parseInt(parts[1]);
            requiredCGPA = Double.parseDouble(parts[2]);
        }
        boolean isAlreadyRegistered = checkIfRegistered(studentId, courseId);
        double studentCGPA = st.ComputeCgpa(studentId);
        boolean iscreditlimit = checkCreditLimit(studentId,semester,year);
        if (isRegister) {
            if(iscreditlimit){
                System.out.println("credit limit exceeded");
            }
            else{
                if (studentCGPA < requiredCGPA)
                {
                    System.out.println("You cannot register for this course without meeting the required CGPA.");
                }
                else {
                    // If the student wants to register for the course and is not already registered, insert a new registration record
                    if (!isAlreadyRegistered){
                        if (!hasClearedPrerequisites(studentId, courseId)) {
                            System.out.println("You cannot register for this course without clearing the prerequisites.");
                        } else {
                            insertRegistrationRecord(studentId, courseId, semester, year);
                            System.out.println("Course successfully registered.");
                        }
                    } else{
                        // Handle the case where the student is already registered for the course
                        System.out.println("student is already registered for the course.");
                    }
                }
            }
         }
         else {
            // If the student wants to deregister from the course and is already registered, delete the registration record
            if (isAlreadyRegistered) {
                deleteRegistrationRecord(studentId, courseId);
                System.out.println("Course successfully deregistered.");
            } else {
                // Handle the case where the student is not registered for the course
                System.out.println("student is not registered for the course.");
            }
        }
    }

}

