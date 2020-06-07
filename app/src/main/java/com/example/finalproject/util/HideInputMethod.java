package com.example.finalproject.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * @author wunu
 */
public class HideInputMethod {
    public static void hideAllInputMethod(Activity act){
        // 从系统服务器中获取输入法管理器
        InputMethodManager imm = (InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 如果软键盘打开则关闭
        if (imm.isActive()){
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
