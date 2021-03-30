package com.example.projectkp.CS.Product.Edit;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class EditItem extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference Product = db.getReference("Product");

    Window window;
    Toolbar toolbar;
    ProgressBar editProgress;
    TextInputLayout editSpeed, editDevice, editPrice;
    TextView editTitle;
    String deviceText, myKey, myRef, myTitle, dbDevice, dbPrice, dbSpeed, myDevice, myPrice, mySpeed;
    Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        storeId();
        getData();

        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.GONE);
                editProgress.setVisibility(View.VISIBLE);
                storeUserData();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void storeId() {
        toolbar = findViewById(R.id.edit_product_toolbar);
        editProgress = findViewById(R.id.edit_product_prog);
        editTitle = findViewById(R.id.edit_product_title);
        editDevice = findViewById(R.id.edit_product_device);
        editSpeed = findViewById(R.id.edit_product_speed);
        editPrice = findViewById(R.id.edit_product_price);
        save = findViewById(R.id.edit_product_button);
        cancel = findViewById(R.id.edit_product_cancel);
    }

    private void getData() {
        myKey = getIntent().getStringExtra("myKey");
        myRef = getIntent().getStringExtra("myRef");
        myTitle = getIntent().getStringExtra("myTitle");
        deviceText = getIntent().getStringExtra("device");
        editTitle.setText(myTitle);
        editDevice.setHint(deviceText);

        Query checkProduct = Product.child(myKey).orderByChild("speed").equalTo(myRef);
        checkProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    dbDevice = snapshot.child(myRef).child("device").getValue(String.class);
                    dbPrice = snapshot.child(myRef).child("price").getValue(String.class);
                    dbSpeed = snapshot.child(myRef).child("speed").getValue(String.class);

                    editDevice.getEditText().setText(dbDevice);
                    editPrice.getEditText().setText(dbPrice);
                    editSpeed.getEditText().setText(dbSpeed);

                } else {
                    Toast.makeText(getApplicationContext(), R.string.data_not_found, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void storeUserData() {

        Query checkProduct = Product.orderByChild("child").equalTo(myKey);
        checkProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {

                    dbDevice = snapshot.child(myKey).child(myRef).child("device").getValue(String.class);
                    dbPrice = snapshot.child(myKey).child(myRef).child("price").getValue(String.class);
                    dbSpeed = snapshot.child(myKey).child(myRef).child("speed").getValue(String.class);

                    myDevice = editDevice.getEditText().getText().toString().trim();
                    myPrice = editPrice.getEditText().getText().toString().trim();
                    mySpeed = editSpeed.getEditText().getText().toString().trim();

                    if (myDevice.isEmpty()) {
                        editDevice.getEditText().setText(dbDevice);
                    }
                    if (myPrice.isEmpty()) {
                        editPrice.getEditText().setText(dbPrice);
                    }
                    if (mySpeed.isEmpty()) {
                        editSpeed.getEditText().setText(dbSpeed);
                    }

                    myDevice = editDevice.getEditText().getText().toString().trim();
                    myPrice = editPrice.getEditText().getText().toString().trim();
                    mySpeed = editSpeed.getEditText().getText().toString().trim();

                    ProductHelper storeData = new ProductHelper(myTitle, mySpeed, myPrice, myDevice);
                    Product.child(myKey).child(myRef).setValue(storeData);

                    editProgress.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.edit_item_success, Toast.LENGTH_SHORT).show();
                } else {
                    editProgress.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Something went wrong\nPlease contact us to fix this", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                editProgress.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}