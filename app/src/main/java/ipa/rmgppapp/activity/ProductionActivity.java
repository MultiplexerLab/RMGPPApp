package ipa.rmgppapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.ViewPagerAdapter;
import ipa.rmgppapp.fragment.FullDaySummery;
import ipa.rmgppapp.fragment.HourlyProductionFragment;
import ipa.rmgppapp.fragment.HourlyReportFragment;
import ipa.rmgppapp.fragment.InputOutputFragment;

public class ProductionActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);

        tabLayout = findViewById(R.id.tabLayoutProfile);
        viewPager = findViewById(R.id.viewPagerProfile);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new HourlyProductionFragment(), "Individual Entry");
        adapter.addFragments(new InputOutputFragment(), "Line\nEntry");
        adapter.addFragments(new HourlyReportFragment(), "Hourly Report");
        adapter.addFragments(new FullDaySummery(), "Full Day Summery");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
