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

        findViewById(R.id.update_img_button_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
                savePhotoLayout.setVisibility(View.VISIBLE);
                savePhoto.setVisibility(View.VISIBLE);
                savePhotoProgress.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.img_updated_button_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto.setVisibility(View.GONE);
                savePhotoProgress.setVisibility(View.VISIBLE);
//                deletePhotoDatabase();
                storePhotoDatabase(userPhotoUriSales, userSalesId);
            }
        });
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

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(UserDetailSales.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserDetailSales.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), R.string.acc_permission, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(UserDetailSales.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        Intent gallerySales = new Intent((Intent.ACTION_GET_CONTENT));
        gallerySales.setType("image/*");
        startActivityForResult(gallerySales, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            userPhotoUriSales = data.getData();
            userPhoto.setImageURI(userPhotoUriSales);
        }
    }

    private void getUserDataPreferences() {

        Glide.with(this).load(userSalesId.getPhotoUrl()).into(userPhoto);

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

    private void storePhotoDatabase(Uri userPhotoUriSales, FirebaseUser currentUser) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("sales_photo").child(myUsernameSales);
        StorageReference imgPath = storageReference.child(userPhotoUriSales.getLastPathSegment());

        imgPath.putFile(userPhotoUriSales).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(myUsernameSales)
                                .setPhotoUri(uri)
                                .build();
                        savePhotoLayout.setVisibility(View.GONE);

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), R.string.photo_updated, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}