package edu.orangecoastcollege.cs272.taskr.view.manager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.orangecoastcollege.cs272.taskr.R;

import edu.orangecoastcollege.cs272.taskr.model.manager.Project;

/**
 * Helper class to provide custom adapter for the <code>Project</code> list.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-15
 */
public class ProjectListAdapter extends ArrayAdapter<Project>
{
    private Context mContext;
    private List<Project> mProjectsList = new ArrayList<>();
    private int mResourceID;

    /**
     * Creates a new <code>ProjectListAdapter</code> given a context, resource id, and list of projects.
     *
     * @param context Context for which adapter is being used (typically activity)
     * @param resourceID Resource ID (typically the layout file name)
     * @param projects List of projects to display
     */
    public ProjectListAdapter(Context context, int resourceID, List<Project> projects)
    {
        super(context, resourceID, projects);
        mContext = context;
        mResourceID = resourceID;
        mProjectsList = projects;
    }

    /**
     * Gets the view associated with the layout.
     * @param pos Position of the Project selected in the list.
     * @param convertView Converted view
     * @param parent Parent (ArrayAdapter)
     * @return New view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        // Selected project in project list
        final Project selectedProject = mProjectsList.get(pos);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceID, null);

        // layout of item in project list view
        LinearLayout projectListLinearLayout = (LinearLayout) view.findViewById(R.id.ma_project_LV_item);

        // Temporary project name and due date in list view to set below
        TextView projectNameTV = (TextView) view.findViewById(R.id.ma_project_LV_item_name);
        TextView projectDueDateTV = (TextView) view.findViewById(R.id.ma_project_LV_item_dueDate);
        // Set project name and due date in list view with that of selected project
        projectListLinearLayout.setTag(selectedProject);
        projectNameTV.setText(selectedProject.getName());
        String newDueDateTV = "Due: " + selectedProject.getDueDate();
        projectDueDateTV.setText(newDueDateTV);

        return view;
    }
}
