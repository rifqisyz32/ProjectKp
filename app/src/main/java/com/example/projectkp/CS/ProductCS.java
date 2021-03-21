package com.example.projectkp.CS;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProductCS extends AppCompatActivity implements AdapterProductCS.onItemClickListener {

    Window window;
    Toolbar toolbar;
    ProgressBar loadRV;
    RecyclerView ProductRV;
    AdapterProductCS adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference productRef = db.collection("Product");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cs);

        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        toolbar = findViewById(R.id.product_toolbar_cs);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                finish();
            }
        });

        loadRV = findViewById(R.id.loadRV_prog);
        ProductRV = findViewById(R.id.recview_product_cs);

        setUpRecyclerView();

        ProductRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductDetailCS.class));
                finish();
            }
        });
    }

    private void setUpRecyclerView(){
        Query productQuery = productRef.orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CSHelper> options = new FirestoreRecyclerOptions.Builder<CSHelper>()
                .setQuery(productQuery, CSHelper.class)
                .build();

        adapter = new AdapterProductCS(options);
        ProductRV.setHasFixedSize(true);
        ProductRV.setLayoutManager(new LinearLayoutManager(this));
        ProductRV.setAdapter(adapter);
        adapter.setOnItemClickListener(ProductCS.this);
        loadRV.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Clicked " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(int position) {
        Toast.makeText(this, "2. Clicked " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "3. Clicked " + position, Toast.LENGTH_SHORT).show();
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
    }
}