package ipa.rmgppapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ipa.rmgppapp.R;
import ipa.rmgppapp.model.ProcessItem;

public class WorkerAssignAdapter extends BaseAdapter {

    ArrayList<ProcessItem> processItemArrayList;
    Context context;

    public WorkerAssignAdapter(ArrayList<ProcessItem> processItemArrayList, Context context) {
        this.processItemArrayList = processItemArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return processItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return processItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.worker_assign_custom_layout, null);
        TextView processName = customView.findViewById(R.id.processName);
        TextView machineType = customView.findViewById(R.id.machineType);
        TextView hourlyTarget = customView.findViewById(R.id.hourlyTarget);
        EditText workerId = customView.findViewById(R.id.workerId);

        processName.setText(processItemArrayList.get(position).getProcessName());
        machineType.setText(processItemArrayList.get(position).getMachineType());
        hourlyTarget.setText(processItemArrayList.get(position).getHourlyTarget());
        if(position==0)
        {
            workerId.setEnabled(false);
            workerId.setText(processItemArrayList.get(0).getAssignedWorkerId());
        }


        return customView;
    }
}
