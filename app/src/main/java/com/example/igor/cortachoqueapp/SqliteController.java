package com.example.igor.cortachoqueapp;

import java.util.ArrayList;

import java.util.HashMap;

import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Switchs ( SwitchId INTEGER PRIMARY KEY, SwitchName TEXT, SwitchAddress TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT, "Switchs Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Switchs";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertSwitch(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SwitchName", queryValues.get("SwitchName"));
        values.put("SwitchAddress", queryValues.get("SwitchAddress"));
        database.insert("Switchs", null, values);
        database.close();
    }

    public int updateSwitch(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SwitchName", queryValues.get("SwitchName"));
        values.put("SwitchAddress", queryValues.get("SwitchAddress"));
        return database.update("Switchs", values, "SwitchId" + " = ?", new String[]{queryValues.get("SwitchId")});
        //String updateQuery = "Update  words set txtWord='"+word+"' where txtWord='"+ oldWord +"'";
        //Log.d(LOGCAT,updateQuery);
        //database.rawQuery(updateQuery, null);
        //return database.update("words", values, "txtWord  = ?", new String[] { word });
    }

    public void deleteSwitch(String id) {
        Log.d(LOGCAT, "delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM  Switchs where SwitchName='" + id + "'";
        Log.d("query", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public ArrayList<HashMap<String, String>> getAllSwitchs() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Switchs";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("SwitchId", cursor.getString(0));
                map.put("SwitchName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;
    }

    public HashMap<String, String> getSwitchInfo(String id) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Switchs where SwitchId='" + id + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                wordList.put("SwitchName", cursor.getString(1));
                wordList.put("SwitchAddress", cursor.getString(2));
                //wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }
}
