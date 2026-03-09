package com.vlt.lab4.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ThiSinhDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ThiSinh";
    public static final String COLUMN_SBD = "sbd";
    public static final String COLUMN_HOTEN = "hoTen";
    public static final String COLUMN_TOAN = "toan";
    public static final String COLUMN_LY = "ly";
    public static final String COLUMN_HOA = "hoa";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_SBD + " TEXT PRIMARY KEY, " +
            COLUMN_HOTEN + " TEXT, " +
            COLUMN_TOAN + " REAL, " +
            COLUMN_LY + " REAL, " +
            COLUMN_HOA + " REAL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Chạy khi app được cài đặt lần đầu và DB chưa tồn tại
        db.execSQL(CREATE_TABLE);

        String insertSample1 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD001', 'Le Thanh Vy', 8.5, 7.0, 9.0)";
        String insertSample2 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD002', 'Nguyen Minh Anh', 6.0, 8.5, 7.5)";
        String insertSample3 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD003', 'Tran Quoc Bao', 9.5, 9.0, 10.0)";
        String insertSample4 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD004', 'Pham Hoang Nam', 4.5, 5.0, 6.0)";
        String insertSample5 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD005', 'Le Thanh Vy', 8.5, 7.0, 9.0)";
        String insertSample6 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD006', 'Nguyen Minh Anh', 6.0, 8.5, 7.5)";
        String insertSample7 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD007', 'Tran Quoc Bao', 9.5, 9.0, 10.0)";
        String insertSample8 = "INSERT INTO " + TABLE_NAME + " VALUES ('SBD008', 'Pham Hoang Nam', 4.5, 5.0, 6.0)";

        db.execSQL(insertSample1);
        db.execSQL(insertSample2);
        db.execSQL(insertSample3);
        db.execSQL(insertSample4);
        db.execSQL(insertSample5);
        db.execSQL(insertSample6);
        db.execSQL(insertSample7);
        db.execSQL(insertSample8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Chạy khi bạn thay đổi DATABASE_VERSION (ví dụ thêm cột mới)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
