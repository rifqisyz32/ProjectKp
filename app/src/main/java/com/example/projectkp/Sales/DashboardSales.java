
package com.example.projectkp.Sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectkp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardSales extends AppCompatActivity {

    FirebaseAuth userSalesAuth;
    FirebaseUser userSalesId;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sales);

        userSalesAuth = FirebaseAuth.getInstance();
        userSalesId = userSalesAuth.getCurrentUser();
        userPhoto = findViewById(R.id.user_detail_photo_sales);
//        Glide.with(this).load(userSalesId.getPhotoUrl()).into(userPhoto);
        Glide.with(this)
                .applyDefaultRequestOptions(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_account_circle_40)
                                .error(R.drawable.ic_baseline_account_circle_40))
                .load(userSalesId.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
                finish();
            }
        });

        findViewById(R.id.to_product_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductSales.class));
                finish();
            }
        });
    }
}