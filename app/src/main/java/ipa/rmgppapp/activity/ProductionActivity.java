package ipa.rmgppapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.ViewPagerAdapter;
import ipa.rmgppapp.fragment.FullDaySummery;
import ipa.rmgppapp.fragment.IndividualEntryFragment;
import ipa.rmgppapp.fragment.HourlyReportFragment;
import ipa.rmgppapp.fragment.LineEntryFragment;
import ipa.rmgppapp.helper.Endpoints;

public class ProductionActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    Toolbar toolbar;
    Button buttonJumpWorkerAssign, buttonLineInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);

        tabLayout = findViewById(R.id.tabLayoutProfile);
        viewPager = findViewById(R.id.viewPagerProfile);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        buttonJumpWorkerAssign = findViewById(R.id.buttonJumpWorkerAssign);
        buttonLineInput = findViewById(R.id.buttonLineInput);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragments(new IndividualEntryFragment(), "Individual Entry");
        adapter.addFragments(new LineEntryFragment(), "Line\nEntry");
        adapter.addFragments(new HourlyReportFragment(), "Hourly Report");
        adapter.addFragments(new FullDaySummery(), "Full Day Summery");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        buttonJumpWorkerAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductionActivity.this, WorkerAssignActivity.class);
                startActivity(intent);
            }
        });


        buttonLineInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(ProductionActivity.this).create();
                dialog.setTitle("Line Input");
                dialog.setCancelable(false);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.dialog_layout_line_input, null);

                dialog.setView(customView);

                Button saveButton = customView.findViewById(R.id.lineInputBtn);
                final EditText lineInput = customView.findViewById(R.id.editTextLineInput);
                final EditText totalHours = customView.findViewById(R.id.editTextTotalHours);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getSharedPreferences("supervisor", MODE_PRIVATE);
                        String styleNo = sharedPreferences.getString("styleNo", "");
                        String lineInputStr = lineInput.getText().toString();
                        if(!lineInputStr.isEmpty()) {
                            insertLineTarget(lineInputStr, styleNo, totalHours.getText().toString());
                            dialog.dismiss();
                        }else{
                            Toast.makeText(ProductionActivity.this, "Insert Data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void insertLineTarget(final String lineInputStr, final String styleNo, final String totalHours) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_LINE_TARGET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("SUCCESS")){
                    Toast.makeText(ProductionActivity.this, "Data is saved!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("lineTargetEntryResponse", response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("lineTargetEntry", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                int lineInput = Integer.parseInt(lineInputStr)/Integer.parseInt(totalHours);
                Map<String, String> params = new HashMap<String, String>();
                params.put("styleNo", styleNo);
                params.put("lineTarget", lineInput+"");
                params.put("totalTarget", lineInputStr);
                params.put("totalHours", totalHours);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
