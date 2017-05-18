package edu.orangecoastcollege.cs272.taskr.model.manager;

/**
 * The <code>SubtaskedAssignment</code> class represents an assignment with a deadline and with
 * possible subtasks attached to it.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-02
 */

public abstract class SubtaskedAssignment extends Assignment
{
    /**
     * Represents whether or not the assignment has subtasks.
     */
    protected boolean mSubtasks;

    /**
     * Creates a new <code>SubtaskedAssignment</code> object.
     * @param id The ID of the assignment.
     * @param name The name of the assignment.
     * @param description The description of the assignment.
     * @param dueDate The due date of the assignment.
     * @param subtasks The truth value of whether the assignment has subtasks.
     */
    protected SubtaskedAssignment(int id, String name, String description, String dueDate,
                                  boolean subtasks)
    {
        super(id, name, description, dueDate);
        setSubtasks(subtasks);
    }

    /**
     * Gets the truth value of whether the assignment has subtasks.
     * @return The truth value of whether the assignment has subtasks.
     */
    public boolean hasSubtasks()
    {
        return mSubtasks;
    }
    /**
     * Sets the truth value of whether the assignment has subtasks.
     * @param subtasks The truth value of whether the assignment has subtasks.
     */
    public void setSubtasks(boolean subtasks)
    {
        mSubtasks = subtasks;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SubtaskedAssignment that = (SubtaskedAssignment) o;

        return mSubtasks == that.mSubtasks;

    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (mSubtasks ? 1 : 0);
        return result;
    }
}
