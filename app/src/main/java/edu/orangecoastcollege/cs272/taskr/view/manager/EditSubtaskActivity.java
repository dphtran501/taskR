package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.DBHelper;
import edu.orangecoastcollege.cs272.taskr.model.manager.Project;
import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.RelatedSubtasksModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;

/**
 * Created by Jeannie on 5/17/2017.
 */

public class EditSubtaskActivity extends AppCompatActivity implements View.OnClickListener
{
    DatabaseController dbc;

    EditText nameET;
    EditText descriptionET;
    DatePicker dueDateDP;

    int subtaskID;
    Subtask subtask;
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

        // Retrieve subtask from ViewSubtaskActivity
        Bundle extras = getIntent().getExtras();
        subtaskID = extras.getInt("vsub_subID");
        getIntent().removeExtra("vsub_subID");
        relatedProjectID = extras.getInt("vsub_projID");
        getIntent().removeExtra("vsub_projID");
        dbc.openDatabase();
        subtask = SubtaskModel.getById(dbc, subtaskID);
        dbc.close();
        dbc.openDatabase();
        relatedProject = ProjectModel.getById(dbc, relatedProjectID);
        dbc.close();

        // Set EditText to original field values
        nameET = (EditText) findViewById(R.id.ma_esub_nameET);
        nameET.setText(subtask.getName());
        descriptionET = (EditText) findViewById(R.id.ma_esub_descriptionET);
        descriptionET.setText(subtask.getDescription());

        // Set DatePicker (min date must be before current date)
        dueDateDP = (DatePicker) findViewById(R.id.ma_esub_dueDateDP);
        dueDateDP.setMinDate(System.currentTimeMillis() - 1000);
        // TODO: set max dte to project due date
        // set datepicker to original due date
        String originalDueDate = subtask.getDueDate();
        int year = Integer.parseInt(originalDueDate.substring(0, 4));
        int month = Integer.parseInt(originalDueDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(originalDueDate.substring(8));
        dueDateDP.updateDate(year, month, day);

        // set up button to be associted with action
        findViewById(R.id.ma_esub_save_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intentChangeView;
        switch (v.getId())
        {
            case R.id.ma_esub_save_button:
                if (nameET.getText().toString() != null && !nameET.getText().toString().isEmpty())
                {
                    editSubtask();
                    intentChangeView = new Intent(this, ViewSubtaskActivity.class);
                    intentChangeView.putExtra("subID", subtaskID);
                    startActivity(intentChangeView);
                }
                break;
        }
    }

    private void editSubtask()
    {
        // Retrieve data
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String dueDate = datePickerToString(dueDateDP);

        // Update subtask in database
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setDueDate(dueDate);
        dbc.openDatabase();
        SubtaskModel.updateSubtask(dbc, subtask);
        dbc.close();
        // update list and listview for allSubsOfProject
        dbc.openDatabase();
        ViewProjectActivity.allSubsOfProjectList = RelatedSubtasksModel.getSubsOfProj(dbc, relatedProject);
        dbc.close();
        ViewProjectActivity.adaptSubtask.notifyDataSetChanged();
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
