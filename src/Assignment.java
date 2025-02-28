import java.sql.Date;

public class Assignment {
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
