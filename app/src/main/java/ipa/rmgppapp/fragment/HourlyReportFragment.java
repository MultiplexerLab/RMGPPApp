package ipa.rmgppapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import ipa.rmgppapp.R;
import ipa.rmgppapp.helper.Endpoints;
import ipa.rmgppapp.model.HourlyReportRow;
import ipa.rmgppapp.model.ProcessItem;

import static android.content.Context.MODE_PRIVATE;

public class HourlyReportFragment extends Fragment {

    public HourlyReportFragment(){

    }
    ArrayList<String[]> tableData;
    private static final String[] TABLE_HEADERS = { "Worker\nName" , "Worker\nId","Process\nName", "8:01-9:00", "9:01-10:00", "10:01-11:00","11:01-12:00","12:01-13:00" };
    private static final String[][] DATA_TO_SHOW = { {"Abul Kashem", "W123", "Neck Join", "89", "88", "98", "95", "97", "92"},
            {"Fatema Jahan", "W124", "Neck Join", "89", "88", "98", "95", "97", "92"},
            {"Morjina Khatun", "W126", "Side Join", "89", "88", "98", "95", "96", "94"},
            {"Habib", "W129", "Ham Join", "89", "88", "88", "85", "97", "99"},};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_hourly_report, container, false);
        TableView tableView = (TableView) customView.findViewById(R.id.tableView);

        tableData = new ArrayList<>();

        TableColumnWeightModel columnModel = new TableColumnWeightModel(8);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        tableView.setColumnModel(columnModel);

        TableColumnDpWidthModel columnModel1 = new TableColumnDpWidthModel(getActivity(), 8, 120);
        columnModel1.setColumnWidth(0, 150);
        tableView.setColumnModel(columnModel1);

        tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), tableData));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));

        getTableData();

        return customView;
    }

    private void getTableData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("hourlyEntry", MODE_PRIVATE);
        String data = sharedPreferences.getString("data", "");

        Gson gson = new Gson();
        Type type = new TypeToken<List<ProcessItem>>() {
        }.getType();
        ArrayList<ProcessItem> processItems = gson.fromJson(data, type);

        for(int i=0; i<processItems.size(); i++){
            final String workerId = processItems.get(i).getAssignedWorkerId();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.GET_HOURLY_RECORD_DATA+"?workerId="+workerId, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String arr[] = new String[12];
                    for(int i=0; i<response.length(); i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            arr[0] = workerId;
                            arr[1] = jsonObject.getString("workerName");
                            arr[2] = jsonObject.getString("processName");
                            String hour = jsonObject.getString("hour");
                            if(hour.contains("9")){
                                arr[3] = jsonObject.getString("quantity");
                            }else if(hour.contains("10")){
                                arr[4] = jsonObject.getString("quantity");
                            }else if(hour.contains("11")){
                                arr[5] = jsonObject.getString("quantity");
                            }else if(hour.contains("12")){
                                arr[6] = jsonObject.getString("quantity");
                            }else if(hour.contains("1pm")){
                                arr[7] = jsonObject.getString("quantity");
                            }else if(hour.contains("2pm")){
                                arr[8] = jsonObject.getString("quantity");
                            }else if(hour.contains("3pm")){
                                arr[9] = jsonObject.getString("quantity");
                            }else if(hour.contains("4pm")){
                                arr[10] = jsonObject.getString("quantity");
                            }else if(hour.contains("5pm")){
                                arr[11] = jsonObject.getString("quantity");
                            }else if(hour.contains("6pm")){
                                arr[12] = jsonObject.getString("quantity");
                            }
                            tableData.add(arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("HourlyReport", error.toString());
                }
            });
            queue.add(jsonArrayRequest);
        }
    }
}
