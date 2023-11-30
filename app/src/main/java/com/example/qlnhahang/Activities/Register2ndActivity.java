package com.example.qlnhahang.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnhahang.DAO.NhanVienDAO;
import com.example.qlnhahang.DAO.QuyenDAO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.R;

import java.util.Calendar;

public class Register2ndActivity extends AppCompatActivity {

    RadioGroup RG_signup_GioiTinh;
    DatePicker DT_signup_NgaySinh;
    Button BTN_signup_next;
    String hoTen,tenDN,eMail,sDT,matKhau,gioiTinh;
    NhanVienDAO nhanVienDAO;
    QuyenDAO quyenDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2nd_layout);

        //lấy đối tượng view
        RG_signup_GioiTinh = (RadioGroup)findViewById(R.id.rg_signup_GioiTinh);
        DT_signup_NgaySinh = (DatePicker)findViewById(R.id.dt_signup_NgaySinh);
        BTN_signup_next = (Button)findViewById(R.id.btn_signup_next);

        //lấy dữ liệu từ bundle của register1
        Bundle bundle = getIntent().getBundleExtra(RegisterActivity.BUNDLE);
        if(bundle != null) {
            hoTen = bundle.getString("hoten");
            tenDN = bundle.getString("tendn");
            eMail = bundle.getString("email");
            sDT = bundle.getString("sdt");
            matKhau = bundle.getString("matkhau");
        }
        //khởi tạo kết nối csdl
        nhanVienDAO = new NhanVienDAO(this);
        quyenDAO = new QuyenDAO(this);

        BTN_signup_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateAge() | !validateGender()){
                    return;
                }

                //lấy các thông tin còn lại
                switch (RG_signup_GioiTinh.getCheckedRadioButtonId()){
                    case R.id.rd_signup_Nam:
                        gioiTinh = "Nam"; break;
                    case R.id.rd_signup_Nu:
                        gioiTinh = "Nữ"; break;
                    case R.id.rd_signup_Khac:
                        gioiTinh = "Khác"; break;
                }
                String ngaySinh = DT_signup_NgaySinh.getDayOfMonth() + "/" + (DT_signup_NgaySinh.getMonth() + 1)
                        +"/"+DT_signup_NgaySinh.getYear();

                //truyền dữ liệu vào obj nhanvienDTO
                NhanVienDTO nhanVienDTO = new NhanVienDTO();
                nhanVienDTO.setHOTENNV(hoTen);
                nhanVienDTO.setTENDN(tenDN);
                nhanVienDTO.setEMAIL(eMail);
                nhanVienDTO.setSDT(sDT);
                nhanVienDTO.setMATKHAU(matKhau);
                nhanVienDTO.setGIOITINH(gioiTinh);
                nhanVienDTO.setNGAYSINH(ngaySinh);

                //nếu nhân viên đầu tiên đăng ký sẽ có quyền quản lý
                if(!nhanVienDAO.KtraTonTaiNV()){
                    quyenDAO.ThemQuyen("Quản lý");
                    quyenDAO.ThemQuyen("Nhân viên");
                    nhanVienDTO.setMAQUYEN(1);
                }else {
                    nhanVienDTO.setMAQUYEN(2);
                }

                //Thêm nv dựa theo obj nhanvienDTO
                long ktra = nhanVienDAO.ThemNhanVien(nhanVienDTO);
                if(ktra != 0){
                    Toast.makeText(Register2ndActivity.this,getResources().getString(R.string.add_sucessful),Toast.LENGTH_SHORT).show();
                    callLoginFromRegister();
                }else{
                    Toast.makeText(Register2ndActivity.this,getResources().getString(R.string.add_failed),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Hàm quay về màn hình trước
    public void backFromRegister2nd(View view){
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    //Hàm chuyển màn hình khi hoàn thành
    public void callLoginFromRegister(){
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //region Validate field
    private boolean validateGender(){
        if(RG_signup_GioiTinh.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,"Hãy chọn giới tính",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = DT_signup_NgaySinh.getYear();
        int isAgeValid = currentYear - userAge;

        if(isAgeValid < 10){
            Toast.makeText(this,"Bạn không đủ tuổi đăng ký!",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    //endregion
}