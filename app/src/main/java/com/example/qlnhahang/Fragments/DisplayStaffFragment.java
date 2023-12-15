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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.Activities.AddStaffActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.CustomAdapter.AdapterDisplayStaff;
import com.example.qlnhahang.DAO.NhanVienDAO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayStaffFragment extends Fragment {

    GridView gvStaff;
    NhanVienDAO nhanVienDAO;
    List<NhanVienDTO> nhanVienDTOS = new ArrayList<>();
    AdapterDisplayStaff adapterDisplayStaff;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("NhanVien");
    ActivityResultLauncher<Intent> resultLauncherAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        long ktra = intent.getLongExtra("ketquaktra",0);
                        String chucnang = intent.getStringExtra("chucnang");
                        if(chucnang.equals("themnv"))
                        {
                            HienThiDSNV();
                            Toast.makeText(getActivity(),"Thêm thành công",Toast.LENGTH_SHORT).show();

                        }else {
                            HienThiDSNV();
                            Toast.makeText(getActivity(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaystaff_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý nhân viên");
        setHasOptionsMenu(true);

        gvStaff = (GridView)view.findViewById(R.id.gvStaff) ;

        nhanVienDAO = new NhanVienDAO(getActivity());
        HienThiDSNV();

        registerForContextMenu(gvStaff);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int manv = nhanVienDTOS.get(vitri).getMANV();

//        switch (id){
//            case R.id.itEdit:
//                Intent iEdit = new Intent(getActivity(),AddStaffActivity.class);
//                iEdit.putExtra("manv",manv);
//                resultLauncherAdd.launch(iEdit);
//                break;
//
//            case R.id.itDelete:
//                boolean ktra = nhanVienDAO.XoaNV(manv);
//                if(ktra){
//                    HienThiDSNV();
//                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_sucessful)
//                            ,Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_failed)
//                            ,Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
        if(id == R.id.itEdit){
            Intent iEdit = new Intent(getActivity(),AddStaffActivity.class);
            iEdit.putExtra("manv",manv);
            resultLauncherAdd.launch(iEdit);
        }else if(id == R.id.itDelete){
            Query query = myRef.orderByChild("manv").equalTo(manv);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(com.google.firebase.database.DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue();
                    }
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_sucessful)
                            ,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_failed)
                            ,Toast.LENGTH_SHORT).show();
                }
            });
//            if(ktra){
//                HienThiDSNV();
//                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_sucessful)
//                        ,Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_failed)
//                        ,Toast.LENGTH_SHORT).show();
//            }
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddStaff = menu.add(1,R.id.itAddStaff,1,"Thêm nhân viên");
        itAddStaff.setIcon(R.drawable.ic_baseline_add_24);
        itAddStaff.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        switch (id){
//            case R.id.itAddStaff:
//                Intent iDangky = new Intent(getActivity(), AddStaffActivity.class);
//                resultLauncherAdd.launch(iDangky);
//                break;
//        }
        if(id == R.id.itAddStaff){
            Intent iDangky = new Intent(getActivity(), AddStaffActivity.class);
            resultLauncherAdd.launch(iDangky);
        }
        return super.onOptionsItemSelected(item);
    }

    private void HienThiDSNV(){
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nhanVienDTOS.clear();
                    for(com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        NhanVienDTO nhanVienDTO = dataSnapshot1.getValue(NhanVienDTO.class);
                        nhanVienDTOS.add(nhanVienDTO);
                    }
                    adapterDisplayStaff = new AdapterDisplayStaff(getActivity(),R.layout.custom_layout_displaystaff,nhanVienDTOS);
                    gvStaff.setAdapter(adapterDisplayStaff);
                    adapterDisplayStaff.notifyDataSetChanged();
                }else{
                    nhanVienDTOS.clear();
                    adapterDisplayStaff = new AdapterDisplayStaff(getActivity(),R.layout.custom_layout_displaystaff,nhanVienDTOS);
                    gvStaff.setAdapter(adapterDisplayStaff);
                    adapterDisplayStaff.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {

            }
        });
    }
}

