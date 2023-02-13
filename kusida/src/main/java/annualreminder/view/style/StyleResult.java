package annualreminder.view.style;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

public class StyleResult {
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示
    private Context     context;

    private RelativeLayout thisView;
    private ImageView      img_result;
    private TextView       txt_info;

    private        MyHandler      handler;//init on main thread                                // 前次控制指令
    private        CountDownTimer countControlTimer;
    // ========================out======================
    private static StyleResult    _instance;

    public static StyleResult getInstance() {
        if (_instance == null)
            _instance = new StyleResult();
        return _instance;
    }

    //===================================================
    //此方法需要放入handler中
    public void show(View parentView, boolean resultOk, String infoTxt) {
        StyleResult.this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (RelativeLayout) layoutInflater.inflate(R.layout.style_result, null);
        img_result = (ImageView) thisView.findViewById(R.id.img_result);
        txt_info = (TextView) thisView.findViewById(R.id.txt_info);
        if (resultOk) {
            img_result.setImageResource(R.drawable.result_ok);
            if (infoTxt == null || infoTxt.length() == 0) {
                txt_info.setText("提交成功");
            } else {
                txt_info.setText(infoTxt);
            }
        } else {
            img_result.setImageResource(R.drawable.result_fail);
            if (infoTxt == null || infoTxt.length() == 0) {
                txt_info.setText("提交失败");
            } else {
                txt_info.setText(infoTxt);
            }
        }
        initViews();
        initEvents();
    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //禁用点击穿透,开启就是false,false,true
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(false);
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        if (handler == null) handler = new MyHandler();
        if (countControlTimer == null) countControlTimer = new CountDownTimer(2000, 500) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                handlehide();
            }
        };
        countControlTimer.cancel();
        countControlTimer.start();
    }

    public void initEvents() {
    }

    private void handlehide() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16596;
        handler.sendMessage(message);
    }

    // ===================================================
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16596:
                    if (popContain == null) return;
                    popContain.dismiss();
                    parentView = null;
                    thisView = null;
                    context = null;
                    break;
            }
        }
    }
}

