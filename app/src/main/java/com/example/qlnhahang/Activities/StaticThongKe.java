package com.example.qlnhahang.Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnhahang.DAO.DonDatDAO;
import com.example.qlnhahang.DTO.DonDatDTO;
import com.example.qlnhahang.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class StaticThongKe extends AppCompatActivity {
    private Spinner spinner,sprinerweek;
    private HorizontalBarChart chartMonth,chartWeek;
    private TextView tvdaonhthu;

    DonDatDAO donDatDAO;
    List<DonDatDTO> listdondat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_thong_ke);

        spinner = findViewById(R.id.spinner_year);
        sprinerweek = findViewById(R.id.spinner_month);
        chartMonth = findViewById(R.id.chartMonth);
        tvdaonhthu = findViewById(R.id.tvdoanhthu);
        chartWeek = findViewById(R.id.chatweek);

        donDatDAO = new DonDatDAO(this);


        Long daonhthu =0L;
        for(DonDatDTO object :donDatDAO.LayDSDonDat()){
            daonhthu+=Long.valueOf(object.getTongTien());
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        tvdaonhthu.setText(formatter.format(daonhthu)+" VNĐ");



        List<Integer> years =new ArrayList<>();
        List<Integer> month =new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currnetYear = calendar.get(Calendar.YEAR);

        for(int i=0;i<5;i++){
            years.add(currnetYear);
            currnetYear=currnetYear-1;
        }

        for(int i=1;i<13;i++){
            month.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getApplicationContext(),
                android.R.layout.simple_spinner_item, years);

        ArrayAdapter<Integer> adaptermonth = new ArrayAdapter<Integer>(getApplicationContext(),
                android.R.layout.simple_spinner_item, month);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        sprinerweek.setAdapter(adaptermonth);

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    onItemSelectedHandler(parent, view, position, id);
                    setDataweek(1);
                    Toast.makeText(getApplicationContext(),"Chạm vào biểu đồ để thay đổi dữ liệu",Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                sprinerweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        onItemSelectedHandlerMonth(parent, view, position, id);
                        Toast.makeText(getApplicationContext(),"Chạm vào biểu đồ để thay đổi dữ liệu",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void onItemSelectedHandlerMonth(AdapterView<?> parent, View view, int position, long id) {
        Adapter adapter = parent.getAdapter();
        Integer month = (Integer) adapter.getItem(position);
        try {
            setDataweek(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setDataweek(Integer month) throws ParseException {
        ArrayList<BarEntry> monthChart =new ArrayList<>();
        List<DonDatDTO> filterDtaaW = filterMonth(month,listdondat);
        Log.d("AAA",filterDtaaW.toString());
        List<ThongkeThang> listthongke = new ArrayList<>();
        for(int i=1;i<=5;i++){
            ThongkeThang thongkeThang = new ThongkeThang();
            thongkeThang.setThang(i);
            Long price = 0L;
            for (DonDatDTO object: filterDtaaW) {
                if(getWEEK(object.getNgayDat())==i) {
                    price += Long.valueOf(object.getTongTien());
                }
            }
            thongkeThang.setPrice(price);
            listthongke.add(thongkeThang);
        }
        for(ThongkeThang object : listthongke){
            Log.d("ARR",object.toString());
            monthChart.add(new BarEntry((float)object.getThang(), (float) (object.getPrice() / 1000)));
        }
        BarDataSet set1;
        set1 = new BarDataSet(monthChart,"ĐƠN VỊ 1000 VND");
        set1.setValueTextSize(14);
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(set1);
        barData.setValueTextSize(14);
        XAxis xl = chartWeek.getXAxis();
        chartWeek.setBackgroundColor(Color.parseColor("#ffffcc"));
        xl.setTextSize(14);

        chartWeek.setData(barData);
    }

    private int getWEEK(String thoiGianKT) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringToDate(thoiGianKT));
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    private List<DonDatDTO> filterMonth(Integer month, List<DonDatDTO> filtedata) {
        List<DonDatDTO> dataNew = new ArrayList<>();
        for (DonDatDTO object: filtedata) {
            if(getMonth(object.getNgayDat()) == month) {
                Log.d("SSS",object.toString());
                dataNew.add(object);
            }
        }
        return dataNew;
    }


    private void setData(int year) throws ParseException {
        ArrayList<BarEntry> monthChart =new ArrayList<>();
        listdondat = filterYear(year,donDatDAO.LayDSDonDat());
        Log.d("AAA",donDatDAO.LayDSDonDat().toString());
        List<ThongkeThang> listthongke = new ArrayList<>();
        for(int i=1;i<13;i++){
            ThongkeThang thongkeThang = new ThongkeThang();
            thongkeThang.setThang(i);
            Long price = 0L;
            for (DonDatDTO object: listdondat) {
                if(getMonth(object.getNgayDat())==i) {
                    price += Long.valueOf( object.getTongTien());
                }
            }
            thongkeThang.setPrice(price);
            listthongke.add(thongkeThang);
        }

        for(ThongkeThang object : listthongke){
            Log.d("ARR",object.toString());
            monthChart.add(new BarEntry((float)object.getThang(), (float) (object.getPrice() / 1000)));
        }

        BarDataSet set1;
        set1 = new BarDataSet(monthChart,"ĐƠN VỊ 1000 VND");
        set1.setValueTextSize(14);
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(set1);
        barData.setValueTextSize(14);
        XAxis xl = chartMonth.getXAxis();
        xl.setTextSize(14);

        chartMonth.setData(barData);

    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) throws ParseException {
        Adapter adapter = adapterView.getAdapter();
        Integer year = (Integer) adapter.getItem(position);
        Toast.makeText(this,year+"",Toast.LENGTH_SHORT).show();
        setData(year);
    }

    private int getMonth(String thoiGianKT) {
        Date date = null;
        try {
            date = convertStringToDate(thoiGianKT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getMonth()+1;
    }

    private List<DonDatDTO> filterYear(Integer year, List<DonDatDTO> all) throws ParseException {
        List<DonDatDTO> dataNew = new ArrayList<>();
        for (DonDatDTO object: all) {
            if(getYear(object.getNgayDat()) == year) {
                dataNew.add(object);
            }
        }
        return dataNew;
    }

    private int getYear(String thoiGianKT) throws ParseException {
        Date date = null;
        date = convertStringToDate(thoiGianKT);
        return date.getYear()+1900;
    }

    private Date convertStringToDate(String thoiGianKT) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(thoiGianKT);
    }

    private class ThongkeThang {
        int thang;
        Long price;

        public ThongkeThang() {
        }

        public ThongkeThang(int thang, Long price) {
            this.thang = thang;
            this.price = price;
        }

        @Override
        public String toString() {
            return "ThongkeThang{" +
                    "thang=" + thang +
                    ", price=" + price +
                    '}';
        }

        public int getThang() {
            return thang;
        }

        public void setThang(int thang) {
            this.thang = thang;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }
    }
}