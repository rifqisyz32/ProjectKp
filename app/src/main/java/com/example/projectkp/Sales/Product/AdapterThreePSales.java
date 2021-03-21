/*
package com.example.projectkp.Sales.Product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Sales.SalesHelper;
import com.example.projectkp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdapterProductSales extends FirestoreRecyclerAdapter<SalesHelper, AdapterProductSales.myViewHolder> {
    private OnItemClickListener listener;

    public AdapterProductSales(@NonNull FirestoreRecyclerOptions<SalesHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull SalesHelper model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getSpeed());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_sales, parent, false);
        return new myViewHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        public myViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.product_title_sales);
            textViewDescription = itemView.findViewById(R.id.product_description_sales);
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
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
}*/

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

import com.example.projectkp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdapterThreePSales extends FirestoreRecyclerAdapter<SalesHelper, AdapterThreePSales.productViewHolder> {

    private OnItemClickListener listener;
    Context productContext;

    public AdapterThreePSales(@NonNull FirestoreRecyclerOptions<SalesHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull productViewHolder holder, int position, @NonNull SalesHelper model) {
        holder.productLayer.setAnimation(AnimationUtils.loadAnimation(productContext, R.anim.fade_scale_animation));
        holder.speed.setAnimation(AnimationUtils.loadAnimation(productContext, R.anim.fade_transition_animation));
        holder.price.setAnimation(AnimationUtils.loadAnimation(productContext, R.anim.fade_transition_animation));

        holder.title.setText(model.getTitle());
        holder.speed.setText(model.getSpeed());
        holder.device.setText(model.getDevice());
        holder.price.setText(model.getPrice());
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_item_sales, parent, false);
        productContext = parent.getContext();
        return new productViewHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class productViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout productLayer;
        TextView title, speed, device, price;

        public productViewHolder(View itemView) {
            super(itemView);

            productLayer = itemView.findViewById(R.id.product_layout_sales);
            title = itemView.findViewById(R.id.product_title_sales);
            speed = itemView.findViewById(R.id.product_speed_sales);
            device = itemView.findViewById(R.id.product_device_sales);
            price = itemView.findViewById(R.id.product_price_sales);

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
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
