package com.example.qlnhahang.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlnhahang.DTO.LoaiMonDTO;
import com.example.qlnhahang.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoaiMonDAO {

    SQLiteDatabase database;
    public LoaiMonDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean ThemLoaiMon(LoaiMonDTO loaiMonDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_LOAIMON_TENLOAI,loaiMonDTO.getTenLoai());
        contentValues.put(CreateDatabase.TBL_LOAIMON_HINHANH,loaiMonDTO.getHinhAnh());
        long ktra = database.insert(CreateDatabase.TBL_LOAIMON,null,contentValues);

        if(ktra != 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean XoaLoaiMon(int maloai){
        long ktra = database.delete(CreateDatabase.TBL_LOAIMON,CreateDatabase.TBL_LOAIMON_MALOAI+ " = " +maloai
                ,null);
        if(ktra !=0 ){
            return true;
        }else {
            return false;
        }
    }

    public boolean SuaLoaiMon(LoaiMonDTO loaiMonDTO,int maloai){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_LOAIMON_TENLOAI,loaiMonDTO.getTenLoai());
        contentValues.put(CreateDatabase.TBL_LOAIMON_HINHANH,loaiMonDTO.getHinhAnh());
        long ktra = database.update(CreateDatabase.TBL_LOAIMON,contentValues
                ,CreateDatabase.TBL_LOAIMON_MALOAI+" = "+maloai,null);
        if(ktra != 0){
            return true;
        }else {
            return false;
        }
    }

    @SuppressLint("Range")
    public List<LoaiMonDTO> LayDSLoaiMon(){
        List<LoaiMonDTO> loaiMonDTOList = new ArrayList<LoaiMonDTO>();
        String query = "SELECT * FROM " +CreateDatabase.TBL_LOAIMON;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            LoaiMonDTO loaiMonDTO = new LoaiMonDTO();
            loaiMonDTO.setMaLoai(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_LOAIMON_MALOAI)));
            loaiMonDTO.setTenLoai(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_LOAIMON_TENLOAI)));
            loaiMonDTO.setHinhAnh(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_LOAIMON_HINHANH)));
            loaiMonDTOList.add(loaiMonDTO);

            cursor.moveToNext();
        }
        return loaiMonDTOList;
    }

    @SuppressLint("Range")
    public LoaiMonDTO LayLoaiMonTheoMa(int maloai){
        LoaiMonDTO loaiMonDTO = new LoaiMonDTO();
        String query = "SELECT * FROM " +CreateDatabase.TBL_LOAIMON+" WHERE "+CreateDatabase.TBL_LOAIMON_MALOAI+" = "+maloai;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            loaiMonDTO.setMaLoai(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_LOAIMON_MALOAI)));
            loaiMonDTO.setTenLoai(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_LOAIMON_TENLOAI)));
            loaiMonDTO.setHinhAnh(cursor.getBlob(cursor.getColumnIndex(CreateDatabase.TBL_LOAIMON_HINHANH)));

            cursor.moveToNext();
        }
        return loaiMonDTO;
    }

}
