package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;

/**
 * Represents the activity view that allows the user to view the details of a specified subtask,
 * specifically its name, description, and due date.
 *
 * Additionally, the user can choose to edit the subtask.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-17
 */
public class ViewSubtaskActivity extends AppCompatActivity implements View.OnClickListener
{
    // Controller
    DatabaseController dbc;

    // Nodes
    TextView subtaskNameTV;
    TextView subtaskDueDateTV;
    TextView subtaskDescriptionTV;

    // Selected subtask from ViewProjectActivity
    int selectedSubtaskID;
    Subtask selectedSubtask;
    // Related project ID from ViewProjectActivity (need this for up button)
    int relatedProjectID;

    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_subtask);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve selected subtask ID from ViewProjectActivity
        selectedSubtaskID = getIntent().getIntExtra("selectedSubtaskID", -1);
        getIntent().removeExtra("selectedSubtaskID");
        // Use selected subtask ID to construct selected subtask from database
        dbc.openDatabase();
        selectedSubtask = SubtaskModel.getById(dbc, selectedSubtaskID);
        dbc.close();
        // Retrieve related project ID from ViewProjectActivity
        relatedProjectID = getIntent().getIntExtra("relatedProjectID", -1);
        getIntent().removeExtra("relatedProjectID");

        // Set subtask name, due date, and description in TextView to those of selected subtask
        subtaskNameTV = (TextView) findViewById(R.id.ma_vsub_nameTV);
        subtaskNameTV.setText(selectedSubtask.getName());
        subtaskDueDateTV = (TextView) findViewById(R.id.ma_vsub_dueDateTV);
        String newDueDateTV = "Due: " + selectedSubtask.getDueDate();
        subtaskDueDateTV.setText(newDueDateTV);
        subtaskDescriptionTV = (TextView) findViewById(R.id.ma_vsub_descriptionTV);
        subtaskDescriptionTV.setText(selectedSubtask.getDescription());

        // Set up (edit subtask) button to be associated with actions
        findViewById(R.id.ma_vsub_edit_button).setOnClickListener(this);
    }

    // Associate (edit subtask) button with actions
    @Override
    public void onClick(View v)
    {
        Intent intentChangeView;
        switch (v.getId())
        {
            case R.id.ma_vsub_edit_button:
                intentChangeView = new Intent(this, EditSubtaskActivity.class);
                intentChangeView.putExtra("subtaskToEditID", selectedSubtaskID);
                // Need related project ID for up buttons in child activity
                intentChangeView.putExtra("relatedProjectID", relatedProjectID);
                startActivity(intentChangeView);
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

    // Refresh selected subtask's details if they've been updated
    @Override
    protected void onResume()
    {
        super.onResume();
        // Update selected subtask
        dbc.openDatabase();
        selectedSubtask = SubtaskModel.getById(dbc, selectedSubtaskID);
        dbc.close();
        // Update nodes showing subtask's details
        subtaskNameTV.setText(selectedSubtask.getName());
        String newDueDateTV = "Due: " + selectedSubtask.getDueDate();
        subtaskDueDateTV.setText(newDueDateTV);
        subtaskDescriptionTV.setText(selectedSubtask.getDescription());
    }
}
