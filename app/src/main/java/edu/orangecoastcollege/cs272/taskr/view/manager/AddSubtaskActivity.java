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
import edu.orangecoastcollege.cs272.taskr.model.manager.RelatedSubtasksModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;

/**
 * Represents the activity view that allows the user to add a subtask to a project in the database.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-16
 */
public class AddSubtaskActivity extends AppCompatActivity implements View.OnClickListener
{
    // Controller
    DatabaseController dbc;

    // Nodes
    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    // Project that added subtask will belong to
    int relatedProjectID;
    Project relatedProject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_add_subtask);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve related project ID from ViewProjectActivity
        relatedProjectID = getIntent().getIntExtra("relatedProjectID", -1);
        getIntent().removeExtra("relatedProjectID");
        // Use related project ID to construct project from database
        dbc.openDatabase();
        relatedProject = ProjectModel.getById(dbc, relatedProjectID);
        dbc.close();

        // Initialize EditText and DatePicker
        nameET = (EditText) findViewById(R.id.ma_asub_nameET);
        descriptionET = (EditText) findViewById(R.id.ma_asub_descriptionET);
        dueDateDP = (DatePicker) findViewById(R.id.ma_asub_dueDateDP);

        // Set DatePicker (min date must be before current date)
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);
        // TODO: set max date to project due date

        // Set up (add subtask) button to be associated with action
        findViewById(R.id.ma_asub_add_button).setOnClickListener(this);

    }

    // Associate (add subtask) button with actions
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ma_asub_add_button:
                if (nameET.getText().toString() != null && !nameET.getText().toString().isEmpty())
                {
                    // Create subtask
                    createSubtask(relatedProject);
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
                upIntent.putExtra("selectedProjectID", relatedProjectID);
                navigateUpTo(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a <code>Subtask</code> using the field values in the EditText and DatePicker,
     * adds the new subtask to the subtask database, and adds the <code>Subtask</code> to the
     * related project's list of related subtasks. Also adds the relation between the new created
     * subtask and the project it belongs to the relationship table.
     * @param p <code>Project</code> to add the <code>Subtask</code> to.
     */
    private void createSubtask(Project p)
    {
        // Retrieve data
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String dueDate = datePickerToString(dueDateDP);

        // Add subtask to database (id -1 is only temporary here)
        Subtask newSubtask = new Subtask(-1, name, description, dueDate);
        dbc.openDatabase();
        int subID = SubtaskModel.save(dbc, newSubtask);
        dbc.close();

        // Set project field "hasSubtasks" to true and add relation to database
        p.setSubtasks(true);
        dbc.openDatabase();
        ProjectModel.updateProject(dbc, p);
        dbc.close();
        dbc.openDatabase();
        RelatedSubtasksModel.addProjSub(dbc, p.getID(), subID);
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
