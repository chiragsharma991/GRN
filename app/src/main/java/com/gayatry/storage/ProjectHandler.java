package com.gayatry.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created  by Android Developer on 1/11/2016.
 */
public class ProjectHandler {

    private static final String TABLE_COUNTRY = "ProjectInfo";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_COUNTRY_ID = "country_id";


    public String CREATE_TABLE = " CREATE TABLE " + TABLE_COUNTRY + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " + KEY_COUNTRY_CODE + " TEXT NOT NULL, " + KEY_COUNTRY_ID + " TEXT NOT NULL, " +
            KEY_NAME + " TEXT NOT NULL); ";
    private SQLiteDatabase mDb;

    public void createCountry(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

/*    public long addCountry(CountryModel _Countrymodel) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, _Countrymodel.name);
        initialValues.put(KEY_COUNTRY_CODE, _Countrymodel.countryCode);
        initialValues.put(KEY_COUNTRY_ID, _Countrymodel.country_id);
        mDb = DatabaseManager.getInstance().openDatabase();
        return mDb.insert(TABLE_COUNTRY, null, initialValues);
    }

    public boolean updateCountry(CountryModel _Countrymodel) {
        Log.e("", "_id " + _Countrymodel.id);
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, _Countrymodel.name);

        mDb = DatabaseManager.getInstance().openDatabase();
        return mDb.update(TABLE_COUNTRY, initialValues, "id=" + _Countrymodel.id, null) > 0;
    }

    public boolean deleteCountry(String _id) {
        mDb = DatabaseManager.getInstance().openDatabase();
        return mDb.delete(TABLE_COUNTRY, "id=" + _id, null) > 0;
    }

    public ArrayList<CountryModel> getAllCountry() {

        ArrayList<CountryModel> arrayList = new ArrayList<>();

        try {
            mDb = DatabaseManager.getInstance().openDatabase();
            Cursor mCursor = mDb.query(TABLE_COUNTRY, null, null, null, null, null, KEY_ID + " DESC");

            mCursor.moveToNext();
            for (int i = 0; i < mCursor.getCount(); i++) {
                CountryModel unit_model = new CountryModel();
                unit_model.id = String.valueOf(mCursor.getInt(Integer.valueOf(mCursor.getColumnIndex(KEY_ID))));
                String upperString = mCursor.getString(mCursor.getColumnIndex(KEY_NAME)).substring(0, 1).toUpperCase() + mCursor.getString(mCursor.getColumnIndex(KEY_NAME)).substring(1);
                unit_model.name = upperString;
                unit_model.countryCode = mCursor.getString(mCursor.getColumnIndex(KEY_COUNTRY_CODE));
                unit_model.country_id = mCursor.getString(mCursor.getColumnIndex(KEY_COUNTRY_ID));
                arrayList.add(unit_model);
                mCursor.moveToNext();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public ArrayList<CountryModel> getCountryDetails(String countryName)
    {
        Cursor cursor = null;
        ArrayList<CountryModel> arrayList = new ArrayList<>();
        try {
            mDb = DatabaseManager.getInstance().openDatabase();
            String qry= "SELECT * FROM CountryInfo where name = '"+countryName+"' or country_code='"+countryName+"'";
            cursor = mDb.rawQuery(qry, null);
            while (cursor.moveToNext())
            {
                CountryModel unit_model = new CountryModel();
                unit_model.id = String.valueOf(cursor.getInt(Integer.valueOf(cursor.getColumnIndex(KEY_ID))));
                unit_model.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                unit_model.countryCode = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_CODE));
                unit_model.country_id = cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_ID));
                arrayList.add(unit_model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DatabaseUtil.closeResource(mDb, null, cursor);
        }
        return arrayList;
    }*/


}
