package edu.orangecoastcollege.cs272.taskr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * /**
 * <code>DBHelper</code> represents the database model for a table containing one primary key
 * and one or more fields.  It provides mechanisms by which new records can be created and
 * existing ones can be updated or deleted. <code>DBHelper</code> also provides functionality
 * to query the database table for a single record, all records or the total count of records.
 *
 * @author	Derek Tran
 * @version	2.0
 * @since 	2017-05-15
 */
public class DBHelper extends SQLiteOpenHelper
{
    private Context mContext;

    // Database
    public static final String DATABASE_NAME = "capstone";
    private static final int DATABASE_VERSION = 1;

    // ************************************ DATABASE TABLES ****************************************
    // Project Table
    private static final String PROJECTS_TABLE_NAME = "projects";
    private static final String[] PROJECTS_FIELD_NAMES = {"proj_id", "name", "description", "due_date", "subtasks"};
    private static final String[] PROJECTS_FIELD_TYPES = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "TEXT", "TEXT", "INTEGER"};
    // Subtask Table
    private static final String SUBTASKS_TABLE_NAME = "subtasks";
    private static final String[] SUBTASKS_FIELD_NAMES = {"sub_id", "name", "description", "due_date"};
    private static final String[] SUBTASKS_FIELD_TYPES = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "TEXT", "TEXT"};
    // Project-Subtask Relationship Table
    private static final String PROJ_SUB_TABLE_NAME = "project_subtask";
    private static final String[] PROJ_SUB_FIELD_NAMES = {"proj_id", "sub_id"};
    private static final String[] PROJ_SUB_FIELD_TYPES = {"INTEGER", "INTEGER"};
    // TODO: add rest of database tables

    //**********************************************************************************************

    /**
     * Creates a new <code>DBHelper</code> using the given context.
     * @param context The context of the current state of the application/object.
     */
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createTable(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES, PROJECTS_FIELD_TYPES));
        db.execSQL(createTable(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES, SUBTASKS_FIELD_TYPES));
        db.execSQL(createTable(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES, PROJ_SUB_FIELD_TYPES));
        // TODO: repeat for rest of database tables
    }

    /**
     * Creates SQL statement used to create database table only if it does not already exist.
     * @param tableName Name of the database table to create.
     * @param fieldNames Field names of the database table to create.
     * @param fieldTypes Field types of the database table to create.
     * @return String representation of the SQL statement used to create the database table if it
     * doesn't already exist.
     */
    private String createTable(String tableName, String[] fieldNames, String[] fieldTypes)
    {
        StringBuilder createSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createSQL.append(tableName).append("(");
        for (int i = 0; i < fieldNames.length; i++)
            createSQL.append(fieldNames[i]).append(" ").append(fieldTypes[i])
                    .append((i < fieldNames.length - 1) ? "," : ")");
        return createSQL.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SUBTASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROJ_SUB_TABLE_NAME);
        // TODO: repeat for rest of database tables
        onCreate(db);
    }

    //*********************************** PROJECT METHODS ******************************************

    /**
     * Returns a list of all <code>Project</code>s in the database.
     * @return List of all <code>Project</code>s in the database.
     */
    public ArrayList<Project> getAllProjects()
    {
        ArrayList<Project> projectsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES, null, null, null, null, null, null);

        if (c.moveToFirst())
        {
            do
            {
                int id = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[0]));
                String name = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[1]));
                String description = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[2]));
                String dueDate = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[3]));
                boolean hasSubtasks = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[4])) == 1;
                projectsList.add(new Project(id, name, description, dueDate, hasSubtasks));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return projectsList;
    }

    /**
     * Returns a <code>Project</code> with the given primary key id.
     * @param key Project's primary key id in the database
     * @return <code>Project</code> with the given primary key id.
     */
    public Project getProject(int key)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES,
                PROJECTS_FIELD_NAMES[0] + "=?", new String[] {String.valueOf(key)},
                null, null, null, null);

        if (c != null)
            c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[0]));
        String name = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[1]));
        String description = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[2]));
        String dueDate = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[3]));
        boolean hasSubtasks = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[4])) == 1;

        c.close();
        db.close();
        return new Project(id, name, description, dueDate, hasSubtasks);
    }

    /**
     * Adds a <code>Project</code> to the database using the non-primary key fields of a
     * given <code>Project</code>.
     * @param p <code>Project</code> whose non-primary key fields are used to create a project
     *          record in the database.
     * @return The auto-generated primary key of the newly added project record.
     */
    public int addProject(Project p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PROJECTS_FIELD_NAMES[1], p.getName());
        values.put(PROJECTS_FIELD_NAMES[2], p.getDescription());
        values.put(PROJECTS_FIELD_NAMES[3], p.getDueDate());
        values.put(PROJECTS_FIELD_NAMES[4], p.hasSubtasks() ? "1" : "0");

        db.insert(PROJECTS_TABLE_NAME, null, values);

        // Retrieve autogenerated primary key
        String selectLastSQL = "SELECT MAX(" + PROJECTS_FIELD_NAMES[0] + ") FROM " + PROJECTS_TABLE_NAME;
        Cursor c = db.rawQuery(selectLastSQL, null);
        if (c != null) c.moveToFirst();
        int key = c.getInt(0);

        c.close();
        db.close();

        return key;
    }

    /**
     * Updates a project record specified by a given <code>Project</code>
     * @param p <code>Project</code> whose record needs to be updated.
     */
    public void updateProject(Project p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PROJECTS_FIELD_NAMES[1], p.getName());
        values.put(PROJECTS_FIELD_NAMES[2], p.getDescription());
        values.put(PROJECTS_FIELD_NAMES[3], p.getDueDate());
        values.put(PROJECTS_FIELD_NAMES[4], p.hasSubtasks() ? "1" : "0");

        db.update(PROJECTS_TABLE_NAME, values, PROJECTS_FIELD_NAMES[0] + "=?",
                new String[]{String.valueOf(p.getID())});

        db.close();
    }

    /**
     * Deletes all projects in the database.
     */
    public void deleteAllProjects()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PROJECTS_TABLE_NAME, null, null);
        db.close();
    }

    /**
     * Deletes a project record specified by a specified <code>Project</code>.
     * @param p <code>Project</code> whose record will be deleted from the database.
     */
    public void deleteProject(Project p)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())});
        db.close();
    }

    /**
     * Returns the number of project records in the database.
     * @return Number of project records in the database.
     */
    public int getProjectCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES, null, null, null, null, null, null);

        int count = c.getCount();

        c.close();
        db.close();
        return count;
    }

    //*********************************** SUBTASK METHODS ******************************************
    /**
     * Returns a list of all <code>Subtask</code>s in the database.
     * @return List of all <code>Subtask</code>s in the database.
     */
    public ArrayList<Subtask> getAllSubtasks()
    {
        ArrayList<Subtask> subtasksList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES, null, null, null, null, null, null);

        if (c.moveToFirst())
        {
            do
            {
                int id = c.getInt(c.getColumnIndex(SUBTASKS_FIELD_NAMES[0]));
                String name = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[1]));
                String description = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[2]));
                String dueDate = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[3]));
                subtasksList.add(new Subtask(id, name, description, dueDate));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return subtasksList;
    }

    /**
     * Returns a <code>Subtask</code> with the given primary key id.
     * @param key Subtask's primary key id in the database
     * @return <code>Subtask</code> with the given primary key id.
     */
    public Subtask getSubtask(int key)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES,
                SUBTASKS_FIELD_NAMES[0] + "=?", new String[] {String.valueOf(key)},
                null, null, null, null);

        if (c != null)
            c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[0]));
        String name = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[1]));
        String description = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[2]));
        String dueDate = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[3]));

        c.close();
        db.close();
        return new Subtask(id, name, description, dueDate);
    }

    /**
     * Adds a <code>Subtask</code> to the database using the non-primary key fields of a
     * given <code>Subtask</code>.
     * @param s <code>Subtask</code> whose non-primary key fields are used to create a subtask
     *          record in the database.
     * @return The auto-generated primary key of the newly added subtask record.
     */
    public int addSubtask(Subtask s)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SUBTASKS_FIELD_NAMES[1], s.getName());
        values.put(SUBTASKS_FIELD_NAMES[2], s.getDescription());
        values.put(SUBTASKS_FIELD_NAMES[3], s.getDueDate());

        db.insert(SUBTASKS_TABLE_NAME, null, values);

        // Retrieve autogenerated primary key
        String selectLastSQL = "SELECT MAX(" + SUBTASKS_FIELD_NAMES[0] + ") FROM " + SUBTASKS_TABLE_NAME;
        Cursor c = db.rawQuery(selectLastSQL, null);
        if (c != null) c.moveToFirst();
        int key = c.getInt(0);

        c.close();
        db.close();

        return key;
    }

    /**
     * Updates a subtask record specified by a given <code>Subtask</code>
     * @param s <code>Subtask</code> whose record needs to be updated.
     */
    public void updateSubtask(Subtask s)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SUBTASKS_FIELD_NAMES[1], s.getName());
        values.put(SUBTASKS_FIELD_NAMES[2], s.getDescription());
        values.put(SUBTASKS_FIELD_NAMES[3], s.getDueDate());

        db.update(SUBTASKS_TABLE_NAME, values, SUBTASKS_FIELD_NAMES[0] + "=?",
                new String[]{String.valueOf(s.getID())});

        db.close();
    }

    /**
     * Deletes all subtasks in the database.
     */
    public void deleteAllSubtasks()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SUBTASKS_TABLE_NAME, null, null);
        db.close();
    }

    /**
     * Deletes a subtask record specified by a specified <code>Subtask</code>.
     * @param s <code>Subtask</code> whose record will be deleted from the database.
     */
    public void deleteSubtask(Subtask s)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(s.getID())});
        db.close();
    }

    /**
     * Returns the number of subtask records in the database.
     * @return Number of subtask records in the database.
     */
    public int getSubtaskCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES, null, null, null, null, null, null);

        int count = c.getCount();

        c.close();
        db.close();
        return count;
    }

    //*********************************PROJECT-SUBTASK METHODS *************************************

    /**
     * Returns a list of <code>Subtask</code>s related to the specified <code>Project</code>.
     * @param p <code>Project</code> whose related subtasks you want to retrieve.
     * @return List of <code>Subtask</code>s related to the specified <code>Project</code>.
     */
    public ArrayList<Subtask> getSubsOfProj(Project p)
    {
        ArrayList<Subtask> relatedSubtasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES, PROJ_SUB_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())}, null, null, null, null);

        if (c.moveToFirst())
        {
            do
            {
                int subID = c.getInt(c.getColumnIndex(PROJ_SUB_FIELD_NAMES[1]));
                relatedSubtasks.add(getSubtask(subID));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return relatedSubtasks;
    }

    /**
     * Adds a project-subtask relationship record to the project-subtask relation table.
     * @param projID ID of the project (its primary key)
     * @param subID ID of the subtask (its primary key)
     */
    public void addProjSub(int projID, int subID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PROJ_SUB_FIELD_NAMES[0], projID);
        values.put(PROJ_SUB_FIELD_NAMES[1], subID);

        db.insert(PROJ_SUB_TABLE_NAME, null, values);

        db.close();
    }

    /**
     * Deletes a project-subtask relationship record from its project-subtask relation table.
     * @param subID The ID of the subtask of the project-subtask relationship to delete (its primary key)
     */
    public void deleteProjSub(int subID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // since subID is auto-incremented, it should be unique in this table
        db.delete(PROJ_SUB_TABLE_NAME, PROJECTS_FIELD_NAMES[1] + "=?",
                new String[] {String.valueOf(subID)});

        db.close();
    }

    /**
     * Deletes all project-subtask relationship records of one project from the relation table.
     * @param projID ID of the project of the project-subtask relationship(s) to delete (its primary key)
     */
    public void deleteSubsOfProj(int projID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PROJ_SUB_TABLE_NAME, PROJECTS_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(projID)});

        db.close();
    }

    /**
     * Deletes all project-subtask relationship records from the database.
     */
    public void deleteAllProjSubs()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PROJ_SUB_TABLE_NAME, null, null);
        db.close();
    }

    /**
     * Returns count of subtasks related to the specified project.
     * @param p Project whose related subtasks you want to count.
     * @return Count of subtasks related to the specified project.
     */
    public int getSubsOfProjCount(Project p)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES, PROJ_SUB_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())}, null, null, null, null);

        int count = c.getCount();

        c.close();
        db.close();
        return count;
    }


}
