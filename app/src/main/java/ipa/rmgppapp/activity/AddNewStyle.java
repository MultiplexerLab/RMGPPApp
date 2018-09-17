package ipa.rmgppapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;

public class AddNewStyle extends AppCompatActivity {

    EditText buyer, description, order, item, quantity, shipmentdate;
    Calendar myCalendar;
    AutoCompleteTextView styleNo;
    ArrayList<String> styleNoList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_style);

        styleNo = (AutoCompleteTextView) findViewById(R.id.styleNo);
        buyer = findViewById(R.id.buyer);
        description = findViewById(R.id.description);
        order = findViewById(R.id.order);
        item = findViewById(R.id.item);
        quantity = findViewById(R.id.quantity);
        shipmentdate = findViewById(R.id.shipMentDate);
        styleNoList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(AddNewStyle.this,
                android.R.layout.simple_dropdown_item_1line, styleNoList);
        styleNo.setAdapter(adapter);

        getStyleList();

        //myCalendar = Calendar.getInstance();
        /*final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        shipmentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddNewStyle.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
        styleNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!styleNo.getText().toString().isEmpty() && styleNo.getText().toString().length()>2){
                    getPlanningData(styleNo.getText().toString());
                }
            }
        });
    }

    private void getPlanningData(String styleNo) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_STYLE_DETAILS+"?styleNo="+styleNo, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    buyer.setText(jsonObject.getString("buyer"));
                    item.setText(jsonObject.getString("item"));
                    description.setText(jsonObject.getString("description"));
                    order.setText(jsonObject.getString("orderNo"));
                    shipmentdate.setText(jsonObject.getString("shipmentData"));
                    quantity.setText(jsonObject.getString("plannedQuantity"));
                    Log.i("Data", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }

    private void getStyleList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_ALL_STYLES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("StyleList", response.toString());
                for(int i=0; i<response.length(); i++){
                    try {
                        styleNoList.add(response.getJSONObject(i).getString("styleNo"));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("ArrayAssignErr", e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("StyleListErr", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }

    /*private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        shipmentdate.setText(sdf.format(myCalendar.getTime()));
    }*/

    public void cancelStyle(View view) {
        finish();
    }
    public void saveStyle(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_NEW_STYLE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("NewStyle", response.toString());
                if(response.contains("SUCCESS")) {
                    Intent intent = new Intent(AddNewStyle.this, StyleListActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AddNewStyle.this, "Server Problem!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("StyleEntryErr", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getSharedPreferences("supervisor", MODE_PRIVATE);
                String lineNo = sharedPreferences.getString("lineNo", "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("plannedLine", lineNo);
                params.put("buyer", buyer.getText().toString());
                params.put("style", styleNo.getText().toString());
                params.put("description", description.getText().toString());
                params.put("item", item.getText().toString());
                params.put("orderNo", order.getText().toString());
                params.put("shipmentDate", shipmentdate.getText().toString());
                params.put("quantity", quantity.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
