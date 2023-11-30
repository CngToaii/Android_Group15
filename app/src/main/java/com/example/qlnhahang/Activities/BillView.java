package com.example.qlnhahang.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnhahang.DAO.NhanVienDAO;
import com.example.qlnhahang.DAO.ThanhToanDAO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.DTO.ThanhToanDTO;
import com.example.qlnhahang.R;
import com.example.qlnhahang.R.drawable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BillView extends AppCompatActivity {
    int madon, manv, maban;
    String ngaydat, tongtien,tennv;
    NhanVienDAO nhanVienDAO;
    ImageView img_detailstatistic_backbtn;
    private ArrayList<String> tenmon = new ArrayList<String>();
    private ArrayList<String> soluong = new ArrayList<String>();
    private ArrayList<String> giathanh = new ArrayList<String>();
    private ArrayList<String> thanhtien = new ArrayList<String>();

    TextView tvIDHOADON,tvTongTien,tvHovatenNV,tvNgayDat;
    TableLayout tableLayout;

    List<ThanhToanDTO> thanhToanDTOList;
    ThanhToanDAO thanhToanDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        AnhXa();

        img_detailstatistic_backbtn = (ImageView)findViewById(R.id.img_detailstatistic_backbtn);
        img_detailstatistic_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        nhanVienDAO = new NhanVienDAO(this);
        thanhToanDAO = new ThanhToanDAO(this);

        Intent intent = getIntent();
        madon = intent.getIntExtra("madon",0);
        manv = intent.getIntExtra("manv",0);
        maban = intent.getIntExtra("maban",0);
        ngaydat = intent.getStringExtra("ngaydat");
        tongtien = intent.getStringExtra("tongtien");

        NhanVienDTO nhanVienDTO = nhanVienDAO.LayNVTheoMa(manv);
        tennv = nhanVienDTO.getHOTENNV();

        thanhToanDTOList = thanhToanDAO.LayDSMonTheoMaDon(madon);

        tvIDHOADON.setText("ID HÓA ĐƠN : "+madon);

        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        tvTongTien.setText("TỔNG TIỀN : "+formatter.format(Long.valueOf(tongtien))+" VND");
        tvNgayDat.setText("Ngày đặt : "+ngaydat);
        tvHovatenNV.setText("Nhân viên : "+tennv);
        tenmon.add("Tên món");
        soluong.add("Số lượng");
        giathanh.add("Đơn giá");
        thanhtien.add("Thành tiền");
        for (ThanhToanDTO object :thanhToanDTOList ){
            tenmon.add(object.getTenMon());
            soluong.add(String.valueOf(object.getSoLuong()));
            giathanh.add(String.valueOf(object.getGiaTien()));
            thanhtien.add(formatter.format(Long.valueOf(object.getGiaTien())*object.getSoLuong()));
        }

        for(int i=0;i<tenmon.size();i++)
        {
            int d = drawable.border;
            TableRow row=new TableRow(this);
            row.setPadding(10,10, 10,10);
            row.setBackgroundResource(d);
            String tenmon1 = tenmon.get(i);
            String soluong1 = soluong.get(i);
            String thanhtien1 = thanhtien.get(i);
            String giathanh1 = giathanh.get(i);
            TextView tv1=new TextView(this);
            tv1.setText(tenmon1);
            tv1.setTextColor(Color.parseColor("#000080"));
            tv1.setTextSize(15f);
            tv1.setPadding(20,5, 20,5);

            TextView tv2=new TextView(this);
            tv2.setText(soluong1);
            tv2.setTextSize(15f);
            tv2.setPadding(20,5, 20,5);
            tv2.setTextColor(Color.parseColor("#000080"));

            TextView tv3=new TextView(this);
            tv3.setTextColor(Color.parseColor("#000080"));
            tv3.setText(thanhtien1);
            tv3.setTextSize(15f);
            tv3.setPadding(20,5, 20,5);

            TextView tv4 =new TextView(this);
            tv4.setTextColor(Color.parseColor("#000080"));
            tv4.setText(giathanh1);
            tv4.setTextSize(15f);
            tv4.setPadding(20,5, 20,5);

            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv4);
            row.addView(tv3);

            tableLayout.addView(row);
        }

    }

    private void AnhXa() {
        tvIDHOADON = findViewById(R.id.tvIDHOADON);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvHovatenNV = findViewById(R.id.tvHovatenNV);
        tvNgayDat = findViewById(R.id.tvNgayDat);
        tableLayout = findViewById(R.id.tbLyaout);
    }
}