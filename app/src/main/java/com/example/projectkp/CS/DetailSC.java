package com.example.projectkp.CS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectkp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailSC extends AppCompatActivity {
    EditText detail_sc;
    Button button_save;
    DatabaseReference kode_referal;
    InputDetailSC input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_s_c);

        findViewById(R.id.go_back_input_sc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TrackOrderCS.class));
                finish();
            }
        });

        detail_sc=(EditText)findViewById(R.id.input_sc_text);
        button_save=(Button)findViewById(R.id.button_save_input_sc);
        kode_referal= FirebaseDatabase.getInstance().getReference().child("track_order");
        input = new InputDetailSC();

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setDetailSC((detail_sc.getText().toString().trim()));
                kode_referal.child(String.valueOf(input.getDetailSC())).setValue(input);
                startActivity(new Intent(getApplicationContext(), TrackOrderCS.class));
                Toast.makeText(DetailSC.this, "Input Sucessfully", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}