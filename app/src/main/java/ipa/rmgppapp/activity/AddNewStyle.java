package ipa.rmgppapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ipa.rmgppapp.R;

public class AddNewStyle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_style);
    }

    public void cancelStyle(View view) {
        finish();
    }

    public void saveStyle(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }
}
