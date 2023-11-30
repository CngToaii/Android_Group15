package com.example.qlnhahang.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlnhahang.CustomAdapter.AdapterDisplayPayment;
import com.example.qlnhahang.CustomAdapter.AdapterPayment;
import com.example.qlnhahang.CustomAdapter.OnclickItem;
import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DAO.ChiTietDonDatDAO;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DAO.ThanhToanDAO;
import com.example.qlnhahang.DTO.ChiTietDonDatDTO;
import com.example.qlnhahang.DTO.ThanhToanDTO;
import com.example.qlnhahang.Fragments.DisplayCategoryFragment;
import com.example.qlnhahang.Fragments.DisplayTableFragment;
import com.example.qlnhahang.R;

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
    List<ThanhToanDTO> thanhToanDTOS;
    AdapterPayment adapterDisplayPayment;
    long tongtien = 0;
    int maban, madondat;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        //region thuộc tính view
        gvDisplayPayment= (RecyclerView)findViewById(R.id.gvDisplayPayment);
        gvDisplayPayment.setHasFixedSize(true);
        gvDisplayPayment.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        IMG_payment_backbtn = (ImageView)findViewById(R.id.img_payment_backbtn);
        TXT_payment_TenBan = (TextView)findViewById(R.id.txt_payment_TenBan);
        TXT_payment_NgayDat = (TextView)findViewById(R.id.txt_payment_NgayDat);
        TXT_payment_TongTien = (TextView)findViewById(R.id.txt_payment_TongTien);
        BTN_payment_ThanhToan = (Button)findViewById(R.id.btn_payment_ThanhToan);
        //endregion

        //khởi tạo kết nối csdl
        donDatDAO = new DonDatDAO(this);
        thanhToanDAO = new ThanhToanDAO(this);
        banAnDAO = new BanAnDAO(this);
        chiTietDonDatDAO = new ChiTietDonDatDAO(this);

        fragmentManager = getSupportFragmentManager();

        //lấy data từ mã bàn đc chọn
        Intent intent = getIntent();
        maban = intent.getIntExtra("maban",0);
        String tenban = intent.getStringExtra("tenban");
        String ngaydat = intent.getStringExtra("ngaydat");

        TXT_payment_TenBan.setText(tenban);
        TXT_payment_NgayDat.setText(ngaydat);

        //ktra mã bàn tồn tại thì hiển thị
        if(maban !=0 ){
            HienThiThanhToan();

            setTongTien();
        }

        BTN_payment_ThanhToan.setOnClickListener(this);
        IMG_payment_backbtn.setOnClickListener(this);

    }

    private void setTongTien() {
        tongtien = 0;
        for (int i=0;i<thanhToanDTOS.size();i++){
            int soluong = thanhToanDTOS.get(i).getSoLuong();
            int giatien = thanhToanDTOS.get(i).getGiaTien();

            tongtien += (soluong * giatien);
        }
        TXT_payment_TongTien.setText(String.valueOf(tongtien) +" VNĐ");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_payment_ThanhToan:
                boolean ktraban = banAnDAO.CapNhatTinhTrangBan(maban,"false");
                boolean ktradondat = donDatDAO.UpdateTThaiDonTheoMaBan(maban,"true");
                boolean ktratongtien = donDatDAO.UpdateTongTienDonDat(madondat,String.valueOf(tongtien));
                if(ktraban && ktradondat && ktratongtien){
                    HienThiThanhToan();
                    TXT_payment_TongTien.setText("0 VNĐ");
                    Toast.makeText(getApplicationContext(),"Thanh toán thành công!",Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getApplicationContext(),"Lỗi thanh toán!",Toast.LENGTH_SHORT);
                }
                break;

            case R.id.img_payment_backbtn:
                finish();
                break;
        }
    }

    //hiển thị data lên gridview
    private void HienThiThanhToan(){
        madondat = (int) donDatDAO.LayMaDonTheoMaBan(maban,"false");
        thanhToanDTOS = thanhToanDAO.LayDSMonTheoMaDon(madondat);
        adapterDisplayPayment = new AdapterPayment(this,getApplicationContext());
        adapterDisplayPayment.setDta(thanhToanDTOS);
        gvDisplayPayment.setAdapter(adapterDisplayPayment);
    }


    @Override
    public void OnClickBack(ThanhToanDTO thanhToanDTO) {
        int count = thanhToanDTO.getSoLuong();
        if(count == 1) {
            Toast.makeText(getApplicationContext(),"Đã đến giới hạn minimum",Toast.LENGTH_SHORT).show();
        }else {
            count--;
            ChiTietDonDatDTO  chitiethoadon = new ChiTietDonDatDTO();
            chitiethoadon.setMaDonDat(Integer.parseInt(thanhToanDTO.getMadon()));
            chitiethoadon.setMaMon(Integer.parseInt(thanhToanDTO.getMaMon()));
            chitiethoadon.setSoLuong(count);
            chiTietDonDatDAO.CapNhatSL(chitiethoadon);
            thanhToanDTOS = thanhToanDAO.LayDSMonTheoMaDon(madondat);
            adapterDisplayPayment.setDta(thanhToanDTOS);
            setTongTien();
        }
    }

    @Override
    public void OnClickNext(ThanhToanDTO thanhToanDTO) {
        int count = thanhToanDTO.getSoLuong();
        count++;
        ChiTietDonDatDTO  chitiethoadon = new ChiTietDonDatDTO();
        chitiethoadon.setMaDonDat(Integer.parseInt(thanhToanDTO.getMadon()));
        chitiethoadon.setMaMon(Integer.parseInt(thanhToanDTO.getMaMon()));
        chitiethoadon.setSoLuong(count);
        chiTietDonDatDAO.CapNhatSL(chitiethoadon);
        thanhToanDTOS = thanhToanDAO.LayDSMonTheoMaDon(madondat);
        adapterDisplayPayment.setDta(thanhToanDTOS);
        setTongTien();
    }

    @Override
    public void OnLongClick(ThanhToanDTO thanhToanDTO) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Thông báo")
                .setMessage("Bạn muốn xóa món ăn này")
                .setPositiveButton("XÓA", (dialogInterface, i) -> {
                    if (chiTietDonDatDAO.deleteMonAn(thanhToanDTO.getMadon(), thanhToanDTO.getMaMon())) {
                        thanhToanDTOS = thanhToanDAO.LayDSMonTheoMaDon(madondat);
                        adapterDisplayPayment.setDta(thanhToanDTOS);
                        setTongTien();
                    }
                })
                .setNegativeButton("KHÔNG", (dialogInterface, i) -> dialogInterface.dismiss()).setIcon(R.drawable.ic_baseline_warning_24)
                .show();
    }


}