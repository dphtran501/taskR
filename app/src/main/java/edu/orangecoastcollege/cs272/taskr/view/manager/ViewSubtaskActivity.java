package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;

/**
 * Represents the activity view that allows the user to view the attributes of a specified subtask,
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
    DatabaseController dbc;

    TextView subtaskNameTV;
    TextView subtaskDueDateTV;
    TextView subtaskDescriptionTV;

    int subID;
    Subtask subtask;
    int relatedProjID;

    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_subtask);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve selected subtask and related project from ViewProjectActivity
        Bundle extras = getIntent().getExtras();
        subID = extras.getInt("subID");
        getIntent().removeExtra("subID");
        relatedProjID = extras.getInt("projID");
        getIntent().removeExtra("projID");
        dbc.openDatabase();
        subtask = SubtaskModel.getById(dbc, subID);
        dbc.close();

        // Set subtask name, due date, and description in text view
        subtaskNameTV = (TextView) findViewById(R.id.ma_vsub_nameTV);
        subtaskNameTV.setText(subtask.getName());
        subtaskDueDateTV = (TextView) findViewById(R.id.ma_vsub_dueDateTV);
        String newDueDateTV = "Due: " + subtask.getDueDate();
        subtaskDueDateTV.setText(newDueDateTV);
        subtaskDescriptionTV = (TextView) findViewById(R.id.ma_vsub_descriptionTV);
        subtaskDescriptionTV.setText(subtask.getDescription());

        // set up button to be associated with actions
        findViewById(R.id.ma_vsub_edit_button).setOnClickListener(this);
    }

    // Associate buttons with actions
    @Override
    public void onClick(View v)
    {
        Intent intentChangeView;
        switch (v.getId())
        {
            case R.id.ma_vsub_edit_button:
                intentChangeView = new Intent(this, EditSubtaskActivity.class);
                intentChangeView.putExtra("vsub_subID", subID);
                intentChangeView.putExtra("vsub_projID", relatedProjID);
                startActivity(intentChangeView);
                break;
        }
    }
}
