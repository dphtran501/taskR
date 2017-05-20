package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.manager.Project;
import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;

/**
 * Represents the activity view that allows the user to edit a <code>Subtask</code> in the database.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-17
 */
public class EditSubtaskActivity extends AppCompatActivity implements View.OnClickListener
{
    // Controller
    DatabaseController dbc;

    // Nodes
    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    // Subtask to be editted
    int subtaskToEditID;
    Subtask subtaskToEdit;
    // Related project ID (need this for up button and setting max date)
    int relatedProjectID;
    Project relatedProject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_edit_subtask);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve subtask and from ViewSubtaskActivity
        subtaskToEditID = getIntent().getIntExtra("subtaskToEditID", -1);
        getIntent().removeExtra("subtaskToEditID");
        dbc.openDatabase();
        subtaskToEdit = SubtaskModel.getById(dbc, subtaskToEditID);
        dbc.close();
        // Retrieve related project from ViewSubtaskActivity
        relatedProjectID = getIntent().getIntExtra("relatedProjectID", -1);
        getIntent().removeExtra("relatedProjectID");
        dbc.openDatabase();
        relatedProject = ProjectModel.getById(dbc, relatedProjectID);
        dbc.close();

        // Set EditText to original field values
        nameET = (EditText) findViewById(R.id.ma_esub_nameET);
        nameET.setText(subtaskToEdit.getName());
        descriptionET = (EditText) findViewById(R.id.ma_esub_descriptionET);
        descriptionET.setText(subtaskToEdit.getDescription());

        // Set DatePicker (min date must be before current date)
        dueDateDP = (DatePicker) findViewById(R.id.ma_esub_dueDateDP);
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date projectDueDate = sdf.parse(relatedProject.getDueDate());
            long msSinceEpoch = projectDueDate.getTime();
            dueDateDP.setMaxDate(msSinceEpoch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Set DatePicker to original due date
        String originalDueDate = subtaskToEdit.getDueDate();
        int year = Integer.parseInt(originalDueDate.substring(0, 4));
        int month = Integer.parseInt(originalDueDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(originalDueDate.substring(8));
        dueDateDP.updateDate(year, month, day);

        // Set up (save subtask) button to be associated with action
        findViewById(R.id.ma_esub_save_button).setOnClickListener(this);
    }

    // Associate (save subtask) button with actions
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ma_esub_save_button:
                if (nameET.getText().toString() != null && !nameET.getText().toString().isEmpty())
                {
                    // Edit subtask
                    editSubtask();
                    finish(); // Goes to onResume in ViewSubtaskActivity
                }
                break;
        }
    }

    // Handling up button in action bar (returns to ViewSubtaskActivity)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent upIntent = new Intent(this, ViewSubtaskActivity.class);
                // Restarts onCreate in ViewSubtaskActivity, so need to pass back projectID and subtaskID
                upIntent.putExtra("selectedSubtaskID", subtaskToEditID);
                upIntent.putExtra("relatedProjectID", relatedProjectID);
                navigateUpTo(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Edits a <code>Subtask</code> using the new field values in the EditText and DatePicker, and
     * updates the subtask in the database.
     */
    private void editSubtask()
    {
        // Retrieve data
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String dueDate = datePickerToString(dueDateDP);

        // Update subtask in database
        subtaskToEdit.setName(name);
        subtaskToEdit.setDescription(description);
        subtaskToEdit.setDueDate(dueDate);
        dbc.openDatabase();
        SubtaskModel.updateSubtask(dbc, subtaskToEdit);
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
