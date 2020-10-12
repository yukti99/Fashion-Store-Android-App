package com.example.fashionista.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.fashionista.Interface.ItemClickListener;
import com.example.fashionista.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListener itemClickListener;
    // constructor
    public CartViewHolder(View itemView){
        super(itemView);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }
    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
