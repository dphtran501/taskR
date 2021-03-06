package edu.orangecoastcollege.cs272.taskr.view.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.scheduler.Template;
import edu.orangecoastcollege.cs272.taskr.model.scheduler.TemplateModel;

/**
 * This scene records the information the user entered and commits them to the database and updates subsequent listViews
 *
 * @author Vincent Hoang
 */
public class SchedulerAddTemplate extends AppCompatActivity implements View.OnClickListener {

    // Fields
    private EditText templateNameET;
    private EditText eventNameET;
    private EditText descriptionET;
    private EditText locationET;
    private boolean startTimeToggle;
    private boolean endTimeToggle;
    private TextView startTime;

    // Database
    DatabaseController dbc;


    /**
     * Initializes the following upon creation:
     *  - Attaches button listeners to buttons defined in XML
     *  - Creates a reference to EditText and TextViews defined in XML
     *  - Gets an instance of the DatabaseController with this activity's context
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduler_add_template);
        findViewById(R.id.s_save_new).setOnClickListener(this);
        findViewById(R.id.s_toggle_startTime).setOnClickListener(this);
        findViewById(R.id.s_toggle_endTime).setOnClickListener(this);
        dbc = DatabaseController.getInstance(this);

        endTimeToggle = false;
        startTimeToggle = false;
        startTime = (TextView) findViewById(R.id.s_startTime_tv);

        templateNameET = (EditText) findViewById(R.id.s_nameET);
        eventNameET = (EditText) findViewById(R.id.s_summaryET);
        descriptionET = (EditText) findViewById(R.id.s_descET);
        locationET = (EditText) findViewById(R.id.s_locationET);
    }

    private void createFromTemplateFields() {
        String templateName = templateNameET.getText().toString();
        String eventName = eventNameET.getText().toString();
        String description = descriptionET.getText().toString();
        String location = locationET.getText().toString();
        //String startTime = String.valueOf(startTimeTP.getHour()) + ":" + String.valueOf(startTimeTP.getMinute());
        //String endTime;
        //if (endTimeToggle) {
        //   endTime = String.valueOf(endTimeTP.getHour()) + ":" + String.valueOf(endTimeTP.getMinute());
        //} else {
        //    endTime = "";

        Template template = new Template(templateName, eventName, description, location, "12:00", "12:00");
        dbc.openDatabase();
        TemplateModel.save(dbc, template);
        dbc.close();
        }

    /**
     * Handles button clicks
     * @param v Parent view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_toggle_startTime:
                startTimeToggle = !startTimeToggle;
                if (startTimeToggle) {
                    findViewById(R.id.s_startTime_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.s_startTime_tv).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.s_startTime_button).setVisibility(View.GONE);
                    findViewById(R.id.s_startTime_tv).setVisibility(View.GONE);
                }
                break;
            case R.id.s_toggle_endTime:
                endTimeToggle = !endTimeToggle;
                if (endTimeToggle) {
                    findViewById(R.id.s_endTime_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.s_endTime_tv).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.s_endTime_button).setVisibility(View.GONE);
                    findViewById(R.id.s_endTime_tv).setVisibility(View.GONE);
                }
                break;
            case R.id.s_startTime_button:
                break;
            case R.id.s_endTime_button:
                break;
            case R.id.s_save_new:
                createFromTemplateFields();
                break;
        }
    }
    }


