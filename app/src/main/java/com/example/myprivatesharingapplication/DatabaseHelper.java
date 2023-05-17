package com.example.myprivatesharingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {

        super(context, "truck_sharing_db", null, 21);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,FULLNAME TEXT, USERNAME TEXT, PASSWORD TEXT,PHONENUMBER TEXT)";
        String CREATE_ORDER_TABLE = "CREATE TABLE ORDERS(ORDERID INTEGER PRIMARY KEY AUTOINCREMENT ,SENDERNAME TEXT,RECEIVERNAME TEXT, PICKUPTIME TEXT,DROPOFFTIME TEXT,WEIGHT TEXT,HEIGHT TEXT,WIDTH TEXT,LENGHT TEXT,QUANTITY TEXT,TYPE TEXT, PICKUPLOCATION TEXT,  PICKUPLATITUDE REAL, PICKUPLONGITUDE REAL, DROPOFFLOCATION TEXT, DROPOFFLATITUDE REAL, DROPOFFLONGITUDE REAL )";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String DROP_USER_TABLE = "DROP TABLE IF EXISTS USERS";
        String DROP_ORDER_TABLE = "DROP TABLE IF EXISTS ORDERS";
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        sqLiteDatabase.execSQL(DROP_ORDER_TABLE);

        onCreate(sqLiteDatabase);
    }
    public long insertUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("FULLNAME",user.getFullname());
        contentValues.put("USERNAME",user.getUsername());
        contentValues.put("PASSWORD",user.getPassword());
        contentValues.put("PHONENUMBER",user.getPhoneNumber());

        long newRow = db.insert("USERS",null,contentValues);
        db.close();
        return newRow;
    }

    public boolean fetchUser(String username, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", new String[]{"userid"}, "USERNAME=? and PASSWORD=?",new String[]{username, password}, null, null, null);
        int rowsCount = cursor.getCount();
        db.close();

        if (rowsCount > 0)
            return  true;
        else
            return false;
    }

    public long insertOrder(Order order)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("SENDERNAME",order.getSenderName());
        contentValues.put("RECEIVERNAME",order.getReceiverName());
        contentValues.put("PICKUPTIME",order.getPickupTime());
        contentValues.put("DROPOFFTIME",order.getDropoffTime());
        contentValues.put("WEIGHT",order.getWeight());
        contentValues.put("HEIGHT",order.getHeight());
        contentValues.put("WIDTH",order.getWidth());
        contentValues.put("LENGHT",order.getLength());
        contentValues.put("QUANTITY",order.getQuantity());
        contentValues.put("TYPE",order.getType());
        contentValues.put("PICKUPLOCATION",order.getPickupLocation());
        contentValues.put("PICKUPLATITUDE",order.getPickupLatitude());
        contentValues.put("PICKUPLONGITUDE",order.getPickupLongitude());
        contentValues.put("DROPOFFLOCATION",order.getDropoffLocation());
        contentValues.put("DROPOFFLATITUDE",order.getDropoffLatitude());
        contentValues.put("DROPOFFLONGITUDE",order.getDropoffLongitude());
        long newRow = db.insert("ORDERS",null,contentValues);
        db.close();
        return newRow;
    }
    public List<Order> fetchAllOrders (String username){
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = " SELECT * FROM ORDERS WHERE SENDERNAME = '"+username.trim()+"'" ;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                String sendername = cursor.getString(1);
                String receivername = cursor.getString(2);
                String pickuptime = cursor.getString(3);
                String dropofftime = cursor.getString(4);
                String weight = cursor.getString(5);
                String height = cursor.getString(6);
                String width = cursor.getString(7);
                String length = cursor.getString(8);
                String quantity = cursor.getString(9);
                String type = cursor.getString(10);
                String pickupLocation = cursor.getString(11);
                double pickupLatitude = cursor.getDouble(12);
                double pickupLongitude = cursor.getDouble(13);
                String dropoffLocation = cursor.getString(14);
                double dropoffLatitude = cursor.getDouble(15);
                double dropoffLongitude = cursor.getDouble(16);

                Order order = new Order(sendername,receivername,pickuptime,dropofftime,weight,height,width,length,quantity,type,pickupLocation,pickupLatitude,pickupLongitude,dropoffLocation,dropoffLatitude,dropoffLongitude);
                orderList.add(order);

            } while (cursor.moveToNext());
        }
        return orderList;
    }

    public List<Order> fetchAllOrdersforAll(){
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM ORDERS";
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                String sendername = cursor.getString(1);
                String receivername = cursor.getString(2);
                String pickuptime = cursor.getString(3);
                String dropofftime = cursor.getString(4);
                String weight = cursor.getString(5);
                String height = cursor.getString(6);
                String width = cursor.getString(7);
                String length = cursor.getString(8);
                String quantity = cursor.getString(9);
                String type = cursor.getString(10);
                String pickupLocation = cursor.getString(11);
                double pickupLatitude = cursor.getDouble(12);
                double pickupLongitude = cursor.getDouble(13);
                String dropoffLocation = cursor.getString(14);
                double dropoffLatitude = cursor.getDouble(15);
                double dropoffLongitude = cursor.getDouble(16);

                Order order = new Order(sendername,receivername,pickuptime,dropofftime,weight,height,width,length,quantity,type,pickupLocation,pickupLatitude,pickupLongitude,dropoffLocation,dropoffLatitude,dropoffLongitude);
                orderList.add(order);

            } while (cursor.moveToNext());
        }
        return orderList;
    }
}
