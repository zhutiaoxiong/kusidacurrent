package common.pinyinzhuanhuan;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import common.GlobalContext;

/**
 * Created by qq522414074 on 2016/11/11.
 */

public class KeyBoard {
    //关闭软件盘
    public static void hintKb() {
        InputMethodManager imm = (InputMethodManager) GlobalContext.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&GlobalContext.getCurrentActivity().getCurrentFocus()!=null){
            if (GlobalContext.getCurrentActivity().getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(GlobalContext.getCurrentActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 进入搜索页面默认弹出软键盘
     */
    public static void openKeyBoard(final EditText edit){
        //进入搜索页面打开软键盘
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(edit, 0);
                           }
                       },

                998);
    }
}
