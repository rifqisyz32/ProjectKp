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

import com.example.projectkp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MiniPack extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference miniPack = db.collection("Minipack");
    private AdapterThreePSales productAdapter;

    Toolbar toolbar;
    ProgressBar loadRV;
    RecyclerView productRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product_sales);

        toolbar = findViewById(R.id.sub_product_toolbar_sales);
        loadRV = findViewById(R.id.sub_product_prog_sales);
        productRV = findViewById(R.id.sub_product_recview_sales);

        setSupportActionBar(toolbar);
        toolbar.setTitle("MiniPack");
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
        Query query = miniPack.orderBy("price", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<SalesHelper> options = new FirestoreRecyclerOptions.Builder<SalesHelper>()
                .setQuery(query, SalesHelper.class)
                .build();

        productAdapter = new AdapterThreePSales(options);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productAdapter);
        loadRV.setVisibility(View.INVISIBLE);

        productAdapter.setOnItemClickListener(new AdapterThreePSales.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SalesHelper note = documentSnapshot.toObject(SalesHelper.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(getApplicationContext(), "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
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