package ipa.rmgppapp.fragment;

import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ipa.rmgppapp.R;
import ipa.rmgppapp.activity.StyleListActivity;
import ipa.rmgppapp.helper.DateTimeInstance;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.HourlyEntry;
import ipa.rmgppapp.model.LineEntry;
import ipa.rmgppapp.model.PlanningData;

import static android.content.Context.MODE_PRIVATE;

public class LineEntryFragment extends Fragment {

    ArrayList<String> problems, statusList;
    String times[] = {"Hour 1", "Hour 2", "Hour 3", "Hour 4", "Hour 5", "Hour 6", "Hour 7", "Hour 8", "Hour 9", "Hour 10"};
    Button saveHourlyEntry;
    EditText hourlyLineTarget, editTextInput, editTextOutput;
    Spinner problemTypeSpinner, problemsSpinner, statusSpinner;
    String problemTypes[] = {"Choose a problem Type", "Input", "Maintenance", "Quality", "Production"};
    ArrayAdapter<String> adapter;
    boolean flag = false;
    ListView listViewLineData;
    ArrayList<String> arrayListLineData;
    ArrayAdapter<String> adapterLineData;
    ArrayList<String> idList;

    public LineEntryFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.fragment_input_output, container, false);
        final Spinner spinnerTime = customView.findViewById(R.id.spinnerTime);
        spinnerTime.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, times));
        editTextInput = customView.findViewById(R.id.editTextInput);
        editTextOutput = customView.findViewById(R.id.editTextOutput);
        problemTypeSpinner = customView.findViewById(R.id.problemTypeSpinner);
        problemsSpinner = customView.findViewById(R.id.problemsSpinner);
        statusSpinner = customView.findViewById(R.id.spinnerStatus);
        saveHourlyEntry = customView.findViewById(R.id.saveHourlyEntry);
        hourlyLineTarget = customView.findViewById(R.id.hourlyLineTarget);
        listViewLineData = customView.findViewById(R.id.listViewLineData);

        arrayListLineData = new ArrayList<>();
        statusList = new ArrayList<>();
        idList = new ArrayList<>();

        getLineData();

        adapterLineData = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListLineData);
        listViewLineData.setAdapter(adapterLineData);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        final String styleNo = sharedPreferences.getString("styleNo", "");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(new Date()).toString();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.CHECK_LINE_TARGET_URL+"?styleNo="+styleNo+"&entryTime="+currentDate, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("responseLineData", response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    hourlyLineTarget.setText(jsonObject.getString("lineTarget"));
                } catch (JSONException e) {
                    Log.e("JSONLineDataErr", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LineDataErr", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
        spinnerTime.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, times));
        problems = new ArrayList<>();
        problems.add("Choose a Problem");

        problemTypeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, problemTypes));
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, problems);
        problemsSpinner.setAdapter(adapter);

        ArrayAdapter adapterStatus = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, statusList);
        statusList.add("Choose a status");
        statusList.add("Resolved");
        statusList.add("Not Resolved");
        statusSpinner.setAdapter(adapterStatus);

        problemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                flag = true;
                getProblemData(problemTypes[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveHourlyEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String requiredDate = df.format(new Date()).toString();
                String problemType = "", problem = "", status = "";

                if(flag){
                    if(!problemTypeSpinner.getSelectedItem().toString().contains("Choose")) {
                        problemType = problemTypeSpinner.getSelectedItem().toString();
                        problem = problemsSpinner.getSelectedItem().toString();
                    }
                }
                if(!statusSpinner.getSelectedItem().toString().contains("Choose")) {
                    status = statusSpinner.getSelectedItem().toString();
                }
                LineEntry lineEntry = new LineEntry(spinnerTime.getSelectedItem().toString(), editTextInput.getText().toString(),
                        editTextOutput.getText().toString(), problemType, problem, status,
                        styleNo, requiredDate, DateTimeInstance.getTimeStamp());
                saveLineEntry(lineEntry);
            }
        });

        listViewLineData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Resolved?").setCancelable(false).setMessage("Is the problem resolved now?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateStatus(position);
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        listViewLineData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Delete?").setCancelable(false).setMessage("Do you want to delete this data? If you delete you can not see it anymore!");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteLineData(idList.get(position));
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return customView;
    }

    private void deleteLineData(String id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Endpoints.DELETE_LINE_DATA_URL+"?id="+id;
        Log.i("url", url.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseDeleteLineData", response.toString());
                if(response.contains("DONE")) {
                    getLineData();
                    Toast.makeText(getActivity(), "The Data is deleted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void updateStatus(int position) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String updateUrl = Endpoints.UPDATE_LINE_DATA_STATUS+"?id="+idList.get(position);
        Log.i("updateUrl", updateUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responseUpdateLineData", response.toString());
                        if(response.contains("DONE")){
                            getLineData();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void getProblemData(String problemType) {
        problems.clear();
        problems.add("Choose a Problem");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_PROBLEM_DATA_URL + "?problemType=" + problemType, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String problem = response.getJSONObject(i).getString("Problem");
                        problems.add(problem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }

    private void saveLineEntry(final LineEntry obj) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_LINE_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("SUCCESS")){
                    Log.i("Response", response.toString());
                    Toast.makeText(getActivity(), "Data is saved!", Toast.LENGTH_SHORT).show();
                    problemTypeSpinner.setSelection(0);
                    problemsSpinner.setSelection(0);
                    getLineData();
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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
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

    public void getLineData(){
        arrayListLineData.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        final String styleNo = sharedPreferences.getString("styleNo", "");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(new Date()).toString();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Endpoints.GET_LINE_RECORD+"?styleNo="+styleNo+
                "&entryTime="+currentDate;
        Log.i("urlLineData", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("responseLineData", response.toString());
                for(int i=0; i<response.length(); i++){
                    try {
                        if(!response.getJSONObject(i).getString("problem").isEmpty()){
                            idList.add(response.getJSONObject(i).getString("id"));
                            String data = response.getJSONObject(i).getString("hour")+
                                    ", Output: "+response.getJSONObject(i).getString("output")+
                                    /*"\nProblem Type: "+response.getJSONObject(i).getString("problemType")+*/
                                    ", Prob: "+response.getJSONObject(i).getString("problem")+
                                    "\nStatus: "+response.getJSONObject(i).getString("status");
                            arrayListLineData.add(data);
                            adapterLineData.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }
}
