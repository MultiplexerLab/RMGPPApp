package ipa.rmgppapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ipa.rmgppapp.R;

public class FullDaySummery extends Fragment {

    TextView lineTargetTotal, lineOutputTotal, activeTarget, lineWip;

    public FullDaySummery(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_full_day_summery, container, false);

        lineTargetTotal = customView.findViewById(R.id.lineTargetTotal);
        lineOutputTotal = customView.findViewById(R.id.lineOutputTotal);
        activeTarget = customView.findViewById(R.id.activeTarget);
        lineWip = customView.findViewById(R.id.lineWIP);
        
        getSummeryData();
        return customView;
    }

    private void getSummeryData() {
    }
}
