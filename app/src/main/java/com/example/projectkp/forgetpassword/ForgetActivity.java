package com.example.projectkp.forgetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.R;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    Button reset, backLogin;
    TextInputLayout emailForget;
    FirebaseAuth forgetAuth;
    ProgressBar forgetProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        reset = findViewById(R.id.reset);
        backLogin = findViewById(R.id.go_back_forget);
        emailForget = findViewById(R.id.email_forget);
        forgetProgress = findViewById(R.id.forget_prog);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetProgress.setVisibility(View.VISIBLE);
                if (!validateEmail()) {
                    forgetProgress.setVisibility(View.GONE);
                    return;
                }

                String email = emailForget.getEditText().getText().toString();
                forgetAuth = FirebaseAuth.getInstance();

                forgetAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        forgetProgress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), R.string.reset_sent, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Forget2Activity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        forgetProgress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private Boolean validateEmail() {
        String val = emailForget.getEditText().getText().toString();

        if (val.isEmpty()) {
            emailForget.setError(getString(R.string.cant_empty));
            return false;
        } else {
            emailForget.setError(null);
            emailForget.setErrorEnabled(false);
            return true;
        }
    }

}