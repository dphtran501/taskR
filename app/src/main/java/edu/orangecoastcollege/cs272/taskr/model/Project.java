package edu.orangecoastcollege.cs272.taskr.model;

/**
 * The <code>Project</code> class represents a project with a deadline and with possible subtasks
 * attached to it.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-02
 */
public class Project extends SubtaskedAssignment
{
    /**
     * Creates a new <code>Project</code> object.
     * @param id The ID of the project.
     * @param name The name of the project.
     * @param description The description of the project.
     * @param dueDate The due date of the project.
     * @param subtasks The truth value of whether or not the project has subtasks.
     */
    public Project(int id, String name, String description, String dueDate, boolean subtasks)
    {
        super(id, name, description, dueDate, subtasks);
    }

    @Override
    public String toString()
    {
        return "Project{}";
    }
}
