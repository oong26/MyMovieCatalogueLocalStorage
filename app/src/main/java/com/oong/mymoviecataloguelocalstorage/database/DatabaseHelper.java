package com.oong.mymoviecataloguelocalstorage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "catalouge";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "favorite";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String POPULARITY = "popularity";
    private static final String POSTER = "poster";
    private static final String DESC = "desc";
    private static final String JENIS = "jenis";

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s " +
            "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL DEFAULT '-'," +
            " %s TEXT NOT NULL DEFAULT '-'," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s INTEGER NOT NULL);",
            TABLE_NAME,
            _ID,
            TITLE,
            RELEASE_DATE,
            POPULARITY,
            POSTER,
            DESC,
            JENIS);

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
