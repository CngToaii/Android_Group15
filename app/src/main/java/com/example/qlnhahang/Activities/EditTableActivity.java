package com.example.qlnhahang.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DTO.BanAnDTO;
import com.example.qlnhahang.R;

public class EditTableActivity extends AppCompatActivity {

    TextInputLayout TXTL_edittable_tenban;
    Button BTN_edittable_SuaBan;
    BanAnDAO banAnDAO;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("BanAn");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittable_layout);

        //thuộc tính view
        TXTL_edittable_tenban = (TextInputLayout)findViewById(R.id.txtl_edittable_tenban);
        BTN_edittable_SuaBan = (Button)findViewById(R.id.btn_edittable_SuaBan);

        //khởi tạo dao mở kết nối csdl

        BanAnDTO banAnDTO = (BanAnDTO) getIntent().getSerializableExtra("banan");

        BTN_edittable_SuaBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenban = TXTL_edittable_tenban.getEditText().getText().toString();

                if(tenban != null || tenban.equals("")){
//                    boolean ktra = banAnDAO.CapNhatTenBan(maban,tenban);
                    banAnDTO.setTenBan(tenban);
                    myRef.child(String.valueOf(banAnDTO.getMaBan())).setValue(banAnDTO).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Intent intent = new Intent();
                            intent.putExtra("ketquasua",true);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else{
                            Intent intent = new Intent();
                            intent.putExtra("ketquasua",false);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}