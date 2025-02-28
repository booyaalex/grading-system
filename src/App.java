import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    /*
     * Name: createEvents();
     * Input: None;
     * Output: None;
     * Purpose: Creates the functionality for the static components;
     */
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
        btn_EditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btn_StudentEditorUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btn_StudentEditorCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    /*
     * Name: displayRightPanel();
     * Input: A JPanel on pnl_RightPanel to make visible;
     * Output: None;
     * Purpose: Make the selected selected JPanel visible, while hiding the others
     * on pnl_RightPanel;
     */
    private void displayRightPanel(JPanel selectedPanel) {
        JPanel[] rightPanels = { pnl_StudentDetailsPanel, pnl_StudentAdderPanel, pnl_StudentEditorPanel,
                pnl_AssignmentDetailsPanel, pnl_AssignmentCreatorPanel, pnl_AssignmentEditorPanel };
        for (JPanel panel : rightPanels) {
            if (panel == selectedPanel) {
                panel.setVisible(true);
            } else {
                panel.setVisible(false);
            }
        }
    }

    /*
     * Name: displayStudentInfo();
     * Input: The index of the selected row on tbl_StudentList;
     * Output: None;
     * Purpose: Display the information of the selected student from tbl_StudentList
     * on pnl_StudentDetailsPanel;
     */
    private void displayStudentInfo(int selectedRowIndex) {
        Database db = new Database();

        Student student = db.getStudent(tbl_StudentList.getValueAt(selectedRowIndex, 0) + " "
                + tbl_StudentList.getValueAt(selectedRowIndex, 1));
        lbl_StudentName.setText(student.firstName + " " + student.lastName);
        lbl_StudentEmail.setText("Email: " + student.email);
        lbl_StudentBirthday.setText("Date of Birth: "
                + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(student.birthday));
        lbl_StudenGrade.setText("Grade: " + String.valueOf(student.overallGrade));

        btn_EditStudent.removeActionListener(btn_EditStudent.getActionListeners()[0]);
        btn_EditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudentEditor(student);
            }
        });

        tbl_StudentGradesTable.setModel(updateStudentAssignmentsTable(student));
        tbl_StudentGradesTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                if (evt.getColumn() != 1) {
                    return;
                }
                student.grades.elementAt(tbl_StudentGradesTable.getSelectedRow()).grade = Integer
                        .valueOf(tbl_StudentGradesTable.getValueAt(tbl_StudentGradesTable.getSelectedRow(), 1) + "");
                student.overallGrade = db.calculateStudentOverallGrade(student);
                db.updateStudent(student);

                updateStudentsTable();
                displayStudentInfo(student);
            }
        });

        displayRightPanel(pnl_StudentDetailsPanel);
    }

    /*
     * Name: displayStudentInfo();
     * Input: The student to display;
     * Output: None;
     * Purpose: Display the information of the given student on
     * pnl_StudentDetailsPanel;
     */
    private void displayStudentInfo(Student student) {
        lbl_StudentName.setText(student.firstName + " " + student.lastName);
        lbl_StudentEmail.setText("Email: " + student.email);
        lbl_StudentBirthday.setText("Date of Birth: "
                + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(student.birthday));
        lbl_StudenGrade.setText("Grade: " + String.valueOf(student.overallGrade));

        btn_EditStudent.removeActionListener(btn_EditStudent.getActionListeners()[0]);
        btn_EditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudentEditor(student);
            }
        });

        tbl_StudentGradesTable.setModel(updateStudentAssignmentsTable(student));
        tbl_StudentGradesTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                Database db = new Database();
                if (evt.getColumn() != 1) {
                    return;
                }
                student.grades.elementAt(tbl_StudentGradesTable.getSelectedRow()).grade = Integer
                        .valueOf(tbl_StudentGradesTable.getValueAt(tbl_StudentGradesTable.getSelectedRow(), 1) + "");
                student.overallGrade = db.calculateStudentOverallGrade(student);
                db.updateStudent(student);

                updateStudentsTable();
                displayStudentInfo(student);
            }
        });

        displayRightPanel(pnl_StudentDetailsPanel);
    }

    /*
     * Name: displayStudentEditor();
     * Input: The student to be displayed;
     * Output: None;
     * Purpose: Takes the students values, sets the input fields on
     * pnl_StudentEditorFields, and displays pnl_StudentEditorPanel;
     */
    private void displayStudentEditor(Student student) {
        txf_StudentEditorFirstNameTextField.setText(student.firstName);
        txf_StudentEditorLastNameTextField.setText(student.lastName);
        txf_StudentEditorEmailTextField.setText(student.email);
        ftf_StudentEditorBirthdayTextField.setValue(student.birthday);

        btn_StudentEditorUpdateButton.removeActionListener(btn_StudentEditorUpdateButton.getActionListeners()[0]);
        btn_StudentEditorUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                String emailFormat = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                student.firstName = txf_StudentEditorFirstNameTextField.getText();
                student.lastName = txf_StudentEditorLastNameTextField.getText();
                if (!txf_StudentEditorEmailTextField.getText().matches(emailFormat)) { 
                    return; //Add Error Message 
                }
                student.email = txf_StudentEditorEmailTextField.getText();
                student.birthday = Date.valueOf(ftf_StudentEditorBirthdayTextField.getValue().toString());

                db.updateStudent(student);
                updateStudentsTable();
                displayStudentInfo(student);
            }
        });

        btn_StudentEditorCancelButton.removeActionListener(btn_StudentEditorCancelButton.getActionListeners()[0]);
        btn_StudentEditorCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudentInfo(student);
            }
        });

        displayRightPanel(pnl_StudentEditorPanel);
    }

    /*
     * Name: makeStudentsTable();
     * Input: None;
     * Output: A template for tbl_StudentList;
     * Purpose: Creates the columns for tbl_StudentList;
     */
    private JTable makeStudentsTable() {
        String[] columnNames = { "First Name", "Last Name", "Email", "Grade" };
        Object[][] data = new Object[0][4];
        return new JTable(data, columnNames);
    }

    /*
     * Name: updateStudentsTable();
     * Input: None;
     * Output: None;
     * Purpose: Updates the data on tbl_StudentList and resizes the columns;
     */
    private void updateStudentsTable() {
        Database db = new Database();

        String[] columnNames = { "First Name", "Last Name", "Email", "Grade" };
        Vector<Student> students = db.getStudents();
        Object[][] data = new Object[students.size()][4];
        for (int i = 0; i < students.size(); i++) {
            Object[] obj = { students.elementAt(i).firstName, students.elementAt(i).lastName,
                    students.elementAt(i).email, students.elementAt(i).overallGrade };
            data[i] = obj;
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tbl_StudentList.setModel(model);

        TableColumnModel columnModel = tbl_StudentList.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(25);
    }

    /*
     * Name: makeAssignmentsTable();
     * Input: None;
     * Output: A template for tbl_AssignmentList;
     * Purpose: Creates the columns for tbl_AssignmentList;
     */
    private JTable makeAssignmentsTable() {
        String[] columnNames = { "Name", "Due Date" };
        Object[][] data = new Object[0][2];
        return new JTable(data, columnNames);
    }

    /*
     * Name: updateAssignmentsTable();
     * Input: None;
     * Output: None;
     * Purpose: Updates the data on tbl_AssignmentList and resizes the columns;
     */
    private void updateAssignmentsTable() {
        Database db = new Database();

        String[] columnNames = { "Name", "Due Date" };
        Vector<Assignment> assignments = db.getAssignments();
        Object[][] data = new Object[assignments.size()][2];
        for (int i = 0; i < assignments.size(); i++) {
            Object[] obj = { assignments.elementAt(i).name, assignments.elementAt(i).dueDate };
            data[i] = obj;
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tbl_AssignmentList.setModel(model);

        TableColumnModel columnModel = tbl_AssignmentList.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(50);
    }

    /*
     * Name: makeStudentAssignmentsTable();
     * Input: None;
     * Output: A template for tbl_StudentGradesTable;
     * Purpose: Creates the columns and size the columns for tbl_StudentGradesTable;
     */
    private JTable makeStudentAssignmentsTable() {
        String[] columnNames = { "Name", "Grade", "Due Date" };
        Object[][] data = new Object[0][3];
        return new JTable(data, columnNames);
    }

    /*
     * Name: updateStudentAssignmentsTable();
     * Input: None;
     * Output: Data for tbl_StudentGradesTable;
     * Purpose: Updates the data on tbl_StudentGradesTable;
     */
    private TableModel updateStudentAssignmentsTable(Student student) {
        String[] columnNames = { "Name", "Grade", "Due Date" };
        Object[][] data = new Object[student.grades.size()][3];
        for (int i = 0; i < student.grades.size(); i++) {
            Object[] obj = { student.grades.elementAt(i).name, student.grades.elementAt(i).grade,
                    student.grades.elementAt(i).dueDate };
            data[i] = obj;
        }
        return new DefaultTableModel(data, columnNames);
    }

    /*
     * Name: createUI();
     * Input: None;
     * Output: None;
     * Purpose: Creates all of the GUI components and sets their values;
     */
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
        pnl_StudentAdderPanel = new JPanel();
        pnl_StudentEditorPanel = new JPanel();
        pnl_AssignmentDetailsPanel = new JPanel();
        pnl_AssignmentCreatorPanel = new JPanel();
        pnl_AssignmentEditorPanel = new JPanel();

        pnl_StudentInfoPanel = new JPanel();
        lbl_StudentInfoTitle = new JLabel();
        pnl_StudentInfoSubPanel = new JPanel();
        lbl_StudentName = new JLabel();
        lbl_StudentEmail = new JLabel();
        lbl_StudentBirthday = new JLabel();
        lbl_StudenGrade = new JLabel();
        btn_EditStudent = new JButton();

        pnl_StudentGradesPanel = new JPanel();
        lbl_StudentGradesTitle = new JLabel();
        tbl_StudentGradesTable = new JTable();

        lbl_StudentEditorTitle = new JLabel();
        pnl_StudentEditorFields = new JPanel();
        lbl_StudentEditorFirstNameLabel = new JLabel();
        txf_StudentEditorFirstNameTextField = new JFormattedTextField();
        lbl_StudentEditorLastNameLabel = new JLabel();
        txf_StudentEditorLastNameTextField = new JFormattedTextField();
        lbl_StudentEditorEmailLabel = new JLabel();
        txf_StudentEditorEmailTextField = new JFormattedTextField();
        lbl_StudentEditorBirthdayLabel = new JLabel();
        ftf_StudentEditorBirthdayTextField = new JFormattedTextField();
        btn_StudentEditorUpdateButton = new JButton();
        btn_StudentEditorCancelButton = new JButton();
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
        updateStudentsTable();
        tbl_StudentList.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tbl_StudentList.setRowHeight(24);
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
        updateAssignmentsTable();
        tbl_AssignmentList.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tbl_AssignmentList.setRowHeight(24);
        pnl_AssignmentListPanel.add(new JScrollPane(tbl_AssignmentList));

        // pnl_StudentDetailsPanel
        gb_constraints.gridx = 0;
        gb_constraints.gridy = 0;
        gb_constraints.weightx = 0.5;
        gb_constraints.weighty = 0.5;
        pnl_StudentDetailsPanel.setLayout(new GridBagLayout());
        pnl_RightPanel.add(pnl_StudentDetailsPanel, gb_constraints);

        // pnl_StudentAdderPanel
        pnl_StudentAdderPanel.setVisible(false);
        pnl_RightPanel.add(pnl_StudentAdderPanel, gb_constraints);

        // pnl_StudentEditorPanel
        pnl_StudentEditorPanel.setLayout(new BoxLayout(pnl_StudentEditorPanel, BoxLayout.PAGE_AXIS));
        pnl_StudentEditorPanel.setVisible(false);
        pnl_RightPanel.add(pnl_StudentEditorPanel, gb_constraints);

        // pnl_AssignmentDetailsPanel
        pnl_AssignmentDetailsPanel.setVisible(false);
        pnl_RightPanel.add(pnl_AssignmentDetailsPanel, gb_constraints);

        // pnl_AssignmentCreatorPanel
        pnl_AssignmentCreatorPanel.setVisible(false);
        pnl_RightPanel.add(pnl_AssignmentCreatorPanel, gb_constraints);

        // pnl_AssignmentEditorPanel
        pnl_AssignmentEditorPanel.setVisible(false);
        pnl_RightPanel.add(pnl_AssignmentEditorPanel, gb_constraints);

        // pnl_StudentInfoPanel
        pnl_StudentInfoPanel.setLayout(new BoxLayout(pnl_StudentInfoPanel, BoxLayout.PAGE_AXIS));
        gb_constraints.insets = new Insets(0, 0, 25, 0);
        gb_constraints.weighty = 0.3;
        pnl_StudentDetailsPanel.add(pnl_StudentInfoPanel, gb_constraints);

        // lbl_StudentInfoTitle
        lbl_StudentInfoTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl_StudentInfoTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_StudentInfoTitle.setText("Student Info");
        pnl_StudentInfoPanel.add(lbl_StudentInfoTitle);

        // pnl_StudentInfoSubPanel
        pnl_StudentInfoSubPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        pnl_StudentInfoSubPanel.setMaximumSize(pnl_StudentDetailsPanel.getMaximumSize());
        pnl_StudentInfoSubPanel.setLayout(new BoxLayout(pnl_StudentInfoSubPanel, BoxLayout.PAGE_AXIS));
        pnl_StudentInfoPanel.add(pnl_StudentInfoSubPanel);

        // lbl_StudentName
        lbl_StudentName.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl_StudentName.setText("Select a Student");
        pnl_StudentInfoSubPanel.add(lbl_StudentName);

        // lbl_StudentEmail
        lbl_StudentEmail.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pnl_StudentInfoSubPanel.add(lbl_StudentEmail);

        // lbl_StudentBirthday
        lbl_StudentBirthday.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pnl_StudentInfoSubPanel.add(lbl_StudentBirthday);

        // lbl_StudenGrade
        lbl_StudenGrade.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pnl_StudentInfoSubPanel.add(lbl_StudenGrade);

        // btn_EditStudent
        btn_EditStudent.setAlignmentX(JButton.CENTER_ALIGNMENT);
        btn_EditStudent.setText("Edit Student");
        pnl_StudentInfoPanel.add(btn_EditStudent);

        // Blank Space
        pnl_StudentDetailsPanel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 100)));

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

        // lbl_StudentEditorTitle
        lbl_StudentEditorTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lbl_StudentEditorTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_StudentEditorTitle.setText("Edit Student");
        pnl_StudentEditorPanel.add(lbl_StudentEditorTitle);

        // pnl_StudentEditorFields
        pnl_StudentEditorFields.setLayout(new GridBagLayout());
        pnl_StudentEditorPanel.add(pnl_StudentEditorFields);

        // lbl_StudentEditorFirstNameLabel
        lbl_StudentEditorFirstNameLabel.setText("First Name");
        gb_constraints.fill = GridBagConstraints.HORIZONTAL;
        gb_constraints.gridx = 0;
        gb_constraints.gridy = 0;
        gb_constraints.insets = new Insets(0, 10, 0, 10);
        gb_constraints.weighty = 0.5;
        pnl_StudentEditorFields.add(lbl_StudentEditorFirstNameLabel, gb_constraints);

        // txf_StudentEditorFirstNameTextField
        txf_StudentEditorFirstNameTextField.setColumns(10);
        gb_constraints.gridx = 1;
        pnl_StudentEditorFields.add(txf_StudentEditorFirstNameTextField, gb_constraints);

        // lbl_StudentEditorLastNameLabel
        lbl_StudentEditorLastNameLabel.setText("Last Name");
        gb_constraints.gridx = 0;
        gb_constraints.gridy = 1;
        pnl_StudentEditorFields.add(lbl_StudentEditorLastNameLabel, gb_constraints);

        // txf_StudentEditorLastNameTextField
        txf_StudentEditorLastNameTextField.setColumns(10);
        gb_constraints.gridx = 1;
        pnl_StudentEditorFields.add(txf_StudentEditorLastNameTextField, gb_constraints);

        // lbl_StudentEditorEmailLabel
        lbl_StudentEditorEmailLabel.setText("Email");
        gb_constraints.gridx = 2;
        gb_constraints.gridy = 0;
        pnl_StudentEditorFields.add(lbl_StudentEditorEmailLabel, gb_constraints);

        // txf_StudentEditorEmailTextField
        txf_StudentEditorEmailTextField.setColumns(10);
        gb_constraints.gridx = 3;
        pnl_StudentEditorFields.add(txf_StudentEditorEmailTextField, gb_constraints);

        // lbl_StudentEditorBirthdayLabel
        lbl_StudentEditorBirthdayLabel.setText("Birthday");
        gb_constraints.gridx = 2;
        gb_constraints.gridy = 1;
        pnl_StudentEditorFields.add(lbl_StudentEditorBirthdayLabel, gb_constraints);

        // ftf_StudentEditorBirthdayTextField
        ftf_StudentEditorBirthdayTextField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));
        ftf_StudentEditorBirthdayTextField.setColumns(10);
        gb_constraints.gridx = 3;
        pnl_StudentEditorFields.add(ftf_StudentEditorBirthdayTextField, gb_constraints);

        // btn_StudentEditorUpdateButton
        btn_StudentEditorUpdateButton.setText("Update");
        pnl_StudentEditorPanel.add(btn_StudentEditorUpdateButton);

        // btn_StudentEditorCancelButton
        btn_StudentEditorCancelButton.setText("Cancel");
        pnl_StudentEditorPanel.add(btn_StudentEditorCancelButton);
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
    private JPanel pnl_StudentAdderPanel;
    private JPanel pnl_StudentEditorPanel;
    private JPanel pnl_AssignmentDetailsPanel;
    private JPanel pnl_AssignmentCreatorPanel;
    private JPanel pnl_AssignmentEditorPanel;

    private JPanel pnl_StudentInfoPanel;
    private JLabel lbl_StudentInfoTitle;
    private JPanel pnl_StudentInfoSubPanel;
    private JLabel lbl_StudentName;
    private JLabel lbl_StudentEmail;
    private JLabel lbl_StudentBirthday;
    private JLabel lbl_StudenGrade;
    private JButton btn_EditStudent;

    private JLabel lbl_StudentEditorTitle;
    private JPanel pnl_StudentEditorFields;
    private JLabel lbl_StudentEditorFirstNameLabel;
    private JTextField txf_StudentEditorFirstNameTextField;
    private JLabel lbl_StudentEditorLastNameLabel;
    private JTextField txf_StudentEditorLastNameTextField;
    private JLabel lbl_StudentEditorEmailLabel;
    private JTextField txf_StudentEditorEmailTextField;
    private JLabel lbl_StudentEditorBirthdayLabel;
    private JFormattedTextField ftf_StudentEditorBirthdayTextField;
    private JButton btn_StudentEditorUpdateButton;
    private JButton btn_StudentEditorCancelButton;

    private JPanel pnl_StudentGradesPanel;
    private JLabel lbl_StudentGradesTitle;
    private JTable tbl_StudentGradesTable;
}
