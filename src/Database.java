import java.sql.*;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Vector;

class Student {
    int studentID;
    String firstName;
    String lastName;
    String email;
    Date birthday;
    int overallGrade;
    Vector<Assignment> grades;

    public Student(int studentID, String firstName, String lastName, String email, Date birthday, int overallGrade) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.overallGrade = overallGrade;
    }
}

class Assignment {
    int assignmentID;
    String name;
    Date dueDate;
    Integer grade = null;

    public Assignment(int assignmentID, String name, Date dueDate) {
        this.assignmentID = assignmentID;
        this.name = name;
        this.dueDate = dueDate;
    }
}

public class Database {
    /*
     * Name: connectToDatabase();
     * Input: None;
     * Output: None;
     * Purpose: Sets up the connection to the MySQL database;
     */
    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/grading",
                    "root", "Keefe2012");
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    /*
     * Name: getStudents();
     * Input: None;
     * Output: A vector of all students in the database;
     * Purpose: Gets all students from the database and returns them;
     */
    public Vector<Student> getStudents() {
        Vector<Student> students = new Vector<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from students");

            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("ID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getDate("BIRTHDAY"),
                        resultSet.getInt("OVERALL_GRADE"));
                student.grades = deserializeAssignments(resultSet.getString("GRADES"));
                students.add(student);
            }

            resultSet.close();
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return students;
    }

    /*
     * Name: getStudent();
     * Input: The students full name (John Doe);
     * Output: The student with the given name;
     * Purpose: Returns the student with the given name;
     */
    public Student getStudent(String name) {
        Student student = null;
        String[] fullName = name.split(" ");
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from students where FIRST_NAME = '" + fullName[0] + "' AND LAST_NAME = '" + fullName[1]
                            + "';");

            while (resultSet.next()) {
                student = new Student(resultSet.getInt("ID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getDate("BIRTHDAY"),
                        resultSet.getInt("OVERALL_GRADE"));
                student.grades = deserializeAssignments(resultSet.getString("GRADES"));
            }

            resultSet.close();
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return student;
    }

    /*
     * Name: getAssignments();
     * Input: None;
     * Output: A vector of all assignments in the database;
     * Purpose: Gets all assignments from the database and returns them;
     */
    public Vector<Assignment> getAssignments() {
        Vector<Assignment> assignments = new Vector<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM assignments");

            while (resultSet.next()) {
                Assignment assignment = new Assignment(resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getDate("DUE_DATE"));
                assignments.add(assignment);
            }

            resultSet.close();
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return assignments;
    }

    /*
     * Name: addStudent();
     * Input: A student to be added;
     * Output: None;
     * Purpose: Takes a student and adds them to the database;
     */
    public void addStudent(Student student) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO students(ID, FIRST_NAME, LAST_NAME, EMAIL, BIRTHDAY, OVERALL_GRADE, GRADES) VALUES('"
                            + student.studentID + "', '" + student.firstName + "', '" + student.lastName + "', '"
                            + student.email + "', '" + student.birthday + "', '" + student.overallGrade + "', '"
                            + serializeAssignments(student.grades)
                            + "')");
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    /*
     * Name: addAssignment();
     * Input: An assignment to be added;
     * Output: None;
     * Purpose: Takes an assignment and adds it to the database, then gives that assignment to each student ungraded;
     */
    public void addAssignment(Assignment assignment) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO assignments(ID, NAME, DUE_DATE) VALUES('"
                            + assignment.assignmentID + "', '" + assignment.name + "', '" + assignment.dueDate + "')");
            statement.close();

            Vector<Student> students = getStudents();
            for (Student student : students) {
                student.grades.add(assignment);
                updateStudent(student);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    /*
     * Name: updateStudent();
     * Input: The student to be updated with updated values;
     * Output: None;
     * Purpose: Takes a student that has different values and updates the database with those new values;
     */
    public void updateStudent(Student student) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "UPDATE students " +
                            "SET FIRST_NAME = '" + student.firstName + "', " +
                            "LAST_NAME = '" + student.lastName + "', " +
                            "EMAIL = '" + student.email + "', " +
                            "OVERALL_GRADE = '" + student.overallGrade + "', " +
                            "GRADES = '" + serializeAssignments(student.grades) + "' " +
                            "WHERE ID = '" + student.studentID + "'");
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    /*
     * Name: calculateStudentOverallGrade();
     * Input: The student and their assignments to be graded;
     * Output: The overall grade of the student;
     * Purpose: Takes a student and their assignment grades and calculates the overall grade of all their assignments;
     */
    public int calculateStudentOverallGrade(Student student) {
        int overallGrade = 0;
        int totalAssignments;

        totalAssignments = student.grades.size();
        for (Assignment assignment : student.grades) {
            if (assignment.grade == null) {
                totalAssignments--;
                continue;
            }
            overallGrade += assignment.grade;
        }
        overallGrade /= totalAssignments;

        return overallGrade;
    }

    /*
     * Name: serializeAssignments();
     * Input: A vector of assignments to be serialized;
     * Output: The serialized assignments;
     * Purpose: Serializes the given vector of assignments and returns it;
     */
    private static String serializeAssignments(Vector<Assignment> assignments) {
        Gson gson = new Gson();
        String result = gson.toJson(assignments);
        return result;
    }

    /*
     * Name: deserializeAssignments();
     * Input: A serialized string of assignments;
     * Output: A vector of assignments;
     * Purpose: Takes a serialized string of assignments, deserializes it, and returns the deserialized vector;
     */
    private static Vector<Assignment> deserializeAssignments(String str) throws Exception {
        Gson gson = new Gson();
        Assignment[] result = gson.fromJson(str, Assignment[].class);
        return new Vector<>(Arrays.asList(result));
    }

    public Database() {
        connectToDatabase();
    }

    private Connection connection = null;
}