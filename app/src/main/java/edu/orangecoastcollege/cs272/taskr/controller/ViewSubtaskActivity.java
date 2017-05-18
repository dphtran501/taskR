package edu.orangecoastcollege.cs272.taskr.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.model.DBHelper;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;

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
    private DBHelper db;

    TextView subtaskNameTV;
    TextView subtaskDueDateTV;
    TextView subtaskDescriptionTV;

    int subID;
    Subtask subtask;

    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_subtask);

        // Initialize dbhelper
        db = new DBHelper(this);

        // Retrieve selected subtask fields from ViewProjectActivity
        Bundle extras = getIntent().getExtras();
        subID = extras.getInt("subID");
        //subtask = db.getSubtask(subID);

        // Set subtask name, due date, and description in text view
        subtaskNameTV = (TextView) findViewById(R.id.ma_vsub_nameTV);
        subtaskDueDateTV = (TextView) findViewById(R.id.ma_vsub_dueDateTV);
        subtaskDescriptionTV = (TextView) findViewById(R.id.ma_vsub_descriptionTV);
        subtaskNameTV.setText(subtask.getName());
        String newDueDateTV = "Due: " + subtask.getDueDate();
        subtaskDueDateTV.setText(newDueDateTV);
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
                intentChangeView.putExtra("subID", subtask.getID());
                startActivity(intentChangeView);
                break;
        }
    }
}
