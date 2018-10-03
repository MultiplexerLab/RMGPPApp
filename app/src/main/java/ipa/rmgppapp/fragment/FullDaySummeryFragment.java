package ipa.rmgppapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;

import static android.content.Context.MODE_PRIVATE;

public class FullDaySummeryFragment extends Fragment {

    TextView lineTargetTotal, lineOutputTotal, totalInput, lineWip, totalStyleInput, totalStyleOutput;
    Button buttonRefresh;

    public FullDaySummeryFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_full_day_summery, container, false);

        lineTargetTotal = customView.findViewById(R.id.lineTargetTotal);
        lineOutputTotal = customView.findViewById(R.id.lineOutputTotal);
        totalInput = customView.findViewById(R.id.totalInput);
        lineWip = customView.findViewById(R.id.lineWIP);
        totalStyleInput = customView.findViewById(R.id.totalStyleInput);
        totalStyleOutput = customView.findViewById(R.id.totalStyleOutput);
        buttonRefresh = customView.findViewById(R.id.buttonRefresh);

        getSummeryData();

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSummeryData();
            }
        });
        return customView;
    }

    private void getSummeryData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("supervisor", MODE_PRIVATE);
        String styleNo = sharedPreferences.getString("styleNo", "");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(new Date()).toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_SUMMERY_DATA+"?styleNo="+styleNo+"&entryTime="+currentDate, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    lineOutputTotal.setText(jsonObject.getString("totalQuantity"));
                    lineTargetTotal.setText(jsonObject.getString("totalTarget"));
                    totalInput.setText(jsonObject.getString("totalInput"));
                    totalStyleInput.setText("Total Style Input: "+jsonObject.getString("totalStyleInput"));
                    totalStyleOutput.setText("Total Style Output: "+jsonObject.getString("totalStyleOutput"));
                    int wip = Integer.parseInt(jsonObject.getString("totalTarget")) - Integer.parseInt(jsonObject.getString("totalQuantity"));
                    lineWip.setText(wip+"");
                } catch (Exception e) {
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
