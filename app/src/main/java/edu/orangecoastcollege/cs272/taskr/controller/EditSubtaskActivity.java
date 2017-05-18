package edu.orangecoastcollege.cs272.taskr.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.model.DBHelper;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;

/**
 * Created by Jeannie on 5/17/2017.
 */

public class EditSubtaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private DBHelper db;

    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    int subtaskID;
    Subtask subtask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_edit_subtask);

        // Initialize database
        db = new DBHelper(this);

        // Retrieve subtask from ViewSubtaskActivity
        Bundle extras = getIntent().getExtras();
        subtaskID = extras.getInt("subID");
        //subtask = db.getSubtask(subtaskID);

        // Set EditText to original field values
        nameET = (EditText) findViewById(R.id.ma_esub_nameET);
       // nameET.setText(subtask.getName());
        descriptionET = (EditText) findViewById(R.id.ma_esub_descriptionET);
        //descriptionET.setText(subtask.getDescription());

        // Set up DatePicker
        dueDateDP = (DatePicker) findViewById(R.id.ma_esub_dueDateDP);
        // TODO: set due date
        // Set DatePicker (min date must be before current date)
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);
        // TODO: set max dte to project due date
        // set datepicker to original due date
        // TODO: convert string datepicker into datepicker

        // set up button to be associted with action
        findViewById(R.id.ma_esub_save_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {

    }

    private DatePicker toDatePicker(String strDatePicker)
    {
        int year = Integer.parseInt(strDatePicker.substring(0, 4));
        int month;
        int day;

        return null;
    }
}
