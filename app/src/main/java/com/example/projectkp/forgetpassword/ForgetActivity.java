package com.example.projectkp.ForgetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectkp.R;
import com.example.projectkp.LoginRegister.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    private final FirebaseAuth forgetAuth = FirebaseAuth.getInstance();

    private TextInputLayout emailForget;
    private ProgressBar forgetProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        emailForget = findViewById(R.id.email_forget);
        forgetProgress = findViewById(R.id.forget_prog);
        Toolbar toolbar = findViewById(R.id.forget_toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        findViewById(R.id.forget_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetProgress.setVisibility(View.VISIBLE);
                if (!validateEmail()) {
                    forgetProgress.setVisibility(View.GONE);
                    return;
                }

                String email = emailForget.getEditText().getText().toString();

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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
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