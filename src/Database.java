import java.sql.*;
import com.google.gson.Gson;
import java.util.Vector;

class Student {
    int studentID;
    String firstName;
    String lastName;
    String email;
    Date birthday;
    int overallGrade;
    Assignment[] grades;

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
                student.grades = deserializeString(resultSet.getString("GRADES"));
                students.add(student);
            }

            resultSet.close();
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return students;
    }

    public Student getStudent(String name) {
        Student student = null;
        String[] fullName = name.split(" ");
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from students where FIRST_NAME = '" + fullName[0] + "' AND LAST_NAME = '" + fullName[1] + "';");

            while (resultSet.next()) {
                student = new Student(resultSet.getInt("ID"),
                        resultSet.getString("FIRST_NAME"),
                        resultSet.getString("LAST_NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getDate("BIRTHDAY"),
                        resultSet.getInt("OVERALL_GRADE"));
                student.grades = deserializeString(resultSet.getString("GRADES"));
            }

            resultSet.close();
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return student;
    }

    public Vector<Assignment> getAssignments() {
        Vector<Assignment> assignments = new Vector<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from assignments");

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

    public void addStudent(Student student) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO students(ID, FIRST_NAME, LAST_NAME, EMAIL, BIRTHDAY, OVERALL_GRADE, GRADES) VALUES('"
                            + student.studentID + "', '" + student.firstName + "', '" + student.lastName + "', '"
                            + student.email + "', '" + student.birthday + "', '" + student.overallGrade + "', '" + serializeString(student.grades)
                            + "');");
            statement.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private static String serializeString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    private static Assignment[] deserializeString(String str) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(str, Assignment[].class);
    }

    public Database() {
        connectToDatabase();
    }

    private Connection connection = null;
}
