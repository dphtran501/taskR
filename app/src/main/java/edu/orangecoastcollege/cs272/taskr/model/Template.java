package edu.orangecoastcollege.cs272.taskr.model;
import android.icu.util.TimeZone;

/**
 * Created by vietn on 4/29/2017.
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

    @Override
    public String toString() {
        return "Template{" +
                "mName='" + mName + '\'' +
                '}';
    }
}
