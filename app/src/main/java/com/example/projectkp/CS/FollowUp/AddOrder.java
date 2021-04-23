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

import com.example.projectkp.CS.MYIR.InputMYIR;
import com.example.projectkp.Helper.FollUpHelper;
import com.example.projectkp.Helper.MYIRHelper;
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


public class AddOrder extends AppCompatActivity {

    private final DatabaseReference Order = FirebaseDatabase.getInstance().getReference("Order");
    private Toolbar toolbar;
    private ProgressBar addProgress;
    private TextInputLayout addSalesId, addStatus, addResult;
    private TextView addTitle;
    private String myTitle;
    private final String myKey = "Follow Up";
    private Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follup_item);

        storeId();
        String myirCode = getIntent().getStringExtra("myir");
        addTitle.setText(myirCode);
        myTitle = myirCode;

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
            addProgress.setVisibility(View.VISIBLE);
            storeOrder();
        });

        cancel.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), InputMYIR.class));
        finish();
    }

    private void storeId() {
        toolbar = findViewById(R.id.add_follup_toolbar);
        addProgress = findViewById(R.id.add_follup_prog);
        addTitle = findViewById(R.id.add_follup_title);
        addStatus = findViewById(R.id.add_follup_status);
        addSalesId = findViewById(R.id.add_follup_sales_id);
        addResult = findViewById(R.id.add_follup_result);
        save = findViewById(R.id.add_follup_button);
        cancel = findViewById(R.id.add_follup_cancel);
    }

    private void storeOrder() {

        if (!validateStatus() | !validateResult() | !validateSalesID()) {
            addProgress.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            return;
        }

        String myStatus = addStatus.getEditText().getText().toString().trim();
        String myResult = addResult.getEditText().getText().toString().trim();
        String mySalesID = addSalesId.getEditText().getText().toString().trim();

        Query checkFollowUp = Order.child(myKey).child("all").orderByChild("title").equalTo(myTitle);
        checkFollowUp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), R.string.order_exist, Toast.LENGTH_SHORT).show();
                    addProgress.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                } else {
                    Calendar currentTime = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
                    String myTime = df.format(currentTime.getTime());
                    FollUpHelper storeData = new FollUpHelper(myTitle, myTime, mySalesID, myStatus, myResult);
                    Order.child(myKey).child("all").child(myTitle).setValue(storeData);
                    moveMYIR(myTitle);

                    addProgress.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.foll_up_success, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveMYIR(String title) {
        Query checkFollowUp = Order.child("MYIR").child("all").orderByChild("title").equalTo(title);
        checkFollowUp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String timeMYIR = snapshot.child(title).child("time").getValue().toString();
                    String userMYIR = snapshot.child(title).child("user").getValue().toString();
                    MYIRHelper moveMYIR = new MYIRHelper(title, userMYIR, timeMYIR);
                    Order.child("MYIR").child("Completed").child(title).setValue(moveMYIR);
                    snapshot.child(title).getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateStatus() {
        String val = addStatus.getEditText().getText().toString();

        if (val.isEmpty()) {
            addStatus.setError(getString(R.string.cant_empty));
            return false;
        } else {
            addStatus.setError(null);
            addStatus.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateResult() {
        String val = addResult.getEditText().getText().toString();

        if (val.isEmpty()) {
            addResult.setError(getString(R.string.cant_empty));
            return false;
        } else {
            addResult.setError(null);
            addResult.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateSalesID() {
        String val = addSalesId.getEditText().getText().toString();

        if (val.isEmpty()) {
            addSalesId.setError(getString(R.string.cant_empty));
            return false;
        } else {
            addSalesId.setError(null);
            addSalesId.setErrorEnabled(false);
            return true;
        }
    }
}