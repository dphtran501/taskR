package edu.orangecoastcollege.cs272.taskr.model.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;

/**
 * <code>SubtaskModel</code> represents the database model for the subtasks table containing one
 * primary key and three other fields. It provides the mechanisms by which new subtask records
 * can be created and existing ones can be updated or deleted. <code>SubtaskModel</code> also
 * provides functionality to query the subtasks database table for a single record or all records.
 *
 * @author	Derek Tran
 * @version	2.0
 * @since 	2017-05-15
 */
public class SubtaskModel extends DatabaseController.LocalDatabaseModel
{

    // Subtasks database table
    private static final String SUBTASKS_TABLE_NAME = "subtasks";
    private static final String[] SUBTASKS_FIELD_NAMES = {"sub_id", "name", "description", "due_date"};
    private static final String[] SUBTASKS_FIELD_TYPES = {"INTEGER PRIMARY KEY AUTOINCREMENT",
            "TEXT", "TEXT", "TEXT"};

    // Constructor
    public SubtaskModel(){}

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + SUBTASKS_TABLE_NAME);
        onCreate(database);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(createTable(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES, SUBTASKS_FIELD_TYPES));
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
     * Returns a <code>Subtask</code> with the given primary key id.
     * @param dbc Controller instance that called this method.
     * @param key Subtask's primary key id in the database.
     * @return <code>Subtask</code> with the given primary key id.
     */
    public static Subtask getById(DatabaseController dbc, int key)
    {
        String[] queryById = { String.valueOf(key)};

        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES, SUBTASKS_FIELD_NAMES[0] + "=?",
                queryById, null, null, null, null);

        if (c != null) c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(SUBTASKS_FIELD_NAMES[0]));
        String name = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[1]));
        String description = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[2]));
        String dueDate = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[3]));

        c.close();
        return new Subtask(id, name, description, dueDate);
    }

    /**
     * Returns a list of all <code>Subtask</code>s in the database.
     * @param dbc Controller instance that called this method.
     * @return List of all <code>Subtask</code>s in the database.
     */
    public static ArrayList<Subtask> getAllSubtasks(DatabaseController dbc)
    {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES, null, null, null, null, null, null);

        if (c.moveToFirst())
        {
            do {
                int id = c.getInt(c.getColumnIndex(SUBTASKS_FIELD_NAMES[0]));
                String name = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[1]));
                String description = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[2]));
                String dueDate = c.getString(c.getColumnIndex(SUBTASKS_FIELD_NAMES[3]));
                subtasksList.add(new Subtask(id, name, description, dueDate));
            } while (c.moveToNext());
        }
        c.close();
        return subtasksList;
    }

    /**
     * Adds a new <code>Subtask</code> to the database using the non-primary key fields of a
     * given <code>Subtask</code>.
     * @param dbc Controller instance that called this method.
     * @param s <code>Subtask</code> whose non-primary key fields are used to create a subtask
     *          record in the database.
     * @return The auto-generated primary key of the newly added subtask record.
     */
    public static int save(DatabaseController dbc, Subtask s)
    {
        ContentValues values = new ContentValues();

        values.put(SUBTASKS_FIELD_NAMES[1], s.getName());
        values.put(SUBTASKS_FIELD_NAMES[2], s.getDescription());
        values.put(SUBTASKS_FIELD_NAMES[3], s.getDueDate());

        SQLiteDatabase db = dbc.database();
        db.insert(SUBTASKS_TABLE_NAME, null, values);

        // Retrieve autogenerated primary key
        String selectLastSQL = "SELECT MAX(" + SUBTASKS_FIELD_NAMES[0] + ") FROM " + SUBTASKS_TABLE_NAME;
        Cursor c = db.rawQuery(selectLastSQL, null);
        if (c != null) c.moveToFirst();
        int key = c.getInt(0);

        c.close();
        return key;
    }

    /**
     * Updates a subtask record specified by a given <code>Subtask</code>.
     * @param dbc Controller instance that called this method.
     * @param s <code>Subtask</code> whose record needs to be updated.
     */
    public static void updateSubtask(DatabaseController dbc, Subtask s)
    {
        ContentValues values = new ContentValues();

        values.put(SUBTASKS_FIELD_NAMES[1], s.getName());
        values.put(SUBTASKS_FIELD_NAMES[2], s.getDescription());
        values.put(SUBTASKS_FIELD_NAMES[3], s.getDueDate());

        SQLiteDatabase db = dbc.database();
        db.update(SUBTASKS_TABLE_NAME, values, SUBTASKS_FIELD_NAMES[0] + "=?",
                new String[]{String.valueOf(s.getID())});
    }

    /**
     * Deletes all subtasks in the database.
     * @param dbc Controller instance that called this method.
     */
    public static void deleteAllSubtasks(DatabaseController dbc)
    {
        SQLiteDatabase db = dbc.database();
        db.delete(SUBTASKS_TABLE_NAME, null, null);
    }

    /**
     * Deletes a subtask record specified by a given <code>Subtask</code>.
     * @param dbc Controller instance that called this method.
     * @param s <code>Subtask</code> whose record will be deleted from the database.
     */
    public static void deleteSubtask(DatabaseController dbc, Subtask s)
    {
        SQLiteDatabase db = dbc.database();

        db.delete(SUBTASKS_TABLE_NAME, SUBTASKS_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(s.getID())});
    }
}
