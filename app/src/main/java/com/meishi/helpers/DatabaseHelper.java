package com.meishi.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String table_name = "collect_shopid";
    public DatabaseHelper(Context context){
        super(context,"DB_TEMP",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table if not exists "+table_name+"("+
                "id integer primary key)";

        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新数据库
    }

    //插入多个
    public void insertDatas(List<Integer> datas){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        for (int i = 0;i<datas.size();i++) {
            Log.d("DatabaseHelper", "insertDatas: ==="+datas.get(i));
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", datas.get(i));
            sqLiteDatabase.insert(table_name,null,contentValues);
        }
    }

    //插入单个个
    public void insertData(int data){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",data);
        database.insert(table_name,null,contentValues);
    }

    //删除单个
    public void deleteData(int data){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(table_name,"id = ?",new String[]{data+""});

    }

    //删除全部
    public void deleteDatas(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(table_name,null,null);
    }

    //查询数据
    public boolean queryData(int data){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(table_name,null,"id = ?",new String[]{data+""},null,null,null);
        if (cursor != null){
            cursor.moveToLast();
            if(cursor.getCount() == 1){
                return false;
            }
        }

        return true;
    }


}
