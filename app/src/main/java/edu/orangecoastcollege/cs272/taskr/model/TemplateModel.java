package edu.orangecoastcollege.cs272.taskr.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;

/**
 * Created by vietn on 5/17/2017.
 */

public class TemplateModel {
    private static final String TEMPLATE_TABLE_NAME = "templates";
    public static final String[] TEMPLATE_FIELD_NAMES = { "_id", "name", "summary", "location", "description", "start_time", "end_time" };
    public static final String[] TEMPLATE_FIELD_TYPES = { " INTEGER PRIMARY KEY AUTOINCREMENT, ", " TEXT, ", " TEXT, ",
            " TEXT,", " TEXT,", " TEXT,", " TEXT" };



    public static class Model extends DatabaseController.LocalDatabaseModel {

        public Model(){
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
            database.execSQL("DROP TABLE IF EXISTS templates");
            onCreate(database);
        }
        @Override
        public void onCreate(SQLiteDatabase database){
            String createQuery = "CREATE TABLE " + "templates" + "("
                    + TEMPLATE_FIELD_NAMES[0] + TEMPLATE_FIELD_TYPES[0]
                    + TEMPLATE_FIELD_NAMES[1] + TEMPLATE_FIELD_TYPES[1]
                    + TEMPLATE_FIELD_NAMES[2] + TEMPLATE_FIELD_TYPES[2]
                    + TEMPLATE_FIELD_NAMES[3] + TEMPLATE_FIELD_TYPES[3]
                    + TEMPLATE_FIELD_NAMES[4] + TEMPLATE_FIELD_TYPES[4]
                    + TEMPLATE_FIELD_NAMES[5] + TEMPLATE_FIELD_TYPES[5]
                    + TEMPLATE_FIELD_NAMES[6] + TEMPLATE_FIELD_TYPES[6] + ")";

            database.execSQL(createQuery);
        }
    }

    public static void save(DatabaseController db, Template template){
        ContentValues values = new ContentValues();
        values.put(TEMPLATE_FIELD_NAMES[1], template.getmName());
        values.put(TEMPLATE_FIELD_NAMES[2], template.getmSummary());
        values.put(TEMPLATE_FIELD_NAMES[3], template.getmLocation());
        values.put(TEMPLATE_FIELD_NAMES[4], template.getmDescription());
        values.put(TEMPLATE_FIELD_NAMES[5], template.getmStartTime());
        values.put(TEMPLATE_FIELD_NAMES[6], template.getmEndTime());

        SQLiteDatabase database = db.database();
        database.insert("templates", null, values);
    }

    public static Template getById(DatabaseController db, int id){
        String[] queryById = { String.valueOf(id)};

        SQLiteDatabase database = db.database();
        Cursor cursor = database.query("templates", TEMPLATE_FIELD_NAMES, TEMPLATE_FIELD_NAMES[0]+"=?", queryById, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Template template = new Template(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));

        template.setmId(id);

        cursor.close();
        return template;
    }

    public static ArrayList<Template> getAllTemplates(DatabaseController db) {
        ArrayList<Template> templatesList = new ArrayList<>();
        SQLiteDatabase database = db.database();
        Cursor cursor = database.query("templates", TEMPLATE_FIELD_NAMES, null, null, null, null, null, null);

        int count = 1;

        if (cursor.moveToFirst()) {
            do {
                Template template = new Template(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                template.setmId(count++);
                templatesList.add(template);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return templatesList;
    }
}
