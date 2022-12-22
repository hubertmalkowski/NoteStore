package com.example.notestore.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;

import com.example.notestore.Storage.Cart.CartContract;
import com.example.notestore.Storage.Cart.CartItem;
import com.example.notestore.Storage.Products.Product;
import com.example.notestore.Storage.Products.ProductsContract;
import com.example.notestore.Storage.User.User;
import com.example.notestore.Storage.User.UserContract;
import com.example.notestore.Storage.User.UserManager;

import java.util.ArrayList;
import java.util.Base64;

public class StorageManager {

    final String LOG_TAG = "STORAGE MANAGER";

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public StorageManager(DBHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
        this.context = context;
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

    //Get user by name
    public User getUser(String name) {
        String[] projection = {
                BaseColumns._ID,
                UserContract.UserEntry.COLUMN_NAME_NAME,
                UserContract.UserEntry.COLUMN_NAME_PASSWORD,
                UserContract.UserEntry.COLUMN_NAME_EMAIL,
        };
        Cursor cursor =  db.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                UserContract.UserEntry.COLUMN_NAME_NAME + " = ?",
                new String[]{name+""},
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry._ID));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_EMAIL));
            return new User(id, name, password, email);
        }
        return  null;
    }
    //Get user by id
    public User getUser(int id) {
        String[] projection = {
                BaseColumns._ID,
                UserContract.UserEntry.COLUMN_NAME_NAME,
                UserContract.UserEntry.COLUMN_NAME_PASSWORD,
                UserContract.UserEntry.COLUMN_NAME_EMAIL,
        };
        Cursor cursor =  db.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                UserContract.UserEntry._ID + " = ?",
                new String[]{id+""},
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null
        );

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_NAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_EMAIL));
            return new User(id, name,  email, password);
        }
        return  null;
    }

    //get user by name and password
    public User getUser(String name, String password) {
        String[] projection = {
                BaseColumns._ID,
                UserContract.UserEntry.COLUMN_NAME_NAME,
                UserContract.UserEntry.COLUMN_NAME_PASSWORD,
                UserContract.UserEntry.COLUMN_NAME_EMAIL,
        };
        Cursor cursor =  db.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                UserContract.UserEntry.COLUMN_NAME_NAME + " = ? AND " + UserContract.UserEntry.COLUMN_NAME_PASSWORD + " = ?",
                new String[]{name+"", password+""},
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry._ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_EMAIL));
            return new User(id, name, password, email);
        }
        return  null;
    }

    //Create new user
    public long addUser(String name, String password, String email) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserContract.UserEntry.COLUMN_NAME_NAME, name);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, password);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_EMAIL, email);
        return this.db.insert(UserContract.UserEntry.TABLE_NAME, null, contentValues);
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
            //Convert base64 to bitmap

//            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE));
//            //Convert image to bitmap
//            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
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
        UserManager userManager = new UserManager(this.context);
        ContentValues contentValues = new ContentValues();
        int currentUser = userManager.getCurrentUserId();
        contentValues.put(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID, id);
        //create cursor that will get item from cart that matches current user and product id
        Cursor cursor = db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ? AND " + CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?",
                new String[]{currentUser+"", id+""},
                null,                   // don't group the rows
                null,                   // don't filter by row groups
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
            contentValues.put(CartContract.CartEntry.COLUMN_NAME_USER_ID, currentUser);
            db.insert(CartContract.CartEntry.TABLE_NAME, null, contentValues);
        }
    }




    public void removeFromCart(int id) {
        UserManager userManager = new UserManager(this.context);
        int currentUser = userManager.getCurrentUserId();
        db.delete(
                CartContract.CartEntry.TABLE_NAME,
                CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?" + " AND " + CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ?",
                new String[]{id+"", currentUser+""}
        );
    }

    public void clearCart() {
        UserManager userManager = new UserManager(this.context);
        int currentUser = userManager.getCurrentUserId();
        db.delete(
                CartContract.CartEntry.TABLE_NAME,
                CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ?",
                new String[]{currentUser+""}
        );
    }
    //Create function that updates quantity of item in cart by product id and delete item if quantity is 0
    public void updateQuantity(int id, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, quantity);
        UserManager userManager = new UserManager(this.context);
        int currentUser = userManager.getCurrentUserId();
        if (quantity == 0) {
            db.delete(
                    CartContract.CartEntry.TABLE_NAME,
                    CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?" + " AND " + CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ?",
                    new String[]{id+"", currentUser+""}
            );
        } else {
            db.update(
                    CartContract.CartEntry.TABLE_NAME,
                    contentValues,
                    CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID + " = ?" + " AND " + CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ?",
                    new String[]{id+"", currentUser+""}
            );
        }
    }


    //get number of items in cart
    public int getCartCount() {
        UserManager userManager = new UserManager(this.context);
        int currentUser = userManager.getCurrentUserId();
        Cursor cursor = db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ?",
                new String[]{currentUser+""},
                null,
                null,
                null
        );
        return cursor.getCount();
    }

    //get cart
    public ArrayList<CartItem> getCart() {
        UserManager userManager = new UserManager(this.context);
        int currentUser = userManager.getCurrentUserId();
        Cursor cursor = db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                CartContract.CartEntry.COLUMN_NAME_USER_ID + " = ?",
                new String[]{currentUser+""},
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








}
