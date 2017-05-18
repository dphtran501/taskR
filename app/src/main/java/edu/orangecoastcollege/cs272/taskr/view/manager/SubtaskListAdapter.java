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

import edu.orangecoastcollege.cs272.taskr.model.manager.Subtask;

/**
 * Helper class to provide custom adapter for the <code>Subtask</code> list.
 *
 * @author  Derek Tran
 * @version 1.0
 * @since   2017-05-15
 */
public class SubtaskListAdapter extends ArrayAdapter<Subtask>
{
    private Context mContext;
    private List<Subtask> mSubtasksList = new ArrayList<>();
    private int mResourceID;

    /**
     * Creates a new <code>SubtaskListAdapter</code> given a context, resource id, and list of subtasks.
     *
     * @param context Context for which adapter is being used (typically activity)
     * @param resourceID Resource ID (typically the layout file name)
     * @param subtasks List of subtasks to display
     */
    public SubtaskListAdapter(Context context, int resourceID, List<Subtask> subtasks)
    {
        super(context, resourceID, subtasks);
        mContext = context;
        mResourceID = resourceID;
        mSubtasksList = subtasks;
    }

    /**
     * Gets the view associated with the layout.
     * @param pos Position of the Subtask selected in the list.
     * @param convertView Converted view
     * @param parent Parent (ArrayAdapter)
     * @return New view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        // Selected subtask in subtask list
        final Subtask selectedSubtask = mSubtasksList.get(pos);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceID, null);

        // layout of item in subtask list view
        LinearLayout subtaskListLinearLayout = (LinearLayout) view.findViewById(R.id.ma_subtask_LV_item);

        // Temporary subtask name and due date in list view to set below
        TextView subtaskNameTV = (TextView) view.findViewById(R.id.ma_subtask_LV_item_name);
        TextView subtaskDueDateTV = (TextView) view.findViewById(R.id.ma_subtask_LV_item_dueDate);
        // Set subtask name and due date in list view with that of the selected subtask
        subtaskListLinearLayout.setTag(selectedSubtask);
        subtaskNameTV.setText(selectedSubtask.getName());
        String newDueDateTV = "Due: " + selectedSubtask.getDueDate();
        subtaskDueDateTV.setText(newDueDateTV);

        return view;
    }
}
