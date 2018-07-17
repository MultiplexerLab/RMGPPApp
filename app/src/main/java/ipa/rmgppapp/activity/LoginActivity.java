package ipa.rmgppapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ipa.rmgppapp.R;

public class LoginActivity extends AppCompatActivity {

    EditText eTSuperVisorId, eTStyleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTSuperVisorId = findViewById(R.id.editTextSuperVisorId);
        eTStyleId = findViewById(R.id.editTextStyleId);
    }


    public void cancelProcess(View view) {
        finish();
    }

    public void continueProcess(View view) {
        boolean error = false;
        String supervisorId = eTSuperVisorId.getText().toString();
        String styleId = eTStyleId.getText().toString();

        if(supervisorId.isEmpty()){
            eTSuperVisorId.setError("Supervisor Id is missing!");
            error = true;
        }
        if(styleId.isEmpty()){
            error = true;
            eTStyleId.setError("Style Id is missing!");
        }

        if(error){
            Toast.makeText(this, "Insert all valid information", Toast.LENGTH_LONG).show();

        }else{
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("styleNo", styleId);
            //intent.putExtra("intVal", 5);
            startActivity(intent);
        }
    }
}
