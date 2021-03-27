
package com.example.projectkp.CS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectkp.CS.Product.ProductListCS;
import com.example.projectkp.R;
import com.example.projectkp.verification.EmailVerifyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardCS extends AppCompatActivity {

    Window window;
    FirebaseAuth csAuth = FirebaseAuth.getInstance();
    FirebaseUser csUser = csAuth.getCurrentUser();
    TextView hiUsername;
    String myUsername;
    ImageView userPhoto;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_cs);

        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        userPhoto = findViewById(R.id.user_detail_photo_cs);
        hiUsername = findViewById(R.id.username_dashboard_cs);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername,"");
            hiUsername.setText(sharedPreferences.getString(sharedUsername, ""));
        }

        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_account_circle_40)
                                .error(R.drawable.ic_baseline_account_circle_40))
                .load(csUser.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetailCS.class));
                finish();
            }
        });

        findViewById(R.id.product_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductListCS.class));
                finish();
            }
        });

        findViewById(R.id.input_sc_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyirCS.class));
                finish();
            }
        });
        findViewById(R.id.track_order_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TrackOrderCS.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername,"");
            hiUsername.setText(sharedPreferences.getString(sharedUsername, ""));
        }

        if (csUser != null) {
            if (!csUser.isEmailVerified()) {
                Intent dataUser = new Intent(getApplicationContext(), EmailVerifyActivity.class);
                dataUser.putExtra("username", myUsername);
                startActivity(dataUser);
                finish();
            }
        }

        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_account_circle_40)
                                .error(R.drawable.ic_baseline_account_circle_40))
                .load(csUser.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);
    }
}