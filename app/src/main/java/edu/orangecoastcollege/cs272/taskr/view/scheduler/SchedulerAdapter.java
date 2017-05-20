package edu.orangecoastcollege.cs272.taskr.view.scheduler;

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
import edu.orangecoastcollege.cs272.taskr.model.scheduler.Template;

/**
 * Adapts a ListView to display contents from an ArrayList of objects.
 *
 * @author Vincent Hoang on 5/3/2017.
 */

public class SchedulerAdapter extends ArrayAdapter<Template> {

    private Context mContext;
    private List<Template> mTemplatesList = new ArrayList<>();
    private int mResourceId;


    /**
     * Constructor for the adapter
     * @param context context of where the adapter is used
     * @param resourceId reference ID of where the ListView is defined in the XML
     * @param templates list of templates to be displayed
     */
    public SchedulerAdapter(Context context, int resourceId, List<Template> templates) {
        super(context, resourceId, templates);
        mContext = context;
        mResourceId = resourceId;
        mTemplatesList = templates;
    }

    /**
     * <code>getView</code> binds an arraylist input with a ListView format from XML layout
     * @param pos position of the ListView object
     * @param convertView Converts default ListView object to format specified in XML
     * @param parent Parent listview
     * @return updated View (listview)
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        final Template selectedTemplate = mTemplatesList.get(pos);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout templateListLinearLayout = (LinearLayout) view.findViewById(R.id.scheduler_list_view_object);

        TextView name = (TextView) view.findViewById(R.id.scheduler_list_item_name);
        TextView desc = (TextView) view.findViewById(R.id.scheduler_list_item_desc);

        templateListLinearLayout.setTag(selectedTemplate);
        String templateName = selectedTemplate.getmName();
        String templateDesc = selectedTemplate.getmSummary();
        name.setText(templateName);
        desc.setText(templateDesc);

        return view;
    }
}
