package view.view4app;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.RelativeLayoutBase;

public class View4AppTopView extends RelativeLayoutBase {

    private final ImageView iv_fence;
    private final TextView tv_fence;
    private final ImageView iv_track;
    private final TextView tv_track;

    public View4AppTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_app_top_view, this, true);
        iv_fence =  findViewById(R.id.iv_fence);
        tv_fence =  findViewById(R.id.tv_fence);
        iv_track =  findViewById(R.id.iv_track);
        tv_track =  findViewById(R.id.tv_track);
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_LISTCHANGE, this);
        ODispatcher.addEventListener(OEventName.GPS_FAVORITE_INTRO_CHANGE_OK, this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {
        // back
        iv_fence.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewGpsCarArea.class);//围栏
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        tv_fence.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ViewGpsCarArea.class);//围栏
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        iv_track.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
            }
        });
        tv_track.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
            }
        });

    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {

    }

    @Override
    public void callback(String key, Object value) {
        super.callback(key, value);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {

    }
}
