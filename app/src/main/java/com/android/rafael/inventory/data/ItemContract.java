package com.android.rafael.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rafael on 8/7/2017.
 */

public class ItemContract {

    private ItemContract(){}

    public static final String CONTENT_AUTHORITY = "com.android.rafael.inventory";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";

    public static final class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public final static String TABLE_NAME = "items";

        public final static String _ID = BaseColumns._ID;

        //Item name, type text
        public final static String COLUMN_ITEM_NAME ="name";

        //Item price, type real
        public final static String COLUMN_ITEM_PRICE ="price";

        //Item quantity, type integer
        public final static String COLUMN_ITEM_QUANTITY ="quantity";

        //Item supplier_email, type text
        public final static String COLUMN_SUPPLIER_EMAIL ="supplier_email";

        //Item image, type text
        public final static String COLUMN_ITEM_IMAGE ="image";
    }
}
