package com.example.notestore.Storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.notestore.Storage.Cart.CartContract;
import com.example.notestore.Storage.Cart.CartItem;
import com.example.notestore.Storage.Products.Product;
import com.example.notestore.Storage.Products.ProductsContract;

import java.util.ArrayList;

public class StorageManager {

    final String LOG_TAG = "STORAGE MANAGER";

    DBHelper dbHelper;
    SQLiteDatabase db;

    public StorageManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    public void close() {
        this.db.close();
    }

    public long addProduct(String name, Double price, String description, String image) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_NAME, name);
        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_PRICE, price);
        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION, description);
        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE, image);
        return this.db.insert(ProductsContract.ProductsEntry.TABLE_NAME, null, contentValues);
    }

    private Cursor getCursorProducts() {
        String[] projection = {
                BaseColumns._ID,
                ProductsContract.ProductsEntry.COLUMN_NAME_NAME,
                ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION,
                ProductsContract.ProductsEntry.COLUMN_NAME_PRICE,
                ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE,
        };
        return db.query(
                ProductsContract.ProductsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    private Cursor getCursorProduct(int id) {
        String[] projection = {
                BaseColumns._ID,
                ProductsContract.ProductsEntry.COLUMN_NAME_NAME,
                ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION,
                ProductsContract.ProductsEntry.COLUMN_NAME_PRICE,
                ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE,
        };
        return db.query(
                ProductsContract.ProductsEntry.TABLE_NAME,
                null,
                ProductsContract.ProductsEntry._ID + " = ?",
                new String[]{id+""},
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null
        );
    }

    public ArrayList<Product> getProducts() {
        Cursor cursor = getCursorProducts();
        ArrayList<Product> products = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_PRICE));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE));

            products.add(new Product( id,name, description, price, image));
//            Log.i(LOG_TAG, products.get(products.size() - 1).toString());
        }
        return products;
    }
    public Product getProduct(int id) {
        Cursor cursor = getCursorProduct(id);
        Product product = null;
        while (cursor.moveToNext()) {
            int ids = cursor.getInt(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_PRICE));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE));

            product = new Product(ids, name, description, price, image);
        }
        return product;
    }



    //cReate method that will add item to cart and update quantity if item already exists
    public void addToCart(int id, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID, id);
        Cursor cursor = db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?",
                new String[]{id+""},
                null,
                null,
                null
        );
        if (cursor.moveToNext()) {
            int quantitys = cursor.getInt(cursor.getColumnIndexOrThrow(CartContract.CartEntry.COLUMN_NAME_QUANTITY));
            contentValues.put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, quantitys+quantity);
            db.update(
                    CartContract.CartEntry.TABLE_NAME,
                    contentValues,
                    CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?",
                    new String[]{id+""}
            );
        } else {
            contentValues.put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, quantity);
            db.insert(CartContract.CartEntry.TABLE_NAME, null, contentValues);
        }
    }

    public void removeFromCart(int id) {
        db.delete(
                CartContract.CartEntry.TABLE_NAME,
                CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?",
                new String[]{id+""}
        );
    }

    public void clearCart() {
        db.delete(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null
        );
    }
    //Create function that updates quantity of item in cart by product id and delete item if quantity is 0
    public void updateQuantity(int id, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, quantity);
        if (quantity == 0) {
            db.delete(
                    CartContract.CartEntry.TABLE_NAME,
                    CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?",
                    new String[]{id+""}
            );
        } else {
            db.update(
                    CartContract.CartEntry.TABLE_NAME,
                    contentValues,
                    CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?",
                    new String[]{id+""}
            );
        }
    }


    //get number of items in cart
    public int getCartCount() {
        Cursor cursor = db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return cursor.getCount();
    }

    //get cart
    public ArrayList<CartItem> getCart() {
        Cursor cursor = db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<CartItem> cart = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(CartContract.CartEntry.COLUMN_NAME_QUANTITY));
            Product product = getProduct(id);
            cart.add(new CartItem(product, quantity, id));
        }
        return cart;
    }



    private Cursor getCursorCart() {
        return db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }





}
