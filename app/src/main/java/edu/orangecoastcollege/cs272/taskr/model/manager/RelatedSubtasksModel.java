package edu.orangecoastcollege.cs272.taskr.model.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;

/**
 * <code>RelatedSubtasksModel</code> represents the database model for the project-subtask
 * relationship table containing the IDs of the project and subtask that are related to each other.
 * It provides the mechanisms by which new project-subtask relationship records can be created and
 * existing ones can be deleted.
 *
 * @author	Derek Tran
 * @version	2.0
 * @since 	2017-05-15
 */
public class RelatedSubtasksModel extends DatabaseController.LocalDatabaseModel
{
    // Project-subtask relationship database table
    private static final String PROJ_SUB_TABLE_NAME = "project_subtask";
    private static final String[] PROJ_SUB_FIELD_NAMES = {"proj_id", "sub_id"};
    private static final String[] PROJ_SUB_FIELD_TYPES = {"INTEGER", "INTEGER"};

    // Constructor
    public RelatedSubtasksModel(){}

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + PROJ_SUB_TABLE_NAME);
        onCreate(database);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(createTable(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES, PROJ_SUB_FIELD_TYPES));
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
     * Returns a list of <code>Subtask</code>s related to the specified <code>Project</code>.
     * @param dbc Controller instance that called this method.
     * @param p <code>Project</code> whose related subtasks you want to retrieve.
     * @return List of <code>Subtask</code>s related to the specified <code>Project</code>.
     */
    public static ArrayList<Subtask> getSubsOfProj(DatabaseController dbc, Project p)
    {
        ArrayList<Subtask> relatedSubtasks = new ArrayList<>();

        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES, PROJ_SUB_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())}, null, null, null, null);

        if (c.moveToFirst())
        {
            do
            {
                int subID = c.getInt(c.getColumnIndex(PROJ_SUB_FIELD_NAMES[1]));
                relatedSubtasks.add(SubtaskModel.getById(dbc, subID));
            } while (c.moveToNext());
        }

        c.close();
        return relatedSubtasks;
    }

    /**
     * Adds a project-subtask relationship record to the database
     * @param dbc Controller instance that called this method.
     * @param projID ID of the project (its primary key)
     * @param subID ID of the subtask (its primary key)
     */
    public static void addProjSub(DatabaseController dbc, int projID, int subID)
    {
        SQLiteDatabase db = dbc.database();
        ContentValues values = new ContentValues();

        values.put(PROJ_SUB_FIELD_NAMES[0], projID);
        values.put(PROJ_SUB_FIELD_NAMES[1], subID);

        db.insert(PROJ_SUB_TABLE_NAME, null, values);
    }

    /**
     * Deletes a project-subtask relationship record from the database.
     * @param dbc Controller instance that called this method.
     * @param subID The ID of the subtask of the project-subtask relationship to delete (its primary key)
     */
    public static void deleteProjSub(DatabaseController dbc, int subID)
    {
        SQLiteDatabase db = dbc.database();

        // since subID is auto-incremented, it should be unique in this table
        db.delete(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES[1] + "=?",
                new String[] {String.valueOf(subID)});
    }

    /**
     * Deletes all project-subtask relationship records of one project from the database.
     * @param dbc Controller instance that called this method.
     * @param projID ID of the project of the project-subtask relationship(s) to delete (its primary key)
     */
    public static void deleteSubsOfProj(DatabaseController dbc, int projID)
    {
        SQLiteDatabase db = dbc.database();

        db.delete(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(projID)});
    }

    /**
     * Deletes all project-subtask relationship records from the database.
     * @param dbc Controller instance that called this method.
     */
    public static void deleteAllProjSubs(DatabaseController dbc)
    {
        SQLiteDatabase db = dbc.database();
        db.delete(PROJ_SUB_TABLE_NAME, null, null);
    }

    /**
     * Returns count of subtasks related to the specified project.
     * @param dbc Controller instance that called this method.
     * @param p Project whose related subtasks you want to count.
     * @return Count of subtasks related to the specified project.
     */
    public static int getSubsOfProjCount(DatabaseController dbc, Project p)
    {
        SQLiteDatabase db = dbc.database();
        Cursor c = db.query(PROJ_SUB_TABLE_NAME, PROJ_SUB_FIELD_NAMES, PROJ_SUB_FIELD_NAMES[0] + "=?",
                new String[] {String.valueOf(p.getID())}, null, null, null, null);

        int count = c.getCount();
        c.close();

        return count;
    }
}
