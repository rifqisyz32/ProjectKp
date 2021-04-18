package com.example.projectkp.CS.FollowUp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.FollUpHelper;
import com.example.projectkp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdapterFollUpItem extends RecyclerView.Adapter<AdapterFollUpItem.follupViewHolder> implements Filterable {

    private OnItemClickListener listener;
    private String colorTV;
    private Context myContext;
    private List<FollUpHelper> myList;
    private List<FollUpHelper> myListFiltered;

    public AdapterFollUpItem(Context context, List<FollUpHelper> list) {
        myContext = context;
        myList = list;
        myListFiltered = list;
    }

    @Override
    public void onBindViewHolder(follupViewHolder holder, int position) {
        FollUpHelper helper = myListFiltered.get(position);

        holder.layer.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_scale_animation));
        holder.title.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));
        holder.user.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));
        holder.time.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));
        holder.status.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.fade_transition_animation));

        holder.title.setText(helper.getTitle());
        holder.user.setText(helper.getSalesID());
        holder.time.setText(helper.getTime());
        holder.status.setText(helper.getStatus());
    }

    @NonNull
    @Override
    public follupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myContext = parent.getContext();
        View v = LayoutInflater.from(myContext).inflate(R.layout.holder_follup_item, parent, false);
        return new follupViewHolder(v);
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
                    List<FollUpHelper> listFiltered = new ArrayList<>();
                    for (FollUpHelper row : myList) {

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

                myListFiltered = (List<FollUpHelper>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class follupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout layer;
        MaterialCardView cardBG;
        TextView title, user, time, status;

        public follupViewHolder(View itemView) {
            super(itemView);

            cardBG = itemView.findViewById(R.id.follup_card);
            layer = itemView.findViewById(R.id.follup_layout);
            title = itemView.findViewById(R.id.follup_title);
            user = itemView.findViewById(R.id.follup_user);
            time = itemView.findViewById(R.id.follup_time);
            status = itemView.findViewById(R.id.follup_stat);

            if (colorTV.equals("CS")) {
                cardBG.setCardBackgroundColor(myContext.getResources().getColor(R.color.cs_temp));
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(position);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public String changeColor(String color) {
        this.colorTV = color;
        return color;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
