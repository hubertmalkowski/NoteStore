package com.example.notestore.Checkout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.notestore.MainActivity;
import com.example.notestore.R;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;
import com.example.notestore.Storage.User.User;
import com.example.notestore.Storage.User.UserManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.function.Function;

public class CheckoutBottomSheet extends BottomSheetDialogFragment {
    Function<Void, Void> callback;
    Double total;
    UserManager userManager;
    StorageManager storageManager;

    public CheckoutBottomSheet(Function<Void, Void> callback, Double total) {
        this.callback = callback;
        this.total = total;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userManager = new UserManager(getContext());
        storageManager = new StorageManager(new DBHelper(getContext()), getContext());
        return inflater.inflate(R.layout.bottom_sheet_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button checkoutButton = view.findViewById(R.id.checkoutConfirm);

        User user = userManager.getCurrentUser();
        checkoutButton.setOnClickListener(v -> {

            sendSmsWithSmsManager(user.getPhoneNumber(), "Thank you for your purchase! Your total is " + total);
            dismiss();

            callback.apply(null);
        });


        TextView name = view.findViewById(R.id.name_number_checkout);
        name.setText(user.getName());

        TextView phoneNumber = view.findViewById(R.id.phone_number_checkout);
        phoneNumber.setText(user.getPhoneNumber());

        TextView total = view.findViewById(R.id.price_checkout);
        total.setText(String.format("$%.2f", this.total));


    }

    private Boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(getContext(), permission)!= PackageManager.PERMISSION_GRANTED) {
            ((MainActivity)getContext()).requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        return true;

    }

    private void sendSmsWithSmsManager(String phoneNumber, String message) {
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            String destinationAddress = phoneNumber;
            String text = message;
            if (!destinationAddress.equals("") && !text.equals("")) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(
                        destinationAddress,
                        null,
                        text,
                        null,
                        null
                );
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(requireContext(), "Denied something", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
