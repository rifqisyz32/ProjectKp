package com.example.projectkp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout fullNameReg, userNameReg, phoneReg, emailReg, passwordReg;
    Button signUp,loginAgain;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("admin");

                reference.setValue("success");
                /*String fullname = fullNameReg.getEditText().getText().toString();
                String username = userNameReg.getEditText().getText().toString();
                String phone = phoneReg.getEditText().getText().toString();
                String email = emailReg.getEditText().getText().toString();
                String password = passwordReg.getEditText().getText().toString();

                UserHelper helper = new UserHelper(fullname,username,phone,email,password);
                reference.child(username).setValue(helper);*/
                Toast.makeText(getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                Intent signUpIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(signUpIntent);
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
}