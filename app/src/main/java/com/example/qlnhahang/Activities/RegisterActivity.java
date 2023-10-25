package com.example.qlnhahang.Activities;

import static com.example.qlnhahang.R.*;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ImageView IMG_signup_back;
    Button BTN_signup_next;
    TextView TXT_signup_title;
    TextInputLayout TXTL_signup_HoVaTen, TXTL_signup_TenDN, TXTL_signup_Email, TXTL_signup_SDT, TXTL_signup_MatKhau;
    public static final String BUNDLE = "BUNDLE";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.register_layout);

        //region lấy đối tượng view
        IMG_signup_back = (ImageView)findViewById(id.img_signup_back);
        BTN_signup_next = (Button)findViewById(id.btn_signup_next);
        TXT_signup_title = (TextView)findViewById(id.txt_signup_title);
        TXTL_signup_HoVaTen = (TextInputLayout)findViewById(id.txtl_signup_HoVaTen);
        TXTL_signup_TenDN = (TextInputLayout)findViewById(id.txtl_signup_TenDN);
        TXTL_signup_Email = (TextInputLayout)findViewById(id.txtl_signup_Email);
        TXTL_signup_SDT = (TextInputLayout)findViewById(id.txtl_signup_SDT);
        TXTL_signup_MatKhau = (TextInputLayout)findViewById(id.txtl_signup_MatKhau);
        //endregion

        BTN_signup_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra validate false => phải thỏa đk validate
                if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassWord()){
                    return;
                }
                String hoTen = TXTL_signup_HoVaTen.getEditText().getText().toString();
                String tenDN = TXTL_signup_TenDN.getEditText().getText().toString();
                String eMail = TXTL_signup_Email.getEditText().getText().toString();
                String sDT = TXTL_signup_SDT.getEditText().getText().toString();
                String matKhau = TXTL_signup_MatKhau.getEditText().getText().toString();

                byBundleNextSignupScreen(hoTen,tenDN,eMail,sDT,matKhau);
            }
        });

    }

    //Hàm quay về màn hình trước
    public void backFromRegister(View view){

        Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(id.layoutRegister),"transition_signup");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    //truyền dữ liệu qua trang đk thứ 2 bằng bundle
    public void byBundleNextSignupScreen(String hoTen, String tenDN, String eMail, String sDT, String matKhau){

        Intent intent = new Intent(getApplicationContext(),Register2ndActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hoten",hoTen);
        bundle.putString("tendn",tenDN);
        bundle.putString("email",eMail);
        bundle.putString("sdt",sDT);
        bundle.putString("matkhau",matKhau);
        intent.putExtra(BUNDLE,bundle);

        startActivity(intent);
        overridePendingTransition(anim.slide_in_right, anim.slide_out_left);
    }

    //region Validate field
    private boolean validateFullName(){
        String val = TXTL_signup_HoVaTen.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_signup_HoVaTen.setError(getResources().getString(string.not_empty));
            return false;
        }else {
            TXTL_signup_HoVaTen.setError(null);
            TXTL_signup_HoVaTen.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = TXTL_signup_TenDN.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,50}\\z";

        if(val.isEmpty()){
            TXTL_signup_TenDN.setError(getResources().getString(string.not_empty));
            return false;
        }else if(val.length()>50){
            TXTL_signup_TenDN.setError("Phải nhỏ hơn 50 ký tự");
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_signup_TenDN.setError("Không được cách chữ!");
            return false;
        }
        else {
            TXTL_signup_TenDN.setError(null);
            TXTL_signup_TenDN.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        String val = TXTL_signup_Email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if(val.isEmpty()){
            TXTL_signup_Email.setError(getResources().getString(string.not_empty));
            return false;
        }else if(!val.matches(checkspaces)){
            TXTL_signup_Email.setError("Email không hợp lệ!");
            return false;
        }
        else {
            TXTL_signup_Email.setError(null);
            TXTL_signup_Email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone(){
        String val = TXTL_signup_SDT.getEditText().getText().toString().trim();


        if(val.isEmpty()){
            TXTL_signup_SDT.setError(getResources().getString(string.not_empty));
            return false;
        }else if(val.length() != 10){
            TXTL_signup_SDT.setError("Số điện thoại không hợp lệ!");
            return false;
        }
        else {
            TXTL_signup_SDT.setError(null);
            TXTL_signup_SDT.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassWord(){
        String val = TXTL_signup_MatKhau.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            TXTL_signup_MatKhau.setError(getResources().getString(string.not_empty));
            return false;
        }else if(!PASSWORD_PATTERN.matcher(val).matches()){
            TXTL_signup_MatKhau.setError("Mật khẩu ít nhất 6 ký tự!");
            return false;
        }
        else {
            TXTL_signup_MatKhau.setError(null);
            TXTL_signup_MatKhau.setErrorEnabled(false);
            return true;
        }
    }
}