package com.example.projectkp.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.CS.DashboardCS;
import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;
import com.example.projectkp.forgetpassword.ForgetActivity;
import com.example.projectkp.verification.EmailVerifyActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth logAuth = FirebaseAuth.getInstance();
    FirebaseUser logUser = logAuth.getCurrentUser();
    TextInputLayout usernameLog, passwordLog;
    Button login, forget, signUp;
    String username, password, myUsername, databaseEmail, databaseRole;
    ProgressBar logProgress;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";
    public static final String sharedEmail = "email";
    public static final String sharedPassword = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        storeId();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logProgress.setVisibility(View.VISIBLE);
                loginUser();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    private void storeId() {
        usernameLog = findViewById(R.id.username_log);
        passwordLog = findViewById(R.id.password_log);
        forget = findViewById(R.id.forget_log);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.to_sign_up);
        logProgress = findViewById(R.id.log_prog);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (logUser != null) {
            if (logUser.isEmailVerified()) {
                checkRole();
            }
        }
    }

    private void checkRole() {

        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername, "");
        }

        Query checkRole = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(myUsername);
        checkRole.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    databaseRole = snapshot.child(myUsername).child("role").getValue(String.class);
                    databaseEmail = snapshot.child(myUsername).child("email").getValue(String.class);

                    if (databaseRole.matches("Sales")) {
                        logProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), DashboardSales.class));
                        finish();
                    } else {
                        logProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                logProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginUser() {

        if (!validateUsername() | !validatePassword()) {
            logProgress.setVisibility(View.GONE);
            return;
        }

        username = usernameLog.getEditText().getText().toString().trim();
        password = passwordLog.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    usernameLog.setError(null);
                    usernameLog.setErrorEnabled(false);

                    databaseRole = snapshot.child(username).child("role").getValue(String.class);
                    databaseEmail = snapshot.child(username).child("email").getValue(String.class);

                    logAuth.signInWithEmailAndPassword(databaseEmail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(sharedUsername, username);
                            editor.putString(sharedEmail, databaseEmail);
                            editor.putString(sharedPassword, password);
                            editor.apply();

                            if (logUser != null) {
                                if (logUser.isEmailVerified()) {
                                    logProgress.setVisibility(View.GONE);
                                    if (databaseRole.matches("Sales")) {
                                        startActivity(new Intent(getApplicationContext(), DashboardSales.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                                    }
                                } else {
                                    logProgress.setVisibility(View.GONE);
                                    Intent dataUser = new Intent(getApplicationContext(), EmailVerifyActivity.class);
                                    dataUser.putExtra("username", username);
                                    startActivity(dataUser);
                                }
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            logProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    logProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), R.string.no_user, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                logProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateUsername() {
        String val = usernameLog.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            usernameLog.setError(getString(R.string.cant_empty));
            return false;
        } else {
            usernameLog.setError(null);
            usernameLog.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordLog.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            passwordLog.setError(getString(R.string.cant_empty));
            passwordLog.requestFocus();
            return false;
        } else {
            passwordLog.setError(null);
            passwordLog.setErrorEnabled(false);
            return true;
        }
    }
}