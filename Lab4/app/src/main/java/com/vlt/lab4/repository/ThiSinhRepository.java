package com.vlt.lab4.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vlt.lab4.database.DatabaseHelper;
import com.vlt.lab4.models.ThiSinh;

import java.util.ArrayList;
import java.util.Collections;

public class ThiSinhRepository {
    private DatabaseHelper dbHelper;
    /*private final ArrayList<ThiSinh> data = new ArrayList<>();
    private final ArrayList<ThiSinh> copy = new ArrayList<>();

    public ThiSinhRepository(Context context) {
        data.add(new ThiSinh("SBD001", "Le Thanh Vy", 7.5f,7.5f,7.5f));
        data.add(new ThiSinh("SBD002", "Ngo Minh Tuan", 2.5f,2.5f,2.5f));
        data.add(new ThiSinh("SBD003", "Thac Duy Anh", 1.5f,1.5f,1.5f));
        data.add(new ThiSinh("SBD004", "Vu Quoc Trung", 3.5f,3.5f,3.5f));
        copy.clear();
        copy.addAll(data);
    }*/

    public ThiSinhRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /*public ArrayList<ThiSinh> getCopy() {
        return copy;
    }

    public ArrayList<ThiSinh> getData() {
        return data;
    }*/

    public ArrayList<ThiSinh> getAll() {
        ArrayList<ThiSinh> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new ThiSinh(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getFloat(2),
                    cursor.getFloat(3),
                    cursor.getFloat(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /*public void add(ThiSinh ts) {
        data.add(ts);
        copy.clear();
        copy.addAll(data);
    }

    public void update(int realIndex, ThiSinh ts) {
        data.set(realIndex, ts);
        copy.clear();
        copy.addAll(data);
    }

    public void delete(int realIndex) {
        data.remove(realIndex);
        copy.clear();
        copy.addAll(data);
    }*/

    public void add(ThiSinh ts) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sbd", ts.getSbd());
        values.put("hoTen", ts.getHoTen());
        values.put("toan", ts.getToan());
        values.put("ly", ts.getLy());
        values.put("hoa", ts.getHoa());

        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    public void update(String oldSbd, ThiSinh ts) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sbd", ts.getSbd());
        values.put("hoTen", ts.getHoTen());
        values.put("toan", ts.getToan());
        values.put("ly", ts.getLy());
        values.put("hoa", ts.getHoa());

        // Cập nhật dòng có SBD trùng với oldSbd
        db.update(DatabaseHelper.TABLE_NAME, values, "sbd = ?", new String[]{oldSbd});
        db.close();
    }

    public void delete(String sbd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, "sbd = ?", new String[]{sbd});
        db.close();
    }

    /*public ArrayList<ThiSinh> filter(String keyword) {
        ArrayList<ThiSinh> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu lệnh SQL: Tìm kiếm các bản ghi có hoTen chứa từ khóa
        // Dấu % đại diện cho bất kỳ ký tự nào đứng trước hoặc sau từ khóa
        String sql = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.COLUMN_HOTEN + " LIKE ?";

        // Đối số cho dấu ?: %keyword%
        String[] selectionArgs = new String[]{"%" + keyword + "%"};

        Cursor cursor = db.rawQuery(sql, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new ThiSinh(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getFloat(3),
                        cursor.getFloat(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public ArrayList<ThiSinh> getAllSortedByTotalScore() {
        ArrayList<ThiSinh> list = getAll();

        Collections.sort(list, (ts1, ts2) ->
                Float.compare(ts1.tongDiem(), ts2.tongDiem()));

        return list;
    }

    public ArrayList<ThiSinh> getAllSortedBySBD() {
        ArrayList<ThiSinh> list = getAll();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM "+DatabaseHelper.TABLE_NAME
                +" ORDER BY "+DatabaseHelper.COLUMN_SBD+" ASC";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new ThiSinh(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getFloat(3),
                        cursor.getFloat(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public ArrayList<ThiSinh> getAllSortedByDTB() {
        ArrayList<ThiSinh> list = getAll();

        Collections.sort(list, (ts1, ts2) ->
                Float.compare(ts1.diemTB(), ts2.diemTB()));

        return list;
    }*/

    public ArrayList<ThiSinh> getData(String keyword, String sortType) {
        ArrayList<ThiSinh> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.COLUMN_HOTEN + " LIKE ?";

        if (sortType.equals("SBD_ASC")) {
            sql += " ORDER BY " + DatabaseHelper.COLUMN_SBD + " ASC";
        }

        Cursor cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new ThiSinh(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (sortType.equals("SCORE_ASC")) {
            Collections.sort(list, (ts1, ts2) -> Float.compare(ts1.tongDiem(), ts2.tongDiem()));
        } else if (sortType.equals("AVG_SCORE_ASC")) {
            Collections.sort(list, (ts1, ts2) -> Float.compare(ts1.diemTB(), ts2.diemTB()));
        }

        return list;
    }
}
