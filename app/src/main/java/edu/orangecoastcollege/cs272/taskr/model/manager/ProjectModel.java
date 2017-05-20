package edu.orangecoastcollege.cs272.taskr.model.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;

/**
 * <code>ProjectModel</code> represents the database model for the projects table containing one
 * primary key and four other fields. It provides the mechanisms by which new project records
 * can be created and existing ones can be updated or deleted. <code>ProjectModel</code> also
 * provides functionality to query the projects database table for a single record or all records.
 *
 * @author	Derek Tran
 * @version	2.0
 * @since 	2017-05-15
 */
public class ProjectModel extends DatabaseController.LocalDatabaseModel
{

    // Projects database table
    private static final String PROJECTS_TABLE_NAME = "projects";
    private static final String[] PROJECTS_FIELD_NAMES = {"proj_id", "name", "description",
            "due_date", "subtasks"};
    private static final String[] PROJECTS_FIELD_TYPES = {"INTEGER PRIMARY KEY AUTOINCREMENT",
            "TEXT", "TEXT", "TEXT", "INTEGER"};

    // Constructor
    public ProjectModel(){}

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE_NAME);
        onCreate(database);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(createTable(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES, PROJECTS_FIELD_TYPES));
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

    /**
     * Returns a <code>Project</code> with the given primary key id.
     * @param dbc Controller instance that called this method.
     * @param key Project's primary key id in the database.
     * @return <code>Project</code> with the given primary key id.
     */
    public static Project getById(DatabaseController dbc, int key)
    {
        String[] queryById = {String.valueOf(key)};

        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES, PROJECTS_FIELD_NAMES[0] + "=?",
                queryById, null, null, null, null);

        if (c != null) c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[0]));
        String name = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[1]));
        String description = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[2]));
        String dueDate = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[3]));
        boolean hasSubtasks = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[4])) == 1;

        c.close();
        return new Project(id, name, description, dueDate, hasSubtasks);
    }

    /**
     * Returns a list of all <code>Project</code>s in the database.
     * @param dbc Controller instance that called this method.
     * @return List of all <code>Project</code>s in the database.
     */
    public static ArrayList<Project> getAllProjects(DatabaseController dbc)
    {
        ArrayList<Project> projectsList = new ArrayList<>();
        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES, null, null, null, null, null, null);

        if (c.moveToFirst())
        {
            do {
                int id = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[0]));
                String name = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[1]));
                String description = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[2]));
                String dueDate = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[3]));
                boolean hasSubtasks = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[4])) == 1;
                projectsList.add(new Project(id, name, description, dueDate, hasSubtasks));
            } while (c.moveToNext());
        }
        c.close();
        return projectsList;
    }

    /**
     * Adds a new <code>Project</code> to the database using the non-primary key fields of a
     * given <code>Project</code>.
     * @param dbc Controller instance that called this method.
     * @param p <code>Project</code> whose non-primary key fields are used to create a project
     *          record in the database.
     * @return The auto-generated primary key of the newly added project record.
     */
    public static int save(DatabaseController dbc, Project p)
    {
        ContentValues values = new ContentValues();

        values.put(PROJECTS_FIELD_NAMES[1], p.getName());
        values.put(PROJECTS_FIELD_NAMES[2], p.getDescription());
        values.put(PROJECTS_FIELD_NAMES[3], p.getDueDate());
        values.put(PROJECTS_FIELD_NAMES[4], p.hasSubtasks() ? "1" : "0");


        SQLiteDatabase db = dbc.database();
        db.insert(PROJECTS_TABLE_NAME, null, values);

        // Retrieve auto-generated primary key
        String selectLastSQL = "SELECT MAX(" + PROJECTS_FIELD_NAMES[0] + ") FROM " + PROJECTS_TABLE_NAME;
        Cursor c = db.rawQuery(selectLastSQL, null);
        if (c != null) c.moveToFirst();
        int key = c.getInt(0);

        c.close();
        return key;
    }

    /**
     * Updates a project record specified by a given <code>Project</code>.
     * @param dbc Controller instance that called this method.
     * @param p <code>Project</code> whose record needs to be updated.
     */
    public static void updateProject(DatabaseController dbc, Project p)
    {
        ContentValues values = new ContentValues();

        values.put(PROJECTS_FIELD_NAMES[1], p.getName());
        values.put(PROJECTS_FIELD_NAMES[2], p.getDescription());
        values.put(PROJECTS_FIELD_NAMES[3], p.getDueDate());
        values.put(PROJECTS_FIELD_NAMES[4], p.hasSubtasks() ? "1" : "0");

        SQLiteDatabase db = dbc.database();
        db.update(PROJECTS_TABLE_NAME, values, PROJECTS_FIELD_NAMES[0] + "=?",
                new String[]{String.valueOf(p.getID())});
    }

    /**
     * Deletes all projects in the database.
     * @param dbc Controller instance that called this method.
     */
    public static void deleteAllProjects(DatabaseController dbc)
    {
        SQLiteDatabase db = dbc.database();
        db.delete(PROJECTS_TABLE_NAME, null, null);
    }

    /**
     * Deletes a project record specified by a given <code>Project</code>.
     * @param dbc Controller instance that called this method.
     * @param p <code>Project</code> whose record will be deleted from the database.
     */
    public static void deleteProject(DatabaseController dbc, Project p)
    {
        SQLiteDatabase db = dbc.database();

        db.delete(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())});
    }

}
