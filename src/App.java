import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import javax.swing.*;

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

public class App {
    private void createEvents() {  
        btn_StudentList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnl_StudentListPanel.setVisible(true);
                pnl_AssignmentListPanel.setVisible(false);
            }
        });
        btn_AssignmentList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnl_AssignmentListPanel.setVisible(true);
                pnl_StudentListPanel.setVisible(false);
            }
        });
    }

    private JTable makeStudentsTable() {
        Database db = new Database();
        String[] columnNames = { "First Name", "Last Name", "Email", "Grade"};
        Vector<Student> students = db.getStudents();
        Object[][] data = new Object[students.size()][4];
        for (int i = 0; i < students.size(); i++) {
            Object[] obj = { students.elementAt(i).firstName, students.elementAt(i).lastName, students.elementAt(i).email, students.elementAt(i).overallGrade };
            data[i] = obj;
        }
        return new JTable(data, columnNames);
    }

    private JTable makeAssignmentsTable() {
        Database db = new Database();
        String[] columnNames = { "Name", "Due Date" };
        Vector<Assignment> assignments = db.getAssignments();
        Object[][] data = new Object[assignments.size()][2];
        for (int i = 0; i < assignments.size(); i++) {
            Object[] obj = { assignments.elementAt(i).name, assignments.elementAt(i).dueDate };
            data[i] = obj;
        }
        return new JTable(data, columnNames);
    }

    private void createUI() {
        
        frame = new JFrame("Teacher Grading");
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        // INITIALIZE COMPONENTS
        //
        pnl_MainPanel = new JPanel();
        pnl_Toolbar = new JPanel();
        pnl_LeftPanel = new JPanel();
        pnl_RightPanel = new JPanel();

        btn_StudentList = new JButton();
        btn_AssignmentList = new JButton();

        pnl_StudentListPanel = new JPanel();
        pnl_AssignmentListPanel = new JPanel();
        tbl_StudentList = new JTable();
        tbl_AssignmentList = new JTable();

        pnl_StudentDetailsPanel = new JPanel();
        pnl_StudentAdderPanel = new JPanel();
        pnl_StudentEditorPanel = new JPanel();

        pnl_AssignmentDetailsPanel = new JPanel();
        pnl_AssignmentCreatorPanel = new JPanel();
        pnl_AssignmentEditorPanel = new JPanel();
        //
        // COMPONENT PROPERTIES
        //
        pnl_MainPanel.setLayout(new GridBagLayout());
        gb_constraints.fill = GridBagConstraints.HORIZONTAL;
        gb_constraints.anchor = GridBagConstraints.HORIZONTAL;
        gb_constraints.gridx = 0;
        gb_constraints.gridy = 0;
        gb_constraints.gridwidth = 3;
        pnl_MainPanel.add(pnl_Toolbar, gb_constraints);

        gb_constraints.fill = GridBagConstraints.BOTH;
        gb_constraints.gridy = 1;
        gb_constraints.gridwidth = 2;
        pnl_MainPanel.add(pnl_LeftPanel, gb_constraints);

        gb_constraints.gridwidth = 1;
        pnl_MainPanel.add(pnl_RightPanel, gb_constraints);

        f_layout.setAlignment(FlowLayout.LEFT);
        pnl_Toolbar.setLayout(f_layout);
        pnl_Toolbar.add(btn_StudentList);
        btn_StudentList.setText("Students");
        pnl_Toolbar.add(btn_AssignmentList);
        btn_AssignmentList.setText("Assignments");

        pnl_LeftPanel.add(pnl_StudentListPanel);
        tbl_StudentList = makeStudentsTable();
        pnl_StudentListPanel.add(new JScrollPane(tbl_StudentList));

        pnl_AssignmentListPanel.setVisible(false);
        pnl_LeftPanel.add(pnl_AssignmentListPanel);
        tbl_AssignmentList = makeAssignmentsTable();
        pnl_AssignmentListPanel.add(new JScrollPane(tbl_AssignmentList));

        pnl_RightPanel.add(pnl_StudentDetailsPanel);
        pnl_RightPanel.add(pnl_StudentAdderPanel);
        pnl_RightPanel.add(pnl_StudentEditorPanel);

        pnl_RightPanel.add(pnl_AssignmentDetailsPanel);
        pnl_RightPanel.add(pnl_AssignmentCreatorPanel);
        pnl_RightPanel.add(pnl_AssignmentEditorPanel);
        //
        // SHOW APPLICATION
        //
        frame.getContentPane().add(pnl_MainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.createUI();
        app.createEvents();
    }

    private static GridBagConstraints gb_constraints = new GridBagConstraints();
    private static FlowLayout f_layout = new FlowLayout();
    
    private JFrame frame;
    private JPanel pnl_MainPanel;
    private JPanel pnl_Toolbar;
    private JPanel pnl_LeftPanel;
    private JPanel pnl_RightPanel;

    private JButton btn_StudentList;
    private JButton btn_AssignmentList;

    private JPanel pnl_StudentListPanel;
    private JTable tbl_StudentList;

    private JPanel pnl_AssignmentListPanel;
    private JTable tbl_AssignmentList;

    private JPanel pnl_StudentDetailsPanel;
    private JPanel pnl_StudentAdderPanel;
    private JPanel pnl_StudentEditorPanel;

    private JPanel pnl_AssignmentDetailsPanel;
    private JPanel pnl_AssignmentCreatorPanel;
    private JPanel pnl_AssignmentEditorPanel;
}
