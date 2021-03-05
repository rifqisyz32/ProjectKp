
package com.example.projectkp.CS;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectkp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardCS extends AppCompatActivity {

    Window window;
    FirebaseAuth userCSAuth;
    FirebaseUser userCSId;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_cs);
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        userCSAuth = FirebaseAuth.getInstance();
        userCSId = userCSAuth.getCurrentUser();
        userPhoto = findViewById(R.id.user_detail_photo_cs);
        Glide.with(this).load(userCSId.getPhotoUrl()).into(userPhoto);

        findViewById(R.id.to_user_detail_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetailCS.class));
                finish();
            }
        });

        findViewById(R.id.to_product_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductCS.class));
                finish();
            }
        });
    }
}