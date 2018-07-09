package ipa.rmgppapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.WorkerAssignAdapter;
import ipa.rmgppapp.model.ProcessItem;

public class WorkerAssignActivity extends AppCompatActivity {

    ListView listViewProcess;
    ArrayList<ProcessItem> processItemArrayList;
    WorkerAssignAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_assign);

        listViewProcess = findViewById(R.id.listViewProcess);
        processItemArrayList = new ArrayList<>();
        setData();
        adapter = new WorkerAssignAdapter(processItemArrayList, this);
        listViewProcess.setAdapter(adapter);
    }

    private void setData() {
        processItemArrayList.add(new ProcessItem("Process\nName", "Machine\nType", "Hourly\nTarget", "Assigned\nWorker Id" ));
        processItemArrayList.add(new ProcessItem("Side Join", "Plain Machine", "100", "" ));
        processItemArrayList.add(new ProcessItem("Neck Join", "Fade Lock", "100", "" ));
        processItemArrayList.add(new ProcessItem("Side Join", "Plain Machine", "100", "" ));
        processItemArrayList.add(new ProcessItem("Neck Join", "Fade Lock", "100", "" ));
        processItemArrayList.add(new ProcessItem("Side Join", "Plain Machine", "100", "" ));
        processItemArrayList.add(new ProcessItem("Neck Join", "Fade Lock", "100", "" ));
        processItemArrayList.add(new ProcessItem("Side Join", "Plain Machine", "100", "" ));
        processItemArrayList.add(new ProcessItem("Neck Join", "Fade Lock", "100", "" ));
        processItemArrayList.add(new ProcessItem("Side Join", "Plain Machine", "100", "" ));
        processItemArrayList.add(new ProcessItem("Neck Join", "Fade Lock", "100", "" ));
        processItemArrayList.add(new ProcessItem("Side Join", "Plain Machine", "100", "" ));
    }

    public void cancelWorkerAssign(View view) {
        finish();
    }

    public void continueWorkerAssign(View view) {
        Intent intent = new Intent(this, ProductionActivity.class);
        startActivity(intent);
    }
}
