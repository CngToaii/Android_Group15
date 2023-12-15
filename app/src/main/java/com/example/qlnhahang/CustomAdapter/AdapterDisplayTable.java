package com.example.qlnhahang.CustomAdapter;

import static com.example.qlnhahang.Activities.LoginActivity.maNv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.Activities.PaymentActivity;
import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DTO.BanAnDTO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.Fragments.DisplayCategoryFragment;
import com.example.qlnhahang.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AdapterDisplayTable extends BaseAdapter implements View.OnClickListener {

    Context context;
    int layout;
    List<BanAnDTO> banAnDTOList;
    ViewHolder viewHolder;
    BanAnDAO banAnDAO;
    DonDatDAO donDatDAO;
    FragmentManager fragmentManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("BanAn");
    DatabaseReference myRef1 = database.getReference("DonDat");
    public AdapterDisplayTable(Context context, int layout, List<BanAnDTO> banAnDTOList) {
        this.context = context;
        this.layout = layout;
        this.banAnDTOList = banAnDTOList;
        fragmentManager = ((HomeActivity) context).getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return banAnDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return banAnDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return banAnDTOList.get(position).getMaBan();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            view = inflater.inflate(layout, parent, false);

            viewHolder.imgBanAn = (ImageView) view.findViewById(R.id.img_customtable_BanAn);
            viewHolder.imgGoiMon = (ImageView) view.findViewById(R.id.img_customtable_GoiMon);
            viewHolder.imgThanhToan = (ImageView) view.findViewById(R.id.img_customtable_ThanhToan);
            viewHolder.imgAnNut = (ImageView) view.findViewById(R.id.img_customtable_AnNut);
            viewHolder.txtTenBanAn = (TextView) view.findViewById(R.id.txt_customtable_TenBanAn);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (banAnDTOList.get(position).isDuocChon()) {
            HienThiButton();
        } else {
            AnButton();
        }

        BanAnDTO banAnDTO = banAnDTOList.get(position);

        //đổi hình theo tình trạng
        if (banAnDTO.isDuocChon()) {
            viewHolder.imgBanAn.setImageResource(R.drawable.iconfull);
        } else {
            viewHolder.imgBanAn.setImageResource(R.drawable.icontrong);
        }

        viewHolder.txtTenBanAn.setText(banAnDTO.getTenBan());
        viewHolder.imgBanAn.setTag(position);

        //sự kiện click
        viewHolder.imgBanAn.setOnClickListener(this);
        viewHolder.imgGoiMon.setOnClickListener(this);
        viewHolder.imgThanhToan.setOnClickListener(this);
        viewHolder.imgAnNut.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        viewHolder = (ViewHolder) ((View) v.getParent()).getTag();

        int vitri1 = (int) viewHolder.imgBanAn.getTag();

        int maban = banAnDTOList.get(vitri1).getMaBan();
        String tenban = banAnDTOList.get(vitri1).getTenBan();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ngaydat = dateFormat.format(calendar.getTime());

//        switch (id){
//            case R.id.img_customtable_BanAn:
        if (id == R.id.img_customtable_BanAn) {
            int vitri = (int) v.getTag();
//            banAnDTOList.get(vitri).setDuocChon(true);
            HienThiButton();
//                break;
//
//            case R.id.img_customtable_AnNut:
        } else if (id == R.id.img_customtable_AnNut) {
            AnButton();
//                break;
//
//            case R.id.img_customtable_GoiMon:
        } else if (id == R.id.img_customtable_GoiMon) {
//            Intent getIHome = ((HomeActivity) context).getIntent();
            int manv = maNv;
            Log.d("vitri>>>>>>>", vitri1+"");
            DonDatDTO donDatDTO = new DonDatDTO();
            Log.d("tinhtrang", banAnDTOList.get(vitri1).getTenBan()+"");
            if (!banAnDTOList.get(vitri1).isDuocChon()) {
                //Thêm bảng gọi món và update tình trạng bàn
                Log.d("tinhtrang", "vào đây");

                donDatDTO.setMaDonDat(new Random().nextInt(1000000));
                donDatDTO.setMaBan(maban);
                donDatDTO.setMaNV(manv);
                donDatDTO.setNgayDat(ngaydat);
                donDatDTO.setTinhTrang("false");
                donDatDTO.setTongTien("0");
                myRef1.child(donDatDTO.getMaBan()+"").child(donDatDTO.getMaDonDat()+"").setValue(donDatDTO);
                myRef.child(String.valueOf(maban)).child("duocChon").setValue(true);
            }
            //chuyển qua trang category
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            DisplayCategoryFragment displayCategoryFragment = new DisplayCategoryFragment();

            Bundle bDataCategory = new Bundle();
            bDataCategory.putInt("maban", maban);
            bDataCategory.putInt("madon", donDatDTO.getMaDonDat());
            displayCategoryFragment.setArguments(bDataCategory);

            transaction.replace(R.id.contentView, displayCategoryFragment).addToBackStack("hienthibanan");
            transaction.commit();
        }
        if (id == R.id.img_customtable_ThanhToan) {
            //chuyển dữ liệu qua trang thanh toán
            Intent iThanhToan = new Intent(context, PaymentActivity.class);
            iThanhToan.putExtra("maban", maban);
            iThanhToan.putExtra("tenban", tenban);
            iThanhToan.putExtra("ngaydat", ngaydat);
            context.startActivity(iThanhToan);

        }
    }

    private void HienThiButton() {
        viewHolder.imgGoiMon.setVisibility(View.VISIBLE);
        viewHolder.imgThanhToan.setVisibility(View.VISIBLE);
        viewHolder.imgAnNut.setVisibility(View.VISIBLE);
    }

    private void AnButton() {
        viewHolder.imgGoiMon.setVisibility(View.INVISIBLE);
        viewHolder.imgThanhToan.setVisibility(View.INVISIBLE);
        viewHolder.imgAnNut.setVisibility(View.INVISIBLE);
    }

    public class ViewHolder {
        ImageView imgBanAn, imgGoiMon, imgThanhToan, imgAnNut;
        TextView txtTenBanAn;
    }
}
