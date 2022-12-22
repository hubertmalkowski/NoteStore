package com.example.notestore.Account.AccountDialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notestore.Account.AccountWrapper;
import com.example.notestore.DialogTemplate;
import com.example.notestore.R;
import com.example.notestore.Storage.User.UserManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterDialog extends DialogTemplate {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_user_register, container, false);
    }

    TextInputEditText name;
    TextInputEditText phoneNumber;
    TextInputEditText password;
    TextInputEditText confirmPassword;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.username);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.repeat_password);

        materialToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.top_bar_register_action:
                    String name = Objects.requireNonNull(this.name.getText()).toString();
                    String phoneNumber = Objects.requireNonNull(this.phoneNumber.getText()).toString();
                    String password = Objects.requireNonNull(this.password.getText()).toString();
                    String confirmPassword = Objects.requireNonNull(this.confirmPassword.getText()).toString();

                    if (password.equals(confirmPassword)) {
                        UserManager userManager = new UserManager(requireContext());

                        try {

                            userManager.registerUser(name,  password, phoneNumber);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            Toast.makeText(requireContext(), "User registered", Toast.LENGTH_SHORT).show();
                            mainActivity.onBackPressed();
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, AccountWrapper.class, null).commit();
                            dismiss();
                            userManager.authenticateUser(name, password);
                        }



                    }
                    return true;
                default:
                    return false;
            }
        });
    }
}