package com.example.qlnhahang;

import static com.example.qlnhahang.Activities.LoginActivity.maNv;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.DTO.NhanVienDTO;

public class Activityprofile extends AppCompatActivity {
    private TextView txtTenNhanVien, txtEmail, txtSoDienThoai, txtNgaySinh, txtGioiTinh;
    private FirebaseDatabase mData = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mData.getReference("NhanVien");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityprofile);

        txtTenNhanVien = (TextView) findViewById(R.id.txthoten);
        txtEmail = (TextView) findViewById(R.id.txtemail);
        txtSoDienThoai = (TextView) findViewById(R.id.txtsdt);
        txtNgaySinh = (TextView) findViewById(R.id.txtnamsinh);
        txtGioiTinh = (TextView) findViewById(R.id.txtgioitinh);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NhanVienDTO nhanVienDTO = dataSnapshot.getValue(NhanVienDTO.class);
                        if (nhanVienDTO.getMANV() == maNv) {
                            txtTenNhanVien.setText(nhanVienDTO.getHOTENNV());
                            txtEmail.setText(nhanVienDTO.getEMAIL());
                            txtSoDienThoai.setText(nhanVienDTO.getSDT());
                            txtNgaySinh.setText(nhanVienDTO.getNGAYSINH());
                            txtGioiTinh.setText(nhanVienDTO.getGIOITINH());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}