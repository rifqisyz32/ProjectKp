package com.example.projectkp;

import android.app.DownloadManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameLog, passwordLog;
    Button login, signUp;
//    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLog = findViewById(R.id.username_log);
        passwordLog = findViewById(R.id.password_log);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.to_sign_up);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
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

   /*@Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }
    }*/

    public void loginUser(View view) {
        if (!validateUserName() | !validatePassword()) {
            return;
        }

        final String username = usernameLog.getEditText().getText().toString().trim();
        final String password = passwordLog.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usernameLog.setError(null);
                    usernameLog.setErrorEnabled(false);

                    String databasePassword = snapshot.child(username).child("password").getValue(String.class);
                    if (databasePassword.equals(password)) {
                        passwordLog.setError(null);
                        passwordLog.setErrorEnabled(false);

                        String databaseUserName = snapshot.child(username).child("username").getValue(String.class);
                        String databaseFullName = snapshot.child(username).child("fullname").getValue(String.class);
                        String databasePhone = snapshot.child(username).child("phone").getValue(String.class);
                        String databaseEmail = snapshot.child(username).child("email").getValue(String.class);

                        Intent dataUser = new Intent(getApplicationContext(),OTPActivity.class);
                        dataUser.putExtra("phone", databasePhone);
                        startActivity(dataUser);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User Not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateUserName(){
        String val = usernameLog.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            usernameLog.setError("Can't be empty");
            return false;
        }
        else {
            usernameLog.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordLog.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            passwordLog.setError("Can't be empty");
            return false;
        } else {
            passwordLog.setError(null);
            return true;
        }
    }
}