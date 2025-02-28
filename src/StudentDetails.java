import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class StudentDetails {
    /*
     * Name: displayStudentInfo();
     * Input: The student to display;
     * Output: None;
     * Purpose: Display the information of the given student on
     * pnl_StudentDetailsPanel;
     */
    public static void displayStudentInfo(Student student) {
        lbl_StudentName.setText(student.firstName + " " + student.lastName);
        lbl_StudentEmail.setText("Email: " + student.email);
        lbl_StudentBirthday.setText("Date of Birth: "
                + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(student.birthday));
        lbl_StudenGrade.setText("Grade: " + String.valueOf(student.overallGrade));

        btn_EditStudent.removeActionListener(btn_EditStudent.getActionListeners()[0]);
        btn_EditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //displayStudentEditor(student);
            }
        });

        tbl_StudentGradesTable.setModel(updateStudentAssignmentsTable(student));
        tbl_StudentGradesTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                App app = new App();
                Database db = new Database();
                if (evt.getColumn() != 1) {
                    return;
                }
                student.grades.elementAt(tbl_StudentGradesTable.getSelectedRow()).grade = Integer
                        .valueOf(tbl_StudentGradesTable.getValueAt(tbl_StudentGradesTable.getSelectedRow(), 1) + "");
                student.overallGrade = db.calculateStudentOverallGrade(student);
                db.updateStudent(student);

                app.updateStudentsTable();
                displayStudentInfo(student);
            }
        });

        app.displayRightPanel("pnl_StudentDetailsPanel");
    }

    /*
     * Name: makeStudentAssignmentsTable();
     * Input: None;
     * Output: A template for tbl_StudentGradesTable;
     * Purpose: Creates the columns and size the columns for tbl_StudentGradesTable;
     */
    private static JTable makeStudentAssignmentsTable() {
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
    private static TableModel updateStudentAssignmentsTable(Student student) {
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
     * Name: createStudentDetailsPanel();
     * Input: None;
     * Output: Creates pnl_StudentDetailsPanel;
     * Purpose: Creates createStudentDetailsPanel, it's components, and returns it;
     */
    public static JPanel createStudentDetailsPanel() {
        //
        // INITIALIZE COMPONENTS
        //
        pnl_StudentDetailsPanel = new JPanel();
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
        //
        // COMPONENT PROPERTIES
        //

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
        btn_EditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
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

        return pnl_StudentDetailsPanel;
    }

    private static App app = new App();
    private static GridBagConstraints gb_constraints = new GridBagConstraints();

    private static JPanel pnl_StudentDetailsPanel;
    private static JPanel pnl_StudentInfoPanel;
    private static JLabel lbl_StudentInfoTitle;
    private static JPanel pnl_StudentInfoSubPanel;
    private static JLabel lbl_StudentName;
    private static JLabel lbl_StudentEmail;
    private static JLabel lbl_StudentBirthday;
    private static JLabel lbl_StudenGrade;
    private static JButton btn_EditStudent;
    private static JPanel pnl_StudentGradesPanel;
    private static JLabel lbl_StudentGradesTitle;
    private static JTable tbl_StudentGradesTable;
}
