package com.example.projectkp.CS.Product.Item;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.CS.Product.Adapter.AdapterProductItem;
import com.example.projectkp.CS.Product.Edit.AddItem;
import com.example.projectkp.CS.Product.Edit.EditItem;
import com.example.projectkp.CS.Product.ProductListCS;
import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Orbit extends AppCompatActivity implements com.example.projectkp.CS.Product.Adapter.AdapterProductItem.OnItemClickListener {

    private final DatabaseReference Product = FirebaseDatabase.getInstance().getReference("Product");
    private AdapterProductItem productAdapter;
    private RecyclerView productRV;
    private List<ProductHelper> productList;
    private ProgressBar loadItem;
    private String deviceText, dbPosition;
    private final String myKey = "Orbit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_orbit);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        deviceText = getApplicationContext().getResources().getString(R.string.periodTime);

        Toolbar toolbar = findViewById(R.id.orbit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(myKey);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextInputEditText searchItem = findViewById(R.id.search_product_item);
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        FloatingActionButton addButton = findViewById(R.id.orbit_add);
        addButton.setVisibility(View.VISIBLE);

        addButton.setOnClickListener(v -> {
            Intent dataUser = new Intent(getApplicationContext(), AddItem.class);
            dataUser.putExtra("myKey", myKey);
            dataUser.putExtra("myTitle", myKey);
            dataUser.putExtra("device", deviceText);
            startActivity(dataUser);
        });

        loadItem = findViewById(R.id.orbit_prog);
        productRV = findViewById(R.id.orbit_rv);
        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProductListCS.class));
        finish();
    }

    private void setUpRecyclerView() {
        productRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productList = new ArrayList<>();

        Product.child(myKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    ProductHelper productHelper = productSnapshot.getValue(ProductHelper.class);
                    productList.add(productHelper);
                }

                productAdapter = new com.example.projectkp.CS.Product.Adapter.AdapterProductItem(getApplicationContext(), productList);
                productRV.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener(Orbit.this);
                productAdapter.changeDeviceText(getString(R.string.periodTV));
                productAdapter.changePriceText("");
                sortArrayList();
                loadItem.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortArrayList() {
        Collections.sort(productList, new Comparator<ProductHelper>() {
            @Override
            public int compare(ProductHelper o1, ProductHelper o2) {
                return extractInt(o1.getSpeed()) - extractInt(o2.getSpeed());
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }

        });
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        dbPosition = productList.get(position).getSpeed();
        Toast.makeText(getApplicationContext(), getString(R.string.productTV) + " " + dbPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteItem(int position) {
        dbPosition = productList.get(position).getSpeed();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_delete_outline_24)
                .setTitle(R.string.delete_item)
                .setMessage(R.string.delete_item_alert)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    Product.child(myKey).child(dbPosition).getRef().removeValue();
                    Toast.makeText(getApplicationContext(), R.string.delete_product_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                }).show();
    }

    @Override
    public void editItem(int position) {
        dbPosition = productList.get(position).getSpeed();
        Intent dataUser = new Intent(getApplicationContext(), EditItem.class);
        dataUser.putExtra("myKey", myKey);
        dataUser.putExtra("myTitle", myKey);
        dataUser.putExtra("device", deviceText);
        dataUser.putExtra("myRef", dbPosition);
        startActivity(dataUser);
    }
}