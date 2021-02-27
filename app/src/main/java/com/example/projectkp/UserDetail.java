package com.example.projectkp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.loginregister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserDetail extends AppCompatActivity {

    TextView fullNameUser, usernameUser, emailUser, phoneUser;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FullName = "fullname";
    public static final String UserName = "username";
    public static final String Phone = "phone";
    public static final String Email = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        fullNameUser = findViewById(R.id.user_detail_fullname_field);
        usernameUser = findViewById(R.id.user_detail_username_field);
        emailUser = findViewById(R.id.user_detail_email_field);
        phoneUser = findViewById(R.id.user_detail_number_field);

        getUserDataPreferences();

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

    private void getUserDataPreferences() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedPreferences.contains(FullName)) {
            fullNameUser.setText(sharedPreferences.getString(FullName, ""));
        }
        if (sharedPreferences.contains(UserName)) {
            usernameUser.setText(sharedPreferences.getString(UserName, ""));
        }
        if (sharedPreferences.contains(Phone)) {
            phoneUser.setText(sharedPreferences.getString(Phone, ""));
        }
        if (sharedPreferences.contains(Email)) {
            emailUser.setText(sharedPreferences.getString(Email, ""));
        }
    }
}