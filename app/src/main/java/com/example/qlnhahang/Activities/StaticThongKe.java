package com.example.qlnhahang.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressLint("SimpleDateFormat")
public class StaticThongKe extends AppCompatActivity {

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    DonDatDAO donDatDAO;
    List<DonDatDTO> listdondat = new ArrayList<>();
    TextView tvbatdau, tvketthuc;
    FloatingActionButton floatingActionButton;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("DonDat");
    final Calendar myCalendar1 = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_thong_ke);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);


        donDatDAO = new DonDatDAO(this);
//        listdondat = donDatDAO.LayDSDonDat();

        barChart = (BarChart) findViewById(R.id.barchart);


        tvbatdau = findViewById(R.id.tvbatdau);
        tvketthuc = findViewById(R.id.tvketthuc);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        Date date = new Date();
        tvketthuc.setText(simpleDateFormat.format(date));
        tvbatdau.setText(simpleDateFormat.format(cal.getTime()));

        DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, month);
                myCalendar1.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                tvbatdau.setText(dateFormat.format(myCalendar1.getTime()));
            }
        };

        tvbatdau.setOnClickListener(view -> {
            new DatePickerDialog(StaticThongKe.this, dates, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
        });

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, month);
                myCalendar2.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                tvketthuc.setText(dateFormat.format(myCalendar2.getTime()));

            }
        };

        tvketthuc.setOnClickListener(view -> {
            new DatePickerDialog(StaticThongKe.this, date2, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
        });

        floatingActionButton.setOnClickListener(view -> {
            if (myCalendar1.before(myCalendar2)) {
                createRandomBarGraph(tvbatdau.getText().toString(), tvketthuc.getText().toString());
                barChart.notifyDataSetChanged();
                Toast.makeText(StaticThongKe.this, "Chạm vào biểu đồ để RESET", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StaticThongKe.this, "Dữ liệu đưa vào không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
        createRandomBarGraph(tvbatdau.getText().toString(), tvketthuc.getText().toString());
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listdondat.clear();
                    for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        DonDatDTO donDatDTO = dataSnapshot1.getChildren().iterator().next().getValue(DonDatDTO.class);
                        listdondat.add(donDatDTO);
                        Log.d("listdondat>>>>>", listdondat.size() + "" + donDatDTO.getNgayDat());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createRandomBarGraph(String Date1, String Date2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1, mDate2);

            barEntries = new ArrayList<>();
            random = new Random();
            for (int j = 0; j < dates.size(); j++) {
                long tong = 0;
                for (DonDatDTO dt : listdondat) {
                    if (dt.getNgayDat().equals(dates.get(j))) {
                        tong += Long.parseLong(dt.getTongTien());
                    }
                }
                barEntries.add(new BarEntry(tong, j));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Vietnam đồng");
        BarData barData = new BarData(dates, barDataSet);
        barChart.setData(barData);

    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        ArrayList<String> list = new ArrayList<String>();
        while (startDate.compareTo(endDate) <= 0) {
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public String getDate(Calendar cld) {
        String curDate = cld.get(Calendar.DAY_OF_MONTH) + "-" + (cld.get(Calendar.MONTH) + 1)
                + "-" + cld.get(Calendar.YEAR);
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(curDate);
            curDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate;
    }

}
