package com.example.projectkp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailLog, passwordLog;
    Button login, signUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLog = findViewById(R.id.email_log);
        passwordLog = findViewById(R.id.password_log);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.to_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }
    }

    public void loginUser(View view) {
        if(!validateEmail() | !validatePassword()){
            return;
        }

        String email = emailLog.getEditText().getText().toString();
        String password = passwordLog.getEditText().getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateEmail(){
        String val = emailLog.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            emailLog.setError("Can't be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            emailLog.setError("Invalid email address");
            return false;
        }
        else {
            emailLog.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = passwordLog.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            passwordLog.setError("Can't be empty");
            return false;
        }
        else {
            passwordLog.setError(null);
            return true;
        }
    }
}