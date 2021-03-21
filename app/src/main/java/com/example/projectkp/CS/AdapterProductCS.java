package com.example.projectkp.CS;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterProductCS extends FirestoreRecyclerAdapter<CSHelper, AdapterProductCS.myViewHolder> {

    private onItemClickListener productListener;

    public AdapterProductCS(@NonNull FirestoreRecyclerOptions<CSHelper> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_product_cs, parent, false);
        return new myViewHolder((v));
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull CSHelper model) {

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getSpeed());
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ConstraintLayout product;
        TextView title, description;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.product_layout_cs);
            title = itemView.findViewById(R.id.product_title_cs);
            description = itemView.findViewById(R.id.product_description_cs);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (productListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    productListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem editProductItem = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem deleteProductItem = menu.add(Menu.NONE, 2, 2, "Delete");

            editProductItem.setOnMenuItemClickListener(this);
            deleteProductItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (productListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            productListener.onEditClick(position);
                            return true;
                        case 2:
                            productListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

    }

    public interface onItemClickListener {
        void onItemClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(onItemClickListener listener) {
        productListener = listener;
    }
}
