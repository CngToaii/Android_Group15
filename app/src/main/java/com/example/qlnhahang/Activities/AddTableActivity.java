package com.example.qlnhahang.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnhahang.DAO.BanAnDAO;
import com.example.qlnhahang.DTO.BanAnDTO;
import com.example.qlnhahang.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddTableActivity extends AppCompatActivity {

    TextInputLayout TXTL_addtable_tenban;
    Button BTN_addtable_TaoBan;
    BanAnDAO banAnDAO;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("BanAn");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtable_layout);

        //region Lấy đối tượng trong view
        TXTL_addtable_tenban = (TextInputLayout)findViewById(R.id.txtl_addtable_tenban);
        BTN_addtable_TaoBan = (Button)findViewById(R.id.btn_addtable_TaoBan);

        BTN_addtable_TaoBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTenBanAn = TXTL_addtable_tenban.getEditText().getText().toString();
                if(sTenBanAn != null || sTenBanAn.equals("")){
                    BanAnDTO banAnDTO = new BanAnDTO();
                    banAnDTO.setTenBan(sTenBanAn);
                    banAnDTO.setMaBan(new Random().nextInt(100000));
                    myRef.child(String.valueOf(banAnDTO.getMaBan())).setValue(banAnDTO).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Intent intent = new Intent();
                            intent.putExtra("ketquathem",true);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
//                    //trả về result cho displaytable
//                    Intent intent = new Intent();
//                    intent.putExtra("ketquathem",ktra);
//                    setResult(RESULT_OK,intent);
//                    finish();
                }
            }
        });
    }

    //validate dữ liệu
    private boolean validateName(){
        String val = TXTL_addtable_tenban.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            TXTL_addtable_tenban.setError(getResources().getString(R.string.not_empty));
            return false;
        }else {
            TXTL_addtable_tenban.setError(null);
            TXTL_addtable_tenban.setErrorEnabled(false);
            return true;
        }
    }
}