package ipa.rmgppapp.fragment;

import android.app.VoiceInteractor;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.HourlyEntry;
import ipa.rmgppapp.model.LineEntry;

import static android.content.Context.MODE_PRIVATE;

public class LineEntryFragment extends Fragment {

    ArrayList<String> problems;
    String times[] = {"Hour 1", "Hour 2", "Hour 3", "Hour 4", "Hour 5", "Hour 6", "Hour 7", "Hour 8", "Hour 9", "Hour 10"};
    Button saveHourlyEntry;
    EditText hourlyLineTarget, editTextInput, editTextOutput, editTextStatus;
    Spinner problemTypeSpinner, problemsSpinner;
    String problemTypes[] = {"Choose a problem Type", "Input", "Maintenance", "Quality", "Production"};
    ArrayAdapter<String> adapter;
    boolean flag = false;
    ListView listViewLineData;
    ArrayList<String> arrayListLineData;
    ArrayAdapter<String> adapterLineData;

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
        editTextStatus = customView.findViewById(R.id.editTextStatus);
        problemTypeSpinner = customView.findViewById(R.id.problemTypeSpinner);
        problemsSpinner = customView.findViewById(R.id.problemsSpinner);
        saveHourlyEntry = customView.findViewById(R.id.saveHourlyEntry);
        hourlyLineTarget = customView.findViewById(R.id.hourlyLineTarget);
        listViewLineData = customView.findViewById(R.id.listViewLineData);

        arrayListLineData = new ArrayList<>();
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
                String problemType = "", problem = "";

                if(flag){
                    if(!problemTypeSpinner.getSelectedItem().toString().contains("Choose")) {
                        problemType = problemTypeSpinner.getSelectedItem().toString();
                        problem = problemsSpinner.getSelectedItem().toString();
                    }
                }
                LineEntry lineEntry = new LineEntry(spinnerTime.getSelectedItem().toString(), editTextInput.getText().toString(),
                        editTextOutput.getText().toString(), problemType, problem, editTextStatus.getText().toString(),
                        styleNo, requiredDate);
                saveLineEntry(lineEntry);
            }
        });
        return customView;
    }

    public void getProblemData(String problemType) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_PROBLEM_DATA_URL + "?problemType=" + problemType, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String problem = response.getJSONObject(i).getString("Problem");
                        problems.add(problem);
                        adapter.notifyDataSetChanged();
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

    private void saveLineEntry(final LineEntry obj) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_LINE_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("SUCCESS")){
                    Toast.makeText(getActivity(), "Data is saved!", Toast.LENGTH_SHORT).show();
                    problemTypeSpinner.setSelection(0);
                    problemsSpinner.setSelection(0);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        final String styleNo = sharedPreferences.getString("styleNo", "");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(new Date()).toString();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_LINE_RECORD+"?styleNo="+styleNo+
                "&entryTime="+currentDate, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    try {
                        String data = "Hour: "+response.getJSONObject(i).getString("hour")+
                                "\nOutput: "+response.getJSONObject(i).getString("output")+
                                "\nProblem Type: "+response.getJSONObject(i).getString("problemType")+
                                "\nProblem: "+response.getJSONObject(i).getString("problem")+
                                "\nStatus: "+response.getJSONObject(i).getString("status");
                        arrayListLineData.add(data);
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
