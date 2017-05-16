package edu.orangecoastcollege.cs272.taskr.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.model.Template;

/**
 * Created by vietn on 5/3/2017.
 */

public class SchedulerAdapter extends ArrayAdapter<Template> {

    private Context mContext;
    private List<Template> mTemplatesList = new ArrayList<>();
    private int mResourceId;

    public SchedulerAdapter(Context context, int resourceId, List<Template> templates) {
        super(context, resourceId, templates);
        mContext = context;
        mResourceId = resourceId;
        mTemplatesList = templates;
    }

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
