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
    Button goBack, changeAcc, verifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif);

        eAuth = FirebaseAuth.getInstance();
        verifyEmail = findViewById(R.id.verify_email);
        changeAcc = findViewById(R.id.change_account);
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
                        Toast.makeText(getApplicationContext(), "Verification email sent", Toast.LENGTH_SHORT).show();
                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Please check your email to verify", Toast.LENGTH_SHORT).show();
                    }
                });
                eAuth.getCurrentUser().sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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

    @Override
    protected void onStart() {
        super.onStart();

        if (eAuth.getCurrentUser() != null) {
            if (eAuth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(getApplicationContext(), EmailVerify2Activity.class));
                finish();
            }
        }
    }
}