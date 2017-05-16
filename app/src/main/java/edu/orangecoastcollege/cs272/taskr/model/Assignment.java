package edu.orangecoastcollege.cs272.taskr.model;

/**
 * The <code>Assignment</code> class represents an assignment with a due date.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-02
 */
public abstract class Assignment
{
    /**
     * Represents the ID of the assignment.
     */
    protected int mID;
    /**
     * Represents the name of the assignment.
     */
    protected String mName;
    /**
     * Represents the description of the assignment.
     */
    protected String mDescription;
    /**
     * Represents the due date of the assignment.
     */
    protected String mDueDate;

    /**
     * Creates a new <code>Assignment</code> object.
     * @param id          The ID of the assignment.
     * @param name        The name of the assignment.
     * @param description The description of the assignment.
     * @param dueDate     The due date of the assignment.
     */
    protected Assignment(int id, String name, String description, String dueDate)
    {
        mID = id;
        setName(name);
        setDescription(description);
        setDueDate(dueDate);
    }

    /**
     * Gets the ID of the assignment.
     * @return The ID of the assignment.
     */
    public int getID()
    {
        return mID;
    }

    /**
     * Gets the name of the assignment.
     * @return The name of the assignment.
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Sets the name of the assignment.
     * @param name The name of the assignment.
     */
    public void setName(String name)
    {
        mName = name;
    }

    /**
     * Gets the description of the assignment.
     * @return The description of the assignment.
     */
    public String getDescription()
    {
        return mDescription;
    }

    /**
     * Sets the description of the assignment.
     * @param description The description of the assignment.
     */
    public void setDescription(String description)
    {
        mDescription = description;
    }

    /**
     * Gets the due date of the assignment.
     * @return The due date of the assignment.
     */
    public String getDueDate()
    {
        return mDueDate;
    }

    /**
     * Sets the due date of the assignment.
     * @param dueDate The due date of the assignment.
     */
    public void setDueDate(String dueDate)
    {
        mDueDate = dueDate;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if (mID != that.mID) return false;
        if (mName != null ? !mName.equals(that.mName) : that.mName != null) return false;
        if (mDescription != null ? !mDescription.equals(that.mDescription) : that.mDescription != null)
            return false;
        return mDueDate != null ? mDueDate.equals(that.mDueDate) : that.mDueDate == null;

    }

    @Override
    public int hashCode()
    {
        int result = mID;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mDueDate != null ? mDueDate.hashCode() : 0);
        return result;
    }
}