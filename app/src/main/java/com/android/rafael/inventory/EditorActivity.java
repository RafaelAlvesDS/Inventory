package com.android.rafael.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.rafael.inventory.data.ItemContract.ItemEntry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* This topic was used as reference for the image part
*   https://discussions.udacity.com/t/unofficial-how-to-pick-an-image-from-the-gallery/314971/4
**/

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    ;

    private static final int EXISTING_ITEM_LOADER = 0;

    private static final int PICK_IMAGE_REQUEST = 0;

    private Uri mCurrentItemUri;

    private EditText mNameEditText;

    private EditText mQuantityEditText;

    private EditText mPriceEditText;

    private EditText mEmailSupplierEditText;

    private ImageView mImageView;

    private boolean mItemHasChanged = false;

    private Uri mImageUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        final Intent[] intent = {getIntent()};
        mCurrentItemUri = intent[0].getData();

        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));

            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mEmailSupplierEditText = (EditText) findViewById(R.id.edit_supplier_email);
        mImageView = (ImageView) findViewById(R.id.image_view);

        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mEmailSupplierEditText.setOnTouchListener(mTouchListener);
        mImageView.setOnTouchListener(mTouchListener);

        Button removeQuantityButton = (Button) findViewById(R.id.button_remove_quantity);
        removeQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityField = 0;
                if (!mQuantityEditText.getText().toString().trim().equals("")) {
                    quantityField = Integer.parseInt(mQuantityEditText.getText().toString().trim());
                    ;
                }
                if (quantityField > 0) {
                    quantityField -= 1;
                }
                mQuantityEditText.setText(Integer.toString(quantityField));
            }
        });

        Button addQuantityButton = (Button) findViewById(R.id.button_add_quantity);
        addQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityField = 0;
                if (!mQuantityEditText.getText().toString().trim().equals("")) {
                    quantityField = Integer.parseInt(mQuantityEditText.getText().toString().trim());
                    ;
                }
                quantityField += 1;
                mQuantityEditText.setText(Integer.toString(quantityField));
            }
        });

        Button contactSupplier = (Button) findViewById(R.id.button_contact_supplier);
        contactSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + mEmailSupplierEditText.getText().toString().trim()));
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        "Order More");
                intent.putExtra(Intent.EXTRA_TEXT, mNameEditText.getText().toString().trim());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        Button addImage = (Button) findViewById(R.id.button_add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent[0] = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent[0].addCategory(Intent.CATEGORY_OPENABLE);
                intent[0].setType("image/*");
                startActivityForResult(Intent.createChooser(intent[0], "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                mImageUri = resultData.getData();
                mImageView.setImageBitmap(getBitmapFromUri(mImageUri));
            }
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {

        if (uri == null || uri.toString().isEmpty())
            return null;
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {

            }
        }
    }

    private int saveItem() {
        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierEmailString = mEmailSupplierEditText.getText().toString().trim();
        String imageString = "";
        if (mImageUri != null) {
            imageString = mImageUri.toString().trim();
        }

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemEntry.COLUMN_ITEM_IMAGE, imageString);
        values.put(ItemEntry.COLUMN_SUPPLIER_EMAIL, supplierEmailString);

        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);

        float price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Float.parseFloat(priceString);
        }
        values.put(ItemEntry.COLUMN_ITEM_PRICE, price);

        if (nameString.equals("")) {
            Toast.makeText(this, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            if (imageString.equals("")) {
                Toast.makeText(this, getString(R.string.image_required), Toast.LENGTH_SHORT).show();
                return 0;
            } else {
                if (supplierEmailString.equals("")) {
                    Toast.makeText(this, getString(R.string.email_required), Toast.LENGTH_SHORT).show();
                    return 0;
                } else {
                    if (mCurrentItemUri == null) {
                        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

                        if (newUri == null) {
                            Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.editor_insert_item_successful),
                                    Toast.LENGTH_SHORT).show();
                        }
                        return 1;
                    } else {
                        int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                        if (rowsAffected == 0) {
                            Toast.makeText(this, getString(R.string.editor_update_item_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.editor_update_item_successful),
                                    Toast.LENGTH_SHORT).show();
                        }
                        return 1;
                    }
                }
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                int succes = saveItem();
                if(succes==1) {
                    finish();
                }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_SUPPLIER_EMAIL,
                ItemEntry.COLUMN_ITEM_IMAGE};

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int supplierEmailColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_SUPPLIER_EMAIL);
            int imageColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_IMAGE);

            String name = cursor.getString(nameColumnIndex);
            int quanity = cursor.getInt(quantityColumnIndex);
            float price = cursor.getFloat(priceColumnIndex);
            String supplierEmail = cursor.getString(supplierEmailColumnIndex);
            String image = cursor.getString(imageColumnIndex);

            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quanity));
            mPriceEditText.setText(Float.toString(price));
            mEmailSupplierEditText.setText(supplierEmail);
            mImageView.setImageBitmap(getBitmapFromUri(Uri.parse(image)));
            mImageUri = Uri.parse(image);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
        mEmailSupplierEditText.setText("");
        mImageView.setImageResource(0);
        mImageUri = null;
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
