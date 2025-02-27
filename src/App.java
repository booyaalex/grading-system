import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.DateFormat;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.util.Locale;
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
        tbl_StudentList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tbl_StudentList.getSelectedRow() != -1) {
                    displayStudentInfo(tbl_StudentList.getSelectedRow());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        tbl_StudentGradesTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                System.out.println("test");
            }
        });
    }

    private void displayRightPanel(JPanel selectedPanel) {
        JPanel[] rightPanels = { pnl_StudentDetailsPanel, pnl_StudentAdderPanel, pnl_StudentEditorPanel, pnl_AssignmentDetailsPanel, pnl_AssignmentCreatorPanel, pnl_AssignmentEditorPanel };
        for(JPanel panel : rightPanels) {
            if(panel == selectedPanel) {
                panel.setVisible(true);
            } else {
                panel.setVisible(false);
            }
        }
    }

    private void displayStudentInfo(int selectedRowIndex) {
        Database db = new Database();

        Student student = db.getStudent(tbl_StudentList.getValueAt(selectedRowIndex, 0) + " "
                + tbl_StudentList.getValueAt(selectedRowIndex, 1));
        lbl_StudentName.setText(student.firstName + " " + student.lastName);
        lbl_StudentEmail.setText("Email: " + student.email);
        lbl_StudentBirthday.setText("Date of Birth: "
                + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(student.birthday));
        lbl_StudenGrade.setText("Grade: " + String.valueOf(student.overallGrade));

        tbl_StudentGradesTable.setModel(makeStudentAssignmentsTable(student));
        tbl_StudentGradesTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                if (evt.getColumn() != 1) { return; }
                student.grades.elementAt(tbl_StudentGradesTable.getSelectedRow()).grade = Integer.valueOf(tbl_StudentGradesTable.getValueAt(tbl_StudentGradesTable.getSelectedRow(), 1) + "");
                student.overallGrade = db.calculateStudentOverallGrade(student);
                db.updateStudent(student);
            }
        });

        displayRightPanel(pnl_StudentDetailsPanel);
    }

    private JTable makeStudentsTable() {
        Database db = new Database();

        String[] columnNames = { "First Name", "Last Name", "Email", "Grade" };
        Vector<Student> students = db.getStudents();
        Object[][] data = new Object[students.size()][4];
        for (int i = 0; i < students.size(); i++) {
            Object[] obj = { students.elementAt(i).firstName, students.elementAt(i).lastName,
                    students.elementAt(i).email, students.elementAt(i).overallGrade };
            data[i] = obj;
        }

        JTable table = new JTable(data, columnNames);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(50);

        return table;
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

        JTable table = new JTable(data, columnNames);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(50);

        return table;
    }

    private JTable makeStudentAssignmentsTable() {
        String[] columnNames = { "Name", "Grade", "Due Date" };
        Object[][] data = new Object[0][3];
        return new JTable(data, columnNames);
    }

    private TableModel makeStudentAssignmentsTable(Student student) {
        String[] columnNames = { "Name", "Grade", "Due Date" };
        Object[][] data = new Object[student.grades.size()][3];
        for (int i = 0; i < student.grades.size(); i++) {
            Object[] obj = { student.grades.elementAt(i).name, student.grades.elementAt(i).grade,
                    student.grades.elementAt(i).dueDate };
            data[i] = obj;
        }
        return new DefaultTableModel(data, columnNames);
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
        lbl_StudentListTitle = new JLabel();
        tbl_StudentList = new JTable();
        pnl_AssignmentListPanel = new JPanel();
        lbl_AssignmentListTitle = new JLabel();
        tbl_AssignmentList = new JTable();

        pnl_StudentDetailsPanel = new JPanel();
        pnl_StudentInfoPanel = new JPanel();
        lbl_StudentInfoTitle = new JLabel();
        lbl_StudentName = new JLabel();
        lbl_StudentEmail = new JLabel();
        lbl_StudentBirthday = new JLabel();
        lbl_StudenGrade = new JLabel();

        pnl_StudentGradesPanel = new JPanel();
        lbl_StudentGradesTitle = new JLabel();
        tbl_StudentGradesTable = new JTable();

        pnl_StudentAdderPanel = new JPanel();
        pnl_StudentEditorPanel = new JPanel();

        pnl_AssignmentDetailsPanel = new JPanel();
        pnl_AssignmentCreatorPanel = new JPanel();
        pnl_AssignmentEditorPanel = new JPanel();
        //
        // COMPONENT PROPERTIES
        //

        // pnl_MainPanel
        pnl_MainPanel.setLayout(new GridBagLayout());

        // pnl_Toolbar
        f_layout.setAlignment(FlowLayout.LEFT);
        pnl_Toolbar.setLayout(f_layout);
        gb_constraints.fill = GridBagConstraints.HORIZONTAL;
        gb_constraints.gridx = 0;
        gb_constraints.gridy = 0;
        gb_constraints.weighty = 0.05;
        gb_constraints.weightx = 1;
        pnl_MainPanel.add(pnl_Toolbar, gb_constraints);

        // pnl_LeftPanel
        pnl_LeftPanel.setLayout(new BoxLayout(pnl_LeftPanel, BoxLayout.PAGE_AXIS));
        gb_constraints.fill = GridBagConstraints.BOTH;
        gb_constraints.gridy = 1;
        gb_constraints.weightx = 0.6;
        gb_constraints.weighty = 0.95;
        pnl_MainPanel.add(pnl_LeftPanel, gb_constraints);

        // pnl_RightPanel
        pnl_RightPanel.setBackground(Color.RED);
        pnl_RightPanel.setLayout(new GridBagLayout());
        gb_constraints.gridx = 1;
        gb_constraints.weightx = 0.4;
        pnl_MainPanel.add(pnl_RightPanel, gb_constraints);

        // btn_StudentList
        btn_StudentList.setText("Students");
        pnl_Toolbar.add(btn_StudentList);

        // btn_AssignmentList
        btn_AssignmentList.setText("Assignments");
        pnl_Toolbar.add(btn_AssignmentList);

        // pnl_StudentListPanel
        pnl_StudentListPanel.setLayout(new BoxLayout(pnl_StudentListPanel, BoxLayout.PAGE_AXIS));
        pnl_LeftPanel.add(pnl_StudentListPanel);

        // lbl_StudentListTitle
        lbl_StudentListTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl_StudentListTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_StudentListTitle.setText("Students");
        pnl_StudentListPanel.add(lbl_StudentListTitle);

        // tbl_StudentList
        tbl_StudentList = makeStudentsTable();
        tbl_StudentList.setAlignmentX(JTable.CENTER_ALIGNMENT);
        pnl_StudentListPanel.add(new JScrollPane(tbl_StudentList));

        // pnl_AssignmentListPanel
        pnl_AssignmentListPanel.setLayout(new BoxLayout(pnl_AssignmentListPanel, BoxLayout.PAGE_AXIS));
        pnl_AssignmentListPanel.setVisible(false);
        pnl_LeftPanel.add(pnl_AssignmentListPanel, gb_constraints);

        // lbl_AssignmentListTitle
        lbl_AssignmentListTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl_AssignmentListTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_AssignmentListTitle.setText("Assignments");
        pnl_AssignmentListPanel.add(lbl_AssignmentListTitle);

        // tbl_AssignmentList
        tbl_AssignmentList = makeAssignmentsTable();
        pnl_AssignmentListPanel.add(new JScrollPane(tbl_AssignmentList));

        // pnl_StudentDetailsPanel
        gb_constraints.gridx = 0;
        gb_constraints.gridy = 0;
        gb_constraints.weightx = 0.5;
        gb_constraints.weighty = 0.5;
        pnl_StudentDetailsPanel.setLayout(new GridBagLayout());
        pnl_RightPanel.add(pnl_StudentDetailsPanel, gb_constraints);

        // pnl_StudentInfoPanel
        pnl_StudentInfoPanel.setLayout(new BoxLayout(pnl_StudentInfoPanel, BoxLayout.PAGE_AXIS));
        gb_constraints.weighty = 0.3;
        pnl_StudentDetailsPanel.add(pnl_StudentInfoPanel, gb_constraints);

        // lbl_StudentInfoTitle
        lbl_StudentInfoTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_StudentInfoTitle.setText("Student Info");
        pnl_StudentInfoPanel.add(lbl_StudentInfoTitle);

        // lbl_StudentName
        lbl_StudentName.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl_StudentName.setText("Select a Student");
        pnl_StudentInfoPanel.add(lbl_StudentName);

        // lbl_StudentEmail
        lbl_StudentEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnl_StudentInfoPanel.add(lbl_StudentEmail);

        // lbl_StudentBirthday
        lbl_StudentBirthday.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnl_StudentInfoPanel.add(lbl_StudentBirthday);

        // lbl_StudenGrade
        lbl_StudenGrade.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnl_StudentInfoPanel.add(lbl_StudenGrade);

        // pnl_StudentGradesPanel
        pnl_StudentGradesPanel.setLayout(new BoxLayout(pnl_StudentGradesPanel, BoxLayout.PAGE_AXIS));
        gb_constraints.gridy = 1;
        gb_constraints.weighty = 0.7;
        pnl_StudentDetailsPanel.add(pnl_StudentGradesPanel, gb_constraints);

        // lbl_StudentGradesTitle
        lbl_StudentGradesTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl_StudentGradesTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_StudentGradesTitle.setText("Student Grades");
        pnl_StudentGradesPanel.add(lbl_StudentGradesTitle);

        // tbl_StudentGradesTable
        tbl_StudentGradesTable = makeStudentAssignmentsTable();
        tbl_StudentGradesTable.setAlignmentX(JTable.CENTER_ALIGNMENT);
        pnl_StudentGradesPanel.add(new JScrollPane(tbl_StudentGradesTable));

        pnl_StudentAdderPanel.setVisible(false);
        pnl_RightPanel.add(pnl_StudentAdderPanel, gb_constraints);

        pnl_StudentEditorPanel.setVisible(false);
        pnl_RightPanel.add(pnl_StudentEditorPanel, gb_constraints);

        pnl_AssignmentDetailsPanel.setVisible(false);
        pnl_RightPanel.add(pnl_AssignmentDetailsPanel, gb_constraints);

        pnl_AssignmentCreatorPanel.setVisible(false);
        pnl_RightPanel.add(pnl_AssignmentCreatorPanel, gb_constraints);

        pnl_AssignmentEditorPanel.setVisible(false);
        pnl_RightPanel.add(pnl_AssignmentEditorPanel, gb_constraints);
        //
        // SHOW APPLICATION
        //
        frame.getContentPane().add(pnl_MainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.createUI();
                app.createEvents();
            }
        });
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
    private JLabel lbl_StudentListTitle;
    private JTable tbl_StudentList;

    private JPanel pnl_AssignmentListPanel;
    private JLabel lbl_AssignmentListTitle;
    private JTable tbl_AssignmentList;

    private JPanel pnl_StudentDetailsPanel;
    private JPanel pnl_StudentInfoPanel;
    private JLabel lbl_StudentInfoTitle;
    private JLabel lbl_StudentName;
    private JLabel lbl_StudentEmail;
    private JLabel lbl_StudentBirthday;
    private JLabel lbl_StudenGrade;

    private JPanel pnl_StudentGradesPanel;
    private JLabel lbl_StudentGradesTitle;
    private JTable tbl_StudentGradesTable;

    private JPanel pnl_StudentAdderPanel;
    private JPanel pnl_StudentEditorPanel;

    private JPanel pnl_AssignmentDetailsPanel;
    private JPanel pnl_AssignmentCreatorPanel;
    private JPanel pnl_AssignmentEditorPanel;
}
