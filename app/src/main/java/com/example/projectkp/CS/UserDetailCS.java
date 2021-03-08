package com.example.projectkp.CS;

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
import android.view.Window;
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

public class UserDetailCS extends AppCompatActivity {

    Window window;
    Toolbar toolbar;
    FirebaseAuth userCSAuth;
    FirebaseUser userCSId;
    RelativeLayout savePhotoLayout;
    ImageView savePhoto, userPhoto, changeThemeBG;
    ProgressBar savePhotoProgress;
    SwitchCompat changeTheme;
    TextView fullNameUser, usernameUser, emailUser, phoneUser, changeThemeText;
    String myUsernameCS;
    Uri userPhotoUriCS;
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
        setContentView(R.layout.activity_user_detail_cs);
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        storeId();
        getUserDataPreferences();
        changeMyTheme();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                finish();
            }
        });

        findViewById(R.id.user_detail_logout_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        findViewById(R.id.update_img_button_cs).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.img_updated_button_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto.setVisibility(View.GONE);
                savePhotoProgress.setVisibility(View.VISIBLE);
//                deletePhotoDatabase();
                storePhotoDatabase(userPhotoUriCS, userCSId);
            }
        });
    }

    private void storeId() {
        toolbar = findViewById(R.id.user_detail_toolbar_cs);
        fullNameUser = findViewById(R.id.user_detail_fullname_field_cs);
        usernameUser = findViewById(R.id.user_detail_username_field_cs);
        emailUser = findViewById(R.id.user_detail_email_field_cs);
        phoneUser = findViewById(R.id.user_detail_number_field_cs);
        userPhoto = findViewById(R.id.user_photo_cs);
        savePhotoLayout = findViewById(R.id.img_updated_cs);
        savePhoto = findViewById(R.id.img_updated_bg_cs);
        savePhotoProgress = findViewById(R.id.img_updated_prog_cs);
        changeTheme = findViewById(R.id.switch_theme_cs);
        changeThemeBG = findViewById(R.id.light_mode_icon_cs);
        changeThemeText = findViewById(R.id.theme_light_desc_cs);
        userCSAuth = FirebaseAuth.getInstance();
        userCSId = userCSAuth.getCurrentUser();
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
        if (ContextCompat.checkSelfPermission(UserDetailCS.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserDetailCS.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), R.string.acc_permission, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(UserDetailCS.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        Intent galleryCS = new Intent((Intent.ACTION_GET_CONTENT));
        galleryCS.setType("image/*");
        startActivityForResult(galleryCS, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            userPhotoUriCS = data.getData();
            userPhoto.setImageURI(userPhotoUriCS);
        }
    }

    private void getUserDataPreferences() {

        Glide.with(this).load(userCSId.getPhotoUrl()).into(userPhoto);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(FullName)) {
            fullNameUser.setText(sharedPreferences.getString(FullName, ""));
        }
        if (sharedPreferences.contains(UserName)) {
            myUsernameCS = sharedPreferences.getString(UserName, "");
            usernameUser.setText(sharedPreferences.getString(UserName, ""));
        }
        if (sharedPreferences.contains(Phone)) {
            phoneUser.setText(sharedPreferences.getString(Phone, ""));
        }
        if (sharedPreferences.contains(Email)) {
            emailUser.setText(sharedPreferences.getString(Email, ""));
        }
    }

    private void storePhotoDatabase(Uri userPhotoUriCS, FirebaseUser currentUser) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("cs_photo").child(myUsernameCS);
        StorageReference imgPath = storageReference.child(userPhotoUriCS.getLastPathSegment());

        imgPath.putFile(userPhotoUriCS).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(myUsernameCS)
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