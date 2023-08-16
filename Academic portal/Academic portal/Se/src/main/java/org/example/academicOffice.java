package org.example;
import java.util.Scanner;

public class academicOffice {
    public void dashboard() {
        acadEdit ad= new acadEdit();
        acadGrades gr= new acadGrades();
        int work = 0;
        while (work != 4) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("enter \n 1: to edit the course catalog \n 2: to view grades of all students \n 3: to generate transcript \n 4: to logout");
            work = myObj.nextInt();
            switch (work) {
                case 1 -> {
                    System.out.println("edit the course catalog");
                    ad.editCourseCatalog();
                }
                case 2 -> {
                    System.out.println("view grades of all students");
                    gr.viewGradesOfAllStudents();
                }
                case 3 -> {
                    System.out.println(" enter entry number of the student for generating transcript");
                    myObj.nextLine();
                    String entryNumber = myObj.nextLine();
                    gr.generateTranscript(entryNumber);
                }
                case 4-> System.out.println("logout");
                default -> System.out.println("enter valid work you want to do");
            }
        }
    }
}
