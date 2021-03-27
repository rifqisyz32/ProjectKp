package com.example.projectkp.Sales.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

public class AdapterPSales extends FirebaseRecyclerAdapter<ProductHelper, AdapterPSales.productViewHolder> {

    private OnItemClickListener listener;
    Context context;

    public AdapterPSales(@NonNull FirebaseRecyclerOptions<ProductHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull productViewHolder holder, int position, @NonNull ProductHelper model) {
        holder.layer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.speed.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.price.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        holder.title.setText(model.getTitle());
        holder.speed.setText(model.getSpeed());
        holder.deviceDB.setText(model.getDevice());
        holder.priceDB.setText(model.getPrice());
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_item, parent, false);
        context = parent.getContext();
        return new productViewHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getRef().removeValue();
    }

    class productViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layer;
        TextView title, speed, device, deviceDB, price, priceDB;

        public productViewHolder(View itemView) {
            super(itemView);

            layer = itemView.findViewById(R.id.product_layout);
            title = itemView.findViewById(R.id.product_title);
            speed = itemView.findViewById(R.id.product_speed);
            device = itemView.findViewById(R.id.product_device);
            deviceDB = itemView.findViewById(R.id.product_device_db);
            price = itemView.findViewById(R.id.product_price);
            priceDB = itemView.findViewById(R.id.product_price_db);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
