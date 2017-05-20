package edu.orangecoastcollege.cs272.taskr.model.scheduler;
import android.icu.util.TimeZone;

/**
 * Template object class
 * Stores the constructor and various get and set methods used for Template objects
 *
 * @author Vincent Hoang on 4/29/2017.
 */

public class Template {

    private String mName;
    private String mSummary;
    private String mLocation;
    private String mDescription;
    private String mStartTime;
    private String mEndTime;
    private String mCalendarId;
    private TimeZone tz;
    private int mId;

    /**
     * Default constructor for Templates
     *
     * @param name The name of the Template object
     * @param summary The name of the Calendar Event
     * @param location The location of the event
     * @param description A brief description of the event
     * @param startTime (Optional) A start time to be saved to the object, if the user desires.
     * @param endTime (Optional) An end time to be saved to the object, if the user desires.
     */
    public Template(String name, String summary, String location, String description, String startTime, String endTime) {
        this.mName = name;
        this.mSummary = summary;
        this.mLocation = location;
        this.mDescription = description;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mCalendarId = "primary";
        this.mId = -1;  // All templates are generated with an ID of -1.
                        // Objects will have their IDs replaced when an arraylist is built from the database file
    }

    /**
     * Sets the summary of the Event
     * @param mSummary The name of the event
     */
    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    /**
     * Sets the location of the event
     * @param mLocation the location of the event
     */
    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    /**
     * Sets the description of the event
     * @param mDescription a brief description of the event
     */
    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    /**
     * Sets the start time of the event
     * @param mStartTime Start time of the event
     */
    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    /**
     * Sets the end time of the event
     * When End Time == Start Time, the event has no end time in Google Calendar until the next day
     * @param mEndTime End time of the event
     */
    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    /**
     * Sets the Calendar Id
     * Default value is "primary". Use when the user wants to post to a different calendar
     * @param mCalendarId the user's calendar id
     */
    public void setmCalendarId(String mCalendarId) {
        this.mCalendarId = mCalendarId;
    }

    /**
     * Gets the time zone of the event
     * @return timezone
     */
    public TimeZone getTz() {
        return tz;
    }

    /**
     * Sets the time zone of the event
     * Leaving this field blank defaults to the user's current location. Use only if the user
     *  wants to post an event in a different time zone
     * @param tz the time zone of the event location
     */
    public void setTz(TimeZone tz) {
        this.tz = tz;
    }

    /**
     * Gets the name of the template
     * @return template name
     */
    public String getmName() {
        return mName;
    }

    /**
     * Gets the summary of the event
     * @return the event name/summary
     */
    public String getmSummary() {
        return mSummary;
    }

    /**
     * Gets the location of the event
     * @return location of the event
     */
    public String getmLocation() {
        return mLocation;
    }

    /**
     * Gets description of the event
     * @return description
     */
    public String getmDescription() {
        return mDescription;
    }

    /**
     * Gets the start time
     * @return start time
     */
    public String getmStartTime() {
        return mStartTime;
    }

    /**
     * Gets the end time
     * @return end time
     */
    public String getmEndTime() {
        return mEndTime;
    }


    /**
     * Gets the calendar ID
     * @return calendar Id; "primary" in most cases
     */
    public String getmCalendarId() {
        return mCalendarId;
    }

    /**
     * Sets the name of the Template
     * Used if the user wants to rename a template
     * @param mName New name for the template
     */
    public void setmName(String mName) {
        this.mName = mName;
    }


    /**
     * Gets the ID of the template.
     * @return ID of the template
     */
    public int getmId() {
        return mId;
    }

    /**
     * Sets the ID of the template
     *
     * Templates are constructed with a placeholder ID of -1. When the templates are pulled from the database,
     * they are assigned an ID so they can be interacted with from the list views
     * @param mId the id of the template
     *
     */
    public void setmId(int mId) {
        this.mId = mId;
    }
}
