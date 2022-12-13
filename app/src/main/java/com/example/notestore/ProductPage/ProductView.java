package com.example.notestore.ProductPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.notestore.CartView.CartView;
import com.example.notestore.MainActivity;
import com.example.notestore.R;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.Products.Product;
import com.example.notestore.Storage.StorageManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialContainerTransform;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductView#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProductView extends Fragment {

    public static final String TAG = "ProductView";

    final String LOG_TAG = "ProductView";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int productIdParam;

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
    FragmentContainerView fragmentContainerView;
    Product product;
    StorageManager storageManager;

    int padding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productIdParam = getArguments().getInt(ARG_PARAM1);
        }
        storageManager = new StorageManager(new DBHelper(getContext()));
        product = storageManager.getProduct(productIdParam);
        parentLayout = (MainActivity) getActivity();
        appBar = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        Log.i(LOG_TAG,appBar.toString());
        appBar.setVisibility(View.GONE);
        parentLayout.bottomNavigationView.setVisibility(View.GONE);
        fragmentContainerView = parentLayout.findViewById(R.id.fragment_container_view);


        padding = fragmentContainerView.getPaddingTop();
        fragmentContainerView.setPadding(0,0,0,0);




        setEnterTransition(new MaterialContainerTransform());
        setSharedElementEnterTransition(new MaterialContainerTransform());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_view, container, false);
    }


    TextView productName;
    TextView productPrice;
    TextView productDescription;
    FloatingActionButton addToCartButtom;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        productAppBar = view.findViewById(R.id.productAppBarLayout);
        topToolbar = view.findViewById(R.id.topAppBarProduct);
        topToolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24);

        productName = view.findViewById(R.id.product_name);
        productName.setText(product.getName());

        productPrice = view.findViewById(R.id.product_price);
        String price = String.format("$%f", product.getPrice());
        productPrice.setText(price.substring(0,price.indexOf(".")));
        productDescription = view.findViewById(R.id.product_description);
        productDescription.setText(product.getDescription());

//        parentLayout.setSupportActionBar(topToolbar);
        productAppBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                productAppBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                view.findViewById(R.id.product_layout).setPadding(0, productAppBar.getHeight(), 0, 0);
            }
        });

        addToCartButtom = view.findViewById(R.id.add_to_cart_button);

        addToCartButtom.setOnClickListener(view12 -> {
//            storageManager.addToCart(product.getId());
//            Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            BottomSheet bottomSheet = new BottomSheet(product.getId(), view.findViewById(R.id.bottomAppBar), v -> {

                Snackbar.make(requireView(), "Added to cart", Snackbar.LENGTH_SHORT)
                        .setAnchorView(view.findViewById(R.id.bottomAppBar))
                        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                        .setAction("GO TO CART", u -> {
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, CartView.class, null).addToBackStack(CartView.TAG).commit();
                        })
                        .show();
                return null;
            });


            bottomSheet.show(getParentFragmentManager(), BottomSheet.TAG);

        });





//        parentLayout.setSupportActionBar(topToolbar);

        topToolbar.setNavigationOnClickListener(view1 -> {
           //Get back to previous fragment
            getParentFragmentManager().popBackStack();

        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.setVisibility(View.VISIBLE);
//        fragmentContainerView.setPadding(0, );
        parentLayout.bottomNavigationView.setVisibility(View.VISIBLE);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.addPadding();
    }
}