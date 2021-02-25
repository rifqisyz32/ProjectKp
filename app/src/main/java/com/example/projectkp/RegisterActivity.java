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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                storeUserData();
//                registerUser(v);
            }
        });

        loginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

    private Boolean validateFullName(){
        String val = fullNameReg.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullNameReg.setError("Can't be empty");
            return false;
        }
        else {
            fullNameReg.setError(null);
            return true;
        }
    }

    private Boolean validateUserName(){
        String val = userNameReg.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            userNameReg.setError("Can't be empty");
            return false;
        }
        else if (val.length()>=12){
            userNameReg.setError("Username too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)){
            userNameReg.setError("White space are not allowed");
            return false;
        }
        else {
            userNameReg.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = emailReg.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            emailReg.setError("Can't be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            emailReg.setError("Invalid email address");
            return false;
        }
        else {
            emailReg.setError(null);
            return true;
        }
    }

    private Boolean validatePhone(){
        String val = phoneReg.getEditText().getText().toString();

        if (val.isEmpty()) {
            phoneReg.setError("Can't be empty");
            return false;
        }
        else {
            phoneReg.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = passwordReg.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            passwordReg.setError("Can't be empty");
            return false;
        }
        else if(!val.matches(passwordPattern)){
            passwordReg.setError("Password too weak");
            return false;
        }
        else {
            passwordReg.setError(null);
            return true;
        }
    }

    public void registerUser(View view){

        if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }

        String email = emailReg.getEditText().getText().toString();
        String password = passwordReg.getEditText().getText().toString();

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
    }

    private void storeUserData(){

        if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }

        String fullName = fullNameReg.getEditText().getText().toString();
        String username = userNameReg.getEditText().getText().toString();
        String phone = phoneReg.getEditText().getText().toString();
        String email = emailReg.getEditText().getText().toString();
        String password = passwordReg.getEditText().getText().toString();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        UserHelper storeData = new UserHelper(fullName,username,phone,email,password);
        reference.child(username).setValue(storeData);
        Toast.makeText(getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}

