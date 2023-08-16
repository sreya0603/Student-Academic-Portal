package org.example;
import java.sql.*;
import java.util.*;
public class acadEdit {
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
    public void editCourseCatalog(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("enter username");
        String username = myObj.nextLine();
        if (!username.equals("Staff Dean's office")) {
            System.out.println("Unauthorized user!");
            return;
        }
        PreparedStatement ps;
        System.out.println("do you want to \n1: add new course 2: edit the course");
        int i = myObj.nextInt();
        if(i==1){
            try {
                System.out.println("enter newCourseCode , newTitle, newLTP, newCredits");
                myObj.nextLine();
                String courseCode= myObj.nextLine();
                String newTitle= myObj.nextLine();
                String newLTP= myObj.nextLine();
                int newCredits;
                try {
                    newCredits= myObj.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    return; // or handle the error in some other way
                }
                myObj.nextLine();
                System.out.println("Enter the prerequisites (separated by commas):");
                String input = myObj.nextLine();
                String[] newPrerequisites;
                if (input.isEmpty()) {
                    newPrerequisites = null;
                } else {
                    newPrerequisites = input.split(",");
                }
                ps = con.prepareStatement("INSERT INTO courses(code,title,LTP,prerequisites,credits)VALUES (?,?,?,?,?)");
                ps.setString(1, courseCode);
                ps.setString(2, newTitle);
                ps.setString(3, newLTP);
                if (newPrerequisites == null) {
                    ps.setNull(4, Types.ARRAY);
                } else {
                    Array sqlArray = con.createArrayOf("VARCHAR", newPrerequisites);
                    ps.setArray(4, sqlArray);
                }
                ps.setInt(5, newCredits);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("successfully added the course");
        }
        else {
            try {
                System.out.println("enter newCourseCode , newTitle, newLTP, newCredits");
                myObj.nextLine();
                String courseCode= myObj.nextLine();
                String newTitle= myObj.nextLine();
                String newLTP= myObj.nextLine();
                int newCredits;
                try {
                    newCredits= myObj.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    return; // or handle the error in some other way
                }
                myObj.nextLine();
                System.out.println("Enter the prerequisites (separated by commas):");
                String input = myObj.nextLine();
                String[] newPrerequisites;
                if (input.isEmpty()) {
                    newPrerequisites = null;
                } else {
                    newPrerequisites = input.split(",");
                }
                ps = con.prepareStatement("UPDATE courses SET title=?, LTP=?, credits=?, prerequisites=? WHERE code=?");
                ps.setString(1, newTitle);
                ps.setString(2, newLTP);
                ps.setInt(3, newCredits);
                if (newPrerequisites == null) {
                    ps.setNull(4, Types.ARRAY);
                } else {
                    Array sqlArray = con.createArrayOf("VARCHAR", newPrerequisites);
                    ps.setArray(4, sqlArray);
                }
                ps.setString(5, courseCode);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("successfully edited the course");
        }
    }

}
