package com.example.notestore.ProductPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notestore.CartView.CartView;
import com.example.notestore.R;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.function.Function;

public class BottomSheet extends BottomSheetDialogFragment {


    public static String TAG = "BottomSheet";
    BottomAppBar bottomAppBar;
    Function<Void,Void> callback;

    private int productId;
    public BottomSheet(int id, BottomAppBar bottomAppBar, Function<Void, Void> callback) {
        this.productId = id;
        this.callback = callback;
    }

    StorageManager storageManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         storageManager = new StorageManager(new DBHelper(getContext()), getContext());

        return inflater.inflate(R.layout.product_page_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Chip chip1 = view.findViewById(R.id.chip1);
        chip1.setOnClickListener(v -> {
            this.addToCart(1);
        });
        Chip chip2 = view.findViewById(R.id.chip2);
        chip2.setOnClickListener(v -> {
            this.addToCart(2);
        });
        Chip chip3 = view.findViewById(R.id.chip3);
        chip3.setOnClickListener(v -> {
            this.addToCart(3);
        });

        super.onViewCreated(view, savedInstanceState);
    }


    private void addToCart(int id){
       storageManager.addToCart(productId, 1);
       switch (id) {
              case 2:
                storageManager.addToCart(4, 1);
                break;
              case 3:
                storageManager.addToCart(4, 2);
                break;
       }
         callback.apply(null);
       dismiss();

    }
}
