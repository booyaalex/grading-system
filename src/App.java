import java.awt.Toolkit;
import java.sql.Date;
import java.time.LocalDateTime;
import javax.swing.*;
import java.util.Vector;

class Student {
    int studentID;
    String firstName;
    String lastName;
    String email;
    Date birthday;
    Integer overallGrade;
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
        pnl_MainPanel = new JPanel();
        pnl_LeftPanel = new JPanel();
        pnl_RightPanel = new JPanel();
        pnl_StudentListPanel = new JPanel();
        pnl_AssignemntListPanel = new JPanel();
        pnl_StudentDetailsPanel = new JPanel();
        pnl_StudentAdderPanel = new JPanel();
        pnl_StudentEditorPanel = new JPanel();
        pnl_AssignemntDetailsPanel = new JPanel();
        pnl_AssignemntCreatorPanel = new JPanel();
        pnl_AssignemntEditorPanel = new JPanel();
        //
        // COMPONENT PROPERTIES
        //
        pnl_LeftPanel.add(pnl_StudentListPanel);
        pnl_LeftPanel.add(pnl_AssignemntListPanel);

        pnl_RightPanel.add(pnl_StudentDetailsPanel);
        pnl_RightPanel.add(pnl_StudentAdderPanel);
        pnl_RightPanel.add(pnl_StudentEditorPanel);
        pnl_RightPanel.add(pnl_AssignemntDetailsPanel);
        pnl_RightPanel.add(pnl_AssignemntCreatorPanel);
        pnl_RightPanel.add(pnl_AssignemntEditorPanel);
        
        pnl_MainPanel.add(pnl_LeftPanel);
        pnl_MainPanel.add(pnl_RightPanel);
        //
        // SHOW APPLICATION
        //
        frame.getContentPane().add(pnl_MainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        Database db = new Database();
        app.createUI();
        Vector<Student> students = db.getStudents();
        for (Student student : students) {
            System.out.println(student.birthday);
        }
    }
    
    private JFrame frame;
    private JPanel pnl_MainPanel;
    private JPanel pnl_LeftPanel;
    private JPanel pnl_RightPanel;
    private JPanel pnl_StudentListPanel;
    private JPanel pnl_AssignemntListPanel;
    private JPanel pnl_StudentDetailsPanel;
    private JPanel pnl_StudentAdderPanel;
    private JPanel pnl_StudentEditorPanel;
    private JPanel pnl_AssignemntDetailsPanel;
    private JPanel pnl_AssignemntCreatorPanel;
    private JPanel pnl_AssignemntEditorPanel;
}
