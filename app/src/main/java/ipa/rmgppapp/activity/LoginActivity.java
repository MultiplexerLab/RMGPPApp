package ipa.rmgppapp.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;

public class LoginActivity extends AppCompatActivity {

    EditText eTSuperVisorId;
    Spinner spinnerLine;
    RequestQueue queue;
    ArrayList<String> lineData;
    Snackbar snackbar;
    LinearLayout rootLayout;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTSuperVisorId = findViewById(R.id.editTextSuperVisorId);
        spinnerLine = findViewById(R.id.spinnerLine);
        rootLayout = findViewById(R.id.rootLayout);

        lineData = new ArrayList<>();
        lineData.add("Choose a Line");
        queue = Volley.newRequestQueue(this);

        if (internetConnected()) {
            getSpinnerData();
            if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
        } else {
            showSnackBar();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lineData);
        spinnerLine.setAdapter(adapter);
    }

    private void getSpinnerData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_PLANNED_LINE_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        lineData.add(response.getJSONObject(i).getString("line"));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("LineError", e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void cancelProcess(View view) {
        finish();
    }

    public void continueProcess(View view) {
        boolean error = false;
        String supervisorId = eTSuperVisorId.getText().toString();
        String lineNo = spinnerLine.getSelectedItem().toString();

        if (supervisorId.isEmpty()) {
            eTSuperVisorId.setError("Supervisor Id is missing!");
            error = true;
        }
        if (lineNo.equals("Choose a Line")) {
            error = true;
        }

        if (error) {
            Toast.makeText(this, "Insert all valid information", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("supervisor", MODE_PRIVATE).edit();
            editor.putString("supervisorId", supervisorId);
            editor.putString("lineNo", lineNo);
            editor.commit();

            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        }
    }

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, "Internet is not connected!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Connect", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9003) {
            if (internetConnected()) {
                getSpinnerData();
            } else {
                showSnackBar();
            }
        }
    }
}
