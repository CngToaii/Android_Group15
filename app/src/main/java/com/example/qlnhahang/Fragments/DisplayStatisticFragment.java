package com.example.qlnhahang.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.qlnhahang.Activities.DetailStatisticActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.Activities.StaticThongKe;
import com.example.qlnhahang.CustomAdapter.AdapterDisplayStatistic;
import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DTO.BanAnDTO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayStatisticFragment extends Fragment {

    ListView lvStatistic;
    List<DonDatDTO> donDatDTOS = new ArrayList<>();
    List<BanAnDTO> banAnDTOS = new ArrayList<>();
    List<NhanVienDTO> nhanVienDTOS = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    DonDatDAO donDatDAO;
    AdapterDisplayStatistic adapterDisplayStatistic;
    FragmentManager fragmentManager;
    int madon, manv, maban;
    String ngaydat, tongtien;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("DonDat");
    DatabaseReference myRef2 = database.getReference("BanAn");
    DatabaseReference myRef1 = database.getReference("NhanVien");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaystatistic_layout, container, false);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Quản lý thống kê");
        setHasOptionsMenu(true);

        lvStatistic = (ListView) view.findViewById(R.id.lvStatistic);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.btnstatic);
        floatingActionButton.setOnClickListener(views -> {
            Intent intent = new Intent(getContext(), StaticThongKe.class);
            startActivity(intent);
        });

//        donDatDTOS = donDatDAO.LayDSDonDat();
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    donDatDTOS.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    DonDatDTO donDatDTO = postSnapshot.getValue(DonDatDTO.class);
//                    donDatDTOS.add(donDatDTO);
                        myRef.child(postSnapshot.getKey()+"").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        DonDatDTO donDatDTO = postSnapshot.getValue(DonDatDTO.class);
                                        donDatDTOS.add(donDatDTO);
                                    }
                                    myRef2.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                                        @Override
                                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                banAnDTOS.clear();
                                                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    BanAnDTO banAnDTO = postSnapshot.getValue(BanAnDTO.class);
                                                    for (int i = 0; i < donDatDTOS.size(); i++) {
                                                        if (donDatDTOS.get(i).getMaBan() == banAnDTO.getMaBan()) {
                                                            banAnDTOS.add(banAnDTO);
                                                        }
                                                    }
                                                }
                                                myRef1.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            nhanVienDTOS.clear();
                                                            for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                NhanVienDTO nhanVienDTO = postSnapshot.getValue(NhanVienDTO.class);
                                                                for (int i = 0; i < donDatDTOS.size(); i++) {
                                                                    if (donDatDTOS.get(i).getMaNV() == nhanVienDTO.getMANV()) {
                                                                        nhanVienDTOS.add(nhanVienDTO);
                                                                    }
                                                                }
                                                            }
                                                            Log.d("kiemtra>>>>", donDatDTOS.get(0).getTinhTrang() + "");
                                                            adapterDisplayStatistic = new AdapterDisplayStatistic(getActivity(), R.layout.custom_layout_displaystatistic, donDatDTOS, nhanVienDTOS,banAnDTOS);
                                                            lvStatistic.setAdapter(adapterDisplayStatistic);
                                                            adapterDisplayStatistic.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        lvStatistic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                madon = donDatDTOS.get(position).getMaDonDat();
                manv = donDatDTOS.get(position).getMaNV();
                maban = donDatDTOS.get(position).getMaBan();
                ngaydat = donDatDTOS.get(position).getNgayDat();
                tongtien = donDatDTOS.get(position).getTongTien();

                Intent intent = new Intent(getActivity(), DetailStatisticActivity.class);
                intent.putExtra("madon", madon);
                intent.putExtra("manv", manv);
                intent.putExtra("maban", maban);
                intent.putExtra("ngaydat", ngaydat);
                intent.putExtra("tongtien", tongtien);
                startActivity(intent);
            }
        });

        return view;
    }
}

