package com.example.notestore.ProductPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.notestore.MainActivity;
import com.example.notestore.ProductsView.ProductsLayout;
import com.example.notestore.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.transition.MaterialContainerTransform;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductView#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProductView extends Fragment {

    final String LOG_TAG = "ProductView";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductView.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductView newInstance(String param1, String param2) {
        ProductView fragment = new ProductView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductView() {
        // Required empty public constructor
    }
    AppBarLayout appBar;
    AppBarLayout productAppBar;
    MainActivity parentLayout;
    MaterialToolbar topToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        parentLayout = (MainActivity) getActivity();
        appBar = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        Log.i(LOG_TAG,appBar.toString());
        appBar.setVisibility(View.GONE);
        parentLayout.bottomNavigationView.setVisibility(View.GONE);
        
        setEnterTransition(new MaterialContainerTransform());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        productAppBar = view.findViewById(R.id.productAppBarLayout);
        topToolbar = view.findViewById(R.id.topAppBarProduct);
        topToolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24);
//        parentLayout.setSupportActionBar(topToolbar);
        productAppBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                productAppBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                view.findViewById(R.id.product_layout).setPadding(0, productAppBar.getHeight(), 0, 0);
            }
        });

//        parentLayout.setSupportActionBar(topToolbar);

        topToolbar.setNavigationOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Dupa", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, ProductsLayout.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.setVisibility(View.VISIBLE);
        parentLayout.bottomNavigationView.setVisibility(View.VISIBLE);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.addPadding();
    }
}