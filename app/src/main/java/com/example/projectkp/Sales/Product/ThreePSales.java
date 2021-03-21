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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ThreePSales extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference threeP = db.collection("3P (Internet + TV + Phone)");
    private AdapterThreePSales productAdapter;

    Toolbar toolbar;
    ProgressBar loadRV;
    RecyclerView productRV;
    FloatingActionButton addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product_sales);

        toolbar = findViewById(R.id.sub_product_toolbar_sales);
        loadRV = findViewById(R.id.sub_product_prog_sales);
        productRV = findViewById(R.id.sub_product_recview_sales);
        addProduct = findViewById(R.id.add_product_sales);

        setSupportActionBar(toolbar);
        toolbar.setTitle("3P Internet + TV + Phone");
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
        Query query = threeP.orderBy("speed", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<SalesHelper> options = new FirestoreRecyclerOptions.Builder<SalesHelper>()
                .setQuery(query, SalesHelper.class)
                .build();

        productAdapter = new AdapterThreePSales(options);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productAdapter);
        loadRV.setVisibility(View.INVISIBLE);

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
