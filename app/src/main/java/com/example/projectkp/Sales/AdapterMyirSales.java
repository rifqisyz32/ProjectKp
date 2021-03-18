package com.example.projectkp.Sales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterMyirSales extends FirebaseRecyclerAdapter<MyirSalesHelper, AdapterMyirSales.myviewholder> {
    public AdapterMyirSales(@NonNull FirebaseRecyclerOptions<MyirSalesHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull MyirSalesHelper model) {
        holder.inputmyir.setText(model.getInputMyir());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_myir_sales,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder{
        TextView inputmyir;
        public myviewholder(@NonNull View itemView){
            super(itemView);
            inputmyir=(TextView)itemView.findViewById(R.id.myir_sales);
        }
    }
}
