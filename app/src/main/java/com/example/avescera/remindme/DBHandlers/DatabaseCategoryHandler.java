package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.vescera on 25/11/2016.
 */

public class DatabaseCategoryHandler {

    private static final String DATABASE_TABLE = "category";

    private static final String ID = "id";
    private static final String CATEGORY = "category";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DatabaseAdapter.DATABASE_NAME, null, DatabaseAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    /**
     * Constructor
     * @param context
     */
    public DatabaseCategoryHandler(Context context) {
        mCtx = context;
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseTaskHandler
     */
    public DatabaseCategoryHandler open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the db
     * return type: void
     */
    public void close(){
        mDbHelper.close();
    }

    public void createCategory(Category category) {
        ContentValues values = new ContentValues();

        values.put(CATEGORY, category.get_category());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Category getCategory(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, CATEGORY }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Category category = new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        return category;
    }

    public void deleteCategory(Category category, Context context) {
        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(category.get_id())});
    }

    public int getCategoriesCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        return count;
    }

    public int getCategoryNextId() {
        int nextId = getCategoriesCount() + 1;
        return nextId;
    }

    public int updateCategory(Category category) {
        ContentValues values = new ContentValues();

        values.put(CATEGORY, category.get_category());

        int rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + category.get_id(), null);

        return rowsAffected;
    }

    public List<Category> getAllCategories() {
        List<Category> categoriesList = new ArrayList<Category>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                categoriesList.add(new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }

        return categoriesList;
    }
}
