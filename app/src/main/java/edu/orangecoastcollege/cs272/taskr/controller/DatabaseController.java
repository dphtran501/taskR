package edu.orangecoastcollege.cs272.taskr.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.RelatedSubtasksModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;
import edu.orangecoastcollege.cs272.taskr.model.scheduler.TemplateModel;

/**
 * DatabaseController directs the flow of database read and write access with the ability to synchronize multiple database calls
 *
 * @author Vincent Hoang
 *
 * Credit for this DatabaseController goes to 1052697/rolf-ツ on stackoverflow
 * The nearly completed skeleton can be found at:
 * <url>http://stackoverflow.com/questions/37712349/how-to-better-arrange-my-sqlite-database-class-in-android</url>
 */

public final class DatabaseController extends SQLiteOpenHelper {

    public static abstract class LocalDatabaseModel {

        public LocalDatabaseModel(){
        }

        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){

        }
        public abstract void onCreate(SQLiteDatabase database);
    }

    private SQLiteDatabase database;
    private int openConnections = 0;
    private static final String DATABASE = "capstone";
    private static final int VERSION = 1;
    private static DatabaseController theOne = null;

    // Add database models here
    private final LocalDatabaseModel[] models = new LocalDatabaseModel[]{
            new TemplateModel(),
            new SubtaskModel(),
            new ProjectModel(),
            new RelatedSubtasksModel()};


    /**
     * Checks to see if an instance of theOne already exists. If null, theOne is initialized with the application context
     * @param context Local activity context passed in from another activity, used to get the global/application context and save it in the controller's instance
     * @return static DatabaseController instance
     */
    public synchronized static DatabaseController getInstance(Context context) {
        if (theOne == null) {
            theOne = new DatabaseController(context.getApplicationContext());
        }
        return theOne;
    }

    private DatabaseController(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    /**
     * Checks to see if the database is initialized or if the number of open connections is 0 to return an error. Otherwise, decrement open connections and close the db if there are no open connections.
     * [Note: close() must be called in the same scope of the openDatabase() call]
     */
    @Override
    public synchronized void close() {
        if(database == null || openConnections == 0){
            throw new IllegalStateException("Database already closed or has never been opened.");
        }
        openConnections--;
        if(openConnections != 0){
            return;
        }
        database = null;
        super.close();
    }

    /**
     * [Note: Do not manually call this method! Use openDatabase(), database() and close()!]
     *
     * Opens the SQLiteDatabase if not already opened.
     * This implementation does the exact same thing as getWritableDatabase and thus will return a writable database.
     *
     * @return the newly opened database or the existing database.
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return getWritableDatabase();
    }

    /**
     *
     * [Note: Do not manually call this method! Use openDatabase(), database() and close()!]
     *
     * Opens the SQLiteDatabase if not already opened.
     *
     * @return the newly opened database or the existing database.
     */
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if(database == null){
            database = super.getWritableDatabase();
        }
        openConnections++;
        return database;
    }

    /**
     * Use this method to open the database. To create a SQLiteDatabase object, use database(). Follow with a close() immediately after.
     */
    public synchronized void openDatabase(){
        getWritableDatabase();
    }

    /**
     * Returns the opened database. Throws an exception if the database has not been opened
     * @return the database.
     */
    public synchronized SQLiteDatabase database(){
        if(database == null){
            throw new IllegalStateException("Database has not been opened yet!");
        }
        return database;
    }

    /**
     * Iterates through the models array and calls their onCreate() methods to initialize the database tables.
     * @param db SQLiteDatabase
     */
    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        setForeignKeyConstraintsEnabled(db);
        for(LocalDatabaseModel model: models){
            model.onCreate(db);
        }
    }

    /**
     * Iterates through the models array and calls their onUpgrade() methods to drop existing tables and create new tables.
     * @param db SQLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        setForeignKeyConstraintsEnabled(db);
        for(LocalDatabaseModel model: models){
            model.onUpgrade(db, oldVersion, newVersion);
        }
    }

    /**
     * Enables use of foreign keys in the database (I think)
     * @param db SQLiteDatabase
     */
    @Override
    public synchronized void onOpen(SQLiteDatabase db) {
        setForeignKeyConstraintsEnabled(db);
    }

    /**
     * Enables use of foreign keys in the database for Android 4.0 devices (I think)
     * @param db SQLiteDatabase
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public synchronized void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db){
        //Skip for Android 4.1 and newer as this is already handled in onConfigure
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN && !db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    /**
     * Gets the record count of the database table
     * @param table to be passed in
     * @return long value
     */
    public long getCount(String table){
        return DatabaseUtils.queryNumEntries(database(), table);
    }

}