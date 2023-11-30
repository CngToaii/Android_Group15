package com.example.qlnhahang.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnhahang.DAO.MonDAO;
import com.example.qlnhahang.DTO.MonDTO;
import com.example.qlnhahang.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddMenuActivity extends AppCompatActivity implements View.OnClickListener{

    Button BTN_addmenu_ThemMon;
    RelativeLayout layout_trangthaimon;
    ImageView IMG_addmenu_ThemHinh, IMG_addmenu_back;
    TextView TXT_addmenu_title;
    TextInputLayout TXTL_addmenu_TenMon,TXTL_addmenu_GiaTien,TXTL_addmenu_LoaiMon;
    RadioGroup RG_addmenu_TinhTrang;
    RadioButton RD_addmenu_ConMon, RD_addmenu_HetMon;
    MonDAO monDAO;
    String tenloai, sTenMon,sGiaTien,sTinhTrang;
    Bitmap bitmapold;
    int maloai;
    int mamon = 0;

    ActivityResultLauncher<Intent> resultLauncherOpenIMG = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Uri uri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            IMG_addmenu_ThemHinh.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmenu_layout);

        //region Lấy đối tượng view
        IMG_addmenu_ThemHinh = (ImageView)findViewById(R.id.img_addmenu_ThemHinh);
        IMG_addmenu_ThemHinh.setTag(R.drawable.drinkfood);
        IMG_addmenu_back = (ImageView)findViewById(R.id.img_addmenu_back);
        TXTL_addmenu_TenMon = (TextInputLayout)findViewById(R.id.txtl_addmenu_TenMon);
        TXTL_addmenu_GiaTien = (TextInputLayout)findViewById(R.id.txtl_addmenu_GiaTien);
        TXTL_addmenu_LoaiMon = (TextInputLayout)findViewById(R.id.txtl_addmenu_LoaiMon);
        BTN_addmenu_ThemMon = (Button)findViewById(R.id.btn_addmenu_ThemMon);
        TXT_addmenu_title = (TextView)findViewById(R.id.txt_addmenu_title);
        layout_trangthaimon = (RelativeLayout)findViewById(R.id.layout_trangthaimon);
        RG_addmenu_TinhTrang = (RadioGroup)findViewById(R.id.rg_addmenu_TinhTrang);
        RD_addmenu_ConMon = (RadioButton)findViewById(R.id.rd_addmenu_ConMon);
        RD_addmenu_HetMon = (RadioButton)findViewById(R.id.rd_addmenu_HetMon);
        //endregion

        Intent intent = getIntent();
        maloai = intent.getIntExtra("maLoai",-1);
        tenloai = intent.getStringExtra("tenLoai");
        monDAO = new MonDAO(this);  //khởi tạo đối tượng dao kết nối csdl
        TXTL_addmenu_LoaiMon.getEditText().setText(tenloai);

        BitmapDrawable olddrawable = (BitmapDrawable)IMG_addmenu_ThemHinh.getDrawable();
        bitmapold = olddrawable.getBitmap();

        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        mamon = getIntent().getIntExtra("mamon",0);
        if(mamon != 0){
            TXT_addmenu_title.setText("Sửa thực đơn");
            MonDTO monDTO = monDAO.LayMonTheoMa(mamon);

            TXTL_addmenu_TenMon.getEditText().setText(monDTO.getTenMon());
            TXTL_addmenu_GiaTien.getEditText().setText(monDTO.getGiaTien());

            byte[] menuimage = monDTO.getHinhAnh();
            Bitmap bitmap = BitmapFactory.decodeByteArray(menuimage,0,menuimage.length);
            IMG_addmenu_ThemHinh.setImageBitmap(bitmap);

            layout_trangthaimon.setVisibility(View.VISIBLE);
            String tinhtrang = monDTO.getTinhTrang();
            if(tinhtrang.equals("true")){
                RD_addmenu_ConMon.setChecked(true);
            }else {
                RD_addmenu_HetMon.setChecked(true);
            }

            BTN_addmenu_ThemMon.setText("Sửa món");
        }

        //endregion

        IMG_addmenu_ThemHinh.setOnClickListener(this);
        BTN_addmenu_ThemMon.setOnClickListener(this);
        IMG_addmenu_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean ktra;
        String chucnang;
        switch (id){
            case R.id.img_addmenu_ThemHinh:
                Intent iGetIMG = new Intent();
                iGetIMG.setType("image/*");
                iGetIMG.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncherOpenIMG.launch(Intent.createChooser(iGetIMG,getResources().getString(R.string.choseimg)));
                break;

            case R.id.img_addmenu_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;

            case R.id.btn_addmenu_ThemMon:
                //ktra validation
                if(!validateImage() | !validateName() | !validatePrice()){
                    return;
                }

                sTenMon = TXTL_addmenu_TenMon.getEditText().getText().toString();
                sGiaTien = TXTL_addmenu_GiaTien.getEditText().getText().toString();
                switch (RG_addmenu_TinhTrang.getCheckedRadioButtonId()){
                    case R.id.rd_addmenu_ConMon: sTinhTrang = "true";   break;
                    case R.id.rd_addmenu_HetMon: sTinhTrang = "false";  break;
                }

                MonDTO monDTO = new MonDTO();
                monDTO.setMaLoai(maloai);
                monDTO.setTenMon(sTenMon);
                monDTO.setGiaTien(sGiaTien);
                monDTO.setTinhTrang(sTinhTrang);
                monDTO.setHinhAnh(imageViewtoByte(IMG_addmenu_ThemHinh));
                if(mamon!= 0){
                    ktra = monDAO.SuaMon(monDTO,mamon);
                    chucnang = "suamon";
                }else {
                    ktra = monDAO.ThemMon(monDTO);
                    chucnang = "themmon";
                }

                //Thêm, sửa món dựa theo obj loaimonDTO
                Intent intent = new Intent();
                intent.putExtra("ktra",ktra);
                intent.putExtra("chucnang",chucnang);
                setResult(RESULT_OK,intent);
                finish();

                break;
        }
    }

    //Chuyển ảnh bitmap về mảng byte lưu vào csdl
    private byte[] imageViewtoByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //region Validate field
    private boolean validateImage(){
        BitmapDrawable drawable = (BitmapDrawable)IMG_addmenu_ThemHinh.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if(bitmap == bitmapold){
            Toast.makeText(getApplicationContext(),"Xin chọn hình ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateName(){
        String val = TXTL_addmenu_TenMon.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addmenu_TenMon.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addmenu_TenMon.setError(null);
            TXTL_addmenu_TenMon.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePrice(){
        String val = TXTL_addmenu_GiaTien.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addmenu_GiaTien.setError(getResources().getString(R.string.not_empty));
            return false;
        }else if(!val.matches(("\\d+(?:\\.\\d+)?"))){
            TXTL_addmenu_GiaTien.setError("Giá tiền không hợp lệ");
            return false;
        }else {
            TXTL_addmenu_GiaTien.setError(null);
            TXTL_addmenu_GiaTien.setErrorEnabled(false);
            return true;
        }
    }
    //endregion

}