package com.example.projectkp.Sales;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;

import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ProductSales extends AppCompatActivity {
    RecyclerView recview;
    AdapterProduct adapter;
=======

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectkp.R;

public class ProductSales extends AppCompatActivity {

>>>>>>> 37593b8125fc315649330f86b582071b87d37a62
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sales);

<<<<<<< HEAD
        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<SalesHelper> options=
                new FirebaseRecyclerOptions.Builder<SalesHelper>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"), SalesHelper.class)
                    .build();
        adapter = new AdapterProduct(options);
        recview.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
=======
        findViewById(R.id.go_back_product_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardSales.class));
            }
        });
>>>>>>> 37593b8125fc315649330f86b582071b87d37a62
    }
}