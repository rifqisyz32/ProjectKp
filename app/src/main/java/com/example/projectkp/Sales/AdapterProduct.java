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

public class AdapterProduct extends FirebaseRecyclerAdapter<SalesHelper, AdapterProduct.myviewholder> {

    public AdapterProduct(@NonNull FirebaseRecyclerOptions<SalesHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull SalesHelper model) {
        holder.products.setText(model.getJenisProduct());
        holder.description.setText(model.getDeskripsiProduct());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_sales,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView products, description;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            products=(TextView)itemView.findViewById(R.id.products_sales);
            description=(TextView)itemView.findViewById(R.id.product_description_sales);
        }
    }
}
