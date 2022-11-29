package com.example.notestore.ProductsView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notestore.MainActivity;
import com.example.notestore.R;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.Product;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.transition.MaterialFadeThrough;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsLayout extends Fragment {
    private final String LOG_TAG = "Products_Fragment";

    public ProductsLayout() {
    }

    public static ProductsLayout newInstance(String param1, String param2) {
        return new ProductsLayout();
    }


    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    ArrayList<Product> products;
    StorageManager storageManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setEnterTransition(new MaterialFadeThrough());

        storageManager = new StorageManager(new DBHelper(getContext()));
        products = storageManager.getProducts();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_products_layout, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = requireView().findViewById(R.id.products_recycler_view);
        ProductListItemAdapter productListItemAdapter = new ProductListItemAdapter(products, getContext());
        Log.i(LOG_TAG, products.toString());
        recyclerView.setAdapter(productListItemAdapter);


        recyclerView.setLayoutManager(gridLayoutManager);

    }
}