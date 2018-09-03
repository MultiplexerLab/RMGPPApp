package ipa.rmgppapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;

import static android.content.Context.MODE_PRIVATE;

public class FullDaySummery extends Fragment {

    TextView lineTargetTotal, lineOutputTotal, activeTarget, lineWip;

    public FullDaySummery(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_full_day_summery, container, false);

        lineTargetTotal = customView.findViewById(R.id.lineTargetTotal);
        lineOutputTotal = customView.findViewById(R.id.lineOutputTotal);
        activeTarget = customView.findViewById(R.id.activeTarget);
        lineWip = customView.findViewById(R.id.lineWIP);
        
        getSummeryData();
        return customView;
    }

    private void getSummeryData() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        String styleNo = sharedPreferences.getString("styleNo", "");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_SUMMERY_DATA+"?styleNo="+styleNo, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    lineOutputTotal.setText(jsonObject.getString("lineOutput"));
                    lineTargetTotal.setText(jsonObject.getString("lineTarget"));
                    activeTarget.setText(jsonObject.getString("activeTarget"));
                    lineWip.setText(jsonObject.getString("lineWip"));
                } catch (JSONException e) {
                    Log.e("SummeryData", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SummeryData", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }
}
