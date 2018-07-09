package ipa.rmgppapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ipa.rmgppapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void cancelProcess(View view) {
        finish();
    }

    public void continueProcess(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
