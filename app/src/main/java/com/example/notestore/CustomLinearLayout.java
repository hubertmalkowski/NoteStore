package com.example.notestore;

import android.content.Context;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {


    public CustomLinearLayout(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
