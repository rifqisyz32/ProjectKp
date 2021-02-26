package com.example.projectkp.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout fullNameReg, userNameReg, phoneReg, emailReg, passwordReg;
    Button signUp, loginAgain;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storeId();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeUserData();
            }
        });

        loginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void storeId() {
        fullNameReg = findViewById(R.id.fullname_reg);
        userNameReg = findViewById(R.id.username_reg);
        phoneReg = findViewById(R.id.phone_reg);
        emailReg = findViewById(R.id.email_reg);
        passwordReg = findViewById(R.id.password_reg);
        loginAgain = findViewById(R.id.login_again);
        signUp = findViewById(R.id.sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void storeUserData() {

        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassword()) {
            return;
        }

        String fullname = fullNameReg.getEditText().getText().toString();
        String username = userNameReg.getEditText().getText().toString();
        String phone = phoneReg.getEditText().getText().toString();
        String email = emailReg.getEditText().getText().toString();
        String password = passwordReg.getEditText().getText().toString();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        Query checkUser = rootNode.getReference("users").orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    UserHelper storeData = new UserHelper(fullname, username, phone, email);
                    reference.child(username).setValue(storeData);

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    userNameReg.setError(getString(R.string.user_exist));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateFullName() {
        String val = fullNameReg.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullNameReg.setError(getString(R.string.cantEmpty));
            return false;
        } else {
            fullNameReg.setError(null);
            fullNameReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUserName() {
        String val = userNameReg.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,12}\\z";

        if (val.isEmpty()) {
            userNameReg.setError(getString(R.string.cantEmpty));
            return false;
        } else if (val.length() > 12) {
            userNameReg.setError(getString(R.string.max_name));
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            userNameReg.setError(getString(R.string.no_space));
            return false;
        } else {
            userNameReg.setError(null);
            userNameReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = emailReg.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            emailReg.setError(getString(R.string.cantEmpty));
            return false;
        } else if (!val.matches(emailPattern)) {
            emailReg.setError(getString(R.string.invalid_email));
            return false;
        } else {
            emailReg.setError(null);
            emailReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val = phoneReg.getEditText().getText().toString();

        if (val.isEmpty()) {
            phoneReg.setError(getString(R.string.cantEmpty));
            return false;
        } else {
            phoneReg.setError(null);
            phoneReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordReg.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            passwordReg.setError(getString(R.string.cantEmpty));
            return false;
        } else if (!val.matches(passwordPattern)) {
            passwordReg.setError(getString(R.string.weak_password));
            return false;
        } else {
            passwordReg.setError(null);
            passwordReg.setErrorEnabled(false);
            return true;
        }
    }

}

