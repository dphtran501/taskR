package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.manager.Project;
import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;

/**
 * Represents the activity view that allows the user to edit a <code>Project</code> in the database.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-18
 */
public class EditProjectActivity extends AppCompatActivity implements View.OnClickListener
{
    // Controller
    DatabaseController dbc;

    // Nodes
    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    // Project to be editted
    int projectToEditID;
    Project projectToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_edit_project);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve project-to-edit ID from ViewProjectActivity
        projectToEditID = getIntent().getIntExtra("projectToEditID", -1);
        getIntent().removeExtra("projectToEditID");
        // Use project-to-edit ID to construct project from database
        dbc.openDatabase();
        projectToEdit = ProjectModel.getById(dbc, projectToEditID);
        dbc.close();

        // Set EditText to original field values
        nameET = (EditText) findViewById(R.id.ma_eproj_nameET);
        nameET.setText(projectToEdit.getName());
        descriptionET = (EditText) findViewById(R.id.ma_eproj_descriptionET);
        descriptionET.setText(projectToEdit.getDescription());

        // Set DatePicker (min date must be before current date)
        dueDateDP = (DatePicker) findViewById(R.id.ma_eproj_dueDateDP);
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);
        // TODO: set max dte to project due date
        // Set DatePicker to original due date
        String originalDueDate = projectToEdit.getDueDate();
        int year = Integer.parseInt(originalDueDate.substring(0, 4));
        int month = Integer.parseInt(originalDueDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(originalDueDate.substring(8));
        dueDateDP.updateDate(year, month, day);

        // Set up (save project) button to be associated with action
        findViewById(R.id.ma_eproj_save_button).setOnClickListener(this);
    }

    // Associate (save project) button with actions
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ma_eproj_save_button:
                if (nameET.getText().toString() != null && !nameET.getText().toString().isEmpty())
                {
                    // Edit project
                    editProject();
                    finish(); // Goes to onResume in ViewProjectActivity
                }
                break;
        }
    }

    // Handling up button in action bar (returns to ViewProjectActivity)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent upIntent = new Intent(this, ViewProjectActivity.class);
                // Restarts onCreate in ViewProjectActivity, so need to pass back projectID
                upIntent.putExtra("selectedProjectID", projectToEditID);
                navigateUpTo(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Edits a <code>Project</code> using the new field values in the EditText and DatePicker,
     * and updates the project in the database.
     */
    private void editProject()
    {
        // Retrieve data
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String dueDate = datePickerToString(dueDateDP);

        // Update subtask in database
        projectToEdit.setName(name);
        projectToEdit.setDescription(description);
        projectToEdit.setDueDate(dueDate);
        dbc.openDatabase();
        ProjectModel.updateProject(dbc, projectToEdit);
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
