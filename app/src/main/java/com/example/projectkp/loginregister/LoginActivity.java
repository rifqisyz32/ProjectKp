package com.example.projectkp.LoginRegister;

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

import com.example.projectkp.CS.Dashboard;
import com.example.projectkp.R;
import com.example.projectkp.ForgetPassword.ForgetActivity;
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

    private final FirebaseAuth logAuth = FirebaseAuth.getInstance();
    private final FirebaseUser logUser = logAuth.getCurrentUser();
    private TextInputLayout logUsername, logPassword;
    private Button login, forget, signUp;
    private String username, password, myUsername, dbEmail, dbRole;
    private ProgressBar logProgress;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";
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
        logUsername = findViewById(R.id.username_log);
        logPassword = findViewById(R.id.password_log);
        forget = findViewById(R.id.forget_log);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.to_sign_up);
        logProgress = findViewById(R.id.log_prog);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername, "");
        }

        if (logUser != null && logUser.isEmailVerified()) {
            checkRole();
        }
    }

    private void checkRole() {

        Query checkRole = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(myUsername);
        checkRole.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    dbRole = snapshot.child(myUsername).child("role").getValue(String.class);

                    if (dbRole.matches("Sales")) {
                        logProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), com.example.projectkp.Sales.Dashboard.class));
                        finish();
                    } else {
                        logProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
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

        username = logUsername.getEditText().getText().toString().trim();
        password = logPassword.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    logUsername.setError(null);
                    logUsername.setErrorEnabled(false);

                    dbEmail = snapshot.child(username).child("email").getValue(String.class);

                    logAuth.signInWithEmailAndPassword(dbEmail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            dbRole = snapshot.child(username).child("role").getValue(String.class);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(sharedUsername, username);
                            editor.putString(sharedPassword, password);
                            editor.apply();

                            logProgress.setVisibility(View.GONE);
                            if (dbRole.matches("Sales")) {
                                startActivity(new Intent(getApplicationContext(), com.example.projectkp.Sales.Dashboard.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            }
                            finish();
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
                    logUsername.setError(getString(R.string.no_user));
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
        String val = logUsername.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            logUsername.setError(getString(R.string.cant_empty));
            return false;
        } else {
            logUsername.setError(null);
            logUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = logPassword.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            logPassword.setError(getString(R.string.cant_empty));
            logPassword.requestFocus();
            return false;
        } else {
            logPassword.setError(null);
            logPassword.setErrorEnabled(false);
            return true;
        }
    }

}