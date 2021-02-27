package com.example.projectkp.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.DashboardActivity;
import com.example.projectkp.R;
import com.example.projectkp.forgetpassword.ForgetActivity;
import com.example.projectkp.verification.EmailVerifyActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameLog, passwordLog;
    Button login, forget, signUp;
    FirebaseAuth lgAuth;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FullName = "fullname";
    public static final String UserName = "username";
    public static final String Phone = "phone";
    public static final String Email = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        storeId();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
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
        lgAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (lgAuth.getCurrentUser() != null) {
            if (lgAuth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        }
    }

    public void loginUser(View view) {
        if (!validateUserName() | !validatePassword()) {
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
                    String databaseEmail = snapshot.child(username).child("email").getValue(String.class);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(FullName, databaseFullName);
                    editor.putString(UserName, databaseUserName);
                    editor.putString(Phone, databasePhone);
                    editor.putString(Email, databaseEmail);
                    editor.apply();

                    lgAuth.signInWithEmailAndPassword(databaseEmail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (lgAuth.getCurrentUser() != null) {
                                if (lgAuth.getCurrentUser().isEmailVerified()) {
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                } else {
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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_user, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateUserName() {
        String val = usernameLog.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            usernameLog.setError(getString(R.string.cantEmpty));
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
            passwordLog.setError(getString(R.string.cantEmpty));
            passwordLog.requestFocus();
            return false;
        } else {
            passwordLog.setError(null);
            passwordLog.setErrorEnabled(false);
            return true;
        }
    }
}