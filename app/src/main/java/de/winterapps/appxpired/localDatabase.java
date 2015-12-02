package de.winterapps.appxpired;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by D062332 on 16.11.2015.
 */
public class localDatabase extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "localDB.db";
    public static final String CREATE_GROCERIES_TABLE = "create table groceries"+
            "(id integer primary key autoincrement, " +
            "name text, " +
            "entryDate integer, " +
            "expireDate integer, " +
            "position_id integer, " +
            "amount integer, "+
            "additionalInformation text, " +
            "template_id integer, " +
            "category_id integer, " +
            "deleted integer, " +
            "household_id integer, " +
            "createUser_id integer)";

    public static final String CREATE_TEMPLATE_TABLE = "CREATE TABLE templates"+
            "("+
            "id integer primary key AUTOINCREMENT, " +
            "name text, " +
            "amount integer, " +
            "additionalInformation text, " +
            "deleted integer, " +
            "household_id integer, " +
            "createUser_id` integer, " +
            "expireDuration` integer, " +
            "category_id integer)";

    public static final String CREATE_POSITION_TABLE = "";
    public static final String CREATE_CATEGORY_TABLE = "";
    public static final String CREATE_MEASURING_TABLE = "";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GROCERIES_TABLE);
        db.execSQL(CREATE_TEMPLATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groceries");
        db.execSQL("DROP TABLE IF EXISTS templates");
        onCreate(db);
    }

    public localDatabase(Context context){
       super(context, DATABASE_NAME, null, 1);
    }

    public boolean addFood(JSONObject foodEntry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values = cleanseValuesAddFood(foodEntry);
        if(values.get("name") == "" ){
            return false;
        }
        long e = db.insert("groceries", null, values);
        db.close();
        if(e == -1){
            return false;
        }
        return true;

    }

    public ContentValues cleanseValuesAddFood(JSONObject foodEntry){
        ContentValues values = new ContentValues();
        long entryDate = System.currentTimeMillis();
        try {
            values.put("name", foodEntry.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("name", "");
        }
        values.put("entryDate", entryDate);
        try {
            values.put("expireDate", foodEntry.getString("expireDate"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("expireDate", 0);
        }
        try {
            values.put("position_id", foodEntry.getString("position_id"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("position_id", 0);
        }
        try {
            values.put("amount", foodEntry.getString("amount"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("amount", 0);
        }
        try {
            values.put("additionalInformation", foodEntry.getString("additionalInformation"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("additionalInformation", 0);
        }
        try {
            values.put("template_id", foodEntry.getString("template_id"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("template_id", 0);
        }
        values.put("category_id", 0);//dummy entry
        values.put("deleted", 0);
        values.put("household_id", 0);//dummy entry
        values.put("createUser_id", 0);//dummy entry

        return values;
    }

    public JSONArray getFood(){
        JSONArray foodArray = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from groceries", null );
        res.moveToFirst();
        while (res.isAfterLast() == false){
            JSONObject foodEntry = new JSONObject();
            try {
                foodEntry.put("id",res.getInt(res.getColumnIndex("id")));
                foodEntry.put("name",res.getString(res.getColumnIndex("name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            foodArray.put(foodEntry);
            res.moveToNext();
        }
        return foodArray;
    }

    public JSONObject getFood(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from groceries where id=" + id, null);
        JSONObject foodEntry = new JSONObject();
        try {
            foodEntry.put("id",res.getInt(res.getColumnIndex("id")));
            foodEntry.put("name",res.getString(res.getColumnIndex("name")));
            foodEntry.put("expireDate",res.getInt(res.getColumnIndex("expireDate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodEntry;
    }

    public boolean deleteFood(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("delete from groceries where id="+id);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean addTemplate(JSONObject templateEntry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values = cleanseValuesAddTemplate(templateEntry);
        long e = db.insert("template", null, values);
        db.close();
        if (e == -1){
            return false;
        }
        return true;
    }

    public ContentValues cleanseValuesAddTemplate(JSONObject templateEntry){
        ContentValues values = new ContentValues();
        try {
            values.put("name", templateEntry.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("name", "");
        }
        try {
            values.put("amount", templateEntry.getString("amount"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("amount", 0);
        }
        try {
            values.put("additionalInformation", templateEntry.getString("additionalInformation"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("additionalInformation", 0);
        }
        values.put("deleted", 0);
        values.put("household_id", 0);//dummy entry
        values.put("createUser_id", 0);//dummy entry
        try {
            values.put("expireDuration", templateEntry.getString("expireDuration"));
        } catch (JSONException e) {
            e.printStackTrace();
            values.put("expireDuration", 0);
        }
        values.put("category_id", 0);//dummy entry
        return values;
    }

    public JSONObject getTemplate(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from templates where id=" + id, null);
        JSONObject templateEntry = new JSONObject();
        try {
            templateEntry.put("id", res.getInt(res.getColumnIndex("id")));
            templateEntry.put("name", res.getString(res.getColumnIndex("name")));
            templateEntry.put("amount", res.getInt(res.getColumnIndex("amount")));
            templateEntry.put("additionalInformation", res.getInt(res.getColumnIndex("additionalInformation")));
            templateEntry.put("expireDuration", res.getInt(res.getColumnIndex("expireDuration")));//im backend berechnen oder frontend?
            templateEntry.put("category_id", res.getInt(res.getColumnIndex("category_id")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return templateEntry;
    }
}
