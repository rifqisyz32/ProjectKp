package com.example.projectkp.Sales.Product.Item;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.example.projectkp.Sales.Product.Adapter.AdapterProductItem;
import com.example.projectkp.Sales.Product.ProductList;
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

public class Orbit extends AppCompatActivity implements com.example.projectkp.Sales.Product.Adapter.AdapterProductItem.OnItemClickListener {

    private final DatabaseReference Product = FirebaseDatabase.getInstance().getReference("Product");
    private AdapterProductItem productAdapter;
    private RecyclerView productRV;
    private List<ProductHelper> productList;
    private ProgressBar loadItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_orbit);

        Toolbar toolbar = findViewById(R.id.orbit_toolbar);
        setSupportActionBar(toolbar);
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

        loadItem = findViewById(R.id.orbit_prog);
        productRV = findViewById(R.id.orbit_rv);
        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProductList.class));
        finish();
    }

    private void setUpRecyclerView() {
        productRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productList = new ArrayList<>();

        Product.child("Orbit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    ProductHelper productHelper = productSnapshot.getValue(ProductHelper.class);
                    productList.add(productHelper);
                }

                productAdapter = new com.example.projectkp.Sales.Product.Adapter.AdapterProductItem(getApplicationContext(), productList);
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
        String dbPosition = productList.get(position).getSpeed();
        Toast.makeText(getApplicationContext(), getString(R.string.productTV) + " " + dbPosition, Toast.LENGTH_SHORT).show();
    }
}