package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;
public class professor {
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
    public void dashboard(String emailID){
        int work = 0;
        profGrade pg = new profGrade();
        profRegister pr=new profRegister();
        String professorName = null;
        try{
        String query = "SELECT nam FROM Faculty WHERE email_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, emailID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            professorName = rs.getString("nam");
        }} catch (SQLException e) {
            e.printStackTrace();
        }
        while (work != 6) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("enter \n 1: to View grade of all students \n 2: to register \n 3:to deregister \n 4: to update/edit course offering \n 5: to update course grade \n 6: to logout");
            work = myObj.nextInt();
            switch (work) {
                case 1 -> {
                    System.out.println("View grade of all students.");
                    pg.viewGrades(professorName);
                }
                case 2 -> {
                    System.out.println("register");
                    pr.registerCourse(professorName);
                }
                case 3 -> {
                    System.out.println("deregister");
                    pr.deregisterCourse();
                }
                case 4 -> {
                    System.out.println("update register");
                    pr.UpdateregisterCourse(professorName);
                }
                case 5 -> {
                    System.out.println("update course grade");
                    pg.updateGrades(professorName);
                }
                case 6 -> System.out.println("logout");
                default -> System.out.println("enter valid work you want to do");
            }
        }
    }
}
