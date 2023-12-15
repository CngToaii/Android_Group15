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
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.Activities.AddTableActivity;
import com.example.qlnhahang.Activities.EditTableActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.CustomAdapter.AdapterDisplayTable;
import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DTO.BanAnDTO;
import com.example.qlnhahang.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayTableFragment extends Fragment {

    GridView GVDisplayTable;
    List<BanAnDTO> banAnDTOList;
    BanAnDAO banAnDAO;
    AdapterDisplayTable adapterDisplayTable;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("BanAn");

    //Dùng activity result (activityforresult ko hổ trợ nữa) để nhận data gửi từ activity addtable
    ActivityResultLauncher<Intent> resultLauncherAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        boolean ktra = true;
                        HienThiDSBan();
                        Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    ActivityResultLauncher<Intent> resultLauncherEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        boolean ktra = true;
                        HienThiDSBan();
                        Toast.makeText(getActivity(), getResources().getString(R.string.edit_sucessful), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaytable_layout, container, false);
        setHasOptionsMenu(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Quản lý bàn");

        GVDisplayTable = (GridView) view.findViewById(R.id.gvDisplayTable);
        banAnDTOList    = new ArrayList<>();
        HienThiDSBan();

        registerForContextMenu(GVDisplayTable);
        return view;
    }

    //tạo ra context menu khi longclick
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu, menu);
    }

    //Xử lí cho từng trường hợp trong contextmenu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        BanAnDTO banAnDTO = banAnDTOList.get(vitri);
//        switch(id){
//            case R.id.itEdit:
        if (id == R.id.itEdit) {
            Intent intent = new Intent(getActivity(), EditTableActivity.class);
            intent.putExtra("banan", banAnDTO);
            resultLauncherEdit.launch(intent);
//                break;
//
//            case R.id.itDelete:
        } else if (id == R.id.itDelete) {
//                boolean ktraxoa = banAnDAO.XoaBanTheoMa(maban);
            myRef.child(String.valueOf(banAnDTO.getMaBan())).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    HienThiDSBan();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_sucessful), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddTable = menu.add(1, R.id.itAddTable, 1, R.string.addTable);
        itAddTable.setIcon(R.drawable.ic_baseline_add_24);
        itAddTable.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        int id = item.getItemId();
//        switch (id){
//            case R.id.itAddTable:
        if (id == R.id.itAddTable) {
            Intent iAddTable = new Intent(getActivity(), AddTableActivity.class);
            resultLauncherAdd.launch(iAddTable);
        }

        return super.onOptionsItemSelected(item);
    }

    private void HienThiDSBan () {
//        banAnDTOList = banAnDAO.LayTatCaBanAn();
        myRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            banAnDTOList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BanAnDTO banAnDTO = dataSnapshot.getValue(BanAnDTO.class);
                                banAnDTOList.add(banAnDTO);
                            }
                            adapterDisplayTable = new AdapterDisplayTable(getActivity(), R.layout.custom_layout_displaytable, banAnDTOList);
                            GVDisplayTable.setAdapter(adapterDisplayTable);
                            adapterDisplayTable.notifyDataSetChanged();
                        }else{
                            banAnDTOList.clear();
                            adapterDisplayTable = new AdapterDisplayTable(getActivity(), R.layout.custom_layout_displaytable, banAnDTOList);
                            GVDisplayTable.setAdapter(adapterDisplayTable);
                            adapterDisplayTable.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
