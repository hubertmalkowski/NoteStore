package com.example.notestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentViewHolder;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.example.notestore.ProductsView.ProductsLayout;
import com.example.notestore.Fragments.Search;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    StorageManager storageManager;
    BottomNavigationView bottomNavigationView;
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

        appBarLayout = findViewById(R.id.appBarLayout);

        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                fragmentContainerView.setPadding(0, appBarLayout.getHeight(), 0, 0);

            }
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_products:
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, ProductsLayout.class, null)
                            .commit();
                    return true;
                case R.id.navigation_search:
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container_view, Search.class, null)
                            .commit();
                    return true;
            }
            return false;
        });


    }



}