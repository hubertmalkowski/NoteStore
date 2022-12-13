package com.example.notestore.CartView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notestore.ProductPage.ProductView;
import com.example.notestore.R;
import com.example.notestore.Storage.Cart.CartItem;
import com.example.notestore.Storage.Products.Product;

import java.util.ArrayList;
import java.util.function.Function;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ItemViewHolder> {

    ArrayList<CartItem> cartItems;
    Context context;
    Function<Integer, Void> onQuantityChange;


    public CartItemAdapter(ArrayList<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    public CartItemAdapter(ArrayList<CartItem> cartItems, Context context, Function<Integer, Void> onQuantityChange) {
        this.cartItems = cartItems;
        this.context = context;
        this.onQuantityChange = onQuantityChange;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Product item = cartItems.get(position).getProduct();

        holder.productNameView.setText(item.getName());
        holder.productPriceView.setText(String.valueOf(item.getPrice() * cartItems.get(position).getQuantity()));
        holder.productQuantityView.setText(String.valueOf(cartItems.get(position).getQuantity()));


        holder.itemView.setOnClickListener(view -> {
            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putInt(ProductView.ARG_PARAM1, item.getId());
            fragmentManager.beginTransaction()
                    .addSharedElement(holder.itemView, "cardToProduct")
                    .replace(R.id.fragment_container_view, ProductView.class,bundle )
                    .setReorderingAllowed(true)
                    .addToBackStack(ProductView.TAG)
                    .commit();
        });
        holder.changeQuantityButton.setOnClickListener(view -> {
            onQuantityChange.apply(position);
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productNameView;
        TextView productPriceView;
        TextView productQuantityView;
        Button changeQuantityButton;

        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            productNameView = itemView.findViewById(R.id.cart_product_title);
            productPriceView = itemView.findViewById(R.id.cart_price);
            productQuantityView = itemView.findViewById(R.id.cart_item_quantity);
            changeQuantityButton = itemView.findViewById(R.id.changeQuantity);
        }
    }
}
