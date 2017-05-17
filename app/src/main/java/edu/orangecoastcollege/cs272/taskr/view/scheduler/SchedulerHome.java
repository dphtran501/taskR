package edu.orangecoastcollege.cs272.taskr.view.scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import edu.orangecoastcollege.cs272.taskr.controller.Controller;
import edu.orangecoastcollege.cs272.taskr.model.DBHelper;
import edu.orangecoastcollege.cs272.taskr.model.Template;
import edu.orangecoastcollege.cs272.taskr.view.scheduler.TemplateListAdapter;

import java.util.ArrayList;

import edu.orangecoastcollege.cs272.taskr.R;

public class SchedulerHome extends AppCompatActivity implements View.OnClickListener {

    TemplateListAdapter adaptTemplate;
    ListView allTemplatesListLV;
    ArrayList<Template> allTemplatesList;
    Controller controller = Controller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduler_home);
        DBHelper db = new DBHelper(this);
        controller.setmDB(db);

        allTemplatesList = controller.getAllTemplates();
        allTemplatesListLV = (ListView) findViewById(R.id.schedulerListView);
        adaptTemplate = new TemplateListAdapter(this, R.layout.scheduler_list_item, allTemplatesList);
        allTemplatesListLV.setAdapter(adaptTemplate);

        findViewById(R.id.scheduler_add_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scheduler_add_button:
                Intent intent = new Intent(this, SchedulerAddTemplate.class);
                startActivity(intent);
                break;
        }
    }
}
