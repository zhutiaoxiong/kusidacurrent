package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import model.ManagerCarList;
import model.carlist.DataCarStatus;

public class ViewClipSeekBar extends RelativeLayout {
    private LayoutInflater inflater;
    private ImageView bg, thumb;
    private float moveY;
    private float downY;
    private float upY;
    public static boolean isScroll;
    private int firstLocation=1;//1滑塊在上面0滑塊在下面，代表滑塊位置
    public interface OnThumbChangeListner{
        void getThumbChange(int location);
    }
    public void setOnThumbChangeListner(OnThumbChangeListner listner){
        this.onThumbChangeListner=listner;
    }
    private OnThumbChangeListner onThumbChangeListner;
    public ViewClipSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_clip_seekbar, this, true);
        bg = (ImageView) findViewById(R.id.bg);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        int resId = ta.getResourceId(R.styleable.androidMe_res, 0);
        if(resId==0){
            bg.setImageResource(R.drawable.door_seek_bg);
        }else{
            bg.setImageResource(resId);
        }
        thumb = (ImageView) findViewById(R.id.thumb);
        ta.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isScroll=true;
                downY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY=event.getY();
                 if (BuildConfig.DEBUG) Log.e("测试滑动", "firstLocation"+firstLocation);
                if(firstLocation==1){
                    if(moveY>downY){
                        int topThumbLocation= ODipToPx.dipToPx(getContext(),40.5f);
//                        int marginTop= ODipToPx.dipToPx(getContext(),3);
                        int marginTop= 0;
                         if (BuildConfig.DEBUG) Log.e("测试滑动", "向下 topThumbLocation"+ topThumbLocation+"marginTop"+marginTop+"downY"+downY+"moveY"+moveY);
                        if(downY>=marginTop&&downY<=topThumbLocation){
                            //执行下滑滑动操作
                            int maxDistabce= ODipToPx.dipToPx(getContext(),40.5f);
                            if((moveY-downY)<=maxDistabce){
                                setThumbLocationScrollDown(moveY,downY);
                            }
                        }
                    }
                }else if(firstLocation==0){
                    if(moveY<downY){
                        int marginBottom= ODipToPx.dipToPx(getContext(),81);
                        int bottomThumbLocation= ODipToPx.dipToPx(getContext(),40.5f);
                         if (BuildConfig.DEBUG) Log.e("测试滑动", "向上 bottomThumbLocation"+ bottomThumbLocation+"marginBottom"+marginBottom+"downY"+downY+"moveY"+moveY);
                        if(downY>=bottomThumbLocation&&downY<=marginBottom){
                            //执行上滑UI
                            //滑动的最大限制距离
                            int maxDistabce= ODipToPx.dipToPx(getContext(),40.5f);
                            if((downY-moveY)<=maxDistabce){
                                setThumbLocationScrollUp(moveY,downY);
                            }
                        }
                    }
                }

