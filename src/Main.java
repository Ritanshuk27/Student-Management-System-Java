import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService studentService = new StudentService();
        studentService.loadStudents();

        System.out.println("============================================================");
        System.out.println("           STUDENT MANAGEMENT SYSTEM - CORE JAVA           ");
        System.out.println("============================================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = StudentUtils.readInt(scanner, "Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addStudent(scanner, studentService);
                    case 2 -> studentService.displayAllStudents();
                    case 3 -> searchStudentById(scanner, studentService);
                    case 4 -> searchStudentByName(scanner, studentService);
                    case 5 -> updateStudent(scanner, studentService);
                    case 6 -> deleteStudent(scanner, studentService);
                    case 7 -> assignCourse(scanner, studentService);
                    case 8 -> updateCourse(scanner, studentService);
                    case 9 -> displayStudentsByCourse(scanner, studentService);
                    case 10 -> showReports(studentService);
                    case 11 -> showRecentlyAddedStudent(studentService);
                    case 12 -> saveData(studentService);
                    case 13 -> loadData(studentService);
                    case 0 -> {
                        studentService.saveStudents();
                        System.out.println("Data saved successfully. Exiting application...");
                        running = false;
                    }
                    default -> System.out.println("Invalid menu choice. Please select a valid option.");
                }
            } catch (StudentNotFoundException | IllegalArgumentException exception) {
                System.out.println("Error: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Unexpected error: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("====================== MENU ======================");
        System.out.println("1.  Add New Student");
        System.out.println("2.  View All Students");
        System.out.println("3.  Search Student by ID");
        System.out.println("4.  Search Student by Name");
        System.out.println("5.  Update Student Information");
        System.out.println("6.  Delete Student");
        System.out.println("7.  Assign Course");
        System.out.println("8.  Update Course");
        System.out.println("9.  Display Students by Course");
        System.out.println("10. Reports");
        System.out.println("11. Display Recently Added Student");
        System.out.println("12. Save Data to File");
        System.out.println("13. Load Data from File");
        System.out.println("0.  Exit");
        System.out.println("==================================================");
    }

    private static void addStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Add New Student ---");
        String name = StudentUtils.readNonEmptyString(scanner, "Enter Name: ");
        long rollNumber = StudentUtils.readLong(scanner, "Enter Roll Number: ");
        int age = StudentUtils.readInt(scanner, "Enter Age: ");
        String gender = StudentUtils.readGender(scanner);
        String course = StudentUtils.readNonEmptyString(scanner, "Enter Course: ");
        String email = StudentUtils.readEmail(scanner);
        String phoneNumber = StudentUtils.readPhoneNumber(scanner);

        Student student = studentService.addStudent(name, rollNumber, age, gender, course, email, phoneNumber);
        System.out.println("Student added successfully.");
        StudentUtils.printStudentDetails(student);
    }

    private static void searchStudentById(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Search Student by ID ---");
        long studentId = StudentUtils.readLong(scanner, "Enter Student ID: ");
        StudentUtils.printStudentDetails(studentService.searchStudent(studentId));
    }

    private static void searchStudentByName(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Search Student by Name ---");
        String name = StudentUtils.readNonEmptyString(scanner, "Enter Name to Search: ");
        List<Student> students = studentService.searchStudent(name);
        if (students.isEmpty()) {
            System.out.println("No students found for name: " + name);
            return;
        }
        StudentUtils.printStudents(students);
    }

    private static void updateStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Update Student Information ---");
        long studentId = StudentUtils.readLong(scanner, "Enter Student ID to update: ");
        String name = StudentUtils.readNonEmptyString(scanner, "Enter New Name: ");
        long rollNumber = StudentUtils.readLong(scanner, "Enter New Roll Number: ");
        int age = StudentUtils.readInt(scanner, "Enter New Age: ");
        String gender = StudentUtils.readGender(scanner);
        String course = StudentUtils.readNonEmptyString(scanner, "Enter New Course: ");
        String email = StudentUtils.readEmail(scanner);
        String phoneNumber = StudentUtils.readPhoneNumber(scanner);

        Student student = studentService.updateStudent(studentId, name, rollNumber, age, gender, course, email, phoneNumber);
        System.out.println("Student information updated successfully.");
        StudentUtils.printStudentDetails(student);
    }

    private static void deleteStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Delete Student ---");
        long studentId = StudentUtils.readLong(scanner, "Enter Student ID to delete: ");
        studentService.deleteStudent(studentId);
        System.out.println("Student deleted successfully.");
    }

    private static void assignCourse(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Assign Course ---");
        long studentId = StudentUtils.readLong(scanner, "Enter Student ID: ");
        String course = StudentUtils.readNonEmptyString(scanner, "Enter Course to Assign: ");
        Student student = studentService.assignCourse(studentId, course);
        System.out.println("Course assigned successfully.");
        StudentUtils.printStudentDetails(student);
    }

    private static void updateCourse(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Update Course ---");
        long studentId = StudentUtils.readLong(scanner, "Enter Student ID: ");
        String course = StudentUtils.readNonEmptyString(scanner, "Enter New Course: ");
        Student student = studentService.updateStudentCourse(studentId, course);
        System.out.println("Course updated successfully.");
        StudentUtils.printStudentDetails(student);
    }

    private static void displayStudentsByCourse(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Display Students by Course ---");
        String course = StudentUtils.readNonEmptyString(scanner, "Enter Course Name: ");
        studentService.displayStudentsByCourse(course);
    }

    private static void showReports(StudentService studentService) {
        System.out.println("\n--- Reports ---");
        System.out.println("Total Students      : " + studentService.getTotalStudents());
        System.out.println("Male Students       : " + studentService.getMaleCount());
        System.out.println("Female Students     : " + studentService.getFemaleCount());
        System.out.printf("Average Age         : %.2f%n", studentService.getAverageAge());
        Student recentlyAdded = studentService.getRecentlyAddedStudent();
        System.out.println("Recently Added      : " + (recentlyAdded != null ? recentlyAdded.getName() + " (ID: " + recentlyAdded.getStudentId() + ")" : "No student added yet"));
    }

    private static void showRecentlyAddedStudent(StudentService studentService) {
        System.out.println("\n--- Recently Added Student ---");
        Student recentlyAdded = studentService.getRecentlyAddedStudent();
        StudentUtils.printStudentDetails(recentlyAdded);
    }

    private static void saveData(StudentService studentService) {
        studentService.saveStudents();
        System.out.println("Data saved successfully.");
    }

    private static void loadData(StudentService studentService) {
        studentService.loadStudents();
        System.out.println("Data loaded successfully.");
    }
}
