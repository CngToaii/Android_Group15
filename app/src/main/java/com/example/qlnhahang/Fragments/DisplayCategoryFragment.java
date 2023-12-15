package com.example.qlnhahang.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.example.qlnhahang.Activities.AddCategoryActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.CustomAdapter.AdapterDisplayCategory;
import com.example.qlnhahang.DAO.LoaiMonDAO;
import com.example.qlnhahang.DTO.LoaiMonDTO;
import com.example.qlnhahang.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayCategoryFragment extends Fragment {

    GridView gvCategory;
    List<LoaiMonDTO> loaiMonDTOList = new ArrayList<>();
    LoaiMonDAO loaiMonDAO;
    AdapterDisplayCategory adapter;
    FragmentManager fragmentManager;
    int maban, madon;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("LoaiMon");

    ActivityResultLauncher<Intent> resultLauncherCategory = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        boolean ktra = true;
                        String chucnang = intent.getStringExtra("chucnang");
                        if (chucnang.equals("themloai")) {
                            HienThiDSLoai();
                            Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            HienThiDSLoai();
                            Toast.makeText(getActivity(), "Sủa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.displaycategory_layout, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Quản lý thực đơn");

        gvCategory = (GridView) view.findViewById(R.id.gvCategory);

        fragmentManager = getActivity().getSupportFragmentManager();

        loaiMonDAO = new LoaiMonDAO(getActivity());
        HienThiDSLoai();

        Bundle bDataCategory = getArguments();
        if (bDataCategory != null) {
            maban = bDataCategory.getInt("maban");
            madon = bDataCategory.getInt("madon");
        }

        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int maloai = loaiMonDTOList.get(position).getMaLoai();
                String tenloai = loaiMonDTOList.get(position).getTenLoai();
                DisplayMenuFragment displayMenuFragment = new DisplayMenuFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("maloai", maloai);
                bundle.putString("tenloai", tenloai);
                bundle.putInt("maban", maban);
                bundle.putInt("madon", madon);
                displayMenuFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contentView, displayMenuFragment).addToBackStack("hienthiloai");
                transaction.commit();
            }
        });

        registerForContextMenu(gvCategory);

        return view;
    }

    //hiển thị contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }

    //xử lí context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int maloai = loaiMonDTOList.get(vitri).getMaLoai();

//        switch (id){
//            case R.id.itEdit:
        if (id == R.id.itDelete) {
            Query query = myRef.orderByChild("maLoai").equalTo(maloai);
            query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                    for (com.google.firebase.database.DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dataSnapshot.getRef().removeValue();
                    }
                    Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            });

        }

        return true;
    }

    //khởi tạo nút thêm loại
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddCategory = menu.add(1, R.id.itAddCategory, 1, R.string.addCategory);
        itAddCategory.setIcon(R.drawable.ic_baseline_add_24);
        itAddCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    //xử lý nút thêm loại
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        switch (id) {
//            case R.id.itAddCategory:
        if (id == R.id.itAddCategory) {
            Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
            resultLauncherCategory.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //hiển thị dữ liệu trên gridview
    private void HienThiDSLoai() {
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loaiMonDTOList.clear();
                    for (com.google.firebase.database.DataSnapshot data : dataSnapshot.getChildren()) {
                        LoaiMonDTO loaiMonDTO = data.getValue(LoaiMonDTO.class);
                        loaiMonDTOList.add(loaiMonDTO);
                    }
                    adapter = new AdapterDisplayCategory(getActivity(), R.layout.custom_layout_displaycategory, loaiMonDTOList);
                    gvCategory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {

            }
        });

    }
}