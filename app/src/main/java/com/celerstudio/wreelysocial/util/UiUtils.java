package com.celerstudio.wreelysocial.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by yassinegharsallah on 01/04/2017.
 */

public class UiUtils {


    public static void showSnackbar(View view, String message, int length) {
        Snackbar.make(view, message, length).setAction("Action", null).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static StateListDrawable selectorBackground(int normal, int pressed) {
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(normal);
        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(pressed);
//        if (stroke == true) {
//            normalDrawable.setStroke(4, pressed);
//            pressedDrawable.setStroke(4, pressed);
//        }
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                pressedDrawable);
        states.addState(new int[]{}, normalDrawable);
        return states;
    }

//    public static TextDrawable getMaterialDrawable(String name) {
//        ColorGenerator generator = ColorGenerator.MATERIAL;
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(name.toString().substring(0, 1), generator.getRandomColor());
//        return drawable;
//    }
//
//    public static TextDrawable getMaterialDrawableTwoLetters(String name) {
//        ColorGenerator generator = ColorGenerator.MATERIAL;
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(name, Color.parseColor("#50A6C4"));
//        return drawable;
//    }

}
