package com.example.qlnhahang.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlnhahang.DTO.MonDTO;
import com.example.qlnhahang.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class MonDAO {
    SQLiteDatabase database;
    public MonDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean ThemMon(MonDTO monDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_MON_MALOAI,monDTO.getMaLoai());
        contentValues.put(CreateDatabase.TBL_MON_TENMON,monDTO.getTenMon());
        contentValues.put(CreateDatabase.TBL_MON_GIATIEN,monDTO.getGiaTien());
        contentValues.put(CreateDatabase.TBL_MON_HINHANH,monDTO.getHinhAnh());
        contentValues.put(CreateDatabase.TBL_MON_TINHTRANG,"true");

        long ktra = database.insert(CreateDatabase.TBL_MON,null,contentValues);

        if(ktra !=0){
            return true;
        }else {
            return false;
        }
    }

    public boolean XoaMon(int mamon){
        long ktra = database.delete(CreateDatabase.TBL_MON,CreateDatabase.TBL_MON_MAMON+ " = " +mamon
                ,null);
        if(ktra !=0 ){
            return true;
        }else {
            return false;
        }
    }

    public boolean SuaMon(MonDTO monDTO,int mamon){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_MON_MALOAI,monDTO.getMaLoai());
        contentValues.put(CreateDatabase.TBL_MON_TENMON,monDTO.getTenMon());
        contentValues.put(CreateDatabase.TBL_MON_GIATIEN,monDTO.getGiaTien());
        contentValues.put(CreateDatabase.TBL_MON_HINHANH,monDTO.getHinhAnh());
        contentValues.put(CreateDatabase.TBL_MON_TINHTRANG,monDTO.getTinhTrang());

        long ktra = database.update(CreateDatabase.TBL_MON,contentValues,
                CreateDatabase.TBL_MON_MAMON+" = "+mamon,null);
        if(ktra !=0){
            return true;
        }else {
            return false;
        }
    }

    @SuppressLint("Range")
    public List<MonDTO> LayDSMonTheoLoai(int maloai){
        List<MonDTO> monDTOList = new ArrayList<MonDTO>();
        String query = "SELECT * FROM " +CreateDatabase.TBL_MON+ " WHERE " +CreateDatabase.TBL_MON_MALOAI+ " = '" +maloai+ "' ";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            MonDTO monDTO = new MonDTO();
            monDTO.setHinhAnh(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_MON_HINHANH)));
            monDTO.setTenMon(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_TENMON)));
            monDTO.setMaLoai(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_MON_MALOAI)));
            monDTO.setMaMon(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_MON_MAMON)));
            monDTO.setGiaTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_GIATIEN)));
            monDTO.setTinhTrang(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_TINHTRANG)));
            monDTOList.add(monDTO);

            cursor.moveToNext();
        }
        return monDTOList;
    }

    @SuppressLint("Range")
    public MonDTO LayMonTheoMa(int mamon){
        MonDTO monDTO = new MonDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_MON+" WHERE "+CreateDatabase.TBL_MON_MAMON+" = "+mamon;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            monDTO.setHinhAnh(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_MON_HINHANH)));
            monDTO.setTenMon(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_TENMON)));
            monDTO.setMaLoai(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_MON_MALOAI)));
            monDTO.setMaMon(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_MON_MAMON)));
            monDTO.setGiaTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_GIATIEN)));
            monDTO.setTinhTrang(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_TINHTRANG)));

            cursor.moveToNext();
        }
        return monDTO;
    }

}
