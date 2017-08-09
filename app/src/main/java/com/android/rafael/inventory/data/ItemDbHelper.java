package com.android.rafael.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.rafael.inventory.data.ItemContract.ItemEntry;

/**
 * Created by Rafael on 8/7/2017.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE =  "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_PRICE + " REAL NOT NULL, "
                + ItemEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_IMAGE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL); ";
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
