package com.example.projectkp.Sales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectkp.CS.DashboardCS;
import com.example.projectkp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputMyir extends AppCompatActivity {
    EditText input_myir;
    Button button_save;
    DatabaseReference kode_referal;
    DatabaseReference track;
    Myir myir;
    TrackOrder trackorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_myir);

        findViewById(R.id.go_back_input_myir_sales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyirSales.class));
                finish();
            }
        });

        input_myir=(EditText)findViewById(R.id.input_myir_text);
        button_save=(Button)findViewById(R.id.button_save_myir);
        kode_referal= FirebaseDatabase.getInstance().getReference().child("Myir");
        track = FirebaseDatabase.getInstance().getReference().child("TrackOrder");
        myir=new Myir();
        trackorder=new TrackOrder();

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myir.setInputMyir(input_myir.getText().toString().trim());
                trackorder.setTrackOrder(input_myir.getText().toString().trim());
                kode_referal.child(String.valueOf(myir.getInputMyir())).setValue(myir);
                track.child(trackorder.getTrackOrder()).setValue(trackorder);
                startActivity(new Intent(getApplicationContext(), MyirSales.class));
                Toast.makeText(InputMyir.this, "Input Sucessfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}