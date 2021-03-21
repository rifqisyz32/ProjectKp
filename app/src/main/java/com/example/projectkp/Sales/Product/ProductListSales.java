package com.example.projectkp.Sales.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;

import java.util.ArrayList;
import java.util.List;

public class ProductListSales extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_sales);

        toolbar = findViewById(R.id.list_product_toolbar_sales);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        RecyclerView rvStaggered = findViewById(R.id.list_product_rv);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvStaggered.setLayoutManager(staggeredGridLayoutManager);

        List<ProductListHelper> list = new ArrayList<>();
        list.add(new ProductListHelper(getString(R.string.internet_phone), R.drawable.internet_phone));
        list.add(new ProductListHelper(getString(R.string.internet_tv), R.drawable.internet_tv));
        list.add(new ProductListHelper(getString(R.string.internet_phone_tv), R.drawable.internet_phone_tv));
        list.add(new ProductListHelper(getString(R.string.guru_pelajar), R.drawable.guru_pelajar));
        list.add(new ProductListHelper(getString(R.string.minipack), R.drawable.internet_phone_tv));
        list.add(new ProductListHelper(getString(R.string.orbit), R.drawable.orbit));

        AdapterProductListSales staggeredProductListHelperAdapter = new AdapterProductListSales(this, list);
        rvStaggered.setAdapter(staggeredProductListHelperAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DashboardSales.class));
        finish();
    }
}