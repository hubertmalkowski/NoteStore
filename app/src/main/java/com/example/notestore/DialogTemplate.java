package com.example.notestore;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.notestore.MainActivity;
import com.example.notestore.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.shape.MaterialShapeDrawable;

public class DialogTemplate extends DialogFragment {

    protected MainActivity mainActivity;
    private static final String LOG_TAG = "LogInDialog";
    protected MaterialToolbar materialToolbar;
    protected AppBarLayout appBarLayout;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainActivity.dialogContainerView.setVisibility(View.VISIBLE);
        mainActivity.bottomNavigationView.setVisibility(View.GONE);


        materialToolbar = view.findViewById(R.id.log_in_toolbar);
        appBarLayout = view.findViewById(R.id.log_in_app_bar_layout);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        mainActivity.dialogContainerView.setVisibility(View.GONE);
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
