package com.example.qlnhahang.DAO;

import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.example.qlnhahang.DTO.BanAnDTO;

import java.util.ArrayList;
import java.util.List;

public class BanAnDAO {
    SQLiteDatabase database;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference("BanAn");

//    public BanAnDAO(Context context) {
//        CreateDatabase createDatabase = new CreateDatabase(context);
//        database = createDatabase.open();
//    }

    //Hàm thêm bàn ăn mới
    public boolean ThemBanAn(String tenban) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CreateDatabase.TBL_BAN_TENBAN,tenban);
//        contentValues.put(CreateDatabase.TBL_BAN_TINHTRANG,"false");
//
//        long ktra = database.insert(CreateDatabase.TBL_BAN,null,contentValues);
//        if(ktra != 0){
//            return true;
//        }else {
//            return false;
//        }
        myRef.push().setValue(tenban).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    //Hàm xóa bàn ăn theo mã
    public boolean XoaBanTheoMa(int maban) {
//        long ktra = database.delete(CreateDatabase.TBL_BAN, CreateDatabase.TBL_BAN_MABAN + " = " + maban, null);
//        if (ktra != 0) {
//            return true;
//        } else {
//            return false;
//        }
        myRef.child(String.valueOf(maban)).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    //Sửa tên bàn
    public boolean CapNhatTenBan(int maban, String tenban) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CreateDatabase.TBL_BAN_TENBAN, tenban);
//
//        long ktra = database.update(CreateDatabase.TBL_BAN, contentValues, CreateDatabase.TBL_BAN_MABAN + " = '" + maban + "' ", null);
//        if (ktra != 0) {
//            return true;
//        } else {
//            return false;
//        }
        myRef.child(String.valueOf(maban)).setValue(tenban).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    //Hàm lấy ds các bàn ăn đổ vào gridview
    public List<BanAnDTO> LayTatCaBanAn() {
        List<BanAnDTO> banAnDTOList = new ArrayList<BanAnDTO>();
//        String query = "SELECT * FROM " + CreateDatabase.TBL_BAN;
//        Cursor cursor = database.rawQuery(query, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            BanAnDTO banAnDTO = new BanAnDTO();
//            banAnDTO.setMaBan(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TBL_BAN_MABAN)));
//            banAnDTO.setTenBan(cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BAN_TENBAN)));
//
//            banAnDTOList.add(banAnDTO);
//            cursor.moveToNext();
//        }
//        return banAnDTOList;
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    banAnDTOList.clear();
                    for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        BanAnDTO banAnDTO = new BanAnDTO();
                        banAnDTO.setMaBan(Integer.parseInt(dataSnapshot1.getKey()));
                        banAnDTO.setTenBan(dataSnapshot1.getValue().toString());
                        banAnDTOList.add(banAnDTO);
                    }

                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {

            }
        });
        return banAnDTOList;
    }

    public String LayTinhTrangBanTheoMa(int maban) {
        final String[] tinhtrang = {""};
//        String query = "SELECT * FROM " + CreateDatabase.TBL_BAN + " WHERE " + CreateDatabase.TBL_BAN_MABAN + " = '" + maban + "' ";
//        Cursor cursor = database.rawQuery(query, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            tinhtrang = cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_MON_TINHTRANG));
//            cursor.moveToNext();
//        }

        Query query = myRef.child(String.valueOf(maban));
        query.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tinhtrang[0] = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {

            }
        });

        return tinhtrang[0];
    }

    public boolean CapNhatTinhTrangBan(int maban, String tinhtrang) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CreateDatabase.TBL_BAN_TINHTRANG, tinhtrang);
//
//        long ktra = database.update(CreateDatabase.TBL_BAN, contentValues, CreateDatabase.TBL_BAN_MABAN + " = '" + maban + "' ", null);
//        if (ktra != 0) {
//            return true;
//        } else {
//            return false;
//        }
        myRef.child(String.valueOf(maban)).setValue(tinhtrang).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public String LayTenBanTheoMa(int maban) {
        final String[] tenban = {""};
//        String query = "SELECT * FROM " + CreateDatabase.TBL_BAN + " WHERE " + CreateDatabase.TBL_BAN_MABAN + " = '" + maban + "' ";
//        Cursor cursor = database.rawQuery(query, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            tenban = cursor.getString(cursor.getColumnIndex(CreateDatabase.TBL_BAN_TENBAN));
//            cursor.moveToNext();
//        }

        Query query = myRef.child(String.valueOf(maban));
        query.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tenban[0] = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {

            }
        });

        return tenban[0];
    }
}
