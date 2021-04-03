package com.example.projectkp.CS.Product.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.ProductHelper;
import com.example.projectkp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MiniPackTestAdapter extends RecyclerView.Adapter<MiniPackTestAdapter.productViewHolder> implements Filterable {

    private OnItemClickListener listener;
    private String deviceTV, priceTV;
    private Context myContext;
    private List<ProductHelper> myList;
    private List<ProductHelper> myListFiltered;

    public MiniPackTestAdapter(Context context, List<ProductHelper> list) {
        myContext = context;
        myList = list;
        myListFiltered = list;
    }

    @Override
    public void onBindViewHolder(productViewHolder holder, int position) {
        ProductHelper helper = myListFiltered.get(position);

        holder.layer.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_scale_animation));
        holder.speed.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));
        holder.price.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));

        holder.title.setText(helper.getTitle());
        holder.speed.setText(helper.getSpeed());
        holder.deviceDB.setText(helper.getDevice());
        holder.priceDB.setText(helper.getPrice());
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.holder_product_item, parent, false);
//        context = parent.getContext();
        return new productViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return myListFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    myListFiltered = myList ;

                }
                else {
                    List<ProductHelper> listFiltered = new ArrayList<>();
                    for (ProductHelper row : myList) {

                        if (row.getSpeed().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }
                    myListFiltered = listFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values= myListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                myListFiltered = (List<ProductHelper>) results.values;
                notifyDataSetChanged();
            }
        };
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
            speed.setTextColor(myContext.getResources().getColor(R.color.cs_temp));
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(position);
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
                        listener.editItem(position);
                        return true;
                    case 2:
                        listener.deleteItem(position);
                        return true;
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void deleteItem(int position);

        void editItem(int position);
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
