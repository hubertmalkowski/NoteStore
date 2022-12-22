package com.example.notestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentViewHolder;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.notestore.Account.AccountWrapper;
import com.example.notestore.CartView.CartView;
import com.example.notestore.ProductPage.ProductView;
import com.example.notestore.ProductsView.ProductsLayout;
import com.example.notestore.Fragments.Search;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.Products.Product;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    StorageManager storageManager;
    public BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentContainerView fragmentContainerView;
    public FragmentContainerView dialogContainerView;

    private static final String LOG_TAG = "MainActivity";

    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogContainerView = findViewById(R.id.full_screen_dialog_container);
        dialogContainerView.setVisibility(View.GONE);


        storageManager = new StorageManager(new DBHelper(getApplicationContext()), getApplicationContext());


        fragmentContainerView = findViewById(R.id.fragment_container_view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ProductsLayout.class, null)
                    .commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container_view, ProductView.class, null).commit();
        appBarLayout = findViewById(R.id.appBarLayout);

        addPadding();

        if (Objects.equals(storageManager.getProducts(), new ArrayList<Product>())) {
            Log.d(LOG_TAG, "No products found");
            addProducts();
        }
//        addProducts();
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                )
                .replace(R.id.fragment_container_view, ProductsLayout.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(ProductsLayout.TAG)
                .commit();




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
                            .addToBackStack(ProductsLayout.TAG)
                            .commit();
                    return true;
                case R.id.navigation_search:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, Search.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(Search.TAG)
                            .commit();
                    return true;
                case R.id.navigation_cart:
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                            )
                            .replace(R.id.fragment_container_view, CartView.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(CartView.TAG)
                            .commit();
                    return true;
                case R.id.navigation_account:
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                            )
                            .replace(R.id.fragment_container_view, AccountWrapper.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(AccountWrapper.TAG)
                            .commit();
                    return true;
            }
            return false;
        });
        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });





        //Make bottom bar item change after back button pressed
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                    if (tag != null &&  tag.equals(ProductsLayout.TAG) ) {
                        bottomNavigationView.setSelectedItemId(R.id.navigation_products);
                    } else if (tag != null &&  tag.equals(Search.TAG) ) {
                        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
                    } else if (tag != null &&  tag.equals(CartView.TAG)  ) {
                        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
                    }
                }
            }
        });

    }


    //Hide toolbar and bottom bar and hide margin
    public void hideToolbarAndBottomBar() {
        appBarLayout.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        fragmentContainerView.setPadding(0, 0, 0, 0);
    }
    //Reverse above method
    public void showToolbarAndBottomBar() {
        appBarLayout.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        addPadding();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialogContainerView.setVisibility(View.GONE);
        showToolbarAndBottomBar();
    }

    void addProducts() {
        storageManager.addProduct("Product 1", 100.0, "Damn", null);
        storageManager.addProduct("Product 2", 200.0, "Damn", null);
        storageManager.addProduct("Product 3", 300.0, "Damn", null);
        storageManager.addProduct("8 Cables", 16.0, "Cables", null);
    }

}