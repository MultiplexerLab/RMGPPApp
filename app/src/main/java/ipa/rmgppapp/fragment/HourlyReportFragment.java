package ipa.rmgppapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.IndividualEntryAdapter;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.HourlyReportRow;
import ipa.rmgppapp.model.ProcessItem;

import static android.content.Context.MODE_PRIVATE;

public class HourlyReportFragment extends Fragment {

    public HourlyReportFragment(){

    }
    ArrayList<String[]> tableData;
    private static final String[] TABLE_HEADERS = { "Worker\nName" , "Worker\nId","Process\nName", "Hour 1", "Hour 2", "Hour 3", "Hour 4", "Hour 5", "Hour 6", "Hour 7", "Hour 8", "Hour 9", "Hour 10", "Total"};
    TableView tableView;
    String prevId="";
    Button buttonRefreshHourlyReport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_hourly_report, container, false);
        tableView = (TableView) customView.findViewById(R.id.tableView);
        buttonRefreshHourlyReport = customView.findViewById(R.id.buttonRefreshHourlyReport);

        tableData = new ArrayList<>();

        TableColumnDpWidthModel columnModel1 = new TableColumnDpWidthModel(getActivity(), 15, 80);
        columnModel1.setColumnWidth(0, 150);
        columnModel1.setColumnWidth(1, 100);
        columnModel1.setColumnWidth(2, 180);
        tableView.setColumnModel(columnModel1);

        getTableData();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("tableData", tableData.toString());
                try {
                    setTableData();
                }catch (Exception e){
                    Log.e("tableDataErr", e.toString());
                }
            }
        }, 3000);

        buttonRefreshHourlyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTableData();
                //setTableData();
            }
        });
        return customView;
    }

    private void setTableData() {
        tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), tableData));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));
    }

    private void getTableData() {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                final ArrayList<ProcessItem> processItems = gson.fromJson(response.toString(), type);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = df.format(new Date()).toString();
                tableData.clear();
                for(int i=0; i<processItems.size(); i++){
                    final String workerId = processItems.get(i).getAssignedWorkerId();
                    final int finalI = i;
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_HOURLY_RECORD_DATA + "?workerId=" + workerId+"&entryTime="+currentDate, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                String arr[] = new String[14];
                                arr[1] = workerId;
                                arr[0] = processItems.get(finalI).getAssignedWorkerName();
                                arr[2] = processItems.get(finalI).getProcessName();

                                int totalQuantity=0;

                                for (int j = 0; j < response.length(); j++) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(j);
                                        Log.i("HourlyData", jsonObject.toString());

                                        String hour = jsonObject.getString("hour");
                                        if (hour.contains("Hour 1")) {
                                            arr[3] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[3]);
                                        } else if (hour.contains("Hour 2")) {
                                            arr[4] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[4]);
                                        } else if (hour.contains("Hour 3")) {
                                            arr[5] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[5]);
                                        } else if (hour.contains("Hour 4")) {
                                            arr[6] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[6]);
                                        } else if (hour.contains("Hour 5")) {
                                            arr[7] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[7]);
                                        } else if (hour.contains("Hour 6")) {
                                            arr[8] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[8]);
                                        } else if (hour.contains("Hour 7")) {
                                            arr[9] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[9]);
                                        } else if (hour.contains("Hour 8")) {
                                            arr[10] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[10]);
                                        } else if (hour.contains("Hour 9")) {
                                            arr[11] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[11]);
                                        } else if (hour.contains("Hour 10")) {
                                            arr[12] = jsonObject.getString("quantity");
                                            totalQuantity = totalQuantity+ Integer.parseInt(arr[12]);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                arr[13] = totalQuantity+"";
                                tableData.add(arr);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("HourlyReport", error.toString());
                            }
                        });
                        queue.add(jsonArrayRequest);
                        prevId = workerId;
                    }
                    setTableData();
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
