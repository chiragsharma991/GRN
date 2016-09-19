package com.gayatry.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "Gayatry.db";

    private static String TAG = "TAG";

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            ProjectHandler countryHandler = new ProjectHandler();
            countryHandler.createCountry(sqLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}