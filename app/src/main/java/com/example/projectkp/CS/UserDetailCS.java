package com.example.projectkp.CS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserDetailCS extends AppCompatActivity {

    FirebaseAuth csAuth = FirebaseAuth.getInstance();
    FirebaseUser csUser = csAuth.getCurrentUser();
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("users");

    Window window;
    Toolbar toolbar;
    Button logoutCS;
    SwitchCompat changeThemeSwitch;
    TextView fullNameCS, usernameCS, emailCS, phoneCS, changeThemeText;
    ImageView photoCS, changeThemeBG;
    String myUsername, myEmail, databaseFullname, databasePhone;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";
    public static final String sharedEmail = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_cs);

        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        storeId();
        getUserData();
        changeMyTheme();

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        logoutCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DashboardCS.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile_cs, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.edit_profile_cs:
                startActivity(new Intent(getApplicationContext(), EditProfileCS.class));
                finish();
                break;

            case R.id.delete_acc_cs:
                break;
        }
    }

    private void storeId() {
        toolbar = findViewById(R.id.user_detail_toolbar_cs);
        fullNameCS = findViewById(R.id.user_detail_fullname_field_cs);
        usernameCS = findViewById(R.id.user_detail_username_field_cs);
        emailCS = findViewById(R.id.user_detail_email_field_cs);
        phoneCS = findViewById(R.id.user_detail_number_field_cs);
        photoCS = findViewById(R.id.user_photo_cs);
        logoutCS = findViewById(R.id.user_detail_logout_cs);
        changeThemeSwitch = findViewById(R.id.switch_theme_cs);
        changeThemeBG = findViewById(R.id.light_mode_icon_cs);
        changeThemeText = findViewById(R.id.theme_light_desc_cs);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void changeMyTheme() {
        int nightModeFlags = changeThemeSwitch.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                changeThemeBG.setImageResource(R.drawable.ic_baseline_dark_mode_24);
                changeThemeSwitch.setChecked(false);
                changeThemeText.setText(R.string.dark_mode);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                changeThemeBG.setImageResource(R.drawable.ic_baseline_light_mode_24);
                changeThemeSwitch.setChecked(true);
                changeThemeText.setText(R.string.light_mode);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                Toast.makeText(getApplicationContext(), "Something wrong\nPlease contact us to fix this", Toast.LENGTH_SHORT).show();
                break;
        }

        changeThemeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeThemeSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
    }

    private void getUserData() {

        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_account_circle_120)
                                .error(R.drawable.ic_baseline_account_circle_120))
                .load(csUser.getPhotoUrl())
                .centerCrop()
                .into(photoCS);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername, "");
            usernameCS.setText(sharedPreferences.getString(sharedUsername, ""));
        }

        if (sharedPreferences.contains(sharedEmail)) {
            myEmail = sharedPreferences.getString(sharedEmail, "");
            emailCS.setText(sharedPreferences.getString(sharedEmail, ""));
        }

        Query checkUser = reference.orderByChild("username").equalTo(myUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    databaseFullname = snapshot.child(myUsername).child("fullname").getValue(String.class);
                    databasePhone = snapshot.child(myUsername).child("phone").getValue(String.class);
                    fullNameCS.setText(databaseFullname);
                    phoneCS.setText(databasePhone);

                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_user, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        emailCS.setText(myEmail);
        usernameCS.setText(myUsername);
    }
}