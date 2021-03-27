package com.example.projectkp.Sales.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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

public class InternetPhoneTV extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference Product = db.getReference("Product");
    private AdapterPSales productAdapter;

    Toolbar toolbar;
    RecyclerView productRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product);

        toolbar = findViewById(R.id.sub_product_toolbar);
        productRV = findViewById(R.id.sub_product_rv);

        setSupportActionBar(toolbar);
        toolbar.setTitle("3P (Internet + Phone + TV)");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddProductSales.class));
            }
        });*/

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
                .setQuery(Product.child("Internet_Phone_TV"), ProductHelper.class)
                .build();

        productAdapter = new AdapterPSales(options);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productAdapter);

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView productRV, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                productAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(productRV);*/

        productAdapter.setOnItemClickListener(new AdapterPSales.OnItemClickListener() {
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
