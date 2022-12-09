package com.example.notestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentViewHolder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.notestore.CartView.CartView;
import com.example.notestore.ProductPage.ProductView;
import com.example.notestore.ProductsView.ProductsLayout;
import com.example.notestore.Fragments.Search;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    StorageManager storageManager;
    public BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentContainerView fragmentContainerView;

    private static final String LOG_TAG = "MainActivity";

    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageManager = new StorageManager(new DBHelper(getApplicationContext()));

        fragmentContainerView = findViewById(R.id.fragment_container_view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ProductsLayout.class, null)
                    .commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container_view, ProductView.class, null).commit();
        appBarLayout = findViewById(R.id.appBarLayout);

        addPadding();



        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_products:
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                            )
                            .replace(R.id.fragment_container_view, ProductsLayout.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
                case R.id.navigation_search:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, Search.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
                case R.id.navigation_cart:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, CartView.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                    return true;
            }
            return false;
        });


    }
    public void addPadding() {
        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                fragmentContainerView.setPadding(0, appBarLayout.getHeight(), 0, 0);
            }
        });
    }

    void addProducts() {
        storageManager.addProduct("Product 1", 100.0, "Damn", null);
        storageManager.addProduct("Product 2", 200.0, "Damn", null);
        storageManager.addProduct("Product 3", 300.0, "Damn", null);
    }

}