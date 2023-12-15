package com.example.qlnhahang.Activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.qlnhahang.DAO.NhanVienDAO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.R;

public class LoginActivity extends AppCompatActivity {
    Button BTN_login_DangNhap, BTN_login_DangKy;
    TextInputLayout TXTL_login_TenDN, TXTL_login_MatKhau;
    NhanVienDAO nhanVienDAO;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Quyen");
    private DatabaseReference myRef1 = database.getReference("NhanVien");
    public static int maNv = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //thuộc tính view
        TXTL_login_TenDN = (TextInputLayout)findViewById(R.id.txtl_login_TenDN);
        TXTL_login_MatKhau = (TextInputLayout)findViewById(R.id.txtl_login_MatKhau);
        BTN_login_DangNhap = (Button)findViewById(R.id.btn_login_DangNhap);
        BTN_login_DangKy = (Button)findViewById(R.id.btn_login_DangKy);

        nhanVienDAO = new NhanVienDAO(this);    //khởi tạo kết nối csdl

        BTN_login_DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserName() | !validatePassWord()){
                    return;
                }

                String tenDN = TXTL_login_TenDN.getEditText().getText().toString();
                String matKhau = TXTL_login_MatKhau.getEditText().getText().toString();
//                int ktra = nhanVienDAO.KiemTraDN(tenDN,matKhau);
//                int maquyen = nhanVienDAO.LayQuyenNV(ktra);
//                if(ktra != 0){
//                    // lưu mã quyền vào shareprefer
//                    SharedPreferences sharedPreferences = getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor =sharedPreferences.edit();
//                    editor.putInt("maquyen",maquyen);
//                    editor.commit();
//
//                    //gửi dữ liệu user qua trang chủ
//                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    intent.putExtra("tendn",TXTL_login_TenDN.getEditText().getText().toString());
//                    intent.putExtra("manv",ktra);
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(getApplicationContext(),"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
//                }
                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            boolean check = false;
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                NhanVienDTO nhanVienDTO = snapshot1.getValue(NhanVienDTO.class);
                                if(nhanVienDTO.getTENDN().equals(tenDN) && nhanVienDTO.getMATKHAU().equals(matKhau)){
                                    //lưu mã quyền vào shareprefer
                                    SharedPreferences sharedPreferences = getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor =sharedPreferences.edit();
                                    editor.putInt("maquyen",nhanVienDTO.getMAQUYEN());
                                    editor.commit();
                                    maNv = nhanVienDTO.getMANV();
                                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                    intent.putExtra("tendn",nhanVienDTO.getTENDN());
                                    intent.putExtra("manv",nhanVienDTO.getMATKHAU());
                                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    check = true;
                                    break;
                                }
                            }
                            if(!check){
                                Toast.makeText(getApplicationContext(),"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    //Hàm quay lại màn hình chính
    public void backFromLogin(View view)
    {
        Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
        //tạo animation cho thành phần
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.layoutLogin),"transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }

    //Hàm chuyển qua trang đăng ký
    public void callRegisterFromLogin(View view)
    {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //region Validate field
    private boolean validateUserName(){
        String val = TXTL_login_TenDN.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_login_TenDN.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_login_TenDN.setError(null);
            TXTL_login_TenDN.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_login_MatKhau.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_login_MatKhau.setError(getResources().getString(R.string.not_empty));
            return false;
        }else{
            TXTL_login_MatKhau.setError(null);
            TXTL_login_MatKhau.setErrorEnabled(false);
            return true;
        }
    }
    //endregion
}