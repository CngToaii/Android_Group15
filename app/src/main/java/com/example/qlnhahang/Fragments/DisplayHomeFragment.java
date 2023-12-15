package com.example.qlnhahang.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.Activities.AddCategoryActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.CustomAdapter.AdapterRecycleViewCategory;
import com.example.qlnhahang.CustomAdapter.AdapterRecycleViewStatistic;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DAO.LoaiMonDAO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.DTO.LoaiMonDTO;
import com.example.qlnhahang.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DisplayHomeFragment extends Fragment implements View.OnClickListener {

    RecyclerView rcv_displayhome_LoaiMon, rcv_displayhome_DonTrongNgay;
    RelativeLayout layout_displayhome_ThongKe, layout_displayhome_XemBan, layout_displayhome_XemMenu, layout_displayhome_XemNV;
    TextView txt_displayhome_ViewAllCategory, txt_displayhome_ViewAllStatistic;
    LoaiMonDAO loaiMonDAO;
    DonDatDAO donDatDAO;
    ImageView imgbanner;
    List<LoaiMonDTO> loaiMonDTOList = new ArrayList<>();
    List<DonDatDTO> donDatDTOS = new ArrayList<>();
    AdapterRecycleViewCategory adapterRecycleViewCategory;
    AdapterRecycleViewStatistic adapterRecycleViewStatistic;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("LoaiMon");
    private DatabaseReference myRef2 = database.getReference("DonDat");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displayhome_layout, container, false);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Trang chủ");
        setHasOptionsMenu(true);

        //region Lấy dối tượng view

        rcv_displayhome_LoaiMon = (RecyclerView) view.findViewById(R.id.rcv_displayhome_LoaiMon);
        rcv_displayhome_DonTrongNgay = (RecyclerView) view.findViewById(R.id.rcv_displayhome_DonTrongNgay);
        layout_displayhome_ThongKe = (RelativeLayout) view.findViewById(R.id.layout_displayhome_ThongKe);
        layout_displayhome_XemBan = (RelativeLayout) view.findViewById(R.id.layout_displayhome_XemBan);
        layout_displayhome_XemMenu = (RelativeLayout) view.findViewById(R.id.layout_displayhome_XemMenu);
        layout_displayhome_XemNV = (RelativeLayout) view.findViewById(R.id.layout_displayhome_XemNV);
        txt_displayhome_ViewAllCategory = (TextView) view.findViewById(R.id.txt_displayhome_ViewAllCategory);
        txt_displayhome_ViewAllStatistic = (TextView) view.findViewById(R.id.txt_displayhome_ViewAllStatistic);
        //endregion

        //khởi tạo kết nối
        loaiMonDAO = new LoaiMonDAO(getActivity());
        donDatDAO = new DonDatDAO(getActivity());

        HienThiDSLoai();
        HienThiDonTrongNgay();

        layout_displayhome_ThongKe.setOnClickListener(this);
        layout_displayhome_XemBan.setOnClickListener(this);
        layout_displayhome_XemMenu.setOnClickListener(this);
        layout_displayhome_XemNV.setOnClickListener(this);
        txt_displayhome_ViewAllCategory.setOnClickListener(this);
        txt_displayhome_ViewAllStatistic.setOnClickListener(this);

        return view;
    }




    private void HienThiDSLoai() {
        rcv_displayhome_LoaiMon.setHasFixedSize(true);
        rcv_displayhome_LoaiMon.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loaiMonDTOList.clear();
                    for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        LoaiMonDTO loaiMonDTO = dataSnapshot1.getValue(LoaiMonDTO.class);
                        loaiMonDTOList.add(loaiMonDTO);
                    }
                    adapterRecycleViewCategory = new AdapterRecycleViewCategory(getActivity(), R.layout.custom_layout_displaycategory, loaiMonDTOList);
                    rcv_displayhome_LoaiMon.setAdapter(adapterRecycleViewCategory);
                    adapterRecycleViewCategory.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {

            }
        });
        adapterRecycleViewCategory = new AdapterRecycleViewCategory(getActivity(), R.layout.custom_layout_displaycategory, loaiMonDTOList);
        rcv_displayhome_LoaiMon.setAdapter(adapterRecycleViewCategory);
        adapterRecycleViewCategory.notifyDataSetChanged();
    }

    private void HienThiDonTrongNgay() {
        rcv_displayhome_DonTrongNgay.setHasFixedSize(true);
        rcv_displayhome_DonTrongNgay.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ngaydat = dateFormat.format(calendar.getTime());

//        donDatDTOS = donDatDAO.LayDSDonDatNgay(ngaydat);
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    donDatDTOS.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        DonDatDTO donDatDTO = ds.getChildren().iterator().next().getValue(DonDatDTO.class);
                        if (donDatDTO.getNgayDat().equals(ngaydat)) {
                            donDatDTOS.add(donDatDTO);
                        }
                    }
                    for(int i = 0; i < donDatDTOS.size(); i++){
                        Log.d("donDatDTOS", String.valueOf(donDatDTOS.get(i).getMaDonDat()));
                    }
                    if(donDatDTOS.size() != 0) {
                        adapterRecycleViewStatistic = new AdapterRecycleViewStatistic(getActivity(), R.layout.custom_layout_displaystatistic, donDatDTOS);
                        rcv_displayhome_DonTrongNgay.setAdapter(adapterRecycleViewStatistic);
                        adapterRecycleViewCategory.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view_trangchu);
//        switch (id){
//            case R.id.layout_displayhome_ThongKe:
        if (id == R.id.layout_displayhome_ThongKe) {
        }

//            case R.id.txt_displayhome_ViewAllStatistic:
        if (id == R.id.txt_displayhome_ViewAllStatistic) {
            FragmentTransaction tranDisplayStatistic = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayStatistic.replace(R.id.contentView, new DisplayStatisticFragment());
            tranDisplayStatistic.addToBackStack(null);
            tranDisplayStatistic.commit();
            navigationView.setCheckedItem(R.id.nav_statistic);
        }
//            case R.id.layout_displayhome_XemBan:
        if (id == R.id.layout_displayhome_XemBan) {
            FragmentTransaction tranDisplayTable = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayTable.replace(R.id.contentView, new DisplayTableFragment());
            tranDisplayTable.addToBackStack(null);
            tranDisplayTable.commit();
            navigationView.setCheckedItem(R.id.nav_table);
        }
//                break;
//
//            case R.id.layout_displayhome_XemMenu:
        if (id == R.id.layout_displayhome_XemMenu) {
            Intent iAddCategory = new Intent(getActivity(), AddCategoryActivity.class);
            startActivity(iAddCategory);
            navigationView.setCheckedItem(R.id.nav_category);
        }
//
//                break;
//            case R.id.layout_displayhome_XemNV:
        if (id == R.id.layout_displayhome_XemNV) {
            FragmentTransaction tranDisplayStaff = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayStaff.replace(R.id.contentView, new DisplayStaffFragment());
            tranDisplayStaff.addToBackStack(null);
            tranDisplayStaff.commit();
            navigationView.setCheckedItem(R.id.nav_staff);
        }
//                break;
//
//            case R.id.txt_displayhome_ViewAllCategory:
        if (id == R.id.txt_displayhome_ViewAllCategory) {
            FragmentTransaction tranDisplayCategory = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayCategory.replace(R.id.contentView, new DisplayCategoryFragment());
            tranDisplayCategory.addToBackStack(null);
            tranDisplayCategory.commit();
            navigationView.setCheckedItem(R.id.nav_category);

        }
    }
}

