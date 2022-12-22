package com.example.notestore.Account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notestore.Account.AccountDialogs.LogInDialog;
import com.example.notestore.Account.AccountDialogs.RegisterDialog;
import com.example.notestore.R;
import com.example.notestore.Storage.User.UserManager;
import com.google.android.material.transition.MaterialFadeThrough;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountWrapper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountWrapper extends Fragment {
    public static final String TAG = "AccountWrapper";


    public AccountWrapper() {
        // Required empty public constructor
    }


    public static AccountWrapper newInstance(String param1, String param2) {
        return new AccountWrapper();
    }

    Button logInButton;
    Button signUpButton;

    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEnterTransition(new MaterialFadeThrough());
        fragmentManager = getParentFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_wrapper, container, false);
    }

    LinearLayout userNotLoggedIn;
    LinearLayout userLoggedIn;
    TextView userNameTextView;
    Button logOutButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logInButton = view.findViewById(R.id.logInButton);
        UserManager userManager = new UserManager(getContext());

        userNotLoggedIn = view.findViewById(R.id.userNotLoggedIn);
        userLoggedIn = view.findViewById(R.id.account_wrapper);
        userNameTextView = view.findViewById(R.id.username_view);
        logOutButton = view.findViewById(R.id.log_out_button);

        if (userManager.getCurrentUserId() != -1) {
            userNotLoggedIn.setVisibility(View.GONE);
            userNameTextView.setText(userManager.getCurrentUser().getName());
            logOutButton.setOnClickListener(v -> {
                userManager.logoutUser();
                userLoggedIn.setVisibility(View.GONE);
                userNotLoggedIn.setVisibility(View.VISIBLE);
            });
        }
        else {
            userLoggedIn.setVisibility(View.GONE);
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInDialog dialogTemplate = new LogInDialog();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(R.id.full_screen_dialog_container, dialogTemplate)
                        .addToBackStack(null)
                        .commit();


            }
        });

        signUpButton = view.findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterDialog dialogTemplate = new RegisterDialog();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(R.id.full_screen_dialog_container, dialogTemplate)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }



}