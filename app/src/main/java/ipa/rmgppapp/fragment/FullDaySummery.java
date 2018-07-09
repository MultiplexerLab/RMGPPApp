package ipa.rmgppapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipa.rmgppapp.R;

public class FullDaySummery extends Fragment {

    public FullDaySummery(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customView = inflater.inflate(R.layout.fragment_full_day_summery, container, false);
        return customView;
    }
}
