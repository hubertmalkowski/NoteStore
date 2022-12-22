package com.example.notestore.CartView;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notestore.Account.AccountWrapper;
import com.example.notestore.Checkout.Checkout;
import com.example.notestore.Checkout.CheckoutBottomSheet;
import com.example.notestore.R;
import com.example.notestore.Storage.Cart.CartItem;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;
import com.example.notestore.Storage.User.UserManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.MaterialFadeThrough;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartView extends Fragment {
    public static final String TAG = "CartView";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CartView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartView.
     */
    // TODO: Rename and change types and number of parameters
    public static CartView newInstance(String param1, String param2) {
        CartView fragment = new CartView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    AppBarLayout appBar;
    ArrayList<CartItem> cartItems;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ExtendedFloatingActionButton checkoutButton;
    FloatingActionButton clearCart;
    StorageManager storageManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




         storageManager = new StorageManager(new DBHelper(getContext()), getContext());
        cartItems = storageManager.getCart();

    }

    private double calculateTotal(){
        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getQuantity() * cartItem.getProduct().getPrice();
        }
        return total;
    }

    TextView noItems;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noItems = view.findViewById(R.id.noItems);



        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.cart_recycler_view);


        checkoutButton = view.findViewById(R.id.checkoutPrice);
        String price = String.format("$%f", calculateTotal());
        checkoutButton.setText(getResources().getText(R.string.checkout) + " " + price.substring(0,price.indexOf(".")));


        this.setAllowEnterTransitionOverlap(true);
        this.setEnterTransition(new MaterialFadeThrough());

        recyclerView.setLayoutManager(linearLayoutManager);
        setADapterOfRecyclerView();

        clearCart = view.findViewById(R.id.clearCart);



        updateView();

        checkoutButton.setOnClickListener(v -> {
            UserManager userManager = new UserManager(requireContext());
            if  (userManager.getCurrentUserId() != -1) {
                CheckoutBottomSheet checkout = new CheckoutBottomSheet(l -> {

                    clearCartItems();
                    return null;

                    }, calculateTotal());
                checkout.show(getChildFragmentManager(), Checkout.TAG);
            } else {
                //Change fragment to AccountWrapper
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out
                        )
                        .replace(R.id.fragment_container_view, AccountWrapper.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(AccountWrapper.TAG)
                        .commit();
            }
        });

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getResources().getString(R.string.cartClear))
                        .setMessage(getResources().getString(R.string.cartClearMessage))
                        .setPositiveButton(getResources().getText(R.string.cartClearConfirm), (dialog, which) -> {
                            clearCartItems();
                        })
                        .setNegativeButton(getResources().getText(R.string.cartClearCancel), (dialog, which) -> {

                        })
                        .show();

            }
        });
    }






    //refreshes cart
    private void refreshCart(){
        StorageManager storageManager = new StorageManager(new DBHelper(getContext()), getContext());
        cartItems = storageManager.getCart();
        setADapterOfRecyclerView();
        updateView();
    }
    private void setADapterOfRecyclerView() {

        recyclerView.setAdapter(new CartItemAdapter(cartItems, getContext(), v -> {
            //get the quantity of the cart item with the given id
            int quantity = cartItems.get(v).getQuantity();

            QuantityBottomSheet quantityBottomSheet = new QuantityBottomSheet(quantity, i -> {
                storageManager.updateQuantity(cartItems.get(v).getId(), i);
                refreshCart();
                return null;
            });
            quantityBottomSheet.show(getChildFragmentManager(), QuantityBottomSheet.TAG);
            //Create test toast
            return null;
        }));
    }



    private void clearCartItems() {
        StorageManager storageManager = new StorageManager(new DBHelper(getContext()), getContext());
        storageManager.clearCart();
        cartItems.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        updateView();
    }
    private void updateView() {
        if (cartItems.size() == 0) {
            noItems.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
            clearCart.setVisibility(View.GONE);
        } else {
            noItems.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
            clearCart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_cart_view, container, false);

    }


}