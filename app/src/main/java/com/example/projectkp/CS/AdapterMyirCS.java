package com.example.projectkp.CS;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.CS.Helper.MyirCSHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.makeText;

public class AdapterMyirCS extends FirebaseRecyclerAdapter<MyirCSHelper, AdapterMyirCS.myviewholder> {

    public AdapterMyirCS(@NonNull FirebaseRecyclerOptions<MyirCSHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull MyirCSHelper model) {
        holder.inputmyir.setText(model.getInputMyir());
        holder.inputsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Myir").child(model.getInputMyir());
                ClipboardManager clipboard = (ClipboardManager) holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", model.getInputMyir().toString());
                if (clipboard == null || clip == null) return;
                clipboard.setPrimaryClip(clip);
                databaseReference.removeValue();
                Toast.makeText( holder.itemView.getContext(),"MYIR Telah Disalin", Toast.LENGTH_SHORT).show();
                 }

        });
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_myirs_cs,parent,false);
        return new AdapterMyirCS.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView inputmyir;
        Button inputsc;
        public myviewholder(@NonNull View itemView){
            super(itemView);
            inputmyir=(TextView)itemView.findViewById(R.id.myir_cs);
            inputsc=itemView.findViewById(R.id.myir_input_sc);
        }
    }
}
