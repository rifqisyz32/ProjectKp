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


public class AddItem extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference Product = db.getReference("Product");

    Window window;
    Toolbar toolbar;
    ProgressBar addProgress;
    TextInputLayout addSpeed, addDevice, addPrice;
    TextView addTitle;
    String myKey, myTitle, deviceText;
    Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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
                addProgress.setVisibility(View.VISIBLE);
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
        toolbar = findViewById(R.id.add_product_toolbar);
        addProgress = findViewById(R.id.add_product_prog);
        addTitle = findViewById(R.id.add_product_title);
        addDevice = findViewById(R.id.add_product_device);
        addSpeed = findViewById(R.id.add_product_speed);
        addPrice = findViewById(R.id.add_product_price);
        save = findViewById(R.id.add_product_button);
        cancel = findViewById(R.id.add_product_cancel);
    }

    private void getData() {
        myKey = getIntent().getStringExtra("myKey");
        myTitle = getIntent().getStringExtra("myTitle");
        deviceText = getIntent().getStringExtra("device");
        addTitle.setText(myTitle);
        addDevice.setHint(deviceText);
    }

    private void storeUserData() {

        if (!validateDevice() | !validatePrice() | !validateSpeed()) {
            addProgress.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            return;
        }

        String myDevice = addDevice.getEditText().getText().toString().trim();
        String myPrice = addPrice.getEditText().getText().toString().trim();
        String mySpeed = addSpeed.getEditText().getText().toString().trim();

        Query checkProduct = Product.child(myKey).orderByChild("speed").equalTo(mySpeed);
        checkProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), R.string.product_exist, Toast.LENGTH_SHORT).show();
                    addProgress.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                } else {
                    ProductHelper storeData = new ProductHelper(myTitle, mySpeed, myPrice, myDevice);
                    Product.child(myKey).child(mySpeed).setValue(storeData);

                    addProgress.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.add_item_success, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateDevice() {
        String val = addDevice.getEditText().getText().toString();

        if (val.isEmpty()) {
            addDevice.setError(getString(R.string.cant_empty));
            return false;
        } else {
            addDevice.setError(null);
            addDevice.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePrice() {
        String val = addPrice.getEditText().getText().toString();

        if (val.isEmpty()) {
            addPrice.setError(getString(R.string.cant_empty));
            return false;
        } else {
            addPrice.setError(null);
            addPrice.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateSpeed() {
        String val = addSpeed.getEditText().getText().toString();

        if (val.isEmpty()) {
            addSpeed.setError(getString(R.string.cant_empty));
            return false;
        } else {
            addSpeed.setError(null);
            addSpeed.setErrorEnabled(false);
            return true;
        }
    }
}