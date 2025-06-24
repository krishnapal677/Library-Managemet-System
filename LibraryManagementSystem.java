package LibraryManagementSystem;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalTime;

// Student class
class Student {
    private String name;
    private String studentId;
    private LocalTime entryTime;
    private LocalTime exitTime;
    private double feesPaid;

    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.feesPaid = 0.0;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public LocalTime getEntryTime() { return entryTime; }
    public LocalTime getExitTime() { return exitTime; }
    public double getFeesPaid() { return feesPaid; }

    public void setEntryTime(LocalTime entryTime) { this.entryTime = entryTime; }
    public void setExitTime(LocalTime exitTime) { this.exitTime = exitTime; }
    public void payFees(double amount) { this.feesPaid += amount; }

    @Override
    public String toString() {
        return "Student ID: " + studentId + "\nName: " + name +
                "\nEntry Time: " + entryTime +
                "\nExit Time: " + exitTime +
                "\nFees Paid: $" + feesPaid;
    }
}

// Library class
class Library {
    private ArrayList<Student> students = new ArrayList<>();

    // Add a student to the library
    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Student added successfully!");
    }

    // Record entry time for a student
    public void recordEntry(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                student.setEntryTime(LocalTime.now());
                System.out.println("Entry time recorded for " + student.getName());
                return;
            }
        }
        System.out.println("Student not found!");
    }

    // Record exit time for a student
    public void recordExit(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                student.setExitTime(LocalTime.now());
                System.out.println("Exit time recorded for " + student.getName());
                return;
            }
        }
        System.out.println("Student not found!");
    }

    // Collect fees from a student
    public void collectFees(String studentId, double amount) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                student.payFees(amount);
                System.out.println("Fees collected from " + student.getName());
                return;
            }
        }
        System.out.println("Student not found!");
    }

    // Display all students
    public void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the library.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
            System.out.println("----------");
        }
    }
}

// Main class (Driver)
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\n***** LIBRARY MANAGEMENT SYSTEM *****");
            System.out.println("1. Add Student");
            System.out.println("2. Record Entry Time");
            System.out.println("3. Record Exit Time");
            System.out.println("4. Collect Fees");
            System.out.println("5. Display All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    String studentId = scanner.nextLine();

                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine();

                    library.addStudent(new Student(name, studentId));
                    break;

                case 2:
                    System.out.print("Enter Student ID to record entry: ");
                    String entryId = scanner.nextLine();
                    library.recordEntry(entryId);
                    break;

                case 3:
                    System.out.print("Enter Student ID to record exit: ");
                    String exitId = scanner.nextLine();
                    library.recordExit(exitId);
                    break;

                case 4:
                    System.out.print("Enter Student ID to collect fees: ");
                    String feeId = scanner.nextLine();
                    System.out.print("Enter amount to collect: ");
                    double amount = scanner.nextDouble();
                    library.collectFees(feeId, amount);
                    break;

                case 5:
                    library.displayStudents();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }
}
