package org.example;
import java.sql.*;
import java.util.Scanner;

public class system {
        public static int authenticate(String email,String password) {
                Connection conn;
                try {
                        Class.forName("org.postgresql.Driver");
                        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + "se", "postgres", "123");
                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email_id = ? AND passwordd = ?");
                        Scanner myObj = new Scanner(System.in);
                        stmt.setString(1, email);
                        stmt.setString(2, password);
                        ResultSet rs = stmt.executeQuery();
                        int role=0;
                        while ( rs.next() ) {
                                role = rs.getInt("rolee");
                        }
                        rs.close();
                        stmt.close();
                        conn.close();
                        return role;
                } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return 0;
                }
        }
        public static void main(String[] args){
                int x=1;
                while(x==1){
                        Scanner myObj = new Scanner(System.in);
                        System.out.println("enter emailId");
                        String email = myObj.nextLine();
                        System.out.println("enter password");
                        String password = myObj.nextLine();
                        int role = authenticate(email, password);
                        if (role==3) {
                                System.out.println("successfully logged in as a student");
                                student page= new student();
                                page.dashboard(email);
                        }
                        else if (role==2) {
                                System.out.println("successfully logged in as a professor");
                                professor page= new professor();
                                page.dashboard(email);
                        }
                        else if (role==1) {
                                System.out.println("successfully logged in as a admin");
                                academicOffice page= new academicOffice();
                                page.dashboard();
                        }
                        else {
                                System.out.println("Invalid username or password.");
                        }
                        System.out.println("enter 1 if want to continue using app");
                        x = myObj.nextInt();
                }
        }
}