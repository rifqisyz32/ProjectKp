package com.example.projectkp.Sales.NewMYIR;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.MYIRHelper;
import com.example.projectkp.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMYIRItem extends RecyclerView.Adapter<AdapterMYIRItem.myirViewHolder> implements Filterable {

    private OnItemClickListener listener;
    private Context myContext;
    private List<MYIRHelper> myList;
    private List<MYIRHelper> myListFiltered;

    public AdapterMYIRItem(Context context, List<MYIRHelper> list) {
        myContext = context;
        myList = list;
        myListFiltered = list;
    }

    @Override
    public void onBindViewHolder(myirViewHolder holder, int position) {
        MYIRHelper helper = myListFiltered.get(position);

        holder.layer.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_scale_animation));
        holder.title.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));
        holder.user.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));
        holder.time.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));

        holder.title.setText(helper.getTitle());
        holder.user.setText(helper.getUser());
        holder.time.setText(helper.getTime());
    }

    @NonNull
    @Override
    public myirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myContext = parent.getContext();
        View v = LayoutInflater.from(myContext).inflate(R.layout.holder_myir_item, parent, false);
        return new myirViewHolder(v);
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

                    myListFiltered = myList;

                } else {
                    List<MYIRHelper> listFiltered = new ArrayList<>();
                    for (MYIRHelper row : myList) {

                        if (row.getTitle().toLowerCase().contains(Key.toLowerCase())) {
                            listFiltered.add(row);
                        }
                    }
                    myListFiltered = listFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = myListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                myListFiltered = (List<MYIRHelper>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class myirViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        RelativeLayout layer;
        TextView title, user, time;

        public myirViewHolder(View itemView) {
            super(itemView);

            layer = itemView.findViewById(R.id.myir_layout);
            title = itemView.findViewById(R.id.myir_title);
            user = itemView.findViewById(R.id.myir_user);
            time = itemView.findViewById(R.id.myir_time);

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

        void editItem(int position);

        void deleteItem(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
