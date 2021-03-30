package com.example.projectkp.CS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectkp.CS.Helper.TrackOrderCSHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TrackOrderCS extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recview;
    AdapterTrackOrderCS adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_cs);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recview = findViewById(R.id.track_order_rv_cs);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TrackOrderCSHelper> options =
                new FirebaseRecyclerOptions.Builder<TrackOrderCSHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TrackOrder"), TrackOrderCSHelper.class)
                        .build();
        adapter = new AdapterTrackOrderCS(options);
        recview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DashboardCS.class));
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