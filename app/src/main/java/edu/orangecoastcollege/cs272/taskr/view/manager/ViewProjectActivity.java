package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.controller.DatabaseController;
import edu.orangecoastcollege.cs272.taskr.model.manager.Project;
import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.RelatedSubtasksModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;

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

    DatabaseController dbc;

    static SubtaskListAdapter adaptSubtask;
    private ListView allSubsOfProjectLV;
    static ArrayList<Subtask> allSubsOfProjectList;

    private Subtask selectedSubtask;

    TextView projectNameTV;
    TextView projectDueDateTV;
    TextView projectDescriptionTV;

    int projectID;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_project);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve selected project from ViewAllProjectsActivity
        Bundle extras = getIntent().getExtras();
        projectID = extras.getInt("projectID");
        getIntent().removeCategory("projectID");
        dbc.openDatabase();
        project = ProjectModel.getById(dbc, projectID);
        dbc.close();

        // Set project name, due date, and description in TextView to those of selected project
        projectNameTV = (TextView) findViewById(R.id.ma_vproj_nameTV);
        projectNameTV.setText(project.getName());
        projectDueDateTV = (TextView) findViewById(R.id.ma_vproj_dueDateTV);
        String newDueDateTV = "Due: " + project.getDueDate();
        projectDueDateTV.setText(newDueDateTV);
        projectDescriptionTV = (TextView) findViewById(R.id.ma_vproj_descriptionTV);
        projectDescriptionTV.setText(project.getDescription());

        // List of subtasks related to project
        dbc.openDatabase();
        allSubsOfProjectList = RelatedSubtasksModel.getSubsOfProj(dbc, project);
        dbc.close();

        // List view of subtasks related to project
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
        Intent intentChangeView;
        switch (v.getId())
        {
            case R.id.ma_vproj_edit_button:
                break;
            case R.id.ma_vproj_view_button:
                if (selectedSubtask != null)
                {
                    intentChangeView = new Intent(this, ViewSubtaskActivity.class);
                    intentChangeView.putExtra("subID", selectedSubtask.getID());
                    intentChangeView.putExtra("projID", projectID);
                    startActivity(intentChangeView);
                }
                break;
            case R.id.ma_vproj_add_button:
                intentChangeView = new Intent(this, AddSubtaskActivity.class);
                intentChangeView.putExtra("projID", projectID);
                startActivity(intentChangeView);
                break;
            case R.id.ma_vproj_delete_button:
                if (selectedSubtask != null)
                {
                    deleteSubtaskFromDB(selectedSubtask);
                    selectedSubtask = null;
                    // set hasSubtasks field of project to false if there's no subtasks left
                    if(allSubsOfProjectList.size() == 0)
                    {
                        project.setSubtasks(false);
                        // update project in database
                        dbc.openDatabase();
                        ProjectModel.updateProject(dbc, project);
                        dbc.close();
                        // update list and listview for allProjectsList
                        dbc.openDatabase();
                        ViewAllProjectsActivity.allProjectsList = ProjectModel.getAllProjects(dbc);
                        dbc.close();
                        ViewAllProjectsActivity.adaptProject.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    private void deleteSubtaskFromDB(Subtask s)
    {
        // Delete related project-subtask relationship from database
        dbc.openDatabase();
        RelatedSubtasksModel.deleteProjSub(dbc, s.getID());
        dbc.close();

        // Delete subtask from database
        dbc.openDatabase();
        SubtaskModel.deleteSubtask(dbc, s);
        dbc.close();

        // Reset list and list view
        allSubsOfProjectList.remove(s);
        adaptSubtask.notifyDataSetChanged();

    }

}
