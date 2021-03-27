
package com.example.projectkp.Sales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectkp.R;
import com.example.projectkp.Sales.MYIR.MyirSales;
import com.example.projectkp.Sales.Product.ProductListSales;
import com.example.projectkp.Sales.TrackOrder.TrackOrderSales;
import com.example.projectkp.verification.EmailVerifyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardSales extends AppCompatActivity {

    FirebaseAuth salesAuth = FirebaseAuth.getInstance();
    FirebaseUser salesUser = salesAuth.getCurrentUser();
    TextView hiUsername;
    String myUsername;
    ImageView userPhoto;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String sharedUsername = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sales);

        userPhoto = findViewById(R.id.user_detail_photo_sales);
        hiUsername = findViewById(R.id.username_dashboard_sales);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(sharedUsername)) {
            myUsername = sharedPreferences.getString(sharedUsername,"");
            hiUsername.setText(sharedPreferences.getString(sharedUsername, ""));
        }

        Glide.with(this)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.ic_baseline_account_circle_40)
                        .error(R.drawable.ic_baseline_account_circle_40))
                .load(salesUser.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
                finish();
            }
        });

        findViewById(R.id.product_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductListSales.class));
                finish();
            }
        });

        findViewById(R.id.input_sc_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyirSales.class));
                finish();
            }
        });

        findViewById(R.id.track_order_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TrackOrderSales.class));
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

        if (salesUser != null) {
            if (!salesUser.isEmailVerified()) {
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
                .load(salesUser.getPhotoUrl())
                .centerCrop()
                .into(userPhoto);
    }
}