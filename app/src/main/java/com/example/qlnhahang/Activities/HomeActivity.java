package com.example.qlnhahang.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.qlnhahang.Activityprofile;
import com.example.qlnhahang.Fragments.DisplayCategoryFragment;
import com.example.qlnhahang.Fragments.DisplayHomeFragment;
import com.example.qlnhahang.Fragments.DisplayStaffFragment;
import com.example.qlnhahang.Fragments.DisplayStatisticFragment;
import com.example.qlnhahang.Fragments.DisplayTableFragment;
import com.example.qlnhahang.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MenuItem selectedFeature, selectedManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    TextView TXT_menu_tennv;
    int maquyen = 0;
    SharedPreferences sharedPreferences;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Quyen");
    private DatabaseReference myRef1 = database.getReference("NhanVien");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //region thuộc tính bên view
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view_trangchu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        View view = navigationView.getHeaderView(0);
        TXT_menu_tennv = (TextView) view.findViewById(R.id.txt_menu_tennv);
        //endregion

        //xử lý toolbar và navigation
        setSupportActionBar(toolbar); //tạo toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //tạo nút mở navigation
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar
                ,R.string.opentoggle,R.string.closetoggle){
            @Override
            public void onDrawerOpened(View drawerView) {    super.onDrawerOpened(drawerView); }

            @Override
            public void onDrawerClosed(View drawerView) {    super.onDrawerClosed(drawerView); }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Tụ động gán tên nv đăng nhập qua Extras
        Intent intent = getIntent();
        String tendn = intent.getStringExtra("tendn");
        TXT_menu_tennv.setText( "Xin chào " + tendn +" !!");

        //lấy file share prefer
        sharedPreferences = getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);

        //hiện thị fragment home mặc định
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
        DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
        tranDisplayHome.replace(R.id.contentView, displayHomeFragment);
        tranDisplayHome.commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
//        switch (id){
//            case R.id.nav_home:
//                //hiển thị tương ứng trên navigation
//                FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
//                DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
//                tranDisplayHome.replace(R.id.contentView,displayHomeFragment);
//                tranDisplayHome.commit();
//                navigationView.setCheckedItem(item.getItemId());
//                drawerLayout.closeDrawers();
//                break;
//
//            case R.id.nav_statistic:
//                //hiển thị tương ứng trên navigation
//                FragmentTransaction tranDisplayStatistic = fragmentManager.beginTransaction();
//                DisplayStatisticFragment displayStatisticFragment = new DisplayStatisticFragment();
//                tranDisplayStatistic.replace(R.id.contentView,displayStatisticFragment);
//                tranDisplayStatistic.commit();
//                navigationView.setCheckedItem(item.getItemId());
//                drawerLayout.closeDrawers();
//                break;
//            case R.id.profile:
//                //gọi activity ra trang welcome
//                Intent intent = new Intent(getApplicationContext(),Activityprofile.class);
//                startActivity(intent);
//                break;
//            case R.id.thongtinungdung:
//                //gọi activity ra trang welcome
//                Intent intentt = new Intent(getApplicationContext(),ThongtinActivity.class);
//                startActivity(intentt);
//                break;
//            case R.id.nav_table:
//                //hiển thị tương ứng trên navigation
//                FragmentTransaction tranDisplayTable = fragmentManager.beginTransaction();
//                DisplayTableFragment displayTableFragment = new DisplayTableFragment();
//                tranDisplayTable.replace(R.id.contentView,displayTableFragment);
//                tranDisplayTable.commit();
//                navigationView.setCheckedItem(item.getItemId());
//                drawerLayout.closeDrawers();
//                break;
//
//            case R.id.nav_category:
//                //hiển thị tương ứng trên navigation
//                FragmentTransaction tranDisplayMenu = fragmentManager.beginTransaction();
//                DisplayCategoryFragment displayCategoryFragment = new DisplayCategoryFragment();
//                tranDisplayMenu.replace(R.id.contentView, displayCategoryFragment);
//                tranDisplayMenu.commit();
//                navigationView.setCheckedItem(item.getItemId());
//                drawerLayout.closeDrawers();
//
//                break;
//
//            case R.id.nav_staff:
//                if(maquyen == 1){
//                    //hiển thị tương ứng trên navigation
//                    FragmentTransaction tranDisplayStaff = fragmentManager.beginTransaction();
//                    DisplayStaffFragment displayStaffFragment = new DisplayStaffFragment();
//                    tranDisplayStaff.replace(R.id.contentView,displayStaffFragment);
//                    tranDisplayStaff.commit();
//                    navigationView.setCheckedItem(item.getItemId());
//                    drawerLayout.closeDrawers();
//                }else {
//                    Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//
//            case R.id.nav_logout:
//                //gọi activity ra trang welcome
//                Intent intenttt = new Intent(getApplicationContext(),WelcomeActivity.class);
//                startActivity(intenttt);
//                break;
//        }

        if(id == R.id.nav_home){
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
            DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
            tranDisplayHome.replace(R.id.contentView,displayHomeFragment);
            tranDisplayHome.commit();
            navigationView.setCheckedItem(item.getItemId());
            drawerLayout.closeDrawers();
        }else if(id == R.id.nav_statistic){
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayStatistic = fragmentManager.beginTransaction();
            DisplayStatisticFragment displayStatisticFragment = new DisplayStatisticFragment();
            tranDisplayStatistic.replace(R.id.contentView,displayStatisticFragment);
            tranDisplayStatistic.commit();
            navigationView.setCheckedItem(item.getItemId());
            drawerLayout.closeDrawers();
        }
        else if(id == R.id.profile){
            //gọi activity ra trang welcome
            Intent intent = new Intent(getApplicationContext(), Activityprofile.class);
            startActivity(intent);
        }

        else if(id == R.id.nav_table){
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayTable = fragmentManager.beginTransaction();
            DisplayTableFragment displayTableFragment = new DisplayTableFragment();
            tranDisplayTable.replace(R.id.contentView,displayTableFragment);
            tranDisplayTable.commit();
            navigationView.setCheckedItem(item.getItemId());
            drawerLayout.closeDrawers();
        }
        else if(id == R.id.nav_category){
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayMenu = fragmentManager.beginTransaction();
            DisplayCategoryFragment displayCategoryFragment = new DisplayCategoryFragment();
            tranDisplayMenu.replace(R.id.contentView, displayCategoryFragment);
            tranDisplayMenu.commit();
            navigationView.setCheckedItem(item.getItemId());
            drawerLayout.closeDrawers();
        }
        else if(id == R.id.nav_staff){
            if(maquyen == 1){
                //hiển thị tương ứng trên navigation
                FragmentTransaction tranDisplayStaff = fragmentManager.beginTransaction();
                DisplayStaffFragment displayStaffFragment = new DisplayStaffFragment();
                tranDisplayStaff.replace(R.id.contentView,displayStaffFragment);
                tranDisplayStaff.commit();
                navigationView.setCheckedItem(item.getItemId());
                drawerLayout.closeDrawers();
            }else {
                Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
            }
        }
        else if(id == R.id.nav_logout){
            //gọi activity ra trang welcome
            Intent intenttt = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intenttt);
        }

        return false;
    }

}