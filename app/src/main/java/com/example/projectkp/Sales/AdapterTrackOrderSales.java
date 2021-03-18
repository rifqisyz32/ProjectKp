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

public class AdapterTrackOrderSales extends FirebaseRecyclerAdapter<TrackOrderSalesHelper, AdapterTrackOrderSales.myviewholder> {

    public AdapterTrackOrderSales(@NonNull FirebaseRecyclerOptions<TrackOrderSalesHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull TrackOrderSalesHelper model) {
        holder.trackorder.setText(model.getTrackOrder());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_track_order_sales,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView trackorder;
        public myviewholder(@NonNull View itemView){
            super(itemView);
            trackorder=(TextView)itemView.findViewById(R.id.myir_track_sales);
        }
    }
}
