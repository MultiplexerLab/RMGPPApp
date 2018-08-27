package ipa.rmgppapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.WorkerAssignAdapter;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.PlanningData;
import ipa.rmgppapp.model.ProcessItem;
import ipa.rmgppapp.model.Worker;

public class WorkerAssignActivity extends AppCompatActivity {

    ListView listViewProcess;
    ArrayList<ProcessItem> processItemArrayList;
    ArrayList<Worker> workerList;
    ArrayList<String> workerIdList;
    WorkerAssignAdapter adapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_assign);

        queue = Volley.newRequestQueue(this);
        listViewProcess = findViewById(R.id.listViewProcess);
        processItemArrayList = new ArrayList<>();
        workerList = new ArrayList<>();
        workerIdList = new ArrayList<>();
        getAllWorkerId();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 4000);
    }

    private void setData() {
        SharedPreferences sharedPreferences = getSharedPreferences("supervisor", MODE_PRIVATE);
        final String description = sharedPreferences.getString("description", "");
        Log.i("description", description);

        if(workerList.size()>0) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_OPERATION_DATA_URL + "?description=" + description, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("getPlanning", response.toString());
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ProcessItem>>() {
                    }.getType();
                    processItemArrayList = gson.fromJson(response.toString(), type);
                    adapter = new WorkerAssignAdapter(WorkerAssignActivity.this, processItemArrayList, workerList, workerIdList);
                    listViewProcess.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorVolley", error.toString());
                }
            });
            queue.add(jsonArrayRequest);
        }
    }

    private void getAllWorkerId() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_HR_DATA_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("responseWorker", response.toString());

                try{
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Worker>>() {
                    }.getType();
                    workerList = gson.fromJson(response.toString(), type);
                    Log.i("workerList", workerList.toString());
                }catch (Exception e){
                    Log.e("WorkerErr", e.toString());
                }
                for(int i=0; i<response.length(); i++){
                    try {
                        String workerId = response.getJSONObject(i).getString("workerId");
                        Log.i("WorkerId", workerId);
                        workerIdList.add(workerId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorVolley", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void cancelWorkerAssign(View view) {
        finish();
    }

    public void continueWorkerAssign(View view) {
        adapter.saveData();
        finish();
    }
}
