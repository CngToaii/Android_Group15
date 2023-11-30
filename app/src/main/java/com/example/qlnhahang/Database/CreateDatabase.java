package com.example.qlnhahang.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateDatabase extends SQLiteOpenHelper {

    public static String TBL_NHANVIEN = "NHANVIEN";
    public static String TBL_MON = "MON";
    public static String TBL_LOAIMON = "LOAIMON";
    public static String TBL_BAN = "BAN";
    public static String TBL_DONDAT = "DONDAT";
    public static String TBL_CHITIETDONDAT = "CHITIETDONDAT";
    public static String TBL_QUYEN = "QUYEN";

    //Bảng nhân viên
    public static String TBL_NHANVIEN_MANV = "MANV";
    public static String TBL_NHANVIEN_HOTENNV = "HOTENNV";
    public static String TBL_NHANVIEN_TENDN = "TENDN";
    public static String TBL_NHANVIEN_MATKHAU = "MATKHAU";
    public static String TBL_NHANVIEN_EMAIL = "EMAIL";
    public static String TBL_NHANVIEN_SDT = "SDT";
    public static String TBL_NHANVIEN_GIOITINH = "GIOITINH";
    public static String TBL_NHANVIEN_NGAYSINH = "NGAYSINH";
    public static String TBL_NHANVIEN_MAQUYEN= "MAQUYEN";

    //Bảng quyền
    public static String TBL_QUYEN_MAQUYEN = "MAQUYEN";
    public static String TBL_QUYEN_TENQUYEN = "TENQUYEN";

    //Bảng món
    public static String TBL_MON_MAMON = "MAMON";
    public static String TBL_MON_TENMON = "TENMON";
    public static String TBL_MON_GIATIEN = "GIATIEN";
    public static String TBL_MON_TINHTRANG = "TINHTRANG";
    public static String TBL_MON_HINHANH = "HINHANH";
    public static String TBL_MON_MALOAI = "MALOAI";

    //Bảng loại món
    public static String TBL_LOAIMON_MALOAI = "MALOAI";
    public static String TBL_LOAIMON_TENLOAI = "TENLOAI";
    public static String TBL_LOAIMON_HINHANH = "HINHANH";

    //Bảng bàn
    public static String TBL_BAN_MABAN = "MABAN";
    public static String TBL_BAN_TENBAN = "TENBAN";
    public static String TBL_BAN_TINHTRANG = "TINHTRANG";

    //Bảng đơn đặt
    public static String TBL_DONDAT_MADONDAT = "MADONDAT";
    public static String TBL_DONDAT_MANV = "MANV";
    public static String TBL_DONDAT_NGAYDAT = "NGAYDAT";
    public static String TBL_DONDAT_TINHTRANG = "TINHTRANG";
    public static String TBL_DONDAT_TONGTIEN = "TONGTIEN";
    public static String TBL_DONDAT_MABAN = "MABAN";

    //Bảng chi tiết đơn đặt
    public static String TBL_CHITIETDONDAT_MADONDAT = "MADONDAT";
    public static String TBL_CHITIETDONDAT_MAMON = "MAMON";
    public static String TBL_CHITIETDONDAT_SOLUONG = "SOLUONG";


    public CreateDatabase(Context context) {
        super(context, "OrderDrink", null, 1);
    }

    //thực hiện tạo bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        String tblNHANVIEN = "CREATE TABLE " +TBL_NHANVIEN+ " ( " +TBL_NHANVIEN_MANV+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_NHANVIEN_HOTENNV+ " TEXT, " +TBL_NHANVIEN_TENDN+ " TEXT, " +TBL_NHANVIEN_MATKHAU+ " TEXT, " +TBL_NHANVIEN_EMAIL+ " TEXT, "
                +TBL_NHANVIEN_SDT+ " TEXT, " +TBL_NHANVIEN_GIOITINH+ " TEXT, " +TBL_NHANVIEN_NGAYSINH+ " TEXT , "+TBL_NHANVIEN_MAQUYEN+" INTEGER)";

        String tblQUYEN = "CREATE TABLE " +TBL_QUYEN+ " ( " +TBL_QUYEN_MAQUYEN+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_QUYEN_TENQUYEN+ " TEXT)" ;

        String tblBAN = "CREATE TABLE " +TBL_BAN+ " ( " +TBL_BAN_MABAN+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_BAN_TENBAN+ " TEXT, " +TBL_BAN_TINHTRANG+ " TEXT )";

        String tblMON = "CREATE TABLE " +TBL_MON+ " ( " +TBL_MON_MAMON+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_MON_TENMON+ " TEXT, " +TBL_MON_GIATIEN+ " TEXT, " +TBL_MON_TINHTRANG+ " TEXT, "
                +TBL_MON_HINHANH+ " BLOB, "+TBL_MON_MALOAI+ " INTEGER )";

        String tblLOAIMON = "CREATE TABLE " +TBL_LOAIMON+ " ( " +TBL_LOAIMON_MALOAI+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_LOAIMON_HINHANH+ " BLOB, " +TBL_LOAIMON_TENLOAI+ " TEXT)" ;

        String tblDONDAT = "CREATE TABLE " +TBL_DONDAT+ " ( " +TBL_DONDAT_MADONDAT+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TBL_DONDAT_MABAN+ " INTEGER, " +TBL_DONDAT_MANV+ " INTEGER, " +TBL_DONDAT_NGAYDAT+ " TEXT, "+TBL_DONDAT_TONGTIEN+" TEXT,"
                +TBL_DONDAT_TINHTRANG+ " TEXT )" ;

        String tblCHITIETDONDAT = "CREATE TABLE " +TBL_CHITIETDONDAT+ " ( " +TBL_CHITIETDONDAT_MADONDAT+ " INTEGER, "
                +TBL_CHITIETDONDAT_MAMON+ " INTEGER, " +TBL_CHITIETDONDAT_SOLUONG+ " INTEGER, "
                + " PRIMARY KEY ( " +TBL_CHITIETDONDAT_MADONDAT+ "," +TBL_CHITIETDONDAT_MAMON+ "))";

        db.execSQL(tblNHANVIEN);
        db.execSQL(tblQUYEN);
        db.execSQL(tblBAN);
        db.execSQL(tblMON);
        db.execSQL(tblLOAIMON);
        db.execSQL(tblDONDAT);
        db.execSQL(tblCHITIETDONDAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //mở kết nối csdl
    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}
