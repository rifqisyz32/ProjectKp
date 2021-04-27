package com.example.projectkp.CS.FollowUp;

import android.content.Intent;
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

import com.example.projectkp.Helper.FollUpHelper;
import com.example.projectkp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditOrder extends AppCompatActivity {

    private final DatabaseReference Order = FirebaseDatabase.getInstance().getReference("Order");
    private Toolbar toolbar;
    private ProgressBar editProgress;
    private TextInputLayout editcsID, editStatus, editResult;
    private TextView editTitle;
    private String myTitle, myStatus, mySalesID, myResult;
    private final String myKey = "Follow Up";
    private Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follup_item);

        storeId();
        getOrderData();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        save.setOnClickListener(v -> {
            save.setVisibility(View.GONE);
            editProgress.setVisibility(View.VISIBLE);
            storeOrder();
        });

        cancel.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), FollUpOrder.class));
        finish();
    }

    private void storeId() {
        toolbar = findViewById(R.id.add_follup_toolbar);
        editProgress = findViewById(R.id.add_follup_prog);
        editTitle = findViewById(R.id.add_follup_title);
        editStatus = findViewById(R.id.add_follup_status);
        editcsID = findViewById(R.id.add_follup_sales_id);
        editResult = findViewById(R.id.add_follup_result);
        save = findViewById(R.id.add_follup_button);
        cancel = findViewById(R.id.add_follup_cancel);
    }

    private void getOrderData() {
        myTitle = getIntent().getStringExtra("myir");

        Query checkFollowUp = Order.child(myKey).child("all").orderByChild("title").equalTo(myTitle);
        checkFollowUp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myStatus = snapshot.child(myTitle).child("status").getValue().toString();
                    mySalesID = snapshot.child(myTitle).child("salesID").getValue().toString();
                    myResult = snapshot.child(myTitle).child("result").getValue().toString();

                    editTitle.setText(myTitle);
                    editcsID.getEditText().setText(mySalesID);
                    editStatus.getEditText().setText(myStatus);
                    editResult.getEditText().setText(myResult);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.order_not_found, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeOrder() {

        if (!validateStatus() | !validateResult() | !validateSalesID()) {
            editProgress.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            return;
        }

        myStatus = editStatus.getEditText().getText().toString().trim();
        myResult = editResult.getEditText().getText().toString().trim();
        mySalesID = editcsID.getEditText().getText().toString().trim();

        Query checkFollowUp = Order.child(myKey).child("all").orderByChild("title").equalTo(myTitle);
        checkFollowUp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String myTime = df.format(currentTime.getTime());
                FollUpHelper storeData = new FollUpHelper(myTitle, myTime, mySalesID, myStatus, myResult);
                Order.child(myKey).child("all").child(myTitle).setValue(storeData);

                editProgress.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), R.string.foll_up_edit, Toast.LENGTH_SHORT).show();
                onBackPressed();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateStatus() {
        String val = editStatus.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            editStatus.setError(getString(R.string.cant_empty));
            return false;
        } else {
            editStatus.setError(null);
            editStatus.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateResult() {
        String val = editResult.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            editResult.setError(getString(R.string.cant_empty));
            return false;
        } else {
            editResult.setError(null);
            editResult.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateSalesID() {
        String val = editcsID.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            editcsID.setError(getString(R.string.cant_empty));
            return false;
        } else {
            editcsID.setError(null);
            editcsID.setErrorEnabled(false);
            return true;
        }
    }
}