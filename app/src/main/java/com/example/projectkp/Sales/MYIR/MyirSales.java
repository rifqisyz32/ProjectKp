package com.example.projectkp.Sales.MYIR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.projectkp.R;
import com.example.projectkp.Sales.DashboardSales;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MyirSales extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar loadRV;
    FloatingActionButton inputMYIR;

    RecyclerView recview;
    AdapterMyirSales adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myir_sales);

        toolbar = findViewById(R.id.inputMYIR_toolbar_sales);
        loadRV = findViewById(R.id.loadRV_input_MYIR);
        inputMYIR = findViewById(R.id.input_MYIR);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputMYIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InputMyir.class));
                finish();
            }
        });

        recview = findViewById(R.id.recview_myir_sales);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MyirSalesHelper> options =
                new FirebaseRecyclerOptions.Builder<MyirSalesHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Myir"), MyirSalesHelper.class)
                        .build();
        adapter = new AdapterMyirSales(options);
        recview.setAdapter(adapter);
        loadRV.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DashboardSales.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}