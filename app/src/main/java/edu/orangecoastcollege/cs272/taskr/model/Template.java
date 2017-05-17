package edu.orangecoastcollege.cs272.taskr.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.TimeZone;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;

/**
 * Created by vietn on 4/29/2017.
 */

public class Template {

    public static final String[] TEMPLATE_FIELD_NAMES = { "_id", "name", "summary", "location", "description", "start_time", "end_time" };
    public static final String[] TEMPLATE_FIELD_TYPES = { " INTEGER PRIMARY KEY AUTOINCREMENT, ", " TEXT, ", " TEXT, ",
            " TEXT,", " TEXT,", " TEXT,", " TEXT" };

    private String mName;
    private String mSummary;
    private String mLocation;
    private String mDescription;
    private String mStartTime;
    private String mEndTime;
    private String mCalendarId;
    private TimeZone tz;
    private int mId;

    public Template(String name, String summary, String location, String description, String startTime, String endTime) {
        this.mName = name;
        this.mSummary = summary;
        this.mLocation = location;
        this.mDescription = description;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mCalendarId = "primary";
        this.mId = -1;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmCalendarId(String mCalendarId) {
        this.mCalendarId = mCalendarId;
    }

    public TimeZone getTz() {
        return tz;
    }

    public void setTz(TimeZone tz) {
        this.tz = tz;
    }

    public String getmName() {
        return mName;
    }

    public String getmSummary() {
        return mSummary;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }


    public String getmCalendarId() {
        return mCalendarId;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void save(DatabaseController db){
            ContentValues values = new ContentValues();
            values.put("name", mName);
            values.put("summary", mSummary);
            values.put("location", mLocation);
            values.put("description", mDescription);
            values.put("start_time", mStartTime);
            values.put("end_time", mEndTime);

            SQLiteDatabase database = db.database();
            database.insert("templates", null, values);
    }

    //More non static methods (getters, setters, database methods) here

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
        db.openDatabase();
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

    @Override
    public String toString() {
        return "Template{" +
                "mName='" + mName + '\'' +
                '}';
    }
}
