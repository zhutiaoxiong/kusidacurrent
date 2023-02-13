package view.home.commonview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.orhanobut.logger.Logger;

import model.locator.MessageDetailBean;

public class BaiDuMessageDetailInfoWindow extends ConstraintLayout {
    private TextView txt_top_left;
    private TextView txt_bao_type;
    private TextView txt_time;
    private TextView txt_aderess;
    private static String TAG = "BaiDuMessageDetailInfoWindow";


    public BaiDuMessageDetailInfoWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.baidu_message_detail_info_window, this, true);
        initView();
        initEvent();
    }

    private void initView() {
        txt_top_left = findViewById(R.id.txt_top_left);
        txt_aderess = findViewById(R.id.txt_aderess);
        txt_bao_type = findViewById(R.id.txt_bao_type);
        txt_time = findViewById(R.id.txt_time);
    }

    private void initEvent() {

    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                MessageDetailBean bean=(MessageDetailBean)msg.obj;
                setUI(bean);
            }
        }
    };

    public void sendMsgSetUI(MessageDetailBean bean) {
        Message message=Message.obtain();
        message.what=1;
        message.obj=bean;
        handler.sendMessage(message);
        Logger.d("sendMsgSetUI");
    }

    private void setUI( MessageDetailBean bean) {
        txt_top_left.setText(bean.name);
        txt_bao_type.setText(bean.type);
        txt_aderess.setText(bean.aderess);
        String timeTwo= ODateTime.time2StringDateDetail(bean.time);
        if(!TextUtils.isEmpty(timeTwo)){
            txt_time.setText(timeTwo);
        }
    }
}
