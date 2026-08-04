import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class StudentService {
    private static final String DEFAULT_DATA_FILE = "students-data.txt";

    private final List<Student> students = new ArrayList<>();
    private long nextStudentId = 1001;
    private Student recentlyAddedStudent;

    public void loadStudents() {
        try {
            List<Student> loadedStudents = StudentUtils.loadStudentsFromFile(DEFAULT_DATA_FILE);
            students.clear();
            students.addAll(loadedStudents);
            for (Student student : students) {
                nextStudentId = Math.max(nextStudentId, student.getStudentId() + 1);
            }
            recentlyAddedStudent = students.stream()
                    .max(Comparator.comparing(Student::getStudentId))
                    .orElse(null);
        } catch (IOException exception) {
            System.out.println("Info: Could not load saved data. Starting with an empty student list.");
        }
    }

    public void saveStudents() {
        try {
            StudentUtils.saveStudentsToFile(students, DEFAULT_DATA_FILE);
        } catch (IOException exception) {
            System.out.println("Warning: Could not save data to file.");
        }
    }

    public Student addStudent(String name, long rollNumber, int age, String gender, String course,
                              String email, String phoneNumber) {
        validateStudentData(name, rollNumber, age, gender, course, email, phoneNumber);
        if (isDuplicateRollNumber(rollNumber, -1)) {
            throw new IllegalArgumentException("Duplicate roll number is not allowed.");
        }

        Student student = new Student(nextStudentId++, name.trim(), rollNumber, age, gender.trim(), course.trim(),
                email.trim(), phoneNumber.trim());
        students.add(student);
        recentlyAddedStudent = student;
        return student;
    }

    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    public Student searchStudent(long studentId) {
        return students.stream()
                .filter(student -> student.getStudentId() == studentId)
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + studentId + " not found."));
    }

    public List<Student> searchStudent(String studentName) {
        String keyword = StudentUtils.normalizeText(studentName).toLowerCase(Locale.ENGLISH);
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName() != null && student.getName().toLowerCase(Locale.ENGLISH).contains(keyword)) {
                result.add(student);
            }
        }
        return result;
    }

    public Student updateStudent(long studentId, String name, long rollNumber, int age, String gender,
                                 String course, String email, String phoneNumber) {
        Student student = searchStudent(studentId);
        validateStudentData(name, rollNumber, age, gender, course, email, phoneNumber);
        if (isDuplicateRollNumber(rollNumber, studentId)) {
            throw new IllegalArgumentException("Duplicate roll number is not allowed.");
        }

        student.setName(name.trim());
        student.setRollNumber(rollNumber);
        student.setAge(age);
        student.setGender(gender.trim());
        student.setCourse(course.trim());
        student.setEmail(email.trim());
        student.setPhoneNumber(phoneNumber.trim());
        return student;
    }

    public Student updateStudentName(long studentId, String newName) {
        Student student = searchStudent(studentId);
        student.setName(requireNonEmpty(newName, "Name"));
        return student;
    }

    public Student updateStudentCourse(long studentId, String newCourse) {
        Student student = searchStudent(studentId);
        student.setCourse(requireNonEmpty(newCourse, "Course"));
        return student;
    }

    public Student assignCourse(long studentId, String course) {
        return updateStudentCourse(studentId, course);
    }

    public boolean deleteStudent(long studentId) {
        Student student = searchStudent(studentId);
        boolean removed = students.remove(student);
        if (removed && recentlyAddedStudent != null && recentlyAddedStudent.getStudentId() == studentId) {
            recentlyAddedStudent = students.stream()
                    .max(Comparator.comparing(Student::getStudentId))
                    .orElse(null);
        }
        return removed;
    }

    public List<Student> getStudentsByCourse(String course) {
        String keyword = StudentUtils.normalizeText(course).toLowerCase(Locale.ENGLISH);
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getCourse() != null && student.getCourse().toLowerCase(Locale.ENGLISH).equals(keyword)) {
                result.add(student);
            }
        }
        return result;
    }

    public int getTotalStudents() {
        return students.size();
    }

    public long getMaleCount() {
        return students.stream()
                .filter(student -> "Male".equalsIgnoreCase(student.getGender()))
                .count();
    }

    public long getFemaleCount() {
        return students.stream()
                .filter(student -> "Female".equalsIgnoreCase(student.getGender()))
                .count();
    }

    public double getAverageAge() {
        if (students.isEmpty()) {
            return 0.0;
        }
        int totalAge = 0;
        for (Student student : students) {
            totalAge += student.getAge();
        }
        return (double) totalAge / students.size();
    }

    public Student getRecentlyAddedStudent() {
        return recentlyAddedStudent;
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    public void displayAllStudents() {
        StudentUtils.printStudents(students);
    }

    public void displayStudentsByCourse(String course) {
        List<Student> byCourse = getStudentsByCourse(course);
        if (byCourse.isEmpty()) {
            System.out.println("No students found for course: " + course);
            return;
        }
        StudentUtils.printStudents(byCourse);
    }

    public void displayStudents(List<Student> studentList) {
        StudentUtils.printStudents(studentList);
    }

    private void validateStudentData(String name, long rollNumber, int age, String gender,
                                     String course, String email, String phoneNumber) {
        requireNonEmpty(name, "Name");
        requireNonEmpty(gender, "Gender");
        requireNonEmpty(course, "Course");
        requireNonEmpty(email, "Email");
        requireNonEmpty(phoneNumber, "Phone Number");

        if (rollNumber <= 0) {
            throw new IllegalArgumentException("Roll number must be a positive number.");
        }
        if (age < 5 || age > 100) {
            throw new IllegalArgumentException("Age must be between 5 and 100.");
        }
        if (!StudentUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!StudentUtils.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    private boolean isDuplicateRollNumber(long rollNumber, long currentStudentId) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber && student.getStudentId() != currentStudentId) {
                return true;
            }
        }
        return false;
    }

    private String requireNonEmpty(String value, String fieldName) {
        String trimmed = StudentUtils.normalizeText(value);
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return trimmed;
    }
}
