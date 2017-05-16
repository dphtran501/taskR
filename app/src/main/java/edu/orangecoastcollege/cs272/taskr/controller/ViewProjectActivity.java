package edu.orangecoastcollege.cs272.taskr.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import edu.orangecoastcollege.cs272.taskr.R;

/**
 * Represents the activity view that allows the user to view the attributes of a specified project,
 * specifically its name, description, and due date.
 *
 * Additionally, the user can choose to view the list of subtasks related to the project, edit the
 * project, or delete the project and its related subtasks.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-14
 */

public class ViewProjectActivity extends AppCompatActivity
{
    TextView dueDateTV;
    TextView descriptionTV;

    // TODO: need to retrieve selected item from projects list view

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_project);

    }
}
