package com.example.qlnhahang.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.CustomAdapter.AdapterPayment;
import com.example.qlnhahang.CustomAdapter.OnclickItem;
import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DAO.ChiTietDonDatDAO;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DAO.ThanhToanDAO;
import com.example.qlnhahang.DTO.ChiTietDonDatDTO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.DTO.MonDTO;
import com.example.qlnhahang.DTO.ThanhToanDTO;
import com.example.qlnhahang.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, OnclickItem {

    ImageView IMG_payment_backbtn;
    TextView TXT_payment_TenBan, TXT_payment_NgayDat, TXT_payment_TongTien;
    Button BTN_payment_ThanhToan;
    RecyclerView gvDisplayPayment;
    DonDatDAO donDatDAO;
    BanAnDAO banAnDAO;
    ChiTietDonDatDAO chiTietDonDatDAO;
    ThanhToanDAO thanhToanDAO;
    List<ThanhToanDTO> thanhToanDTOS = new ArrayList<>();
    AdapterPayment adapterDisplayPayment;
    long tongtien = 0;
    int maban, madondat;
    FragmentManager fragmentManager;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("DonDat");
    private DatabaseReference myRef1 = database.getReference("ChiTietDonDat");
    private DatabaseReference myRef2 = database.getReference("Mon");
    private DatabaseReference myRef3 = database.getReference("BanAn");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        //region thuộc tính view
        gvDisplayPayment = (RecyclerView) findViewById(R.id.gvDisplayPayment);
        gvDisplayPayment.setHasFixedSize(true);
        gvDisplayPayment.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        IMG_payment_backbtn = (ImageView) findViewById(R.id.img_payment_backbtn);
        TXT_payment_TenBan = (TextView) findViewById(R.id.txt_payment_TenBan);
        TXT_payment_NgayDat = (TextView) findViewById(R.id.txt_payment_NgayDat);
        TXT_payment_TongTien = (TextView) findViewById(R.id.txt_payment_TongTien);
        BTN_payment_ThanhToan = (Button) findViewById(R.id.btn_payment_ThanhToan);
        //endregion

        //khởi tạo kết nối csdl
        donDatDAO = new DonDatDAO(this);
        thanhToanDAO = new ThanhToanDAO(this);
        chiTietDonDatDAO = new ChiTietDonDatDAO(this);

        fragmentManager = getSupportFragmentManager();

        //lấy data từ mã bàn đc chọn
        Intent intent = getIntent();
        maban = intent.getIntExtra("maban", 0);
        String tenban = intent.getStringExtra("tenban");
        String ngaydat = intent.getStringExtra("ngaydat");

        TXT_payment_TenBan.setText(tenban);
        TXT_payment_NgayDat.setText(ngaydat);

        //ktra mã bàn tồn tại thì hiển thị
        if (maban != 0) {
            HienThiThanhToan();

            setTongTien();
        }

        BTN_payment_ThanhToan.setOnClickListener(this);
        IMG_payment_backbtn.setOnClickListener(this);

    }

    private void setTongTien() {
        tongtien = 0;
        for (int i = 0; i < thanhToanDTOS.size(); i++) {
            int soluong = thanhToanDTOS.get(i).getSoLuong();
            int giatien = thanhToanDTOS.get(i).getGiaTien();

            tongtien += (soluong * giatien);
        }
        TXT_payment_TongTien.setText(String.valueOf(tongtien) + " VNĐ");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id){
//            case R.id.btn_payment_ThanhToan:
//                boolean ktraban = banAnDAO.CapNhatTinhTrangBan(maban,"false");
//                boolean ktradondat = donDatDAO.UpdateTThaiDonTheoMaBan(maban,"true");
//                boolean ktratongtien = donDatDAO.UpdateTongTienDonDat(madondat,String.valueOf(tongtien));
//                if(ktraban && ktradondat && ktratongtien){
//                    HienThiThanhToan();
//                    TXT_payment_TongTien.setText("0 VNĐ");
//                    Toast.makeText(getApplicationContext(),"Thanh toán thành công!",Toast.LENGTH_SHORT);
//                }else{
//                    Toast.makeText(getApplicationContext(),"Lỗi thanh toán!",Toast.LENGTH_SHORT);
//                }
//                break;
//
//            case R.id.img_payment_backbtn:
//                finish();
//                break;
//        }
        if (id == R.id.btn_payment_ThanhToan) {
//            boolean ktraban = banAnDAO.CapNhatTinhTrangBan(maban, "false");
            myRef3.child(String.valueOf(maban)).child("duocChon").setValue(false);
//            boolean ktradondat = donDatDAO.UpdateTThaiDonTheoMaBan(maban, "true");
            myRef.child(String.valueOf(maban)).child(thanhToanDTOS.get(0).getMadon() + "").child("tinhTrang").setValue("true");
//            boolean ktratongtien = donDatDAO.UpdateTongTienDonDat(madondat, String.valueOf(tongtien));
            myRef.child(String.valueOf(maban)).child(thanhToanDTOS.get(0).getMadon() + "").child("tongTien").setValue(String.valueOf(tongtien));
            thanhToanDTOS.clear();
            adapterDisplayPayment = new AdapterPayment(PaymentActivity.this, getApplicationContext());
            adapterDisplayPayment.setDta(thanhToanDTOS);
            gvDisplayPayment.setAdapter(adapterDisplayPayment);
            TXT_payment_TongTien.setText("0 VNĐ");
            Toast.makeText(getApplicationContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
            finish();
//            if (ktraban && ktradondat && ktratongtien) {
//                HienThiThanhToan();
//                TXT_payment_TongTien.setText("0 VNĐ");
//                Toast.makeText(getApplicationContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT);
//            } else {
//                Toast.makeText(getApplicationContext(), "Lỗi thanh toán!", Toast.LENGTH_SHORT);
//            }
        } else if (id == R.id.img_payment_backbtn) {
            finish();
        }
    }

    //hiển thị data lên gridview
    private void HienThiThanhToan() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        myRef.child(ds.getKey() + "").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot ds1 : snapshot.getChildren()) {
                                        DonDatDTO donDatDTO = ds1.getValue(DonDatDTO.class);
                                        if (donDatDTO.getMaBan() == maban && donDatDTO.getTinhTrang().equals("false")) {
                                            Log.d("mamonmaban2", donDatDTO.getMaBan() + ">>>>>>>>" + maban + ">>>>>");
                                            Query query = myRef1.child(donDatDTO.getMaDonDat() + "");
                                            query.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        thanhToanDTOS.clear();
                                                        for (DataSnapshot ds1 : snapshot.getChildren()) {
                                                            ChiTietDonDatDTO chiTietDonDatDTO = ds1.getValue(ChiTietDonDatDTO.class);
                                                            myRef2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()) {
                                                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                                                            MonDTO monDTO = ds.getValue(MonDTO.class);
                                                                            if (chiTietDonDatDTO.getMaMon() == monDTO.getMaMon()) {
                                                                                ThanhToanDTO thanhToanDTO = new ThanhToanDTO();
                                                                                thanhToanDTO.setMadon(String.valueOf(chiTietDonDatDTO.getMaDonDat()));
                                                                                thanhToanDTO.setMaMon(String.valueOf(chiTietDonDatDTO.getMaMon()));
                                                                                thanhToanDTO.setTenMon(monDTO.getTenMon());
                                                                                thanhToanDTO.setSoLuong(chiTietDonDatDTO.getSoLuong());
                                                                                thanhToanDTO.setGiaTien(Integer.parseInt(monDTO.getGiaTien()));
                                                                                thanhToanDTO.setHinhAnh(monDTO.getHinhAnh());
                                                                                thanhToanDTOS.add(thanhToanDTO);
                                                                            }
                                                                        }
                                                                        adapterDisplayPayment = new AdapterPayment(PaymentActivity.this, getApplicationContext());
                                                                        adapterDisplayPayment.setDta(thanhToanDTOS);
                                                                        gvDisplayPayment.setAdapter(adapterDisplayPayment);
                                                                        setTongTien();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        thanhToanDTOS.clear();
                                                        adapterDisplayPayment = new AdapterPayment(PaymentActivity.this, getApplicationContext());
                                                        adapterDisplayPayment.setDta(thanhToanDTOS);
                                                        gvDisplayPayment.setAdapter(adapterDisplayPayment);
                                                        setTongTien();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }

                                            });
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

    }


    @Override
    public void OnClickBack(ThanhToanDTO thanhToanDTO) {
        int count = thanhToanDTO.getSoLuong();
        if (count == 1) {
            Toast.makeText(getApplicationContext(), "Đã đến giới hạn minimum", Toast.LENGTH_SHORT).show();
        } else {
            count--;
            ChiTietDonDatDTO chitiethoadon = new ChiTietDonDatDTO();
            chitiethoadon.setMaDonDat(Integer.parseInt(thanhToanDTO.getMadon()));
            chitiethoadon.setMaMon(Integer.parseInt(thanhToanDTO.getMaMon()));
            chitiethoadon.setSoLuong(count);
            myRef1.child(thanhToanDTO.getMadon()).child(thanhToanDTO.getMaMon()).setValue(chitiethoadon);
            setTongTien();
        }
    }

    @Override
    public void OnClickNext(ThanhToanDTO thanhToanDTO) {
        int count = thanhToanDTO.getSoLuong();
        count++;
        ChiTietDonDatDTO chitiethoadon = new ChiTietDonDatDTO();
        chitiethoadon.setMaDonDat(Integer.parseInt(thanhToanDTO.getMadon()));
        chitiethoadon.setMaMon(Integer.parseInt(thanhToanDTO.getMaMon()));
        chitiethoadon.setSoLuong(count);
        myRef1.child(thanhToanDTO.getMadon()).child(thanhToanDTO.getMaMon()).setValue(chitiethoadon);
    }

    @Override
    public void OnLongClick(ThanhToanDTO thanhToanDTO) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Thông báo")
                .setMessage("Bạn muốn xóa món ăn này")
                .setPositiveButton("XÓA", (dialogInterface, i) -> {
                    myRef1.child(thanhToanDTO.getMadon()).child(thanhToanDTO.getMaMon()).removeValue();
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss()).setIcon(R.drawable.ic_baseline_warning_24)
                .show();
    }


}