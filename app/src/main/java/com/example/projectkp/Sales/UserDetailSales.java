package com.example.projectkp.Sales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserDetailSales extends AppCompatActivity {

    TextView fullNameUser, usernameUser, emailUser, phoneUser;
    ProgressBar userDetailProgress;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FullName = "fullname";
    public static final String UserName = "username";
    public static final String Phone = "phone";
    public static final String Email = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_sales);

        fullNameUser = findViewById(R.id.user_detail_fullname_field_sales);
        usernameUser = findViewById(R.id.user_detail_username_field_sales);
        emailUser = findViewById(R.id.user_detail_email_field_sales);
        phoneUser = findViewById(R.id.user_detail_number_field_sales);
        userDetailProgress = findViewById(R.id.user_detail_prog_sales);

        getUserDataPreferences();

        Toolbar toolbar = findViewById(R.id.user_detail_toolbar_sales);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardSales.class));
            }
        });

        findViewById(R.id.user_detail_logout_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetailProgress.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                userDetailProgress.setVisibility(View.GONE);
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