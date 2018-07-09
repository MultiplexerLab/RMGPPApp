package ipa.rmgppapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipa.rmgppapp.R;

public class HourlyProductionFragment extends Fragment {

    public HourlyProductionFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_hourly_production, container, false);
        return customView;
    }
}
