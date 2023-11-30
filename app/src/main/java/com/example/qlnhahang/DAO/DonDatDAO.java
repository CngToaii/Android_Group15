package com.example.qlnhahang.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class DonDatDAO {

    SQLiteDatabase database;
    public DonDatDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public long ThemDonDat(DonDatDTO donDatDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_DONDAT_MABAN,donDatDTO.getMaBan());
        contentValues.put(CreateDatabase.TBL_DONDAT_MANV,donDatDTO.getMaNV());
        contentValues.put(CreateDatabase.TBL_DONDAT_NGAYDAT,donDatDTO.getNgayDat());
        contentValues.put(CreateDatabase.TBL_DONDAT_TINHTRANG,donDatDTO.getTinhTrang());
        contentValues.put(CreateDatabase.TBL_DONDAT_TONGTIEN,donDatDTO.getTongTien());

        long madondat = database.insert(CreateDatabase.TBL_DONDAT,null,contentValues);

        return madondat;
    }

    @SuppressLint("Range")
    public List<DonDatDTO> LayDSDonDat(){
        List<DonDatDTO> donDatDTOS = new ArrayList<DonDatDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_DONDAT;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DonDatDTO donDatDTO = new DonDatDTO();
            donDatDTO.setMaDonDat(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MADONDAT)));
            donDatDTO.setMaBan(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MABAN)));
            donDatDTO.setTongTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_TONGTIEN)));
            donDatDTO.setTinhTrang(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_TINHTRANG)));
            donDatDTO.setNgayDat(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_NGAYDAT)));
            donDatDTO.setMaNV(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MANV)));
            donDatDTOS.add(donDatDTO);

            cursor.moveToNext();
        }
        return donDatDTOS;
    }

    @SuppressLint("Range")
    public List<DonDatDTO> LayDSDonDatNgay(String ngaythang){
        List<DonDatDTO> donDatDTOS = new ArrayList<DonDatDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_DONDAT+" WHERE "+CreateDatabase.TBL_DONDAT_NGAYDAT+" like '"+ngaythang+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DonDatDTO donDatDTO = new DonDatDTO();
            donDatDTO.setMaDonDat(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MADONDAT)));
            donDatDTO.setMaBan(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MABAN)));
            donDatDTO.setTongTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_TONGTIEN)));
            donDatDTO.setTinhTrang(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_TINHTRANG)));
            donDatDTO.setNgayDat(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_NGAYDAT)));
            donDatDTO.setMaNV(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MANV)));
            donDatDTOS.add(donDatDTO);

            cursor.moveToNext();
        }
        return donDatDTOS;
    }

    @SuppressLint("Range")
    public long LayMaDonTheoMaBan(int maban, String tinhtrang){
        String query = "SELECT * FROM " +CreateDatabase.TBL_DONDAT+ " WHERE " +CreateDatabase.TBL_DONDAT_MABAN+ " = '" +maban+ "' AND "
                +CreateDatabase.TBL_DONDAT_TINHTRANG+ " = '" +tinhtrang+ "' ";
        long magoimon = 0;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            magoimon = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_DONDAT_MADONDAT));

            cursor.moveToNext();
        }
        return magoimon;
    }

    public boolean UpdateTongTienDonDat(int madondat,String tongtien){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_DONDAT_TONGTIEN,tongtien);
        long ktra  = database.update(CreateDatabase.TBL_DONDAT,contentValues,
                CreateDatabase.TBL_DONDAT_MADONDAT+" = "+madondat,null);
        if(ktra != 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean UpdateTThaiDonTheoMaBan(int maban,String tinhtrang){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_DONDAT_TINHTRANG,tinhtrang);
        long ktra = database.update(CreateDatabase.TBL_DONDAT,contentValues,CreateDatabase.TBL_DONDAT_MABAN+
                " = '"+maban+"'",null);
        if(ktra !=0){
            return true;
        }else {
            return false;
        }
    }

}
