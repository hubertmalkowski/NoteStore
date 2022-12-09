package com.example.notestore.ProductsView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notestore.R;
import com.example.notestore.Storage.Product;

import java.util.ArrayList;

public class ProductListItemAdapter extends RecyclerView.Adapter<ProductListItemAdapter.ItemViewHolder> {

    ArrayList<Product> products;
    Context context;
    private final String LOG_TAG = "Item_adapter" ;

    public ProductListItemAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Log.d(LOG_TAG, products.toString());

        Product item = products.get(position);
        Log.d(LOG_TAG, item.toString());
        holder.productNameView.setText(item.getName());
        holder.productPriceView.setText(String.valueOf(item.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), item.getName(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productNameView;
        TextView productPriceView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productPriceView = itemView.findViewById(R.id.product_price);
            productNameView = itemView.findViewById(R.id.product_name);
        }




    }
}
