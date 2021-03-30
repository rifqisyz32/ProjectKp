package com.example.projectkp.Sales.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;
import com.example.projectkp.loginregister.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetailSales extends AppCompatActivity {

    FirebaseAuth salesAuth = FirebaseAuth.getInstance();
    FirebaseUser salesUser = salesAuth.getCurrentUser();
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("users");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("sales_photo");

    Toolbar toolbar;
    Button logoutSales;
    SwitchCompat changeThemeSwitch;
    TextView fullNameSales, usernameSales, emailSales, phoneSales, changeThemeText;
    ImageView photoSales, changeThemeBG;
    String myUsername, dbEmail, dbFullname, dbPhone;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_sales);

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

        logoutSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DashboardSales.class));
        finish();
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
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_baseline_delete_outline_24)
                        .setTitle(R.string.delete_acc)
                        .setMessage(R.string.delete_acc_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteMyAcc();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
        }
    }

    private void storeId() {
        toolbar = findViewById(R.id.user_detail_toolbar_sales);
        fullNameSales = findViewById(R.id.user_detail_fullname_field_sales);
        usernameSales = findViewById(R.id.user_detail_username_field_sales);
        emailSales = findViewById(R.id.user_detail_email_field_sales);
        phoneSales = findViewById(R.id.user_detail_number_field_sales);
        photoSales = findViewById(R.id.user_photo_sales);
        logoutSales = findViewById(R.id.user_detail_logout_sales);
        changeThemeSwitch = findViewById(R.id.switch_theme_sales);
        changeThemeBG = findViewById(R.id.light_mode_icon_sales);
        changeThemeText = findViewById(R.id.theme_light_desc_sales);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void deleteMyAcc() {

        storageReference.child(myUsername).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(myUsername).getRef().removeValue();
                salesUser.delete();
                Toast.makeText(getApplicationContext(), R.string.acc_delete, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                .load(salesUser.getPhotoUrl())
                .centerCrop()
                .into(photoSales);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername, "");
            usernameSales.setText(sharedPreferences.getString(sharedUsername, ""));
        }

        Query checkUser = reference.orderByChild("username").equalTo(myUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    dbFullname = snapshot.child(myUsername).child("fullname").getValue(String.class);
                    dbPhone = snapshot.child(myUsername).child("phone").getValue(String.class);
                    dbEmail = snapshot.child(myUsername).child("email").getValue(String.class);

                    emailSales.setText(dbEmail);
                    fullNameSales.setText(dbFullname);
                    phoneSales.setText(dbPhone);

                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_user, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}