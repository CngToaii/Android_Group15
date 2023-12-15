package com.example.qlnhahang.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DAO.NhanVienDAO;
import com.example.qlnhahang.DTO.BanAnDTO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.R;

import java.util.List;

public class AdapterDisplayStatistic extends BaseAdapter {

    Context context;
    int layout;
    List<DonDatDTO> donDatDTOS;
    List<NhanVienDTO> nhanVienDTOS;
    List<BanAnDTO> banAnDTOS;
    ViewHolder viewHolder;
    NhanVienDAO nhanVienDAO;
    BanAnDAO banAnDAO;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("BanAn");
    DatabaseReference myRef1 = database.getReference("NhanVien");

    public AdapterDisplayStatistic(Context context, int layout, List<DonDatDTO> donDatDTOS, List<NhanVienDTO> nhanVienDTOS, List<BanAnDTO> banAnDTOS) {
        this.context = context;
        this.layout = layout;
        this.donDatDTOS = donDatDTOS;
        this.nhanVienDTOS = nhanVienDTOS;
        this.banAnDTOS = banAnDTOS;
    }

    @Override
    public int getCount() {
        return donDatDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return donDatDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);

            viewHolder.txt_customstatistic_MaDon = (TextView) view.findViewById(R.id.txt_customstatistic_MaDon);
            viewHolder.txt_customstatistic_NgayDat = (TextView) view.findViewById(R.id.txt_customstatistic_NgayDat);
            viewHolder.txt_customstatistic_TenNV = (TextView) view.findViewById(R.id.txt_customstatistic_TenNV);
            viewHolder.txt_customstatistic_TongTien = (TextView) view.findViewById(R.id.txt_customstatistic_TongTien);
            viewHolder.txt_customstatistic_TrangThai = (TextView) view.findViewById(R.id.txt_customstatistic_TrangThai);
            viewHolder.txt_customstatistic_BanDat = (TextView) view.findViewById(R.id.txt_customstatistic_BanDat);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DonDatDTO donDatDTO = donDatDTOS.get(position);

        viewHolder.txt_customstatistic_MaDon.setText("Mã đơn: " + donDatDTO.getMaDonDat());
        viewHolder.txt_customstatistic_NgayDat.setText(donDatDTO.getNgayDat());
        viewHolder.txt_customstatistic_TongTien.setText(donDatDTO.getTongTien() + " VNĐ");
        if (donDatDTO.getTinhTrang().equals("true")) {
            viewHolder.txt_customstatistic_TrangThai.setText("Đã thanh toán");
        } else {
            viewHolder.txt_customstatistic_TrangThai.setText("Chưa thanh toán");
        }
//        NhanVienDTO nhanVienDTO = nhanVienDAO.LayNVTheoMa(donDatDTO.getMaNV());
        for (int i = 0; i < nhanVienDTOS.size(); i++) {
            if (donDatDTO.getMaNV() == nhanVienDTOS.get(i).getMANV()) {
                viewHolder.txt_customstatistic_TenNV.setText(nhanVienDTOS.get(i).getHOTENNV());
            }
        }
//        viewHolder.txt_customstatistic_BanDat.setText(banAnDAO.LayTenBanTheoMa(donDatDTO.getMaBan()));

        for (int i = 0; i < banAnDTOS.size(); i++) {
            if (donDatDTO.getMaBan() == banAnDTOS.get(i).getMaBan()) {
                viewHolder.txt_customstatistic_BanDat.setText(banAnDTOS.get(i).getTenBan());
            }
        }
        return view;
    }

    public class ViewHolder {
        TextView txt_customstatistic_MaDon, txt_customstatistic_NgayDat, txt_customstatistic_TenNV, txt_customstatistic_TongTien, txt_customstatistic_TrangThai, txt_customstatistic_BanDat;

    }
}