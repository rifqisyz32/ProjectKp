package com.example.projectkp.verification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.CS.DashboardCS;
import com.example.projectkp.R;

public class EmailVerify2Activity extends AppCompatActivity {

    ProgressBar verifyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif2);

        verifyProgress = findViewById(R.id.reset_success_prog);

        findViewById(R.id.reset_success_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyProgress.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                verifyProgress.setVisibility(View.GONE);
            }
        });

    }
}