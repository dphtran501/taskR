package edu.orangecoastcollege.cs272.taskr.controller;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.model.DBHelper;
import edu.orangecoastcollege.cs272.taskr.model.Template;

/**
 * Created by VincentHoang on 5/16/2017.
 */



public final class Controller extends Application {

    DBHelper mDB = new DBHelper(getApplicationContext());
    private static Controller theOne;
    ArrayList<Template> mAllTemplatesList;
    Template mTemplate;

    public Controller() {
    }

    public static Controller getInstance(){
        if (theOne == null) {
            theOne = new Controller();
        }
        return theOne;
    }

    public void setmDB(DBHelper db) {
        this.mDB = db;
    }

    public ArrayList<Template> getAllTemplates() {
        ArrayList<Template> templates = new ArrayList<>();
        Cursor cursor = theOne.mDB.getAllTemplatesDB();

        if (cursor.moveToFirst()) {
            do {
                Template template = new Template(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                templates.add(template);
            } while (cursor.moveToNext());
        }
        return templates;
    }

    public void addTemplate(Template template) {
        theOne.mDB.addTemplateDB(template);
    }
}
