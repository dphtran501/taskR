package edu.orangecoastcollege.cs272.taskr.model.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;



/**
 * Created by vietn on 5/17/2017.
 */

public class RelatedSubtasksModel extends DatabaseController.LocalDatabaseModel
{

    private static final String PROJ_SUB_TABLE_NAME = "project_subtask";
    private static final String[] PROJ_SUB_FIELD_NAMES = {"proj_id", "sub_id"};
    private static final String[] PROJ_SUB_FIELD_TYPES = {" INTEGER, ", " INTEGER"};


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
        String createQuery = "CREATE TABLE IF NOT EXISTS " + PROJ_SUB_TABLE_NAME + "("
                + PROJ_SUB_FIELD_NAMES[0] + PROJ_SUB_FIELD_TYPES[0]
                + PROJ_SUB_FIELD_NAMES[1] + PROJ_SUB_FIELD_TYPES[1] + ")";
        database.execSQL(createQuery);
    }


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
     * Adds a project-subtask relationship record to the project-subtask relation table.
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
     * Deletes a project-subtask relationship record from its project-subtask relation table.
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
     * Deletes all project-subtask relationship records of one project from the relation table.
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
     */
    public static void deleteAllProjSubs(DatabaseController dbc)
    {
        SQLiteDatabase db = dbc.database();
        db.delete(PROJ_SUB_TABLE_NAME, null, null);
    }

    /**
     * Returns count of subtasks related to the specified project.
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
