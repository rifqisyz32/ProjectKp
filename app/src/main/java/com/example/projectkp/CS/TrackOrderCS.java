package com.example.projectkp.CS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TrackOrderCS extends AppCompatActivity {
    RecyclerView recview;
    AdapterTrackOrderCS adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_c_s);

        findViewById(R.id.go_back_track_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                finish();
            }
        });
        recview = findViewById(R.id.recview_track_cs);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TrackOrderCSHelper> options =
                new FirebaseRecyclerOptions.Builder<TrackOrderCSHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TrackOrder"), TrackOrderCSHelper.class)
                        .build();
        adapter = new AdapterTrackOrderCS(options);
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