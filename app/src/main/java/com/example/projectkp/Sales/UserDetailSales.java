package com.example.projectkp.Sales;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectkp.R;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserDetailSales extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth userSalesAuth;
    FirebaseUser userSalesId;
    RelativeLayout savePhotoLayout;
    ProgressBar savePhotoProgress;
    SwitchCompat changeTheme;
    TextView fullNameUser, usernameUser, emailUser, phoneUser, changeThemeText;
    ImageView savePhoto, userPhoto, changeThemeBG;
    Uri userPhotoUriSales;
    String myUsernameSales;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String FullName = "fullname";
    public static final String UserName = "username";
    public static final String Phone = "phone";
    public static final String Email = "email";
    public static final String Photo = "photo";
    public static final String Role = "role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_sales);

        storeId();
        getUserDataPreferences();
        changeMyTheme();

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardSales.class));
                finish();
            }
        });

        findViewById(R.id.user_detail_logout_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile_sales, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.edit_profile_sales:
                startActivity(new Intent(getApplicationContext(), EditProfileSales.class));
                finish();
                break;

            case R.id.delete_acc_sales:
                break;
        }
    }

    private void storeId() {
        toolbar = findViewById(R.id.user_detail_toolbar_sales);
        fullNameUser = findViewById(R.id.user_detail_fullname_field_sales);
        usernameUser = findViewById(R.id.user_detail_username_field_sales);
        emailUser = findViewById(R.id.user_detail_email_field_sales);
        phoneUser = findViewById(R.id.user_detail_number_field_sales);
        userPhoto = findViewById(R.id.user_photo_sales);
        savePhotoLayout = findViewById(R.id.img_updated_sales);
        savePhoto = findViewById(R.id.img_updated_bg_sales);
        savePhotoProgress = findViewById(R.id.img_updated_prog_sales);
        changeTheme = findViewById(R.id.switch_theme_sales);
        changeThemeBG = findViewById(R.id.light_mode_icon_sales);
        changeThemeText = findViewById(R.id.theme_light_desc_sales);
        userSalesAuth = FirebaseAuth.getInstance();
        userSalesId = userSalesAuth.getCurrentUser();
    }

    private void changeMyTheme() {
        int nightModeFlags = changeTheme.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                changeThemeBG.setImageResource(R.drawable.ic_baseline_dark_mode_24);
                changeTheme.setChecked(false);
                changeThemeText.setText(R.string.dark_mode);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                changeThemeBG.setImageResource(R.drawable.ic_baseline_light_mode_24);
                changeTheme.setChecked(true);
                changeThemeText.setText(R.string.light_mode);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                Toast.makeText(getApplicationContext(), "Something wrong\nPlease contact us to fix this", Toast.LENGTH_SHORT).show();
                break;
        }

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeTheme.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
    }

    private void getUserDataPreferences() {

        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_account_circle_120)
                                .error(R.drawable.ic_baseline_account_circle_120))
                .load(userSalesId.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(FullName)) {
            fullNameUser.setText(sharedPreferences.getString(FullName, ""));
        }
        if (sharedPreferences.contains(UserName)) {
            myUsernameSales = sharedPreferences.getString(UserName, "");
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