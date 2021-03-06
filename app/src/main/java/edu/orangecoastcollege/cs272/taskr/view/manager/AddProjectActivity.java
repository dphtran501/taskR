package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.manager.Project;
import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;

/**
 * Represents the activity view that allows the user to add a project to the database.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-12
 */
public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener
{
    // Controller
    DatabaseController dbc;

    // Nodes
    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_add_project);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Initialize EditText and DatePicker
        nameET = (EditText) findViewById(R.id.ma_aproj_nameET);
        descriptionET = (EditText) findViewById(R.id.ma_aproj_descriptionET);
        dueDateDP = (DatePicker) findViewById(R.id.ma_aproj_dueDateDP);

        // Set DatePicker (min date must be before current date)
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);

        // Set up (add project) button to be associated with action
        findViewById(R.id.ma_aproj_add_button).setOnClickListener(this);
    }

    // Associate (add project) button with actions
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ma_aproj_add_button:
                if (nameET.getText().toString() != null && !nameET.getText().toString().isEmpty())
                {
                    // Create project
                    createProject();
                    finish();   // Goes to onResume in ViewAllProjectsActivity
                }
                break;
        }
    }

    /**
     * Creates a <code>Project</code> using the field values in the EditText and DatePicker, and
     * adds the new project to the project database.
     */
    private void createProject()
    {
        // Retrieve data
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String dueDate = datePickerToString(dueDateDP);
        boolean hasSubtasks = false;    // no subtasks when creating project for first time

        // Add project to database (id -1 is only temporary here)
        Project newProject = new Project(-1, name, description, dueDate, hasSubtasks);
        dbc.openDatabase();
        ProjectModel.save(dbc, newProject);
        dbc.close();
    }

    /**
     * Converts the DatePicker into a string of format "YYYY-MM-DD".
     * @param dp DatePicker to convert to a string.
     * @return DatePicker as a string of format "YYYY-MM-DD".
     */
    private String datePickerToString(DatePicker dp)
    {
        int year = dp.getYear();
        int month = dp.getMonth() + 1;
        int day = dp.getDayOfMonth();

        return checkDigit(year) + "-" + checkDigit(month) + "-" + checkDigit(day);
    }

    /**
     * Adds a "0" to front of number if it's less than 10 (e.g. 7 -> "07"; 11 -> "11")
     * @param n Number to convert.
     * @return Number converted to a string. If number is less than 10, an "0" is placed in front.
     */
    private String checkDigit(int n)
    {
        return (n < 10) ? ("0" + n) : String.valueOf(n);
    }

}
