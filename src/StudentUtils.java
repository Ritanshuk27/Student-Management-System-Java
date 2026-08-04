import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StudentUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Path DEFAULT_STORAGE_PATH = Paths.get("students-data.txt");

    private StudentUtils() {
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input type. Please enter a valid integer.");
            }
        }
    }

    public static long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input type. Please enter a valid number.");
            }
        }
    }

    public static String readGender(Scanner scanner) {
        while (true) {
            String gender = readNonEmptyString(scanner, "Enter Gender (Male/Female/Other): ");
            String normalized = gender.trim().toLowerCase(Locale.ENGLISH);
            if (normalized.equals("male") || normalized.equals("female") || normalized.equals("other")) {
                return capitalize(normalized);
            }
            System.out.println("Invalid gender value. Please enter Male, Female, or Other.");
        }
    }

    public static String readEmail(Scanner scanner) {
        while (true) {
            String email = readNonEmptyString(scanner, "Enter Email: ");
            if (isValidEmail(email)) {
                return email;
            }
            System.out.println("Invalid email format. Example: student@example.com");
        }
    }

    public static String readPhoneNumber(Scanner scanner) {
        while (true) {
            String phoneNumber = readNonEmptyString(scanner, "Enter Phone Number (10 digits): ");
            if (isValidPhoneNumber(phoneNumber)) {
                return phoneNumber;
            }
            System.out.println("Invalid phone number. It must be 10 digits and start with 6, 7, 8, or 9.");
        }
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }

    public static String normalizeText(String value) {
        return value == null ? "" : value.trim();
    }

    public static void printSeparator() {
        System.out.println("------------------------------------------------------------");
    }

    public static void printStudentHeader() {
        System.out.printf("%-8s %-15s %-20s %-5s %-10s %-15s %-28s %-12s%n",
                "ID", "Roll No.", "Name", "Age", "Gender", "Course", "Email", "Phone");
        printSeparator();
    }

    public static void printStudentRow(Student student) {
        System.out.printf("%-8d %-15d %-20s %-5d %-10s %-15s %-28s %-12s%n",
                student.getStudentId(),
                student.getRollNumber(),
                trimToLength(student.getName(), 20),
                student.getAge(),
                trimToLength(student.getGender(), 10),
                trimToLength(student.getCourse(), 15),
                trimToLength(student.getEmail(), 28),
                trimToLength(student.getPhoneNumber(), 12));
    }

    public static void printStudents(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        printStudentHeader();
        for (Student student : students) {
            printStudentRow(student);
        }
    }

    public static void printStudentDetails(Student student) {
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        printSeparator();
        System.out.println(student);
        printSeparator();
    }

    public static void saveStudentsToFile(List<Student> students, String fileName) throws IOException {
        Path path = Paths.get(fileName == null || fileName.isBlank() ? DEFAULT_STORAGE_PATH.toString() : fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Student student : students) {
                writer.write(serializeStudent(student));
                writer.newLine();
            }
        }
    }

    public static List<Student> loadStudentsFromFile(String fileName) throws IOException {
        Path path = Paths.get(fileName == null || fileName.isBlank() ? DEFAULT_STORAGE_PATH.toString() : fileName);
        List<Student> students = new ArrayList<>();
        if (!Files.exists(path)) {
            return students;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    students.add(deserializeStudent(line));
                }
            }
        }
        return students;
    }

    private static String serializeStudent(Student student) {
        return student.getStudentId() + "|" + escape(student.getName()) + "|" + student.getRollNumber() + "|"
                + student.getAge() + "|" + escape(student.getGender()) + "|" + escape(student.getCourse()) + "|"
                + escape(student.getEmail()) + "|" + escape(student.getPhoneNumber()) + "|"
                + (student.getCreatedAt() != null ? student.getCreatedAt().toString() : "");
    }

    private static Student deserializeStudent(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid student data in file.");
        }

        Student student = new Student();
        student.setStudentId(Long.parseLong(parts[0]));
        student.setName(unescape(parts[1]));
        student.setRollNumber(Long.parseLong(parts[2]));
        student.setAge(Integer.parseInt(parts[3]));
        student.setGender(unescape(parts[4]));
        student.setCourse(unescape(parts[5]));
        student.setEmail(unescape(parts[6]));
        student.setPhoneNumber(unescape(parts[7]));

        if (parts.length >= 9 && !parts[8].isBlank()) {
            try {
                student.setCreatedAt(LocalDateTime.parse(parts[8]));
            } catch (DateTimeParseException exception) {
                student.setCreatedAt(LocalDateTime.now());
            }
        }
        return student;
    }

    private static String trimToLength(String value, int maxLength) {
        String text = value == null ? "" : value;
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }

    private static String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase(Locale.ENGLISH) + value.substring(1).toLowerCase(Locale.ENGLISH);
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("|", "/");
    }

    private static String unescape(String value) {
        return value == null ? "" : value.replace("/", "|");
    }
}

class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}

class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
