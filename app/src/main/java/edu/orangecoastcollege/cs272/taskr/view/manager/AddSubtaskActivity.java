package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
 * Represents the activity view that allows the user to add a subtask to a project in the database,
 * while adding that project to the database.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-16
 */
public class AddSubtaskActivity extends AppCompatActivity implements View.OnClickListener
{
    DatabaseController dbc;

    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    int relatedProjID;
    Project relatedProject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_add_subtask);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve related project from ViewProjectActivity
        Bundle extras = getIntent().getExtras();
        relatedProjID = extras.getInt("projID");
        dbc.openDatabase();
        relatedProject = ProjectModel.getById(dbc, relatedProjID);
        dbc.close();

        // Initialize EditText and DatePicker
        nameET = (EditText) findViewById(R.id.ma_asub_nameET);
        descriptionET = (EditText) findViewById(R.id.ma_asub_descriptionET);
        dueDateDP = (DatePicker) findViewById(R.id.ma_asub_dueDateDP);

        // Set DatePicker (min date must be before current date)
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);
        // TODO: set max date to project due date

        // Set up button to be associated with action
        findViewById(R.id.ma_asub_add_button).setOnClickListener(this);

    }

    // Associate button with actions
    @Override
    public void onClick(View v)
    {
        Intent intentChangeView;
        switch (v.getId())
        {
            case R.id.ma_asub_add_button:
                if (nameET.getText().toString() != null && !nameET.getText().toString().isEmpty())
                {
                    createSubtask(relatedProject);
                    intentChangeView = new Intent(this, ViewProjectActivity.class);
                    startActivity(intentChangeView);
                }
                break;
        }
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

        // Set project hasSubtasks to true and add relation to database
        p.setSubtasks(true);
        dbc.openDatabase();
        ProjectModel.updateProject(dbc, p);
        dbc.close();
        dbc.openDatabase();
        RelatedSubtasksModel.addProjSub(dbc, p.getID(), subID);
        dbc.close();

        // Update list and list view
        ViewProjectActivity.allSubsOfProjectList.add(new Subtask(subID, name, description, dueDate));
        ViewProjectActivity.adaptSubtask.notifyDataSetChanged();
        // Update list and list view for allProjectsList too since a project was updated
        dbc.openDatabase();
        ViewAllProjectsActivity.allProjectsList = ProjectModel.getAllProjects(dbc);
        dbc.close();
        ViewAllProjectsActivity.adaptProject.notifyDataSetChanged();

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
