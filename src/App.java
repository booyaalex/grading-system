import java.awt.Toolkit;
import java.time.LocalDateTime;
import javax.swing.*;
import com.google.gson.Gson;

class Student {
    int studentID;
    String firstName;
    String lastName;
    String email;
    Integer overallGrade;
    Assignment[] grades;
}

class Assignment {
    int assignmentId;
    String name;
    LocalDateTime dueDate;
    Integer grade = null;
}

public class App {
    private void createUI() {
        frame = new JFrame("Teacher Grading");
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        // INITIALIZE COMPONENTS
        //
    }
    private static String serializeString() {
        Gson gson = new Gson();
        Student student = new Student();
        student.firstName = "Leo";
        student.lastName = "Divine";
        student.email = "ldivine2027@gmail.com";
        student.overallGrade = 100;

        String json = gson.toJson(student);

        return json;
    }
    public static void main(String[] args) throws Exception {
        App app = new App();
        app.createUI();
        System.out.println(serializeString());
    }

    private JFrame frame;
    //private JPanel pnl_StudentListPanel;
    //private JPanel pnl_StudentDetailsPanel;
}
