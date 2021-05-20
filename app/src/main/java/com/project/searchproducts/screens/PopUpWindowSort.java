package com.project.searchproducts.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.project.searchproducts.R;
import com.project.searchproducts.network.SortType;

public class PopUpWindowSort {
    private IOnClickPopUpWindowSort iOnClickPopUpWindowSort;
    private PopupWindow popupWindow;
    private RadioButton radioButtonByScore;
    private RadioButton radioButtonByMinPrice;
    private RadioButton radioButtonByMaxPrice;

    @SuppressLint("ClickableViewAccessibility")
    public void showPopupWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_window_sort, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

       radioButtonByScore = popupView.findViewById(R.id.radioButtonByScore);
       radioButtonByMinPrice = popupView.findViewById(R.id.radioButtonByMinPrice);
       radioButtonByMaxPrice = popupView.findViewById(R.id.radioButtonByMaxPrice);

        radioButtonByScore.setOnClickListener(this::onRadioButtonClicked);
        radioButtonByMinPrice.setOnClickListener(this::onRadioButtonClicked);
        radioButtonByMaxPrice.setOnClickListener(this::onRadioButtonClicked);
    }

    public void setCurrentType(SortType sortType) {
        if (sortType.equals(SortType.SCORE)) {
            radioButtonByScore.setChecked(true);
        } else if (sortType.equals(SortType.MIN)) {
            radioButtonByMinPrice.setChecked(true);
        } else {
            radioButtonByMaxPrice.setChecked(true);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtonByScore:
                if (checked) {
                    iOnClickPopUpWindowSort.onClickPopUpWindow(SortType.SCORE);
                    popupWindow.dismiss();
                }
                break;
            case R.id.radioButtonByMinPrice:
                if (checked) {
                    iOnClickPopUpWindowSort.onClickPopUpWindow(SortType.MIN);
                    popupWindow.dismiss();
                }
                break;
            case R.id.radioButtonByMaxPrice:
                if (checked) {
                    iOnClickPopUpWindowSort.onClickPopUpWindow(SortType.MAX);
                    popupWindow.dismiss();
                }
                break;
        }
    }

    public void setIOnClickPopUpWindow(IOnClickPopUpWindowSort iOnClickPopUpWindowSort) {
        this.iOnClickPopUpWindowSort = iOnClickPopUpWindowSort;
    }
}
