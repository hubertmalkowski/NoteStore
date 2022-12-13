package com.example.notestore.CartView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notestore.R;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.Slider;

import java.util.function.Function;

public class QuantityBottomSheet extends BottomSheetDialogFragment {

    public static String TAG = "QuantityBottomSheet";
    private int startQuantity;
    Function<Integer, Void> callback;


    public QuantityBottomSheet() {}

    public QuantityBottomSheet(int startQuantity, Function<Integer, Void> callback) {
        this.startQuantity = startQuantity;
        this.callback = callback;
    }

    Slider slider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_cart_quantity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slider = view.findViewById(R.id.slideQuantity);
        slider.setValue(startQuantity);

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                callback.apply((int) slider.getValue());
                dismiss();
            }
        });
    }
}
