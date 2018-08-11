package ipa.rmgppapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ipa.rmgppapp.R;
import ipa.rmgppapp.activity.ProductionActivity;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.ProcessItem;
import ipa.rmgppapp.model.Worker;

import static android.content.Context.MODE_PRIVATE;

public class WorkerAssignAdapter extends BaseAdapter {

    Context context;
    ArrayList<ProcessItem> processItemArrayList;
    ArrayList<Worker> workerArrayList;
    ArrayList<String> workerIdList;

    public WorkerAssignAdapter(Context context, ArrayList<ProcessItem> processItemArrayList, ArrayList<Worker> workerArrayList, ArrayList<String> workerIdList) {
        this.context = context;
        this.processItemArrayList = processItemArrayList;
        this.workerArrayList = workerArrayList;
        this.workerIdList = workerIdList;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view != null) return view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom_view = inflater.inflate(R.layout.worker_assign_custom_layout, null);

        final TextView processName, machineType, hourlyTarget;
        processName = custom_view.findViewById(R.id.processName);
        machineType = custom_view.findViewById(R.id.machineType);
        hourlyTarget = custom_view.findViewById(R.id.hourlyTarget);
        //Button buttonSaveWorkerAssign = custom_view.findViewById(R.id.buttonSaveWorkerAssign);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, workerIdList);
        final AutoCompleteTextView workerIdView = (AutoCompleteTextView)
                custom_view.findViewById(R.id.workerId);
        workerIdView.setAdapter(adapter);

        workerIdView.setText(processItemArrayList.get(position).getAssignedWorkerId());

        processName.setText(processItemArrayList.get(position).getProcessName());
        machineType.setText(processItemArrayList.get(position).getMachineType());
        hourlyTarget.setText(Math.round(processItemArrayList.get(position).getHourlyTarget()) + "");

        /*if(position==(processItemArrayList.size()-1)){
            buttonSaveWorkerAssign.setVisibility(View.VISIBLE);
        }

        buttonSaveWorkerAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });*/

        workerIdView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence value, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!workerIdView.getText().toString().isEmpty()) {
                    try {
                        processItemArrayList.get(position).setAssignedWorkerId(s.toString());
                        Worker worker = getWorkerInfo(s.toString());
                        processItemArrayList.get(position).setAssignedWorkerName(worker.getName());
                        processItemArrayList.get(position).setHourlyTarget((double) Math.round(processItemArrayList.get(position).getHourlyTarget()));
                    }catch (Exception e){
                        Log.e("ArrayListErr", e.toString());
                    }
                } else {
                    Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return custom_view;
    }

    private Worker getWorkerInfo(String s) {
        Worker worker = null;
        for(int i=0; i<workerArrayList.size(); i++){
            if(workerArrayList.get(i).getWorkerId().contains(s)){
                worker = workerArrayList.get(i);
            }
        }
        return worker;
    }

    public void saveData() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_ASSIGNED_WORKER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("responseWorker", response);
                if (response.contains("Error")) {

                } else {
                    Gson gson = new Gson();
                    String jsonProcess = gson.toJson(processItemArrayList);
                    SharedPreferences.Editor editor = context.getSharedPreferences("hourlyEntry", MODE_PRIVATE).edit();
                    editor.putString("data", jsonProcess);
                    editor.commit();

                    Intent intent = new Intent(context, ProductionActivity.class);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorInWorkerAssign", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                /*ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
                for(int i=0; i<processItemArrayList.size(); i++){
                    JSONObject jsonObject = new JSONObject(processItemArrayList.get(i));
                    jsonObjectArrayList.add(i, );
                }*/
                String jsonArrayString = "";

                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    jsonArrayString = gson.toJson(processItemArrayList);

                } catch (Exception e) {
                    Log.e("ArrayException", e.toString());
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("supervisor", MODE_PRIVATE);
                String description = sharedPreferences.getString("description", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("jsonArrayString", jsonArrayString);
                params.put("tag", description);
                Log.i("jsonArrayString", jsonArrayString);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
