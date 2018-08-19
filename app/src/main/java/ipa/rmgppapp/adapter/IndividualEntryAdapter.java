package ipa.rmgppapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.HourlyEntry;
import ipa.rmgppapp.model.ProcessItem;

import static android.content.Context.MODE_PRIVATE;

public class IndividualEntryAdapter extends RecyclerView.Adapter<IndividualEntryAdapter.MyViewHolder> {

    private ArrayList<ProcessItem> processItems;
    Context context;
    String times[] = {"9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm"};

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView workerId, workerName, processName;
        EditText cuttingSlStart, cuttingSlEnd, hourlyOutput;
        Spinner spinnerTime;
        Button saveIndividualEntry;

        public MyViewHolder(View view) {
            super(view);
            spinnerTime = view.findViewById(R.id.spinnerTime);
            workerId = view.findViewById(R.id.workerId);
            workerName = view.findViewById(R.id.workerName);
            processName = view.findViewById(R.id.processName);
            /*cuttingSlStart = view.findViewById(R.id.cuttingSlNo);
            cuttingSlEnd = view.findViewById(R.id.cuttingSlNo2);*/
            hourlyOutput = view.findViewById(R.id.hourlyOutput);
            saveIndividualEntry = view.findViewById(R.id.saveIndividualEntry);
            spinnerTime.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, times));

            saveIndividualEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(hourlyOutput.getText().toString().isEmpty()){
                        Toast.makeText(context, "Insert hourly output!", Toast.LENGTH_SHORT).show();
                    }else {
                        //int quantity = calculateQuantity(cuttingSlStart.getText().toString(), cuttingSlEnd.getText().toString());
                        HourlyEntry obj = new HourlyEntry(spinnerTime.getSelectedItem().toString(), workerId.getText().toString(), workerName.getText().toString(),
                                processName.getText().toString(),
                                Integer.parseInt(hourlyOutput.getText().toString()));
                        saveIndividualEntry(obj);
                    }
                }
            });
        }
    }

    private int calculateQuantity(String cuttingSlStart, String cuttingSlEnd) {
        int quantity = 0;
        String styleNo1 = cuttingSlStart.substring(0, 2);
        String size1 = cuttingSlStart.substring(3, 3);
        int quantity1 = Integer.parseInt(cuttingSlStart.substring(4, 6));
        String styleNo2 = cuttingSlEnd.substring(0, 2);
        String size2 = cuttingSlEnd.substring(3, 3);
        int quantity2 = Integer.parseInt(cuttingSlEnd.substring(4, 6));

        if(size1.equals(size2)){
            if(quantity1<quantity2){
                quantity = (quantity2 - quantity1)+1;
            }else{
                quantity = (quantity1 - quantity2)+1;
            }
        }
        return quantity;
    }

    private void saveIndividualEntry(final HourlyEntry obj) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_HOURLY_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("SUCCESS")){
                    Toast.makeText(context, "Data is saved!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("InsertIndividualEntry", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String jsonString = "";
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    jsonString = gson.toJson(obj);

                } catch (Exception e) {
                    Log.e("ArrayException", e.toString());
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("supervisor", MODE_PRIVATE);
                String supervisor = sharedPreferences.getString("supervisorId", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("jsonString", jsonString);
                params.put("supervisor", supervisor);
                Log.i("jsonString", jsonString);
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public IndividualEntryAdapter(Context context, ArrayList<ProcessItem> processItems) {
        this.processItems = processItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receycler_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProcessItem processItem = processItems.get(position);
        holder.workerId.setText(processItem.getAssignedWorkerId());
        holder.workerName.setText(processItem.getAssignedWorkerName());
        holder.processName.setText(processItem.getProcessName());
    }

    @Override
    public int getItemCount() {
        return processItems.size();
    }
}
