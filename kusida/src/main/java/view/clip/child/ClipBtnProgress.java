package view.clip.child;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;

import view.view4me.myblue.ThreadManager;

/**
 * Default : No Left Image,Center txt , Right No Arrow, Down Line
 */
public class ClipBtnProgress extends RelativeLayout {
    private TextView txt_background, txt_setup;
    private int maxSize, uiWidth;
    public static int COLOR_YELLOW = 0xFF6600, COLOR_GREEN = 0x0066FF;

    protected MyHandler handler = new MyHandler();

    public ClipBtnProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_btn_progress, this, true);
        txt_setup = (TextView) findViewById(R.id.txt_setup);
        txt_background = (TextView) findViewById(R.id.txt_background);
    }

    public void setText(String txt) {
        handleSetText(txt);
    }

    public void setBGColor(int colorStatic) {
        handleSetColor(colorStatic);
    }

    public String getText() {
        return txt_setup.getText().toString();
    }

    public void setMax(int size) {
        if (uiWidth == 0) uiWidth = txt_background.getWidth();
        maxSize = size;
    }

    public void setProgress(int size) {
        if(maxSize>0){
            handleSetProgress(uiWidth * size / maxSize);
        }
    }

    // ===================================================
    private void handleSetProgress(final int uiW) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = 11;
//                message.arg1 = uiW;
//                handler.sendMessage(message);
//            }
//        }).start();
        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 11;
                message.arg1 = uiW;
                handler.sendMessage(message);
            }
        });
    }

    private void handleSetText(String txt) {
        Message message = new Message();
        message.what = 22;
        message.obj = txt;
        handler.sendMessage(message);
    }

    private void handleSetColor(int colorStatic) {
        Message message = new Message();
        message.what = 33;
        message.arg1 = colorStatic;
        handler.sendMessage(message);
    }

    // ===================================================
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    int uiW = msg.arg1;
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) txt_background.getLayoutParams();
                    if(uiW>uiWidth){
                        params.width = uiWidth;
                    }else{
                        params.width = uiW;
                    }
                    txt_background.setLayoutParams(params);
                    break;
                case 22:
                    String txt = (String) msg.obj;
                    txt_setup.setText(txt);
                    break;
                case 33:
                    int colorStatic = msg.arg1;
                    if (colorStatic == COLOR_YELLOW) {
                        txt_background.setBackgroundResource(R.color.yellow_text);
                    } else if (colorStatic == COLOR_GREEN) {
                        txt_background.setBackgroundResource(R.color.light_blue);
                    }
                    break;
            }
        }
    }
}
