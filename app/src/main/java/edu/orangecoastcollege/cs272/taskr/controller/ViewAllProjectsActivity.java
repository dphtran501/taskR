package edu.orangecoastcollege.cs272.taskr.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.R;

import edu.orangecoastcollege.cs272.taskr.model.manager.Project;
import edu.orangecoastcollege.cs272.taskr.model.manager.ProjectModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.RelatedSubtasksModel;
import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;
import edu.orangecoastcollege.cs272.taskr.model.manager.SubtaskModel;
import edu.orangecoastcollege.cs272.taskr.view.ProjectListAdapter;

/**
 * Represents the activity view that allows the user to view all <code>Project</code>s in the
 * database. The user can also select a <code>Project</code> in the list and choose to either view
 * the details of that <code>Project</code> or delete it from the database. In addition, the user
 * can add a new <code>Project</code> to the database.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-15
 */
public class ViewAllProjectsActivity extends AppCompatActivity implements View.OnClickListener
{

    static ProjectListAdapter adaptProject;
    private ListView allProjectsLV;
    static ArrayList<Project> allProjectsList;
    private Project selectedProject;
    DatabaseController dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_home);

        // list and list view
        dbc = DatabaseController.getInstance(this);

        dbc.openDatabase();
        allProjectsList = ProjectModel.getAllProjects(dbc);
        dbc.close();


        allProjectsLV = (ListView) findViewById(R.id.ma_projectsLV);
        adaptProject = new ProjectListAdapter(this, R.layout.ma_project_list_item, allProjectsList);
        allProjectsLV.setAdapter(adaptProject);
        // handle list view item clicks
        allProjectsLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectedProject = allProjectsList.get(position);
                //Context context = ViewAllProjectsActivity.this;
                //Toast.makeText(context, String.valueOf(selectedProject.getID()), Toast.LENGTH_LONG).show();
            }
        });

        // set up buttons to be associated with actions
        findViewById(R.id.ma_view_project_button).setOnClickListener(this);
        findViewById(R.id.ma_add_project_button).setOnClickListener(this);
        findViewById(R.id.ma_delete_project_button).setOnClickListener(this);
    }

    // Associate buttons with actions
    @Override
    public void onClick(View v)
    {
        Intent intentChangeView;
        switch (v.getId())
        {
            case R.id.ma_view_project_button:
                if (allProjectsLV.getSelectedItem() != null)
                {
                    //Project selectedProject = getSelectedProject();
                    intentChangeView = new Intent(this, ViewProjectActivity.class);
                    intentChangeView.putExtra("projectID", selectedProject.getID());
                    intentChangeView.putExtra("projectName", selectedProject.getName());
                    intentChangeView.putExtra("projectDescription", selectedProject.getDescription());
                    intentChangeView.putExtra("projectDueDate", selectedProject.getDueDate());
                    intentChangeView.putExtra("projectHasSubtasks", selectedProject.hasSubtasks());
                    startActivity(intentChangeView);
                }
                break;
            case R.id.ma_add_project_button:
                intentChangeView = new Intent(this, AddProjectActivity.class);
                startActivity(intentChangeView);
                break;
            case R.id.ma_delete_project_button:
                if (selectedProject != null)
                {
                    deleteProjectFromDB(selectedProject);
                    selectedProject = null;
                }
                break;
        }
    }


    /**
     * Gets the selected <code>Project</code> in the list view.
     * @return Selected <code>Project</code> in the list view.
     */
    /*
    private Project getSelectedProject()
    {
        int projectPos = allProjectsLV.getSelectedItemPosition();
        return allProjectsList.get(projectPos);
    }
    */

    /**
     * Deletes a project and any related subtasks from the database.
     * @param p <code>Project</code> to delete.
     */
    private void deleteProjectFromDB(Project p)
    {
        // Handle deletion of project's subtasks and their respective relations in the database
        if (p.hasSubtasks())
        {
            // Retrieve all related subtasks before deleting their relations in the relation table
            dbc.openDatabase();
            ArrayList<Subtask> relatedSubtasks = RelatedSubtasksModel.getSubsOfProj(dbc, p);
            dbc.close();

            dbc.openDatabase();
            RelatedSubtasksModel.deleteSubsOfProj(dbc, p.getID());
            dbc.close();

            // Delete related subtasks from database
            if (!relatedSubtasks.isEmpty())

                for (Subtask s : relatedSubtasks) {
                    SubtaskModel.deleteSubtask(dbc, s);
                }

        }

        // Delete project from database
        dbc.openDatabase();
        ProjectModel.deleteProject(dbc, p);
        dbc.close();

        // Reset list and list view
        allProjectsList.remove(p);
        adaptProject.notifyDataSetChanged();
    }
}