//                setThumbLocation(moveY);
                break;
            case MotionEvent.ACTION_UP:
                isScroll=false;
                upY=event.getY();
                if(firstLocation==1){
                    if(upY>downY){
                        ///1/4滑块距离
                        int topThumbLocation= ODipToPx.dipToPx(getContext(),40.5f);
//                        int marginTop= ODipToPx.dipToPx(getContext(),3);
                        int marginTop= 0;
                        if(downY>=marginTop&&downY<=topThumbLocation){
                            int percentFourThumb= ODipToPx.dipToPx(getContext(),40.5f/4);
                            if((upY-downY)<percentFourThumb){
                                setThumbLocation(1);
                            }else{
                                DataCarStatus currentStatus= ManagerCarList.getInstance().getCurrentCarStatus();
                                if(currentStatus!=null){
                                    if(currentStatus.isTheft==1){
                                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "当前操作无效，请先解防后再操作");
                                        setThumbLocation(1);
                                    }else{
                                        onThumbChangeListner.getThumbChange(0);
                                        setThumbLocation(0);
                                         if (BuildConfig.DEBUG) Log.e("ViewControlpanalonline", "滑动设置的要发打开的指令的UI ");
                                    }
                                }
                            }
                        }
                    }else{
                        setThumbLocation(1);
                    }
                }else if(firstLocation==0){
                    if(upY<downY){
                        ///1/4滑块距离
                        int marginBottom= ODipToPx.dipToPx(getContext(),81);
                        int bottomThumbLocation= ODipToPx.dipToPx(getContext(),40.5f);
                        int percentFourThumb= ODipToPx.dipToPx(getContext(),40.5f/4);
                        if(downY>=bottomThumbLocation&&downY<=marginBottom){
                            if((downY-upY)<percentFourThumb){
                                setThumbLocation(0);
                            }else{
                                DataCarStatus currentStatus= ManagerCarList.getInstance().getCurrentCarStatus();
                                if(currentStatus!=null){
                                    if(currentStatus.isTheft==1){
                                        setThumbLocation(0);
                                    }else{
                                        onThumbChangeListner.getThumbChange(1);
                                        setThumbLocation(1);
                                         if (BuildConfig.DEBUG) Log.e("ViewControlpanalonline", "滑动设置的要发关闭的指令的UI ");
                                    }
                                }
                            }
                        }
                    }else{
                        setThumbLocation(0);
                    }
                }
                break;
        }
        return true;
    }
    private void setThumbLocationScrollUp(float moveyY,float downYy){
        LayoutParams layout=(LayoutParams)thumb.getLayoutParams();
        //获得button控件的位置属性，需要注意的是，可以将button换成想变化位置的其它控件
         if (BuildConfig.DEBUG) Log.e("测试滑动", " 向上滑动"+"margintop"+ ODipToPx.dipToPx(getContext(),3)+(int)(downYy-moveyY)+"marginbottom"+(ODipToPx.dipToPx(getContext(),49)-(int)(downYy-moveyY)));
        layout.setMargins(0, ODipToPx.dipToPx(getContext(),40.5f)-(int)(downYy-moveyY), 0, 0+(int)(downYy-moveyY));
        //设置button的新位置属性,left，top，right，bottom
        thumb.setLayoutParams(layout);
    }
    private void setThumbLocationScrollDown(float moveYy,float downYy){
        LayoutParams layout=(LayoutParams)thumb.getLayoutParams();
        //获得button控件的位置属性，需要注意的是，可以将button换成想变化位置的其它控件
         if (BuildConfig.DEBUG) Log.e("测试滑动", " 向下滑动"+"margintop"+ ODipToPx.dipToPx(getContext(),3)+(int)(moveYy-downYy)+"marginbottom"+(ODipToPx.dipToPx(getContext(),49)-(int)(moveYy-downYy)));
        layout.setMargins(0, 0+(int)(moveYy-downYy), 0, ODipToPx.dipToPx(getContext(),40.5f)-(int)(moveYy-downYy));
        //设置button的新位置属性,left，top，right，bottom
        thumb.setLayoutParams(layout);
    }
    public void setThumbLocation(int arg1){
        Message message=Message.obtain();
        message.what=110;
        message.arg1=arg1;
        handler.sendMessage(message);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage( Message msg) {
            if(msg.what==110){
                firstLocation=msg.arg1;
                 if (BuildConfig.DEBUG) Log.e("ViewControlpanalonline", "firstLocation: "+firstLocation );
                if(msg.arg1==1){
                    LayoutParams layout=(LayoutParams)thumb.getLayoutParams();
                    //获得button控件的位置属性，需要注意的是，可以将button换成想变化位置的其它控件
                    layout.setMargins(0, 0, 0, ODipToPx.dipToPx(getContext(),37));
                    //设置button的新位置属性,left，top，right，bottom
                    thumb.setLayoutParams(layout);
                    thumb.invalidate();
                }else{
                    LayoutParams layout=(LayoutParams)thumb.getLayoutParams();
                    //获得button控件的位置属性，需要注意的是，可以将button换成想变化位置的其它控件
                    layout.setMargins(0, ODipToPx.dipToPx(getContext(),37), 0, 0);
                    //设置button的新位置属性,left，top，right，bottom
                    thumb.setLayoutParams(layout);
                    thumb.invalidate();
                }
            }
        }
    };
}
