package com.android.rafael.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.rafael.inventory.data.ItemContract.ItemEntry;

/**
 * Created by Rafael on 8/7/2017.
 */

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
        final int rowId = cursor.getInt(cursor.getColumnIndex(ItemEntry._ID));

        String itemName = cursor.getString(nameColumnIndex);
        final String itemQuantity = cursor.getString(quantityColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);

        final int quantity = Integer.parseInt(itemQuantity);
        Button salesButton = (Button) view.findViewById(R.id.button_sale);
        salesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityTemp = quantity;
                if(quantityTemp > 0){
                    quantityTemp -= 1;
                }
                ContentValues values = new ContentValues();

                values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityTemp);
                Uri updateUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI,rowId);
                context.getApplicationContext().getContentResolver().update(updateUri, values, null, null);
            }
        });

        nameTextView.setText(itemName);
        quantityTextView.setText(context.getString(R.string.hint_item_quantity)+": "+itemQuantity);
        priceTextView.setText(context.getString(R.string.money)+itemPrice);
    }
}
