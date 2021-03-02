package com.example.projectkp.verification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.DashboardActivity;
import com.example.projectkp.R;

public class EmailVerify2Activity extends AppCompatActivity {

    Button continueIn;
    ProgressBar verifyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif2);

        continueIn = findViewById(R.id.reset_success_login);
        verifyProgress = findViewById(R.id.reset_success_prog);
        continueIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyProgress.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                verifyProgress.setVisibility(View.GONE);
                finish();
            }
        });

    }
}