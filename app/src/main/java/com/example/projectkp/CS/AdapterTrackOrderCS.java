package com.example.projectkp.CS;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.CS.Helper.TrackOrderCSHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterTrackOrderCS extends FirebaseRecyclerAdapter<TrackOrderCSHelper, AdapterTrackOrderCS.myviewholder> {

    public AdapterTrackOrderCS(@NonNull FirebaseRecyclerOptions<TrackOrderCSHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull TrackOrderCSHelper model) {
        holder.trackorder.setText(model.getTrackOrder());
        holder.detailsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(holder.itemView.getContext(), DetailSC.class);
                holder.itemView.getContext().startActivity(detail);
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_track_order_cs,parent,false);
        return new AdapterTrackOrderCS.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView trackorder;
        Button detailsc;
        public myviewholder(@NonNull View itemView){
            super(itemView);
            trackorder=(TextView)itemView.findViewById(R.id.myir_track_cs);
            detailsc= (Button) itemView.findViewById(R.id.myir_detail_sc);
        }
    }
}
