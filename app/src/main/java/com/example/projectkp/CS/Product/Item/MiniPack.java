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

public class MiniPack extends AppCompatActivity implements AdapterProductItem.OnItemClickListener {

    private final DatabaseReference Product = FirebaseDatabase.getInstance().getReference("Product");
    private AdapterProductItem productAdapter;
    private RecyclerView productRV;
    private List<ProductHelper> productList;
    private ProgressBar loadItem;
    private String deviceText, dbPosition;
    private final String myKey = "Mini_Pack";
    private final String myTitle = "MiniPack";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product);

        deviceText = getApplicationContext().getResources().getString(R.string.channel);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        Toolbar toolbar = findViewById(R.id.sub_product_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(myTitle);
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

        FloatingActionButton bonusButton = findViewById(R.id.sub_product_bonus);
        bonusButton.setVisibility(View.GONE);

        FloatingActionButton addButton = findViewById(R.id.sub_product_add);
        addButton.setVisibility(View.VISIBLE);

        addButton.setOnClickListener(v -> {
            Intent dataUser = new Intent(getApplicationContext(), AddItem.class);
            dataUser.putExtra("myKey", myKey);
            dataUser.putExtra("myTitle", myTitle);
            dataUser.putExtra("device", deviceText);
            startActivity(dataUser);
        });

        loadItem = findViewById(R.id.sub_product_prog);
        productRV = findViewById(R.id.sub_product_rv);
        testUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProductListCS.class));
        finish();
    }

    /*
    private void setUpRecyclerView() {
        FirebaseRecyclerOptions<ProductHelper> options = new FirebaseRecyclerOptions.Builder<ProductHelper>()
                .setQuery(Product.child(myKey), ProductHelper.class)
                .build();

        productAdapter = new MiniPackTestAdapter(options);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(MiniPack.this);
        productAdapter.changeDeviceText(getString(R.string.channelTV));
        productAdapter.changePriceText(getString(R.string.period));
    }
    */

    /*
    private void testAddDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.add_product);
        LinearLayout linearLayout = new LinearLayout(this);

        TextInputEditText addItem = new TextInputEditText(this);
        addItem.setMinEms(32);
        addItem.setHint(R.string.add_product);
        addItem.setInputType(InputType.TYPE_CLASS_NUMBER);
        addItem.setTextColor(getResources().getColor(R.color.colorSubtitle));
        linearLayout.addView(addItem);
        linearLayout.setPadding(16, 16, 16, 16);
        alertDialog.setView(linearLayout);

        alertDialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!validateDialogText(addItem)) {
                    return;
                }
                ;

                String addItemText = addItem.getText().toString().trim();

                Intent dataUser = new Intent(getApplicationContext(), AddItem.class);
                dataUser.putExtra("myKey", myKey);
                dataUser.putExtra("myTitle", addItemText);
                dataUser.putExtra("device", deviceText);
                startActivity(dataUser);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create().show();
    }
    */

    private void testUpRecyclerView() {

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

                productAdapter = new AdapterProductItem(getApplicationContext(), productList);
                productRV.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener(MiniPack.this);
                productAdapter.changeDeviceText(getString(R.string.channelTV));
                productAdapter.changePriceText(getString(R.string.period));
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
                    Toast.makeText(getApplicationContext(), R.string.delete_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                }).show();
    }

    @Override
    public void editItem(int position) {
        dbPosition = productList.get(position).getSpeed();
        Intent dataUser = new Intent(getApplicationContext(), EditItem.class);
        dataUser.putExtra("myKey", myKey);
        dataUser.putExtra("myTitle", myTitle);
        dataUser.putExtra("device", deviceText);
        dataUser.putExtra("myRef", dbPosition);
        startActivity(dataUser);
    }
}