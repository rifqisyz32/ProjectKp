package com.example.projectkp.verification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectkp.DashboardActivity;
import com.example.projectkp.R;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerifyActivity extends AppCompatActivity {

    TextView usernameEmail;
    FirebaseAuth eAuth;
    Button goBack, verifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif);

        eAuth = FirebaseAuth.getInstance();
        verifyEmail = findViewById(R.id.verify_email);
        goBack = findViewById(R.id.go_back_email_verify);
        usernameEmail = findViewById(R.id.email_verify_username);

        String username = getIntent().getStringExtra("username");
        usernameEmail.setText(username);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailVerifyActivity.this, LoginActivity.class));
                finish();
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EmailVerifyActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EmailVerifyActivity.this, EmailVerify2Activity.class));
                        finish();
                    }
                });
                eAuth.getCurrentUser().sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EmailVerifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}