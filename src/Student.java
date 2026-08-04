import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Student {
    private long studentId;
    private String name;
    private long rollNumber;
    private int age;
    private String gender;
    private String course;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public Student() {
        this.createdAt = LocalDateTime.now();
    }

    public Student(long studentId, String name, long rollNumber, int age, String gender,
                   String course, String email, String phoneNumber) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
        this.age = age;
        this.gender = gender;
        this.course = course;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(long rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return String.format(
                "Student ID: %d%nRoll Number: %d%nName: %s%nAge: %d%nGender: %s%nCourse: %s%nEmail: %s%nPhone: %s%nAdded On: %s",
                studentId,
                rollNumber,
                name,
                age,
                gender,
                course,
                email,
                phoneNumber,
                createdAt != null ? createdAt.format(formatter) : "N/A"
        );
    }
}
