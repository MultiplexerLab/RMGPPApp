package ipa.rmgppapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import ipa.rmgppapp.R;

public class ReportActivity extends AppCompatActivity {

    private static final String[] TABLE_HEADERS = { "SL No" ,"Buyer", "Style", "Order", "Quantity", "Ship Date", "Status" };

    private static final String[][] DATA_TO_SHOW = { { "1", "OVS", "OVS123", "PO123" , "10000", "10 August, 2018", "Cutting complete"},
            { "2", "SICEM", "OVS124", "PO124" , "20000", "30 August, 2018", "Cutting complete"},
            { "3", "Li&Fung", "Li123", "PO222" , "50000", "30 September, 2018", "Markers Done"}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        TableView tableView = (TableView) findViewById(R.id.tableView);

        tableView.addOnScrollListener(new MyOnScrollListener());
        TableColumnWeightModel columnModel = new TableColumnWeightModel(7);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        tableView.setColumnModel(columnModel);

        TableColumnDpWidthModel columnModel1 = new TableColumnDpWidthModel(this, 7, 100);
        columnModel1.setColumnWidth(0, 80);
        columnModel1.setColumnWidth(7, 200);
        tableView.setColumnModel(columnModel1);

        tableView.setDataAdapter(new SimpleTableDataAdapter(this, DATA_TO_SHOW));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
    }

    public void addStyle(View view) {
        Intent intent = new Intent(this, AddNewStyle.class);
        startActivity(intent);
    }

    public void cancelReport(View view) {
        finish();
    }

    public void continueReport(View view) {
        Intent intent = new Intent(ReportActivity.this, WorkerAssignActivity.class);
        startActivity(intent);
    }


    private class MyOnScrollListener implements OnScrollListener {
        @Override
        public void onScroll(final ListView tableDataView, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
            // listen for scroll changes
        }

        @Override
        public void onScrollStateChanged(final ListView tableDateView, final ScrollState scrollState) {
            // listen for scroll state changes
        }
    }
}
