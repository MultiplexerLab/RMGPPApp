package ipa.rmgppapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import ipa.rmgppapp.R;

public class HourlyReportFragment extends Fragment {

    public HourlyReportFragment(){

    }
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

        tableView.addOnScrollListener(new HourlyReportFragment.MyOnScrollListener());
        TableColumnWeightModel columnModel = new TableColumnWeightModel(8);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        tableView.setColumnModel(columnModel);

        TableColumnDpWidthModel columnModel1 = new TableColumnDpWidthModel(getActivity(), 8, 120);
        columnModel1.setColumnWidth(0, 150);
        tableView.setColumnModel(columnModel1);

        tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), DATA_TO_SHOW));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), TABLE_HEADERS));

        return customView;
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
