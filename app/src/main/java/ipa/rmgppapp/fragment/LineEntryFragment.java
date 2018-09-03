package ipa.rmgppapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.HourlyEntry;
import ipa.rmgppapp.model.LineEntry;

import static android.content.Context.MODE_PRIVATE;

public class LineEntryFragment extends Fragment {

    String times[] = {"9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm"};
    Button saveHourlyEntry;
    EditText editTextInput, editTextOutput, editTextProblemType, editTextStatus;

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
        editTextProblemType = customView.findViewById(R.id.editTextProblemType);
        editTextStatus = customView.findViewById(R.id.editTextStatus);

        saveHourlyEntry = customView.findViewById(R.id.saveHourlyEntry);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        final String styleNo = sharedPreferences.getString("styleNo", "");

        saveHourlyEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                LineEntry lineEntry = new LineEntry(spinnerTime.getSelectedItem().toString(), editTextInput.getText().toString(),
                        editTextOutput.getText().toString(), editTextProblemType.getText().toString(), editTextStatus.getText().toString(),
                        styleNo, date.toString());
                saveLineEntry(lineEntry);
            }
        });
        return customView;
    }

    private void saveLineEntry(final LineEntry obj) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_LINE_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("SUCCESS")){
                    Toast.makeText(getActivity(), "Data is saved!", Toast.LENGTH_SHORT).show();
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
}
