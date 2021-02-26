package com.example.projectkp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.loginregister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserDetail extends AppCompatActivity {

    TextView fullNameUser, usernameUser, emailUser, phoneUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        fullNameUser = findViewById(R.id.user_detail_name_field);
        usernameUser = findViewById(R.id.user_detail_name);
        emailUser = findViewById(R.id.user_detail_email_field);
        phoneUser = findViewById(R.id.user_detail_number_field);

        /*String fullname = getIntent().getStringExtra("fullname");
        String username = getIntent().getStringExtra("username");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");

        fullNameUser.setText(fullname);
        usernameUser.setText(username);
        phoneUser.setText(phone);
        emailUser.setText(email);*/

        findViewById(R.id.user_detail_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });

        findViewById(R.id.user_detail_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }
}