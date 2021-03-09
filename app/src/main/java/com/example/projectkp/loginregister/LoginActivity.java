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
import com.example.projectkp.verification.EmailVerify2Activity;
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
    String username, email;
    ProgressBar logProgress;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FullName = "fullname";
    public static final String UserName = "username";
    public static final String Phone = "phone";
    public static final String Email = "email";
    public static final String Password = "password";
    public static final String Role = "role";

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
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
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

        if (logAuth.getCurrentUser() != null) {
            if (logAuth.getCurrentUser().isEmailVerified()) {
                checkRole();
            }
        }
    }

    private void checkRole() {

        sharedPreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedPreferences.contains(UserName)) {
            username = sharedPreferences.getString(UserName, "");
        }

        Query checkRole = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);
        checkRole.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String databaseRole = snapshot.child(username).child("role").getValue(String.class);

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
        if (!validateUserName() | !validatePassword()) {
            logProgress.setVisibility(View.GONE);
            return;
        }

        String username = usernameLog.getEditText().getText().toString().trim();
        String password = passwordLog.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    usernameLog.setError(null);
                    usernameLog.setErrorEnabled(false);

                    String databaseFullName = snapshot.child(username).child("fullname").getValue(String.class);
                    String databaseUserName = snapshot.child(username).child("username").getValue(String.class);
                    String databasePhone = snapshot.child(username).child("phone").getValue(String.class);
                    String databaseRole = snapshot.child(username).child("role").getValue(String.class);
                    email = logUser.getEmail();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(FullName, databaseFullName);
                    editor.putString(UserName, databaseUserName);
                    editor.putString(Phone, databasePhone);
                    editor.putString(Email, email);
                    editor.putString(Role, databaseRole);
                    editor.putString(Password, password);
                    editor.apply();

                    logAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
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
                                    dataUser.putExtra("username", databaseUserName);
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

    private Boolean validateUserName() {
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