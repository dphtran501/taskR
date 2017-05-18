package edu.orangecoastcollege.cs272.taskr.model.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;



/**
 * Created by vietn on 5/17/2017.
 */

public class ProjectModel extends DatabaseController.LocalDatabaseModel
{

    // Projects database table
    private static final String PROJECTS_TABLE_NAME = "projects";
    private static final String[] PROJECTS_FIELD_NAMES = {"proj_id", "name", "description", "due_date", "subtasks"};
    private static final String[] PROJECTS_FIELD_TYPES = {" INTEGER PRIMARY KEY AUTOINCREMENT, ", " TEXT, ", " TEXT, ", " TEXT, ", " INTEGER"};

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
        String createQuery = "CREATE TABLE IF NOT EXISTS " + PROJECTS_TABLE_NAME + "("
                + PROJECTS_FIELD_NAMES[0] + PROJECTS_FIELD_TYPES[0]
                + PROJECTS_FIELD_NAMES[1] + PROJECTS_FIELD_TYPES[1]
                + PROJECTS_FIELD_NAMES[2] + PROJECTS_FIELD_TYPES[2]
                + PROJECTS_FIELD_NAMES[3] + PROJECTS_FIELD_TYPES[3]
                + PROJECTS_FIELD_NAMES[4] + PROJECTS_FIELD_TYPES[4] + ")";
        database.execSQL(createQuery);
    }

    public static Project getById(DatabaseController dbc, int key)
    {
        String[] queryById = { String.valueOf(key)};

        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES,
                PROJECTS_FIELD_NAMES[0] + "=?", queryById,
                null, null, null, null);

        if (c != null)
            c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[0]));
        String name = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[1]));
        String description = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[2]));
        String dueDate = c.getString(c.getColumnIndex(PROJECTS_FIELD_NAMES[3]));
        boolean hasSubtasks = c.getInt(c.getColumnIndex(PROJECTS_FIELD_NAMES[4])) == 1;

        c.close();
        return new Project(id, name, description, dueDate, hasSubtasks);
    }

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

    public static void deleteAllProjects(DatabaseController dbc)
    {
        SQLiteDatabase db = dbc.database();
        db.delete(PROJECTS_TABLE_NAME, null, null);
    }

    public static void deleteProject(DatabaseController dbc, Project p)
    {
        SQLiteDatabase db = dbc.database();

        db.delete(PROJECTS_TABLE_NAME, PROJECTS_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())});
    }

}
