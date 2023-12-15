package com.example.qlnhahang.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.qlnhahang.Activities.AddMenuActivity;
import com.example.qlnhahang.Activities.AmountMenuActivity;
import com.example.qlnhahang.Activities.HomeActivity;
import com.example.qlnhahang.CustomAdapter.AdapterDisplayMenu;
import com.example.qlnhahang.DAO.MonDAO;
import com.example.qlnhahang.DTO.MonDTO;
import com.example.qlnhahang.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayMenuFragment extends Fragment {

    int maloai, maban, madon;
    String tenloai,tinhtrang;
    GridView gvDisplayMenu;
    MonDAO monDAO;
    List<MonDTO> monDTOList = new ArrayList<>();
    AdapterDisplayMenu adapterDisplayMenu;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef1 = database.getReference("Mon");
    ActivityResultLauncher<Intent> resultLauncherMenu = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        boolean ktra = true;
                        String chucnang = intent.getStringExtra("chucnang");
                        if(chucnang.equals("themmon"))
                        {
                            HienThiDSMon();
                            Toast.makeText(getActivity(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                        }else {
                            if(ktra){
                                HienThiDSMon();
                                Toast.makeText(getActivity(),"Sửa thành công",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"Sửa thất bại",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.displaymenu_layout,container,false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Quản lý thực đơn");
        monDAO = new MonDAO(getActivity());

        gvDisplayMenu = (GridView)view.findViewById(R.id.gvDisplayMenu);

        Bundle bundle = getArguments();
        if(bundle !=null){
            maloai = bundle.getInt("maloai");
            tenloai = bundle.getString("tenloai");
            maban = bundle.getInt("maban");
            madon = bundle.getInt("madon");
            HienThiDSMon();

            gvDisplayMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //nếu lấy đc mã bàn mới mở
                    tinhtrang = monDTOList.get(position).getTinhTrang();
                    if(maban != 0){
                        if(tinhtrang.equals("true")){
                            Intent iAmount = new Intent(getActivity(), AmountMenuActivity.class);
                            iAmount.putExtra("maban",maban);
                            iAmount.putExtra("mamon",monDTOList.get(position).getMaMon());
                            iAmount.putExtra("madon",madon);
                            startActivity(iAmount);
                        }else {
                            Toast.makeText(getActivity(),"Món đã hết, không thể thêm", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        setHasOptionsMenu(true);
        registerForContextMenu(gvDisplayMenu);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    getParentFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });

        return view;
    }

    //tạo 1 menu context show lựa chọn
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    //Tạo phần sửa và xóa trong menu context
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int mamon = monDTOList.get(vitri).getMaMon();

//        switch (id){
//            case R.id.itEdit:
        if (id == R.id.itEdit) {
            Intent iEdit = new Intent(getActivity(), AddMenuActivity.class);
            iEdit.putExtra("mamon",mamon);
            iEdit.putExtra("maLoai",maloai);
            iEdit.putExtra("tenLoai",tenloai);
            resultLauncherMenu.launch(iEdit);
//                break;
//
//            case R.id.itDelete:
        } else if (id == R.id.itDelete) {
//                boolean ktra = monDAO.XoaMon(mamon);
//                if(ktra){
//                    HienThiDSMon();
//                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_sucessful)
//                            ,Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.delete_failed)
//                            ,Toast.LENGTH_SHORT).show();
//                }
            Query query = myRef1.orderByChild("maMon").equalTo(mamon);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
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
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itAddMenu = menu.add(1,R.id.itAddMenu,1,R.string.addMenu);
        itAddMenu.setIcon(R.drawable.ic_baseline_add_24);
        itAddMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        switch (id){
//            case R.id.itAddMenu:
        if (id == R.id.itAddMenu) {
            Intent intent = new Intent(getActivity(), AddMenuActivity.class);
            intent.putExtra("maLoai",maloai);
            intent.putExtra("tenLoai",tenloai);
            resultLauncherMenu.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void HienThiDSMon(){
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monDTOList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MonDTO monDTO = dataSnapshot.getValue(MonDTO.class);
                    if(monDTO.getMaLoai() == maloai){
                        monDTOList.add(monDTO);
                    }
                }
                adapterDisplayMenu = new AdapterDisplayMenu(getActivity(),R.layout.custom_layout_displaymenu,monDTOList);
                gvDisplayMenu.setAdapter(adapterDisplayMenu);
                adapterDisplayMenu.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}