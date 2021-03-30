package com.example.projectkp.CS.Product.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class AdapterProductCS extends FirebaseRecyclerAdapter<ProductHelper, AdapterProductCS.productViewHolder> {

    private OnItemClickListener listener;
    private String deviceTV, priceTV;
    Context context;

    public AdapterProductCS(@NonNull FirebaseRecyclerOptions<ProductHelper> options) {
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

    class productViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
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

            device.setText(changeDeviceText(deviceTV));
            price.setText(changePriceText(priceTV));
            speed.setTextColor(context.getResources().getColor(R.color.cs_temp));
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(getSnapshots().getSnapshot(position), position);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.clearHeader();
            MenuItem editMenu = menu.add(Menu.NONE, 1, 1, R.string.edit);
            MenuItem deleteMenu = menu.add(Menu.NONE, 2, 2, R.string.delete);

            editMenu.setOnMenuItemClickListener(this);
            deleteMenu.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                switch (item.getItemId()) {
                    case 1:
                        listener.editItem(getSnapshots().getSnapshot(position), position);
                        return true;
                    case 2:
                        listener.deleteItem(getSnapshots().getSnapshot(position), position);
                        return true;
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DataSnapshot dataSnapshot, int position);

        void deleteItem(DataSnapshot dataSnapshot, int position);

        void editItem(DataSnapshot dataSnapshot, int position);
    }

    public String changeDeviceText(String device) {
        this.deviceTV = device;
        return device;
    }

    public String changePriceText(String price) {
        this.priceTV = price;
        return price;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
