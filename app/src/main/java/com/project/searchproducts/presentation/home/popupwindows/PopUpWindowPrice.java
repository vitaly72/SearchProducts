package com.project.searchproducts.presentation.home.popupwindows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.project.searchproducts.R;

public class PopUpWindowPrice {
    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private TextView textViewOk;

    private IOnClickPopUpWindowPrice iOnClickPopUpWindowPrice;

    @SuppressLint("ClickableViewAccessibility")
    public void showPopupWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_window_price, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editTextMinPrice = popupView.findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = popupView.findViewById(R.id.editTextMaxPrice);
        textViewOk = popupView.findViewById(R.id.textViewOkPrice);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        textViewOk.setOnClickListener(v -> {
            iOnClickPopUpWindowPrice.onClickPopUpWindow(editTextMinPrice.getText().toString(),
                    editTextMaxPrice.getText().toString());
            popupWindow.dismiss();
        });
    }

    public void setPriceRange(String min, String max) {
        editTextMinPrice.setHint(min);
        editTextMaxPrice.setHint(max);
    }

    public void setIOnClickPopUpWindow(IOnClickPopUpWindowPrice iOnClickPopUpWindowPrice) {
        this.iOnClickPopUpWindowPrice = iOnClickPopUpWindowPrice;
    }
}
