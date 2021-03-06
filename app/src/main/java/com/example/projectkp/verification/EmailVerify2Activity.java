package com.example.projectkp.verification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.CS.DashboardCS;
import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmailVerify2Activity extends AppCompatActivity {

    ProgressBar verifyProgress;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UserName = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verif2);

        verifyProgress = findViewById(R.id.reset_success_prog);

        findViewById(R.id.retry_verif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                    checkRole();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.check_email, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.reset_success_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyProgress.setVisibility(View.VISIBLE);
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                    checkRole();
                } else {
                    verifyProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), R.string.check_email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkRole() {
        String username = "default";

        sharedPreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedPreferences.contains(UserName)) {
            username = sharedPreferences.getString(UserName, "");
        }

        String finalUsername = username;
        Query checkRole = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").equalTo(username);
        checkRole.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String databaseRole = snapshot.child(finalUsername).child("role").getValue(String.class);

                    if (databaseRole.matches("Sales")) {
                        verifyProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), DashboardSales.class));
                        finish();
                    } else {
                        verifyProgress.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                verifyProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                checkRole();
            }
        }
    }

}