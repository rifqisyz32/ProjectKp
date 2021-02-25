package com.example.projectkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout fullNameReg, userNameReg, phoneReg, emailReg, passwordReg;
    Button signUp, loginAgain;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameReg = findViewById(R.id.fullname_reg);
        userNameReg = findViewById(R.id.username_reg);
        phoneReg = findViewById(R.id.phone_reg);
        emailReg = findViewById(R.id.email_reg);
        passwordReg = findViewById(R.id.password_reg);
        loginAgain = findViewById(R.id.login_again);
        signUp = findViewById(R.id.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = fullNameReg.getEditText().getText().toString();
                if (fullname.isEmpty()) {
                    fullNameReg.setError("Can't be empty");
                    return;
                }

                String username = userNameReg.getEditText().getText().toString();
                if (username.isEmpty()) {
                    userNameReg.setError("Can't be empty");
                    return;
                }

                String phone = phoneReg.getEditText().getText().toString();
                if (phone.isEmpty()) {
                    phoneReg.setError("Can't be empty");
                    return;
                }

                String email = emailReg.getEditText().getText().toString();
                if (email.isEmpty()) {
                    emailReg.setError("Can't be empty");
                    return;
                }

                String password = passwordReg.getEditText().getText().toString();
                if (password.isEmpty()) {
                    passwordReg.setError("Can't be empty");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                loginAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
            }
        });
    }
}