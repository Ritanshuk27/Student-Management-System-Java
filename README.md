# Student Management System

A professional, interview-ready **Core Java Student Management System** built with OOP, `ArrayList`, `Scanner`, exception handling, and optional file persistence. It is designed as a clean console application that demonstrates strong Java fundamentals without Spring Boot, databases, or build tools.

## GitHub Repository Description
A modular Core Java console application for managing student records with registration, search, update, course assignment, reports, validation, and optional file storage. Built to demonstrate clean OOP design and interview-friendly coding practices.

## Features
- Add new students with auto-generated Student IDs
- View all students
- Search student by ID or name
- Update student information
- Delete student records
- Assign and update courses
- Display students by course
- Reports: total students, male/female count, average age, recently added student
- Input validation for empty values, duplicate roll numbers, email format, and phone number format
- Exception handling for invalid menu choices, missing students, and invalid input types
- Optional file handling for saving and loading student data

## Tech Stack
- Java 17
- Core Java
- Object-Oriented Programming
- ArrayList Collections
- Scanner Class
- Exception Handling
- File Handling (optional)
- Console-based UI

## Project Structure
```text
StudentManagementSystem/
├── src/
│   ├── Main.java
│   ├── Student.java
│   ├── StudentService.java
│   └── StudentUtils.java
├── README.md
├── LICENSE
└── .gitignore
```

## How to Run
1. Open terminal in the project root folder.
2. Compile the source files:

```bash
javac src/*.java
```

3. Run the application:

```bash
java -cp src Main
```

## Sample Console Output
```text
============================================================
           STUDENT MANAGEMENT SYSTEM - CORE JAVA
============================================================

====================== MENU ======================
1.  Add New Student
2.  View All Students
3.  Search Student by ID
4.  Search Student by Name
5.  Update Student Information
6.  Delete Student
7.  Assign Course
8.  Update Course
9.  Display Students by Course
10. Reports
11. Display Recently Added Student
12. Save Data to File
13. Load Data from File
0.  Exit
==================================================
Enter your choice: 1

--- Add New Student ---
Enter Name: Rahul Sharma
Enter Roll Number: 101
Enter Age: 19
Enter Gender (Male/Female/Other): Male
Enter Course: BCA
Enter Email: rahul@gmail.com
Enter Phone Number (10 digits): 9876543210
Student added successfully.
------------------------------------------------------------
Student ID: 1001
Roll Number: 101
Name: Rahul Sharma
Age: 19
Gender: Male
Course: BCA
Email: rahul@gmail.com
Phone: 9876543210
Added On: 04-Aug-2026 10:15:30
------------------------------------------------------------
```

## Input Validation Rules
- Roll number must be unique
- Email must follow a valid format
- Phone number must be 10 digits and start with 6, 7, 8, or 9
- Empty input is not allowed
- Age must be between 5 and 100

## OOP Concepts Used
- Encapsulation through private fields and public getters/setters
- Constructors for initializing student objects
- Method overloading for search and update operations
- Separation of concerns across model, service, and utility classes
- Reusable helper methods for validation and formatting

## Exception Handling
- Invalid menu choice is handled with a default switch case
- Invalid input type is handled while reading numbers from `Scanner`
- Student not found is handled through a custom runtime exception
- Invalid data is rejected with clear error messages


## Future Enhancements
- Add a graphical user interface
- Integrate database storage with MySQL or PostgreSQL
- Add export to CSV or PDF
- Add sorting and filtering options
- Track attendance and marks
- Add login and role-based access
- Add pagination for large student lists

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
