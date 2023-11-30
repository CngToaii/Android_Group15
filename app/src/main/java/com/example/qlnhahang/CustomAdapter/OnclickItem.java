package com.example.qlnhahang.CustomAdapter;

import com.example.qlnhahang.DTO.ThanhToanDTO;

public interface OnclickItem {
    void OnClickBack(ThanhToanDTO thanhToanDTO);
    void OnClickNext(ThanhToanDTO thanhToanDTO);
    void OnLongClick(ThanhToanDTO thanhToanDTO);
}
