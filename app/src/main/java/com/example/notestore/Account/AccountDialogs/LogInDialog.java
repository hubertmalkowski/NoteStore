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

public class LogInDialog extends DialogTemplate {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_user_log_in, container, false);
    }

    TextInputEditText name;
    TextInputEditText password;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);

        materialToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.top_bar_login_action:
                    String name = Objects.requireNonNull(this.name.getText()).toString();
                    String password = Objects.requireNonNull(this.password.getText()).toString();
                    UserManager userManager = new UserManager(requireContext());
                    try {
                        userManager.authenticateUser(name, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(requireContext(), "User logged in", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, AccountWrapper.class, null).commit();

                        mainActivity.onBackPressed();
                        dismiss();
                    }
                    break;
            }
            return true;
        });

    }
}
