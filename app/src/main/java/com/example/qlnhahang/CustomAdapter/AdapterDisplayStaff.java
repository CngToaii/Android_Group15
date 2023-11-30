package com.example.qlnhahang.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlnhahang.DAO.QuyenDAO;
import com.example.qlnhahang.DTO.NhanVienDTO;
import com.example.qlnhahang.R;

import java.util.List;

public class AdapterDisplayStaff extends BaseAdapter {

    Context context;
    int layout;
    List<NhanVienDTO> nhanVienDTOS;
    ViewHolder viewHolder;
    QuyenDAO quyenDAO;

    public AdapterDisplayStaff(Context context, int layout, List<NhanVienDTO> nhanVienDTOS){
        this.context = context;
        this.layout = layout;
        this.nhanVienDTOS = nhanVienDTOS;
        quyenDAO = new QuyenDAO(context);
    }

    @Override
    public int getCount() {
        return nhanVienDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return nhanVienDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return nhanVienDTOS.get(position).getMANV();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,parent,false);

            viewHolder.img_customstaff_HinhNV = (ImageView)view.findViewById(R.id.img_customstaff_HinhNV);
            viewHolder.txt_customstaff_TenNV = (TextView)view.findViewById(R.id.txt_customstaff_TenNV);
            viewHolder.txt_customstaff_TenQuyen = (TextView)view.findViewById(R.id.txt_customstaff_TenQuyen);
            viewHolder.txt_customstaff_SDT = (TextView)view.findViewById(R.id.txt_customstaff_SDT);
            viewHolder.txt_customstaff_Email = (TextView)view.findViewById(R.id.txt_customstaff_Email);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        NhanVienDTO nhanVienDTO = nhanVienDTOS.get(position);

        viewHolder.txt_customstaff_TenNV.setText(nhanVienDTO.getHOTENNV());
        viewHolder.txt_customstaff_TenQuyen.setText(quyenDAO.LayTenQuyenTheoMa(nhanVienDTO.getMAQUYEN()));
        viewHolder.txt_customstaff_SDT.setText(nhanVienDTO.getSDT());
        viewHolder.txt_customstaff_Email.setText(nhanVienDTO.getEMAIL());

        return view;
    }

    public class ViewHolder{
        ImageView img_customstaff_HinhNV;
        TextView txt_customstaff_TenNV, txt_customstaff_TenQuyen,txt_customstaff_SDT, txt_customstaff_Email;
    }
}
