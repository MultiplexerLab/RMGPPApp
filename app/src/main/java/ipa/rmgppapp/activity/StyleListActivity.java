package ipa.rmgppapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.PlanningData;

public class StyleListActivity extends AppCompatActivity {

    private static final String[] TABLE_HEADERS = {"SL No", "Buyer", "Style", "Item", "Order", "Quantity", "Ship Date"};

    RequestQueue queue;
    ArrayList<PlanningData> planningDataArrayList;
    ArrayList<String[]> tableData;
    TableView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stylelist);

        tableView = findViewById(R.id.tableViewReport);
        queue = Volley.newRequestQueue(this);
        planningDataArrayList = new ArrayList<>();
        tableData = new ArrayList<>();

        getStyles();
    }

    private void getStyles() {
        planningDataArrayList.clear();
        tableData.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("supervisor", MODE_PRIVATE);
        final String lineNo = sharedPreferences.getString("lineNo", "");
        Log.i("lineNo", lineNo);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_PLANNING_DATA_URL + "?lineNo=" + lineNo, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i("response", jsonArray.toString());
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<PlanningData>>() {
                    }.getType();
                    planningDataArrayList = gson.fromJson(jsonArray.toString(), type);

                    for (int i = 0; i < planningDataArrayList.size(); i++) {
                        PlanningData obj = planningDataArrayList.get(i);
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
        tableView = findViewById(R.id.tableViewReport);
        TableColumnDpWidthModel columnModel1 = new TableColumnDpWidthModel(this, 7, 100);
        columnModel1.setColumnWidth(0, 60);
        columnModel1.setColumnWidth(2, 180);
        columnModel1.setColumnWidth(3, 150);
        columnModel1.setColumnWidth(4, 150);
        columnModel1.setColumnWidth(6, 150);

        tableView.setColumnModel(columnModel1);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        Log.i("tableData", tableData.toString());
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tableData));

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                String description = planningDataArrayList.get(rowIndex).getDescription();
                String temp[] = description.split(" ");
                Log.i("descriptionTag", description);

                SharedPreferences.Editor editor = getSharedPreferences("supervisor", MODE_PRIVATE).edit();
                editor.putString("description", description);
                editor.putString("styleNo", planningDataArrayList.get(rowIndex).getStyle());
                editor.commit();

                if (description.isEmpty()) {
                    Toast.makeText(StyleListActivity.this, "You have to choose a style!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(StyleListActivity.this, ProductionActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    public void addStyle(View view) {
        Intent intent = new Intent(StyleListActivity.this, AddNewStyle.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        tableView.getDataAdapter().notifyDataSetChanged();
        super.onResume();
    }

    public void refreshStyles(View view) {
        getStyles();
    }
}
