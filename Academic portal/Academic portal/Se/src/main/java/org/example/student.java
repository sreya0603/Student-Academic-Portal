package org.example;
import java.util.Scanner;
public class student {
    public void dashboard(String email) {
        STgrades g = new STgrades();
        curriculum c=new curriculum();
        STregister stu=new STregister();
        String[] str = email.split("@",2);
        String studentId=str[0];
        int work = 0;
        while (work != 6) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("enter \n 1: to register a course \n 2: to deregister \n 3: to view grades \n 4: to compute current cgpa \n 5: for graduation checking \n 6:to logout");
            work = myObj.nextInt();
            switch (work) {
                case 1 -> {
                    System.out.println("register a course");
                    stu.updateRegister(studentId, true);
                }
                case 2 -> {
                    System.out.println("deregister");
                    stu.updateRegister(studentId, false);
                }
                case 3 -> {
                    System.out.println("view grades");
                    g.viewgrade(studentId);
                }
                case 4 -> {
                    System.out.println("compute current cgpa");
                    double x=g.ComputeCgpa(studentId);
                    System.out.printf("CGPA for student %s is: %.2f%n", studentId, x);
                }
                case 5->{
                    System.out.println("graduation check");
                    boolean m= c.checkGraduationEligibility(studentId);
                    if(m){
                        System.out.println("eligible for graduation");}
                    else{
                        System.out.println("not eligible for graduation");
                    }
                }
                case 6-> System.out.println("logout");
                default -> System.out.println("enter valid work you want to do");
            }
        }
    }
}

