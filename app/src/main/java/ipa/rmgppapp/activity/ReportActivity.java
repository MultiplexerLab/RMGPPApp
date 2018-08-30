package ipa.rmgppapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.colorizers.TableDataRowColorizer;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.PlanningData;

public class ReportActivity extends AppCompatActivity {

    private static final String[] TABLE_HEADERS = {"SL No", "Buyer", "Style", "Item", "Order", "Quantity", "Ship Date"};

    /*private static final String[][] DATA_TO_SHOW = {{"1", "OVS", "OVS123", "PO123", "10000", "10 August, 2018", "Cutting complete"},
            {"2", "SICEM", "OVS124", "PO124", "20000", "30 August, 2018", "Cutting complete"},
            {"3", "Li&Fung", "Li123", "PO222", "50000", "30 September, 2018", "Markers Done"}};*/

    RequestQueue queue;
    ArrayList<PlanningData> planningData;
    ArrayList<String[]> tableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        queue = Volley.newRequestQueue(this);
        planningData = new ArrayList<>();
        tableData = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("supervisor", MODE_PRIVATE);
        final String lineNo = sharedPreferences.getString("lineNo", "");
        Log.i("lineNo", lineNo);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_PLANNING_DATA_URL + "?lineNo=" + lineNo, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("response", response.toString());
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<PlanningData>>() {
                    }.getType();
                    planningData = gson.fromJson(response.toString(), type);

                    for (int i = 0; i < planningData.size(); i++) {
                        PlanningData obj = planningData.get(i);
                        tableData.add(i, new String[]{(i + 1) + "", obj.getBuyer(), obj.getStyle(), obj.getItem(), obj.getOrderNo(), obj.getPlannedQuantity(), obj.getShipmentData()});
                    }
                    setTableData();
                } catch (Exception e) {
                    Log.e("ErrorInPlanningData", e.toString());
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

    public void setTableData() {
        final TableView tableView = findViewById(R.id.tableViewReport);
        TableColumnDpWidthModel columnModel1 = new TableColumnDpWidthModel(this, 7, 100);
        columnModel1.setColumnWidth(0, 70);
        columnModel1.setColumnWidth(2, 180);
        columnModel1.setColumnWidth(3, 150);
        columnModel1.setColumnWidth(4, 150);
        columnModel1.setColumnWidth(6, 120);
        tableView.setColumnModel(columnModel1);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        Log.i("tableData", tableData.toString());
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableData));

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                String description = planningData.get(rowIndex).getDescription();
                String temp[] = description.split(" ");
                Log.i("descriptionTag", temp[0]);

                SharedPreferences.Editor editor = getSharedPreferences("supervisor", MODE_PRIVATE).edit();
                editor.putString("description", temp[0]);
                editor.putString("styleNo", planningData.get(rowIndex).getStyle());
                editor.commit();

                try {
                    LinearLayout linearLayout = findViewById(R.id.rootLayoutReport);
                    //tableView.getDataAdapter().getView(rowIndex, null, linearLayout).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //tableView.getDataAdapter().getCellView(rowIndex, 2, linearLayout).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //tableView.getDataAdapter().notifyDataSetChanged();
                }catch (Exception e){
                    Log.e("tableDataErr", e.toString());
                }
            }
        });
    }

    public void cancelReport(View view) {
        finish();
    }

    public void continueReport(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("supervisor", MODE_PRIVATE);
        String description = sharedPreferences.getString("description", "");
        if (description.isEmpty()) {
            Toast.makeText(this, "You have to choose a style!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ReportActivity.this, ProductionActivity.class);
            startActivity(intent);
        }
    }
}
