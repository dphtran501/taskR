package edu.orangecoastcollege.cs272.taskr.model;

/**
 * The <code>Subtask</code> class represents a subtask of an assignment.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-02
 */
public class Subtask extends Assignment
{
    /**
     * Creates a new <code>Subtask</code> object.
     * @param id The id of the subtask.
     * @param name The name of the subtask.
     * @param description The description of the subtask.
     * @param dueDate The due date of the subtask.
     */
    public Subtask(int id, String name, String description, String dueDate)
    {
        super(id, name, description, dueDate);
    }

}
