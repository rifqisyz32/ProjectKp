package com.example.projectkp.Sales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TrackOrderSales extends AppCompatActivity {
    RecyclerView recview;
    AdapterTrackOrderSales adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_sales);

        findViewById(R.id.go_back_track_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardSales.class));
                finish();
            }
        });

        recview = findViewById(R.id.recview_track_sales);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TrackOrderSalesHelper> options =
                new FirebaseRecyclerOptions.Builder<TrackOrderSalesHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TrackOrder"), TrackOrderSalesHelper.class)
                        .build();
        adapter = new AdapterTrackOrderSales(options);
        recview.setAdapter(adapter);
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