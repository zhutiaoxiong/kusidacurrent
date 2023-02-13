package annualreminder.view.style;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.OConver;

public class StyleConfirm extends RelativeLayout {
    private TextView txt_title, txt_info,btn_cancel, btn_confirm;
    private ImageView img_splitline,view_background;

    private MyHandler handler = new MyHandler();

    private OnConfirmClickListener listener;
    private String title,info;
    // ===================================================
    public StyleConfirm(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.style_confirm, this, true);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_info = (TextView) findViewById(R.id.txt_info);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        img_splitline = (ImageView) findViewById(R.id.img_splitline);
        view_background = (ImageView) findViewById(R.id.view_background);
        initEvent();
    }
    private void initEvent() {
        view_background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)listener.onConfirm(false);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)listener.onConfirm(false);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)listener.onConfirm(true);
            }
        });
    }
    public void show(String title,String info,OnConfirmClickListener listener){
        this.title = title;
        this.info = info;
        this.listener = listener;
        handleChangeData();
    }
    // ===================================================
    public void handleChangeData() {
        if (handler == null) return;
        Message message = new Message();
        message.what = 16597;
        handler.sendMessage(message);
    }

    // ===================================================
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 16597:
                    //set UI
                    if(title!=null)txt_title.setText(title);
                    if(info!=null)txt_info.setText(OConver.StrToDBC(info));
                    break;
            }
        }
    }
    // ===================================================
    public interface OnConfirmClickListener {
        void onConfirm(boolean isClickConfirm);
    }
    //必要，事件回调
    public void setOnlyConfrimListener(OnConfirmClickListener listener){
        this.listener = listener;
    }
}

