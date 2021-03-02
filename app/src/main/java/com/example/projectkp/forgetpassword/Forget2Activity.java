package com.example.projectkp.forgetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.R;
import com.example.projectkp.loginregister.LoginActivity;

public class Forget2Activity extends AppCompatActivity {

    Button resetLogin;
    ProgressBar forget2Progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget2);

        resetLogin = findViewById(R.id.reset_success_login);
        forget2Progress = findViewById(R.id.forget2_prog);

        resetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget2Progress.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                forget2Progress.setVisibility(View.GONE);
                finish();
            }
        });
    }
}