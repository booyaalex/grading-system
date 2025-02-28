import java.sql.Date;
import java.util.Vector;

public class Student {
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