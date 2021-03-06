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

import com.example.projectkp.R;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerifyActivity extends AppCompatActivity {

    TextView usernameEmail;
    FirebaseAuth verifyAuth;
    Button goBack, changeAcc, verifyEmail;
    ProgressBar verifyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif);

        storeId();

        String username = getIntent().getStringExtra("username");
        usernameEmail.setText(username);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyProgress.setVisibility(View.VISIBLE);

                verifyAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        verifyProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), EmailVerify2Activity.class));
                        finish();
                    }
                });

                verifyAuth.getCurrentUser().sendEmailVerification().addOnFailureListener(new OnFailureListener() {
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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void storeId() {
        verifyAuth = FirebaseAuth.getInstance();
        verifyEmail = findViewById(R.id.verify_email);
        changeAcc = findViewById(R.id.change_account);
        goBack = findViewById(R.id.go_back_email_verify);
        usernameEmail = findViewById(R.id.email_verify_username);
        verifyProgress = findViewById(R.id.email_verify_prog);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (verifyAuth.getCurrentUser() != null) {
            if (verifyAuth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(getApplicationContext(), EmailVerify2Activity.class));
                finish();
            }
        }
    }

}