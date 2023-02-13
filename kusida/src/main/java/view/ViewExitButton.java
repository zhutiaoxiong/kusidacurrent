package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

/**
 * Created by qq522414074 on 2016/10/12.
 */
public class ViewExitButton extends LinearLayoutBase {
    private Button exit_button;
    private LinearLayout  exit_layout;
    private int startX;
    private int startY;
    private long starttime;
    private long endtime;
    private boolean isClick;

    public ViewExitButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_prombox_exit_button,this,true);
        exit_button=(Button)findViewById(R.id.exit_button);
        exit_layout=(LinearLayout)findViewById(R.id.exit_button_layout) ;
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

        exit_button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        isClick=false;
                        isClick=true;
                        starttime=System.currentTimeMillis();
                        // 获取手指按下的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        // 获取手指移动到了哪个点的坐标
                        int movingX = (int) event.getRawX();
                        int movingY = (int) event.getRawY();
                        // 相对于上一个点，手指在X和Y方向分别移动的距离
                        int dx = movingX - startX;
                        int dy = movingY - startY;
                        // 获取TextView上一次上 下 左 右各边与父控件的距离
                        int left = exit_button.getLeft();
                        int right = exit_button.getRight();
                        int top = exit_button.getTop();
                        int bottom = exit_button.getBottom();
                        // 设置本次TextView的上 下 左 右各边与父控件的距离
                        int l=left + dx;
                        int t=top + dy;
                        int r=right + dx;
                        int b=bottom + dy;
                        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        int height = wm.getDefaultDisplay().getHeight();
                        if(l<=0||r>=width||t<=0||b>=height){
                            break;
                        }
                        exit_button.layout(left + dx, top + dy, right + dx, bottom + dy);

                        // 本次移动的结尾作为下一次移动的开始
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        if(dx!=0||dy!=0){
                            isClick=false;
                        }else{
                            isClick=true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        endtime=System.currentTimeMillis();
                        //按钮的水平或者竖直固定长度button
                        int button= ODipToPx.dipToPx(getContext(),45);
                        //手指抬起时所移动的水平或者竖直距离
//                       if((endtime-starttime)>0.1*3000L){
//                           isClick=false;
//
//                       }else{
//                           isClick=true;
//                       }
//                        if(dx>button||dx<-button||dy>button||dy<-button){
//                            isClick=false;
//                        }else{
//                            isClick=true;
//                        }
//                        if(isClick){
//                            ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_SHOW);
//                        }
                        if(isClick){
                            isClick=false;
                            ODispatcher.dispatchEvent(OEventName.EXIT_DEMOMODE_WINDOW_SHOW);
                        }else{
                            isClick=true;
                        }
                        break;
                }
                exit_layout.invalidate();
                return true;//如果返回true,从手指接触屏幕到手指离开屏幕，将不会触发点击事件。
            }
        });
    }

    @Override
    protected void invalidateUI() {

    }
}
