package com.example.projectkp.CS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MyirCS extends AppCompatActivity {
    RecyclerView recview;
    AdapterMyirCS adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myir_c_s);
        findViewById(R.id.go_back_myir_cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardCS.class));
                finish();
            }
        });
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        recview = findViewById(R.id.recview_myir_cs);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MyirCSHelper> options =
                new FirebaseRecyclerOptions.Builder<MyirCSHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Myir"), MyirCSHelper.class)
                        .build();
        adapter = new AdapterMyirCS(options);
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