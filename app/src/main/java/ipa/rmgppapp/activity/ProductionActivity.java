package ipa.rmgppapp.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import ipa.rmgppapp.R;
import ipa.rmgppapp.adapter.ViewPagerAdapter;
import ipa.rmgppapp.fragment.FullDaySummery;
import ipa.rmgppapp.fragment.IndividualEntryFragment;
import ipa.rmgppapp.fragment.HourlyReportFragment;
import ipa.rmgppapp.fragment.LineEntryFragment;

public class ProductionActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    Toolbar toolbar;
    Button buttonJumpWorkerAssign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);

        tabLayout = findViewById(R.id.tabLayoutProfile);
        viewPager = findViewById(R.id.viewPagerProfile);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        buttonJumpWorkerAssign = findViewById(R.id.buttonJumpWorkerAssign);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragments(new IndividualEntryFragment(), "Individual Entry");
        adapter.addFragments(new LineEntryFragment(), "Line\nEntry");
        adapter.addFragments(new HourlyReportFragment(), "Hourly Report");
        adapter.addFragments(new FullDaySummery(), "Full Day Summery");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        buttonJumpWorkerAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductionActivity.this, WorkerAssignActivity.class);
                startActivity(intent);
            }
        });
    }
}
