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

public class WorkerAssignAdapter extends BaseAdapter{

    Context context;
    ArrayList<ProcessItem> processItemArrayList;

    public WorkerAssignAdapter(Context context, ArrayList<ProcessItem> processItemArrayList) {
        this.context = context;
        this.processItemArrayList = processItemArrayList;
    }

    @Override
    public int getCount() {
        return processItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom_view = inflater.inflate(R.layout.worker_assign_custom_layout, null);

        TextView processName, machineType, hourlyTarget;
        EditText workerId;
        processName = custom_view.findViewById(R.id.processName);
        machineType = custom_view.findViewById(R.id.machineType);
        hourlyTarget = custom_view.findViewById(R.id.hourlyTarget);
        workerId = custom_view.findViewById(R.id.workerId);

        processName.setText(processItemArrayList.get(position).getProcessName());
        machineType.setText(processItemArrayList.get(position).getMachineType());
        hourlyTarget.setText(processItemArrayList.get(position).getHourlyTarget());

        return custom_view;
    }
}
