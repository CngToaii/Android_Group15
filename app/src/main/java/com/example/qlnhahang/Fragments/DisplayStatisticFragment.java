package com.example.qlnhahang.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.qlnhahang.Activities.BillView;
import com.example.qlnhahang.Activities.DetailStatisticActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.Activities.StaticThongKe;
import com.example.qlnhahang.CustomAdapter.AdapterDisplayStatistic;
import com.example.qlnhahang.CustomAdapter.IonclickItem;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.R;

import java.util.List;

public class DisplayStatisticFragment extends Fragment implements IonclickItem {

    ListView lvStatistic;
    List<DonDatDTO> donDatDTOS;
    FloatingActionButton floatingActionButton;
    DonDatDAO donDatDAO;
    AdapterDisplayStatistic adapterDisplayStatistic;
    FragmentManager fragmentManager;
    int madon, manv, maban;
    String ngaydat, tongtien;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaystatistic_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý thống kê");
        setHasOptionsMenu(true);

        lvStatistic = (ListView)view.findViewById(R.id.lvStatistic);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.btnstatic) ;
        floatingActionButton.setOnClickListener(views->{
            Intent intent = new Intent(getContext(), StaticThongKe.class);
            startActivity(intent);
        });
        donDatDAO = new DonDatDAO(getActivity());

        donDatDTOS = donDatDAO.LayDSDonDat();
        adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(),R.layout.custom_layout_displaystatistic,donDatDTOS,this);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();

        lvStatistic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                madon = donDatDTOS.get(position).getMaDonDat();
                manv = donDatDTOS.get(position).getMaNV();
                maban = donDatDTOS.get(position).getMaBan();
                ngaydat = donDatDTOS.get(position).getNgayDat();
                tongtien = donDatDTOS.get(position).getTongTien();

                Intent intent = new Intent(getActivity(), DetailStatisticActivity.class);
                intent.putExtra("madon",madon);
                intent.putExtra("manv",manv);
                intent.putExtra("maban",maban);
                intent.putExtra("ngaydat",ngaydat);
                intent.putExtra("tongtien",tongtien);
                startActivity(intent);
            }
        });
//        lvStatistic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                madon = donDatDTOS.get(position).getMaDonDat();
//                manv = donDatDTOS.get(position).getMaNV();
//                maban = donDatDTOS.get(position).getMaBan();
//                ngaydat = donDatDTOS.get(position).getNgayDat();
//                tongtien = donDatDTOS.get(position).getTongTien();
//
//                Intent intent = new Intent(getActivity(), BillView.class);
//                intent.putExtra("madon",madon);
//                intent.putExtra("manv",manv);
//                intent.putExtra("maban",maban);
//                intent.putExtra("ngaydat",ngaydat);
//                intent.putExtra("tongtien",tongtien);
//                startActivity(intent);
//                return false;
//            }
//        });
        return view;
    }

    @Override
    public void onClick(DonDatDTO donDatDTO) {
        Intent intent = new Intent(getActivity(), BillView.class);
                intent.putExtra("madon",donDatDTO.getMaDonDat());
                intent.putExtra("manv",donDatDTO.getMaNV());
                intent.putExtra("maban",donDatDTO.getMaBan());
                intent.putExtra("ngaydat",donDatDTO.getNgayDat());
                intent.putExtra("tongtien",donDatDTO.getTongTien());
                startActivity(intent);
    }
}
