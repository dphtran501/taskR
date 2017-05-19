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
 * Represents the activity view that allows the user to view the details of a specified project,
 * specifically its name, description, due date, and its list of subtasks (if any).
 *
 * Additionally, the user can choose to edit the project, add subtasks, delete subtasks, and view
 * the details of a selected subtask of the project.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-14
 */

public class ViewProjectActivity extends AppCompatActivity implements View.OnClickListener
{
    // Controller
    DatabaseController dbc;

    // Project's subtask array list, the list view containing the array list, and the adapter that links them
    static ArrayList<Subtask> allSubsOfProjectList;
    static SubtaskListAdapter adaptSubtask;
    private ListView allSubsOfProjectLV;

    // Subtask selected in the list view
    private Subtask selectedSubtask;

    // Nodes
    TextView projectNameTV;
    TextView projectDueDateTV;
    TextView projectDescriptionTV;

    // Selected project from ViewAllProjectsActivity
    int selectedProjectID;
    Project selectedProject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_view_project);

        // Controller instance
        dbc = DatabaseController.getInstance(this);

        // Retrieve selected project ID from ViewAllProjectsActivity
        selectedProjectID = getIntent().getIntExtra("selectedProjectID", -1); // -1 if no value in "selectedProjectID"
        getIntent().removeExtra("selectedProjectID");
        // Use selected project ID to construct selected project from database
        dbc.openDatabase();
        selectedProject = ProjectModel.getById(dbc, selectedProjectID);
        dbc.close();

        // Set project name, due date, and description in TextView to those of selected project
        projectNameTV = (TextView) findViewById(R.id.ma_vproj_nameTV);
        projectNameTV.setText(selectedProject.getName());
        projectDueDateTV = (TextView) findViewById(R.id.ma_vproj_dueDateTV);
        String newDueDateTV = "Due: " + selectedProject.getDueDate();
        projectDueDateTV.setText(newDueDateTV);
        projectDescriptionTV = (TextView) findViewById(R.id.ma_vproj_descriptionTV);
        projectDescriptionTV.setText(selectedProject.getDescription());

        // List of subtasks related to project (if any)
        dbc.openDatabase();
        allSubsOfProjectList = RelatedSubtasksModel.getSubsOfProj(dbc, selectedProject);
        dbc.close();

        // List view of subtasks related to project
        allSubsOfProjectLV = (ListView) findViewById(R.id.ma_projectSubsLV);
        adaptSubtask = new SubtaskListAdapter(this, R.layout.ma_subtask_list_item, allSubsOfProjectList);
        allSubsOfProjectLV.setAdapter(adaptSubtask);

        // Get selected subtask when it's clicked in list view
        allSubsOfProjectLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectedSubtask = allSubsOfProjectList.get(position);
            }
        });

        // Set up buttons to be associated with actions
        findViewById(R.id.ma_vproj_edit_button).setOnClickListener(this);   // edit project
        findViewById(R.id.ma_vproj_view_button).setOnClickListener(this);   // view subtask
        findViewById(R.id.ma_vproj_add_button).setOnClickListener(this);    // add subtask
        findViewById(R.id.ma_vproj_delete_button).setOnClickListener(this); // delete subtask
    }

    // Associate buttons with actions
    @Override
    public void onClick(View v)
    {
        Intent intentChangeView;
        switch (v.getId())
        {
            // Edit project
            case R.id.ma_vproj_edit_button:
                intentChangeView = new Intent(this, EditProjectActivity.class);
                intentChangeView.putExtra("projectToEditID", selectedProjectID);
                startActivity(intentChangeView);
                break;
            // View selected subtask
            case R.id.ma_vproj_view_button:
                if (selectedSubtask != null)
                {
                    intentChangeView = new Intent(this, ViewSubtaskActivity.class);
                    intentChangeView.putExtra("selectedSubtaskID", selectedSubtask.getID());
                    // Need related project ID for up buttons in child activities
                    intentChangeView.putExtra("relatedProjectID", selectedProjectID);
                    startActivity(intentChangeView);
                }
                break;
            // Add new subtask
            case R.id.ma_vproj_add_button:
                intentChangeView = new Intent(this, AddSubtaskActivity.class);
                intentChangeView.putExtra("relatedProjectID", selectedProjectID);
                startActivity(intentChangeView);
                break;
            // Delete selected subtask
            case R.id.ma_vproj_delete_button:
                if (selectedSubtask != null)
                {
                    deleteSubtaskFromDB(selectedSubtask);
                    // Update list and list view of subtasks related to project
                    allSubsOfProjectList.remove(selectedSubtask);
                    adaptSubtask.notifyDataSetChanged();
                    selectedSubtask = null;
                    // Set related project's "hasSubtasks" field to false if there's no subtasks left
                    if(allSubsOfProjectList.size() == 0)
                    {
                        selectedProject.setSubtasks(false);
                        // Update project in database
                        dbc.openDatabase();
                        ProjectModel.updateProject(dbc,selectedProject);
                        dbc.close();
                    }
                }
                break;
        }
    }

    // Refresh list and list view of project's subtasks when returning to this activity
    // Also refresh selected project's details if they've been updated
    @Override
    protected void onResume()
    {
        super.onResume();
        // Update selected project
        dbc.openDatabase();
        selectedProject = ProjectModel.getById(dbc, selectedProjectID);
        dbc.close();
        // Update nodes showing project's details
        projectNameTV.setText(selectedProject.getName());
        String newDueDateTV = "Due: " + selectedProject.getDueDate();
        projectDueDateTV.setText(newDueDateTV);
        projectDescriptionTV.setText(selectedProject.getDescription());
        // Update list and list view of project's subtasks
        dbc.openDatabase();
        allSubsOfProjectList.clear();
        allSubsOfProjectList.addAll(RelatedSubtasksModel.getSubsOfProj(dbc, selectedProject));
        dbc.close();
        adaptSubtask.notifyDataSetChanged();
    }

    /**
     * Deletes a subtask from the database.
     * @param s <code>Subtask</code> to delete.
     */
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
    }

}
