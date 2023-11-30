package com.example.qlnhahang.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.Database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    SQLiteDatabase database;
    public NhanVienDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public long ThemNhanVien(NhanVienDTO nhanVienDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_NHANVIEN_HOTENNV,nhanVienDTO.getHOTENNV());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_TENDN,nhanVienDTO.getTENDN());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_MATKHAU,nhanVienDTO.getMATKHAU());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_EMAIL,nhanVienDTO.getEMAIL());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_SDT,nhanVienDTO.getSDT());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_GIOITINH,nhanVienDTO.getGIOITINH());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_NGAYSINH,nhanVienDTO.getNGAYSINH());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_MAQUYEN,nhanVienDTO.getMAQUYEN());

        long ktra = database.insert(CreateDatabase.TBL_NHANVIEN,null,contentValues);
        return ktra;
    }

    public long SuaNhanVien(NhanVienDTO nhanVienDTO,int manv){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_NHANVIEN_HOTENNV,nhanVienDTO.getHOTENNV());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_TENDN,nhanVienDTO.getTENDN());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_MATKHAU,nhanVienDTO.getMATKHAU());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_EMAIL,nhanVienDTO.getEMAIL());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_SDT,nhanVienDTO.getSDT());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_GIOITINH,nhanVienDTO.getGIOITINH());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_NGAYSINH,nhanVienDTO.getNGAYSINH());
        contentValues.put(CreateDatabase.TBL_NHANVIEN_MAQUYEN,nhanVienDTO.getMAQUYEN());

        long ktra = database.update(CreateDatabase.TBL_NHANVIEN,contentValues,
                CreateDatabase.TBL_NHANVIEN_MANV+" = "+manv,null);
        return ktra;
    }

    public int KiemTraDN(String tenDN, String matKhau){
        String query = "SELECT * FROM " +CreateDatabase.TBL_NHANVIEN+ " WHERE "
                +CreateDatabase.TBL_NHANVIEN_TENDN +" = '"+ tenDN+"' AND "+CreateDatabase.TBL_NHANVIEN_MATKHAU +" = '" +matKhau +"'";
        int manv = 0;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            manv = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MANV)) ;
            cursor.moveToNext();
        }
        return manv;
    }

    public boolean KtraTonTaiNV(){
        String query = "SELECT * FROM "+CreateDatabase.TBL_NHANVIEN;
        Cursor cursor =database.rawQuery(query,null);
        if(cursor.getCount() != 0){
            return true;
        }else {
            return false;
        }
    }

    public List<NhanVienDTO> LayDSNV(){
        List<NhanVienDTO> nhanVienDTOS = new ArrayList<NhanVienDTO>();
        String query = "SELECT * FROM "+CreateDatabase.TBL_NHANVIEN;

        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            NhanVienDTO nhanVienDTO = new NhanVienDTO();
            nhanVienDTO.setHOTENNV(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_HOTENNV)));
            nhanVienDTO.setEMAIL(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_EMAIL)));
            nhanVienDTO.setGIOITINH(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_GIOITINH)));
            nhanVienDTO.setNGAYSINH(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_NGAYSINH)));
            nhanVienDTO.setSDT(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_SDT)));
            nhanVienDTO.setTENDN(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_TENDN)));
            nhanVienDTO.setMATKHAU(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MATKHAU)));
            nhanVienDTO.setMANV(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MANV)));
            nhanVienDTO.setMAQUYEN(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MAQUYEN)));

            nhanVienDTOS.add(nhanVienDTO);
            cursor.moveToNext();
        }
        return nhanVienDTOS;
    }

    public boolean XoaNV(int manv){
        long ktra = database.delete(CreateDatabase.TBL_NHANVIEN,CreateDatabase.TBL_NHANVIEN_MANV+ " = " +manv
                ,null);
        if(ktra !=0 ){
            return true;
        }else {
            return false;
        }
    }

    public NhanVienDTO LayNVTheoMa(int manv){
        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        String query = "SELECT * FROM "+CreateDatabase.TBL_NHANVIEN+" WHERE "+CreateDatabase.TBL_NHANVIEN_MANV+" = "+manv;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            nhanVienDTO.setHOTENNV(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_HOTENNV)));
            nhanVienDTO.setEMAIL(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_EMAIL)));
            nhanVienDTO.setGIOITINH(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_GIOITINH)));
            nhanVienDTO.setNGAYSINH(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_NGAYSINH)));
            nhanVienDTO.setSDT(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_SDT)));
            nhanVienDTO.setTENDN(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_TENDN)));
            nhanVienDTO.setMATKHAU(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MATKHAU)));
            nhanVienDTO.setMANV(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MANV)));
            nhanVienDTO.setMAQUYEN(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MAQUYEN)));

            cursor.moveToNext();
        }
        return nhanVienDTO;
    }

    public int LayQuyenNV(int manv){
        int maquyen = 0;
        String query = "SELECT * FROM "+CreateDatabase.TBL_NHANVIEN+" WHERE "+CreateDatabase.TBL_NHANVIEN_MANV+" = "+manv;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            maquyen = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_NHANVIEN_MAQUYEN));

            cursor.moveToNext();
        }
        return maquyen;
    }


}
