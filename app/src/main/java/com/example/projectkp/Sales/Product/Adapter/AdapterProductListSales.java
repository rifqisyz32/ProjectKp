package com.example.projectkp.Sales.Product.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectkp.Helper.ProductListHelper;
import com.example.projectkp.R;
import com.example.projectkp.Sales.Product.Item.InternetPhone;
import com.example.projectkp.Sales.Product.Item.InternetPhoneTV;
import com.example.projectkp.Sales.Product.Item.InternetTV;
import com.example.projectkp.Sales.Product.Item.MiniPack;
import com.example.projectkp.Sales.Product.Item.Orbit;

import java.util.List;

public class AdapterProductListSales extends RecyclerView.Adapter<AdapterProductListSales.productViewHolder> {

    Context mContext;
    List<ProductListHelper> list;

    public AdapterProductListSales(Context mContext, List<ProductListHelper> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.holder_product_list, viewGroup, false);
        productViewHolder viewHolder = new productViewHolder((v));

        viewHolder.listProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productPosition = viewHolder.getAdapterPosition();
                moveToProduct(productPosition);
            }
        });

        return viewHolder;
    }

    public void moveToProduct(int position) {
        switch (position) {
            case 0:
                mContext.startActivity(new Intent(mContext, InternetPhone.class));
                break;
            
            case 1:
                mContext.startActivity(new Intent(mContext, InternetTV.class));
                break;
                
            case 2:
                mContext.startActivity(new Intent(mContext, InternetPhoneTV.class));
                break;

            case 3:
                mContext.startActivity(new Intent(mContext, MiniPack.class));
                break;

            case 4:
                mContext.startActivity(new Intent(mContext, Orbit.class));
                break;

            default:
                Toast.makeText(mContext, "Something wrong, please contact us to fix this", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder productVH, int i) {
        productVH.listProductItem.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        productVH.listProductImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        productVH.listProductText.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));

        productVH.listProductImage.setImageResource(list.get(i).getIcon());
        productVH.listProductText.setText(list.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class productViewHolder extends RecyclerView.ViewHolder {

        private CardView listProductItem;
        private ImageView listProductImage;
        private TextView listProductText;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            listProductItem = itemView.findViewById(R.id.list_product_item_card);
            listProductImage = itemView.findViewById(R.id.list_product_item_img);
            listProductText = itemView.findViewById(R.id.list_product_item_text);
        }
    }
}
