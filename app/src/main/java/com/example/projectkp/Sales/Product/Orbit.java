package com.example.projectkp.Sales.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Orbit extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference Product = db.getReference("Product");
    private AdapterOrbitSales productAdapter;

    Toolbar toolbar;
    RecyclerView productRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orbit);

        toolbar = findViewById(R.id.orbit_toolbar);
        productRV = findViewById(R.id.orbit_rv);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Orbit");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProductListSales.class));
        finish();
    }

    private void setUpRecyclerView() {
        FirebaseRecyclerOptions<ProductHelper> options = new FirebaseRecyclerOptions.Builder<ProductHelper>()
                .setQuery(Product.child("Orbit"), ProductHelper.class)
                .build();

        productAdapter = new AdapterOrbitSales(options);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(new AdapterOrbitSales.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                String myKey = dataSnapshot.getKey();
                Toast.makeText(getApplicationContext(), "Position: " + position + " ID: " + myKey, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        productAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }
}