package com.example.projectkp.CS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.R;
import com.example.projectkp.Sales.AdapterProduct;
import com.example.projectkp.Sales.SalesHelper;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ProductCS extends AppCompatActivity {
    RecyclerView recview;
    AdapterProductCS adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cs);

        findViewById(R.id.go_back_product_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardCS.class));
            }
        });
        recview=(RecyclerView)findViewById(R.id.recview_cs);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<CSHelper> options=
                new FirebaseRecyclerOptions.Builder<CSHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"), CSHelper.class)
                        .build();
        adapter = new AdapterProductCS(options);
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
    }
}