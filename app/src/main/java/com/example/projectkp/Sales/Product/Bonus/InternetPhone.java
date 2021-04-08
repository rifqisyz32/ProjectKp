package com.example.projectkp.Sales.Product.Bonus;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectkp.R;

public class InternetPhone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonus_internet_phone);

        String myKey = getIntent().getStringExtra("myKey");
        if (!myKey.isEmpty()) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
            }
        }

        Toolbar toolbar = findViewById(R.id.bonus_product_toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("2P (Internet + Phone)");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}