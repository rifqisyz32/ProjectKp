package com.example.projectkp.verification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectkp.R;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifyActivity extends AppCompatActivity {

    FirebaseAuth verifyAuth = FirebaseAuth.getInstance();
    FirebaseUser verifyUser = verifyAuth.getCurrentUser();
    TextView myEmail;
    Button changeAcc, verifyEmail;
    ProgressBar verifyProgress;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif);

        storeId();

        String username = getIntent().getStringExtra("username");
        myEmail.setText(username);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyProgress.setVisibility(View.VISIBLE);
                verifyUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        verifyProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), EmailVerify2Activity.class));
                        finish();
                    }
                });

                verifyUser.sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        verifyProgress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        changeAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void storeId() {
        toolbar = findViewById(R.id.verify_email_toolbar);
        verifyEmail = findViewById(R.id.verify_email_button);
        changeAcc = findViewById(R.id.verify_email_change_account);
        myEmail = findViewById(R.id.verify_email_username);
        verifyProgress = findViewById(R.id.verify_email_prog);
    }
}