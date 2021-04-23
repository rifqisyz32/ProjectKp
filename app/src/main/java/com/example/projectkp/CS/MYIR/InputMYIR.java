package com.example.projectkp.CS.MYIR;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.CS.Dashboard;
import com.example.projectkp.CS.FollowUp.AddOrder;
import com.example.projectkp.Helper.MYIRHelper;
import com.example.projectkp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InputMYIR extends AppCompatActivity implements AdapterMYIRItem.OnItemClickListener {

    private final DatabaseReference Order = FirebaseDatabase.getInstance().getReference("Order");
    private AdapterMYIRItem myirAdapter;
    private RecyclerView myirRV;
    private List<MYIRHelper> myirList;
    private ProgressBar loadItem;
    private String dbPosition;
    private final String myKey = "MYIR";
    private Dialog addDialog;
    private TextInputLayout addInput;
    private TextView addCancel, addSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_myir);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_cs));
        }

        Toolbar toolbar = findViewById(R.id.input_myir_new_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextInputEditText searchItem = findViewById(R.id.search_myir_item);
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myirAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.input_myir_new_add).setVisibility(View.GONE);
        loadItem = findViewById(R.id.input_myir_new_prog);
        myirRV = findViewById(R.id.input_myir_new_rv);
        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }

    private void setUpRecyclerView() {
        myirRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myirList = new ArrayList<>();

        Order.child(myKey).child("all").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myirList.clear();
                for (DataSnapshot myirSnapshot : snapshot.getChildren()) {
                    MYIRHelper myirHelper = myirSnapshot.getValue(MYIRHelper.class);
                    myirList.add(myirHelper);
                }

                myirAdapter = new AdapterMYIRItem(getApplicationContext(), myirList);
                myirRV.setAdapter(myirAdapter);
                myirAdapter.setOnItemClickListener(InputMYIR.this);
                myirAdapter.changeColor("CS");
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
        Collections.sort(myirList, new Comparator<MYIRHelper>() {

            @Override
            public int compare(MYIRHelper o1, MYIRHelper o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        myirAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        dbPosition = myirList.get(position).getTitle();
        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", dbPosition);

        if (clipboard == null || clip == null)
            return;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), getString(R.string.myirTV) + dbPosition + " " + getString(R.string.copas), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void follUpItem(int position) {
        dbPosition = myirList.get(position).getTitle();
        Intent order = new Intent(getApplicationContext(), AddOrder.class);
        order.putExtra("myir", dbPosition);
        startActivity(order);
        finish();
    }

    @Override
    public void editItem(int position) {
        dbPosition = myirList.get(position).getTitle();
        addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.dialog_add_myir);
        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addInput = addDialog.findViewById(R.id.dialog_add_item_input);
        TextView addTitle = addDialog.findViewById(R.id.dialog_add_item_title);
        addTitle.setTextColor(getResources().getColor(R.color.cs_temp));
        addCancel = addDialog.findViewById(R.id.dialog_add_item_no);
        addCancel.setTextColor(getResources().getColor(R.color.cs_temp));
        addSubmit = addDialog.findViewById(R.id.dialog_add_item_yes_sales);
        TextView addSubmitCS = addDialog.findViewById(R.id.dialog_add_item_yes_cs);
        addSubmit.setVisibility(View.GONE);
        addSubmitCS.setVisibility(View.VISIBLE);

        addTitle.setText(R.string.edit_myir);
        addSubmit.setText(R.string.edit);
        addInput.getEditText().setText(dbPosition);

        addSubmitCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addText = addInput.getEditText().getText().toString().trim();
                if (addText.isEmpty()) {
                    addInput.setError(getString(R.string.cant_empty));
                } else {
                    addInput.setError(null);
                    addInput.setErrorEnabled(false);

                    editMYIR(addText, dbPosition);
                    Toast.makeText(getApplicationContext(), R.string.myir_edited, Toast.LENGTH_SHORT).show();

                }
            }
        });

        addCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }

    private void editMYIR(String myTitle, String position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String myUser = currentUser.getDisplayName();
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
        String myTime = df.format(currentTime.getTime());

        Query checkMYIR = Order.child(myKey).child("all").orderByChild("title").equalTo(myTitle);
        checkMYIR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), R.string.myir_exist, Toast.LENGTH_SHORT).show();
                } else {
                    MYIRHelper storeData = new MYIRHelper(myTitle, myUser, myTime);
                    snapshot.child(myTitle).getRef().setValue(storeData);
                    snapshot.child(position).getRef().removeValue();
                    addDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteItem(int position) {
        dbPosition = myirList.get(position).getTitle();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_delete_outline_24)
                .setTitle(R.string.delete_myir)
                .setMessage(R.string.delete_item_alert)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    Order.child(myKey).child("all").child(dbPosition).getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), R.string.delete_myir_success, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                }).show();
    }
}