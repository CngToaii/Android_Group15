package com.example.qlnhahang.DAO;

import static com.example.qlnhahang.Database.CreateDatabase.TBL_CHITIETDONDAT_MADONDAT;
import static com.example.qlnhahang.Database.CreateDatabase.TBL_CHITIETDONDAT_MAMON;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlnhahang.DTO.ChiTietDonDatDTO;
import com.example.qlnhahang.Database.CreateDatabase;

public class ChiTietDonDatDAO {

    SQLiteDatabase database;
    public ChiTietDonDatDAO(Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean KiemTraMonTonTai(int madondat, int mamon){
        String query = "SELECT * FROM " +CreateDatabase.TBL_CHITIETDONDAT+ " WHERE " + TBL_CHITIETDONDAT_MAMON+
                " = " +mamon+ " AND " + TBL_CHITIETDONDAT_MADONDAT+ " = "+madondat;
        Cursor cursor = database.rawQuery(query,null);
        return cursor.getCount() != 0;
    }

    @SuppressLint("Range")
    public int LaySLMonTheoMaDon(int madondat, int mamon){
        int soluong = 0;
        String query = "SELECT * FROM " +CreateDatabase.TBL_CHITIETDONDAT+ " WHERE " + TBL_CHITIETDONDAT_MAMON+
                " = " +mamon+ " AND " + TBL_CHITIETDONDAT_MADONDAT+ " = "+madondat;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            soluong = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_CHITIETDONDAT_SOLUONG));
            cursor.moveToNext();
        }
        return soluong;
    }

    public boolean CapNhatSL(ChiTietDonDatDTO chiTietDonDatDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_CHITIETDONDAT_SOLUONG, chiTietDonDatDTO.getSoLuong());

        long ktra = database.update(CreateDatabase.TBL_CHITIETDONDAT,contentValues, TBL_CHITIETDONDAT_MADONDAT+ " = "
                +chiTietDonDatDTO.getMaDonDat()+ " AND " + TBL_CHITIETDONDAT_MAMON+ " = "
                +chiTietDonDatDTO.getMaMon(),null);
        if(ktra !=0){
            return true;
        }else {
            return false;
        }
    }


    public boolean CapNhatChiTietDonHang(ChiTietDonDatDTO chiTietDonDatDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_CHITIETDONDAT_SOLUONG, chiTietDonDatDTO.getSoLuong());

        return database.update(CreateDatabase.TBL_CHITIETDONDAT,contentValues, TBL_CHITIETDONDAT_MADONDAT+ " = "
                +chiTietDonDatDTO.getMaDonDat()+ " AND " + TBL_CHITIETDONDAT_MAMON+ " = "
                +chiTietDonDatDTO.getMaMon(),null) > 0;

    }

    public boolean ThemChiTietDonDat(ChiTietDonDatDTO chiTietDonDatDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TBL_CHITIETDONDAT_SOLUONG,chiTietDonDatDTO.getSoLuong());
        contentValues.put(TBL_CHITIETDONDAT_MADONDAT,chiTietDonDatDTO.getMaDonDat());
        contentValues.put(TBL_CHITIETDONDAT_MAMON,chiTietDonDatDTO.getMaMon());

        long ktra = database.insert(CreateDatabase.TBL_CHITIETDONDAT,null,contentValues);
        if(ktra !=0){
            return true;
        }else {
            return false;
        }
    }

    public  boolean deleteMonAn(String madonhang,String mamonan){
        return database.delete(CreateDatabase.TBL_CHITIETDONDAT,TBL_CHITIETDONDAT_MADONDAT +"=" +madonhang
                + " AND " +TBL_CHITIETDONDAT_MAMON +"=" +mamonan ,null) > 0;
    }

}
