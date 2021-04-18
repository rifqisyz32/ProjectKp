package com.example.projectkp.Sales.FollowUp;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.CS.FollowUp.AdapterFollUpItem;
import com.example.projectkp.Helper.FollUpHelper;
import com.example.projectkp.R;
import com.example.projectkp.Sales.Dashboard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FollUpOrder extends AppCompatActivity implements com.example.projectkp.CS.FollowUp.AdapterFollUpItem.OnItemClickListener {

    private final DatabaseReference Order = FirebaseDatabase.getInstance().getReference("Order");
    private final String myKey = "Follow Up";
    private String myRef = "all";
    private AdapterFollUpItem follUpAdapter;
    private RecyclerView follUpRV;
    private List<FollUpHelper> follUpList;
    private ProgressBar loadItem;
    private String dbPosition, dbMYIR, dbStatus, dbSalesID, dbTime, dbResult;
    private Dialog resultDialog;
    private AppCompatRadioButton allOrder, completeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follup_order);

        Toolbar toolbar = findViewById(R.id.follup_order_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.follow_up);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        allOrder = findViewById(R.id.all_order_radio);
        allOrder.setTextColor(getResources().getColor(R.color.white));
        allOrder.setBackground(getResources().getDrawable(R.drawable.rb_sales_selector));
        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allOrder.setTextColor(getResources().getColor(R.color.white));
                completeOrder.setTextColor(getResources().getColor(R.color.sales_temp));

                myRef = "all";
                loadItem.setVisibility(View.VISIBLE);
                setUpRecyclerView(myRef);
            }
        });

        completeOrder = findViewById(R.id.complete_order_radio);
        completeOrder.setTextColor(getResources().getColor(R.color.sales_temp));
        completeOrder.setBackground(getResources().getDrawable(R.drawable.rb_sales_selector));
        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeOrder.setTextColor(getResources().getColor(R.color.white));
                allOrder.setTextColor(getResources().getColor(R.color.sales_temp));

                myRef = "Completed";
                loadItem.setVisibility(View.VISIBLE);
                setUpRecyclerView(myRef);
            }
        });

        loadItem = findViewById(R.id.follup_order_prog);
        follUpRV = findViewById(R.id.follup_order_rv);
        setUpRecyclerView(myRef);

        TextInputEditText searchItem = findViewById(R.id.search_order_item);
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                follUpAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }

    private void setUpRecyclerView(String ref) {
        follUpRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        follUpList = new ArrayList<>();

        Order.child(myKey).child(ref).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                follUpList.clear();
                for (DataSnapshot follUpSnapshot : snapshot.getChildren()) {
                    FollUpHelper follUpHelper = follUpSnapshot.getValue(FollUpHelper.class);
                    follUpList.add(follUpHelper);
                }

                follUpAdapter = new com.example.projectkp.CS.FollowUp.AdapterFollUpItem(getApplicationContext(), follUpList);
                follUpRV.setAdapter(follUpAdapter);
                follUpAdapter.setOnItemClickListener(FollUpOrder.this);
                follUpAdapter.changeColor("Sales");
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
        Collections.sort(follUpList, new Comparator<FollUpHelper>() {

            @Override
            public int compare(FollUpHelper o1, FollUpHelper o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        follUpAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        dbPosition = follUpList.get(position).getTitle();
        resultDialog = new Dialog(this);
        resultDialog.setContentView(R.layout.dialog_order_result);
        resultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout dialogBG = resultDialog.findViewById(R.id.dialog_follup_item_bg);
        LinearLayout edLayout = resultDialog.findViewById(R.id.dialog_follup_item_ed_layout);
        TextView resultTitleCS = resultDialog.findViewById(R.id.dialog_follup_item_title);
        TextView resultTitle = resultDialog.findViewById(R.id.dialog_follup_item_title_sales);
        TextView resultCopyCS = resultDialog.findViewById(R.id.dialog_follup_item_yes_cs);
        TextView resultMYIR = resultDialog.findViewById(R.id.dialog_follup_item_myirDB);
        TextView resultSalesID = resultDialog.findViewById(R.id.dialog_follup_item_salesID_DB);
        TextView resultTime = resultDialog.findViewById(R.id.dialog_follup_item_time);
        TextView resultStatus = resultDialog.findViewById(R.id.dialog_follup_item_statusDB);
        TextView resultValue = resultDialog.findViewById(R.id.dialog_follup_item_sc_resultDB);
        TextView resultCopy = resultDialog.findViewById(R.id.dialog_follup_item_yes_sales);
        TextView resultClose = resultDialog.findViewById(R.id.dialog_follup_item_no);

        dialogBG.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_scale_animation));
        resultTitleCS.setVisibility(View.GONE);
        resultTitle.setVisibility(View.VISIBLE);
        edLayout.setVisibility(View.GONE);
        resultCopyCS.setVisibility(View.GONE);

        Query checkFollowUp = Order.child(myKey).child(myRef).orderByChild("title").equalTo(dbPosition);
        checkFollowUp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), R.string.order_not_found, Toast.LENGTH_SHORT).show();
                } else {
                    dbMYIR = snapshot.child(dbPosition).child("title").getValue().toString();
                    dbSalesID = snapshot.child(dbPosition).child("salesID").getValue().toString();
                    dbStatus = snapshot.child(dbPosition).child("status").getValue().toString();
                    dbTime = snapshot.child(dbPosition).child("time").getValue().toString();
                    dbResult = snapshot.child(dbPosition).child("result").getValue().toString();

                    resultMYIR.setText(dbMYIR);
                    resultSalesID.setText(dbSalesID);
                    resultStatus.setText(dbStatus);
                    resultTime.setText(dbTime);
                    resultValue.setText(dbResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        resultCopy.setVisibility(View.VISIBLE);
        resultCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", dbResult);

            if (clipboard == null || clip == null)
                return;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), getString(R.string.sc_copied), Toast.LENGTH_SHORT).show();

            if (myRef.matches("all")) {
                Query deleteFollowUp = Order.child(myKey).child(myRef).orderByChild("title").equalTo(dbPosition);
                deleteFollowUp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getApplicationContext(), R.string.order_not_found, Toast.LENGTH_SHORT).show();
                        } else {
                            Date currentTime = Calendar.getInstance().getTime();
                            String myTime = currentTime.toString();
                            FollUpHelper moveData = new FollUpHelper(dbMYIR, myTime, dbSalesID, dbStatus, dbResult);
                            Order.child(myKey).child("Completed").child(dbPosition).setValue(moveData);
                            snapshot.child(dbPosition).getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            resultDialog.dismiss();
        });

        resultClose.setTextColor(this.getResources().getColor(R.color.sales_temp));
        resultClose.setOnClickListener(v -> resultDialog.dismiss());
        resultDialog.show();
    }
}