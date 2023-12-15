package com.example.qlnhahang.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.qlnhahang.DAO.LoaiMonDAO;
import com.example.qlnhahang.DTO.LoaiMonDTO;
import com.example.qlnhahang.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Button BTN_addcategory_TaoLoai;
    ImageView IMG_addcategory_back, IMG_addcategory_ThemHinh;
    TextView TXT_addcategory_title;
    TextInputLayout TXTL_addcategory_TenLoai;
    LoaiMonDAO loaiMonDAO;
    int maloai = 0;
    Bitmap bitmapold;   //Bitmap dạng ảnh theo ma trận các pixel

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("LoaiMon");


    //dùng result launcher do activityforresult ko dùng đc nữa
    ActivityResultLauncher<Intent> resultLauncherOpenIMG = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Uri uri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            IMG_addcategory_ThemHinh.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategory_layout);

        loaiMonDAO = new LoaiMonDAO(this);  //khởi tạo đối tượng dao kết nối csdl

        //region Lấy đối tượng view
        BTN_addcategory_TaoLoai = (Button)findViewById(R.id.btn_addcategory_TaoLoai);
        TXTL_addcategory_TenLoai = (TextInputLayout)findViewById(R.id.txtl_addcategory_TenLoai);
        IMG_addcategory_back = (ImageView)findViewById(R.id.img_addcategory_back);
        IMG_addcategory_ThemHinh = (ImageView)findViewById(R.id.img_addcategory_ThemHinh);

        //endregion

        BitmapDrawable olddrawable = (BitmapDrawable)IMG_addcategory_ThemHinh.getDrawable();
        bitmapold = olddrawable.getBitmap();

        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        maloai = getIntent().getIntExtra("maloai",0);
        if(maloai != 0){
            TXT_addcategory_title.setText(getResources().getString(R.string.editcategory));
            LoaiMonDTO loaiMonDTO = loaiMonDAO.LayLoaiMonTheoMa(maloai);

            //Hiển thị lại thông tin từ csdl
            TXTL_addcategory_TenLoai.getEditText().setText(loaiMonDTO.getTenLoai());

            String categoryimage = loaiMonDTO.getHinhAnh();
            byte[] categoryimagebyte = Base64.decode(categoryimage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(categoryimagebyte,0,categoryimagebyte.length);
            IMG_addcategory_ThemHinh.setImageBitmap(bitmap);

            BTN_addcategory_TaoLoai.setText("Sửa loại");
        }
        //endregion

        IMG_addcategory_back.setOnClickListener(this);
        IMG_addcategory_ThemHinh.setOnClickListener(this);
        BTN_addcategory_TaoLoai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        final boolean[] ktra = {false};
        String chucnang;
//        switch (id){
//            case R.id.img_addcategory_back:
        if(id == R.id.img_addcategory_back){
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //animation
//                break;
//
//            case R.id.img_addcategory_ThemHinh:
        }else if(id == R.id.img_addcategory_ThemHinh){
            Intent iGetIMG = new Intent();
            iGetIMG.setType("image/*"); //lấy những mục chứa hình ảnh
            iGetIMG.setAction(Intent.ACTION_GET_CONTENT);   //lấy mục hiện tại đang chứa hình
            resultLauncherOpenIMG.launch(Intent.createChooser(iGetIMG,getResources().getString(R.string.choseimg)));    //mở intent chọn hình ảnh
//                break;
//
//            case R.id.btn_addcategory_TaoLoai:
        }else if(id == R.id.btn_addcategory_TaoLoai){
            if(!validateImage() | !validateName()){
                return;
            }

            String sTenLoai = TXTL_addcategory_TenLoai.getEditText().getText().toString();
            LoaiMonDTO loaiMonDTO = new LoaiMonDTO();
            loaiMonDTO.setTenLoai(sTenLoai);
            loaiMonDTO.setHinhAnh(imageViewtoByte(IMG_addcategory_ThemHinh));
            if(maloai != 0){
                myRef.child(String.valueOf(maloai)).setValue(loaiMonDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ktra[0] = true;
                        }else {
                            ktra[0] = false;
                        }
                    }
                });
                chucnang = "sualoai";
            }else {
                loaiMonDTO.setMaLoai(new Random().nextInt(100000));
                myRef.child(String.valueOf(loaiMonDTO.getMaLoai())).setValue(loaiMonDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ktra[0] = true;
                        }else {
                            ktra[0] = false;
                        }
                    }
                });
                chucnang = "themloai";
            }

            //Thêm, sửa loại dựa theo obj loaimonDTO
            Intent intent = new Intent();
            intent.putExtra("ktra", ktra[0]);
            intent.putExtra("chucnang",chucnang);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    //Chuyển ảnh bitmap về mảng byte lưu vào csdl
    private String imageViewtoByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        String imageString = Base64.encodeToString(byteArray,Base64.DEFAULT);
        return imageString;
    }

    //region validate fields
    private boolean validateImage(){
        BitmapDrawable drawable = (BitmapDrawable)IMG_addcategory_ThemHinh.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if(bitmap == bitmapold){
            Toast.makeText(getApplicationContext(),"Xin chọn hình ảnh",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateName(){
        String val = TXTL_addcategory_TenLoai.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addcategory_TenLoai.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addcategory_TenLoai.setError(null);
            TXTL_addcategory_TenLoai.setErrorEnabled(false);
            return true;
        }
    }
    //endregion

}