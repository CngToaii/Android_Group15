package com.example.qlnhahang.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.DAO.ChiTietDonDatDAO;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DTO.ChiTietDonDatDTO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.R;

public class AmountMenuActivity extends AppCompatActivity {

    TextInputLayout TXTL_amountmenu_SoLuong;
    Button BTN_amountmenu_DongY;
    int maban, mamon, madon;
    DonDatDAO donDatDAO;
    ChiTietDonDatDAO chiTietDonDatDAO;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("DonDat");
    private DatabaseReference myRef1 = database.getReference("ChiTietDonDat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_menu_layout);

        //Lấy đối tượng view
        TXTL_amountmenu_SoLuong = (TextInputLayout) findViewById(R.id.txtl_amountmenu_SoLuong);
        BTN_amountmenu_DongY = (Button) findViewById(R.id.btn_amountmenu_DongY);

        //khởi tạo kết nối csdl
        donDatDAO = new DonDatDAO(this);
        chiTietDonDatDAO = new ChiTietDonDatDAO(this);

        //Lấy thông tin từ bàn được chọn
        Intent intent = getIntent();
        maban = intent.getIntExtra("maban", 0);
        mamon = intent.getIntExtra("mamon", 0);
        madon = intent.getIntExtra("madon", 0);
        BTN_amountmenu_DongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateAmount()) {
                    return;
                }
                Log.d("mamonmaban1", maban + ">>>>>");
                Log.d("mamonmaban1", mamon + "");
//                int madondat = (int) donDatDAO.LayMaDonTheoMaBan(maban,"false");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                //tinhTrang
                                //ds.getValue().toString() = "{367525={tinhTrang=false, maBan=57379, tongTien=0, ngayDat=03-11-2023, maDonDat=367525, maNV=0}}>>>>>
                                //lấy tinhTrang ,madondat
                                myRef.child(ds.getKey()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            for (DataSnapshot ds1 : snapshot.getChildren()) {
                                                DonDatDTO donDatDTO = ds1.getValue(DonDatDTO.class);
                                                Log.d("mamonmaban1", donDatDTO.getTinhTrang() + ">>>>>");
                                                if (donDatDTO.getMaBan() == maban && donDatDTO.getTinhTrang().equals("false")) {
                                                    String tinhtrang = donDatDTO.getTinhTrang();
                                                    int sluong = Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString());
                                                    ChiTietDonDatDTO chiTietDonDatDTO = new ChiTietDonDatDTO();
                                                    chiTietDonDatDTO.setMaMon(mamon);
                                                    chiTietDonDatDTO.setMaDonDat(donDatDTO.getMaDonDat());
                                                    chiTietDonDatDTO.setSoLuong(sluong);
                                                    myRef1.child(donDatDTO.getMaDonDat() + "").child(String.valueOf(mamon)).setValue(chiTietDonDatDTO);
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_sucessful), Toast.LENGTH_SHORT).show();
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                if (ktra) {
//                    //update số lượng món đã chọn
//                    int sluongcu = chiTietDonDatDAO.LaySLMonTheoMaDon(madondat, mamon);
//                    int sluongmoi = Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString());
//                    int tongsl = sluongcu + sluongmoi;
//
//                    ChiTietDonDatDTO chiTietDonDatDTO = new ChiTietDonDatDTO();
//                    chiTietDonDatDTO.setMaMon(mamon);
//                    chiTietDonDatDTO.setMaDonDat(madondat);
//                    chiTietDonDatDTO.setSoLuong(tongsl);
//
//                    myRef1.child(String.valueOf(madondat)).child(String.valueOf(mamon)).setValue(chiTietDonDatDTO);
//
//                } else {
//                    //thêm số lượng món nếu chưa chọn món này
//                    int sluong = Integer.parseInt(TXTL_amountmenu_SoLuong.getEditText().getText().toString());
//                    ChiTietDonDatDTO chiTietDonDatDTO = new ChiTietDonDatDTO();
//                    chiTietDonDatDTO.setMaMon(mamon);
//                    chiTietDonDatDTO.setMaDonDat(madondat);
//                    chiTietDonDatDTO.setSoLuong(sluong);
//
//                    boolean ktracapnhat = chiTietDonDatDAO.ThemChiTietDonDat(chiTietDonDatDTO);
//                    if (ktracapnhat) {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_sucessful), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_failed), Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });
    }

    //validate số lượng
    private boolean validateAmount() {
        // Lấy giá trị từ EditText
        String val = TXTL_amountmenu_SoLuong.getEditText().getText().toString().trim();

        // Kiểm tra nếu giá trị để trống, đặt giá trị mặc định là 1 và không đặt lỗi
        if (val.isEmpty()) {
            val = "1";
            return false;
        } else if (!val.matches("\\d+(?:\\.\\d+)?")) {
            // Kiểm tra giá trị không phải là số
            TXTL_amountmenu_SoLuong.setError("Số lượng không hợp lệ");
            return false;
        }

        // Nếu giá trị hợp lệ, xóa lỗi
        TXTL_amountmenu_SoLuong.setError(null);
        TXTL_amountmenu_SoLuong.setErrorEnabled(false);
        return true;
    }

}
