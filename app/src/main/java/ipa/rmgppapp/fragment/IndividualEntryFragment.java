package ipa.rmgppapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.IndividualEntryAdapter;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.ProcessItem;

import static android.content.Context.MODE_PRIVATE;

public class IndividualEntryFragment extends Fragment {

    RecyclerView mRecyclerView;
    RequestQueue queue;

    public IndividualEntryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View customView = inflater.inflate(R.layout.fragment_hourly_production, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = (RecyclerView) customView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("hourlyEntry", MODE_PRIVATE);
        String data = sharedPreferences.getString("data", "");

        getAssignedWorkerData();

        /*if (data.isEmpty()) {
            Toast.makeText(getActivity(), "Assign Workers first!", Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ProcessItem>>() {
            }.getType();
            ArrayList<ProcessItem> processItems = gson.fromJson(data, type);
            IndividualEntryAdapter adapter = new IndividualEntryAdapter(getActivity(), processItems);
            mRecyclerView.setAdapter(adapter);
        }*/
        return customView;
    }

    private void getAssignedWorkerData() {
        queue = Volley.newRequestQueue(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        String tag = sharedPreferences.getString("description", "");

        Log.i("TagGet", tag);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_ASSIGNED_WORKER_URL + "?tag=" + tag, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<ProcessItem>>() {
                }.getType();
                Log.i("DataAssignedWorker", response.toString());
                ArrayList<ProcessItem> processItems = gson.fromJson(response.toString(), type);
                IndividualEntryAdapter adapter = new IndividualEntryAdapter(getActivity(), processItems);
                mRecyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorWorkerAssign", error.toString());
            }
        });

        queue.add(stringRequest);
    }
}