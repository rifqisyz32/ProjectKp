package com.example.projectkp.CS.Product.Item;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.CS.Product.Adapter.AdapterProductCS;
import com.example.projectkp.CS.Product.Edit.AddItem;
import com.example.projectkp.CS.Product.Edit.EditItem;
import com.example.projectkp.CS.Product.ProductListCS;
import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InternetPhoneTV extends AppCompatActivity implements AdapterProductCS.OnItemClickListener {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference Product = db.getReference("Product");
    private AdapterProductCS productAdapter;

    private RecyclerView productRV;
    private String deviceText;
    private final String myKey = "Internet_Phone_TV";
    private final String myTitle = "3P (Internet + Phone + TV)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product);

        Toolbar toolbar = findViewById(R.id.sub_product_toolbar);
        productRV = findViewById(R.id.sub_product_rv);
        FloatingActionButton addButton = findViewById(R.id.sub_product_add);
        FloatingActionButton bonusButton = findViewById(R.id.sub_product_bonus);
        deviceText = getApplicationContext().getResources().getString(R.string.device);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        setSupportActionBar(toolbar);
        toolbar.setTitle(myTitle);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        bonusButton.setVisibility(View.GONE);
        addButton.setVisibility(View.VISIBLE);
        setUpRecyclerView();

        addButton.setOnClickListener(v -> {
            Intent dataUser = new Intent(getApplicationContext(), AddItem.class);
            dataUser.putExtra("myKey", myKey);
            dataUser.putExtra("myTitle", myTitle);
            dataUser.putExtra("device", deviceText);
            startActivity(dataUser);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProductListCS.class));
        finish();
    }

    private void setUpRecyclerView() {
        FirebaseRecyclerOptions<ProductHelper> options = new FirebaseRecyclerOptions.Builder<ProductHelper>()
                .setQuery(Product.child(myKey), ProductHelper.class)
                .build();

        productAdapter = new AdapterProductCS(options);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(InternetPhoneTV.this);
        productAdapter.changeDeviceText(getString(R.string.deviceTV));
        productAdapter.changePriceText(getString(R.string.period));
    }

    @Override
    protected void onStart() {
        super.onStart();
        productAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }

    @Override
    public void onItemClick(DataSnapshot dataSnapshot, int position) {
        String myKey = dataSnapshot.getKey();
        Toast.makeText(getApplicationContext(), getString(R.string.productTV) + " " + myKey, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteItem(DataSnapshot dataSnapshot, int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_delete_outline_24)
                .setTitle(R.string.delete_item)
                .setMessage(R.string.delete_item_alert)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(getApplicationContext(), R.string.delete_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

                }).show();
    }

    @Override
    public void editItem(DataSnapshot dataSnapshot, int position) {
        String myRef = dataSnapshot.getKey();
        Intent dataUser = new Intent(getApplicationContext(), EditItem.class);
        dataUser.putExtra("myKey", myKey);
        dataUser.putExtra("myTitle", myTitle);
        dataUser.putExtra("device", deviceText);
        dataUser.putExtra("myRef", myRef);
        startActivity(dataUser);
    }
}
