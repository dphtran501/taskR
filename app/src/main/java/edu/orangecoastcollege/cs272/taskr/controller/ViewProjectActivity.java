package edu.orangecoastcollege.cs272.taskr.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.model.DBHelper;
import edu.orangecoastcollege.cs272.taskr.model.Project;
import edu.orangecoastcollege.cs272.taskr.model.Subtask;
import edu.orangecoastcollege.cs272.taskr.view.SubtaskListAdapter;

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

public class ViewProjectActivity extends AppCompatActivity implements View.OnClickListener
{
    private DBHelper db;

    static SubtaskListAdapter adaptSubtask;
    private ListView allSubsOfProjectLV;
    static ArrayList<Subtask> allSubsOfProjectList;

    private Subtask selectedSubtask;

    TextView projectNameTV;
    TextView projectDueDateTV;
    TextView projectDescriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_project);

        // Retrieve selected project fields from ViewAllProjectsActivity
        Bundle extras = getIntent().getExtras();
        int projectID = extras.getInt("projectID");
        String projectName = extras.getString("projectName");
        String projectDueDate = extras.getString("projectDueDate");
        String projectDescription = extras.getString("projectDescription");
        boolean projectHasSubtasks = extras.getBoolean("projectHasSubtasks");
        Project project = new Project(projectID, projectName, projectDescription, projectDueDate, projectHasSubtasks);

        // Set project name, due date, and description in text view
        projectNameTV = (TextView) findViewById(R.id.ma_vproj_nameTV);
        projectDueDateTV = (TextView) findViewById(R.id.ma_vproj_dueDateTV);
        projectDescriptionTV = (TextView) findViewById(R.id.ma_vproj_descriptionTV);
        projectNameTV.setText(projectName);
        String newDueDateTV = "Due: " + projectDueDate;
        projectDueDateTV.setText(newDueDateTV);
        projectDescriptionTV.setText(projectDescription);

        // list and list view
        db = new DBHelper(this);
        allSubsOfProjectList = db.getSubsOfProj(project);
        allSubsOfProjectLV = (ListView) findViewById(R.id.ma_projectSubsLV);
        adaptSubtask = new SubtaskListAdapter(this, R.layout.ma_subtask_list_item, allSubsOfProjectList);
        allSubsOfProjectLV.setAdapter(adaptSubtask);
        // handle list view item clicks
        allSubsOfProjectLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectedSubtask = allSubsOfProjectList.get(position);
                //Context context = ViewAllProjectsActivity.this;
                //Toast.makeText(context, String.valueOf(selectedProject.getID()), Toast.LENGTH_LONG).show();
            }
        });

        // set up buttons to be associated with actions
        findViewById(R.id.ma_vproj_edit_button).setOnClickListener(this);
        findViewById(R.id.ma_vproj_view_button).setOnClickListener(this);
        findViewById(R.id.ma_vproj_add_button).setOnClickListener(this);
        findViewById(R.id.ma_vproj_delete_button).setOnClickListener(this);
    }

    // Associate buttons with actions
    @Override
    public void onClick(View v)
    {

    }
}
