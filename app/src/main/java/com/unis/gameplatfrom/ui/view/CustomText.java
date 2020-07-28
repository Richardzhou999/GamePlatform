package com.unis.gameplatfrom.ui.view;

import android.content.Context;
import android.graphics.Canvas;

import android.util.AttributeSet;
import android.widget.TextView;

import javax.annotation.Nullable;

public class CustomText extends android.support.v7.widget.AppCompatTextView {

    private String mText = "跳过";


    public CustomText(Context context) {
        super(context);
    }

    public CustomText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public String ReversalByString(String text){

        String result = null;


        //先反转
        StringBuilder firstBuilder = new StringBuilder();  //ABC
        firstBuilder.append(text);
        String first = firstBuilder.reverse().toString(); //CBA
        String[] array = first.split("");

        StringBuilder resultBuilder = new StringBuilder();
        for(String reversal : array){


            StringBuilder appendBuilder = new StringBuilder();
            appendBuilder.append(reversal);
            appendBuilder = appendBuilder.reverse();
            String str = appendBuilder.toString();
            resultBuilder.append(str);

        }


        result = resultBuilder.toString();


        return result;
    }




}
