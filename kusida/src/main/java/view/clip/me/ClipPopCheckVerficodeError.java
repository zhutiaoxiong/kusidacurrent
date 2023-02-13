package view.clip.me;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;

/**
 * Created by qq522414074 on 2016/8/19.
 */
public class ClipPopCheckVerficodeError {
    private PopupWindow popContain;//弹出管理
    private View parentView;//本对象显示
    private RelativeLayout thisView;
    private Context context;
    private ImageView checkfailed;
    private View      touch_exit;
    private String    mark;//选择标记
    private OCallBack callback;
    // ========================out======================
    private static ClipPopCheckVerficodeError _instance;

    public static ClipPopCheckVerficodeError getInstance() {
        if (_instance == null)
            _instance = new ClipPopCheckVerficodeError();
        return _instance;
    }

    //===================================================
    public void show(View parentView, int resid,String mark, OCallBack callback) {
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.clip_check_verficode_failed, null);
        checkfailed = (ImageView) thisView.findViewById(R.id.check_verficode_error);
        checkfailed.setImageResource(resid);
//        touch_exit = (View) thisView.findViewById(R.id.touch_exit);
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        // 设置阴影效果
//        WindowManager.LayoutParams params=ActivityKulala.ActivityKulalathis.getWindow().getAttributes();
//        params.alpha=0.4f;
//        ActivityKulala.ActivityKulalathis.getWindow().setAttributes(params);
//		parentView.setAlpha(0.4f);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		popContain.setBackgroundDrawable(parentView.getResources().getDrawable(R.color.black));
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
//		popContain.setAnimationStyle(R.style.WindowEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    exitThis();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public void exitThis() {
//        WindowManager.LayoutParams params=ActivityKulala.ActivityKulalathis.getWindow().getAttributes();
//        params.alpha=1.0f;
//        ActivityKulala.ActivityKulalathis.getWindow().setAttributes(params);
//		parentView.setAlpha(1.0f);
        popContain.dismiss();
    }

    public void initEvents() {
        checkfailed.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View view) {
               exitThis();
            }
        });
    }
}
