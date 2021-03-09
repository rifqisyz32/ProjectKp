
package com.example.projectkp.Sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectkp.R;
import com.example.projectkp.CS.UserDetailCS;

public class DashboardSales extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sales);

        findViewById(R.id.to_user_detail_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetailSales.class));
            }
        });

        findViewById(R.id.to_product_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductSales.class));
            }
        });
    }
}