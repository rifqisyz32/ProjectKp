package com.example.projectkp.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.projectkp.Helper.UserHelper;
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
    private TextInputLayout fullnameReg, usernameReg, phoneReg, emailReg, passwordReg;
    private FirebaseAuth regAuth;
    private ProgressBar regProgress;
    private AppCompatRadioButton csReg, salesReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storeId();

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regProgress.setVisibility(View.VISIBLE);
                storeUserData();
            }
        });

        findViewById(R.id.login_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void storeId() {
        regAuth = FirebaseAuth.getInstance();
        fullnameReg = findViewById(R.id.fullname_reg);
        usernameReg = findViewById(R.id.username_reg);
        phoneReg = findViewById(R.id.phone_reg);
        emailReg = findViewById(R.id.email_reg);
        passwordReg = findViewById(R.id.password_reg);
        regProgress = findViewById(R.id.reg_prog);
        csReg = findViewById(R.id.cs_reg);
        salesReg = findViewById(R.id.sales_reg);
    }

    private void storeUserData() {

        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassword() | !validateRadioButton()) {
            regProgress.setVisibility(View.GONE);
            return;
        }

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        String fullname = fullnameReg.getEditText().getText().toString();
        String username = usernameReg.getEditText().getText().toString();
        String phone = phoneReg.getEditText().getText().toString();
        String email = emailReg.getEditText().getText().toString();
        String password = passwordReg.getEditText().getText().toString();
        String roleCS = csReg.getText().toString();
        String roleSales = salesReg.getText().toString();

        Query checkUser = reference.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    usernameReg.setError(null);
                    usernameReg.setErrorEnabled(false);

                    regAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            UserHelper storeData = new UserHelper(fullname, username, phone, email, roleCS);

                            if (csReg.isChecked()) {
                                storeData.setRole(roleCS);
                            } else {
                                storeData.setRole(roleSales);
                            }

                            reference.child(username).setValue(storeData);

                            regProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            regProgress.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    regProgress.setVisibility(View.GONE);
                    usernameReg.setError(getString(R.string.user_exist));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                regProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateRadioButton() {
        RadioGroup radioGroup;
        radioGroup = findViewById(R.id.role_reg);
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), getString(R.string.pick_role), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private Boolean validateFullName() {
        String val = fullnameReg.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fullnameReg.setError(getString(R.string.cant_empty));
            return false;
        } else {
            fullnameReg.setError(null);
            fullnameReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUserName() {
        String val = usernameReg.getEditText().getText().toString().trim();
        String alphanum = "\\A\\w{4,12}\\z";

        if (val.isEmpty()) {
            usernameReg.setError(getString(R.string.cant_empty));
            return false;
        } else if (val.length() > 12) {
            usernameReg.setError(getString(R.string.max_name));
            return false;
        } else if (val.length() < 4) {
            usernameReg.setError(getString(R.string.min_name));
            return false;
        } else if (!val.matches(alphanum)) {
            usernameReg.setError(getString(R.string.alphanum));
            return false;
        } else {
            usernameReg.setError(null);
            usernameReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = emailReg.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            emailReg.setError(getString(R.string.cant_empty));
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
        String val = phoneReg.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            phoneReg.setError(getString(R.string.cant_empty));
            return false;
        } else {
            phoneReg.setError(null);
            phoneReg.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordReg.getEditText().getText().toString().trim();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            passwordReg.setError(getString(R.string.cant_empty));
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

