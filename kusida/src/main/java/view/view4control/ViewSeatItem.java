package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.RelativeLayoutBase;

import static com.kulala.dispatcher.OEventName.CAR_STATUS_SECOND_CHANGE;

/**
 * Created by qq522414074 on 2016/9/18.
 */
public class ViewSeatItem extends RelativeLayoutBase implements OEventObject {
    private ImageView image_seat_status,image_seat_type;
    private RelativeLayout image_bg;
    private TextView txt_seat;
    private int comand;
    private int mEnable;
    private int mStatus;
    private long mCarId;
//    private View mParentView;

    public ViewSeatItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_seat_item, this, true);
        image_seat_status =  findViewById(R.id.image_seat_status);
        image_seat_type=findViewById(R.id.image_seat_type) ;
        txt_seat=findViewById(R.id.txt_seat) ;
        image_bg=findViewById(R.id.image_bg) ;
        @SuppressLint("CustomViewStyleable") TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.androidMe);
        comand = ta.getInteger(R.styleable.androidMe_comand, 1);
        initViews();
        initEvents();
        ta.recycle();
        ODispatcher.addEventListener(CAR_STATUS_SECOND_CHANGE,this);
    }

    private void setInitUI(){
        if(comand%2==0){
            if(comand==2){
                txt_seat.setText("主驾加热");
            }else if(comand==4){
                txt_seat.setText("副驾加热");
            }
        }else{
            if(comand==1){
                txt_seat.setText("主驾通风");
            }else if(comand==3){
                txt_seat.setText("副驾通风");
            }
        }
    }
    public int getmStatus(){
        return mStatus;
    }

//    public void setUIEnable(int enable,int status,long carId,View parentView){
//        mEnable=enable;
//        mStatus=status;
//        mCarId=carId;
//        mParentView=parentView;
//        switch (enable){
//            case 1:
//                if(comand%2==0){
//                    if(mStatus==0){
//                        image_seat_status.setImageResource(R.drawable.img_seat_light_gray);
//                    }else{
//                        image_seat_status.setImageResource(R.drawable.img_seat_light_red);
//                    }
//                    image_seat_type.setImageResource(R.drawable.img_seat_type_red);
//                }else{
//                    if(mStatus==0){
//                        image_seat_status.setImageResource(R.drawable.img_seat_light_gray);
//                    }else{
//                        image_seat_status.setImageResource(R.drawable.img_seat_light_blue);
//                    }
//                    image_seat_type.setImageResource(R.drawable.img_seat_type_blue);
//                }
//                break;
//            case 2:
//                image_seat_status.setImageResource(R.drawable.img_seat_light_gray);
//                image_seat_type.setImageResource(R.drawable.img_seat_type_gray);
//                break;
//
//        }
//    }

    public void setUIEnable(int enable,int status,long carId){
        mEnable=enable;
        mStatus=status;
        mCarId=carId;
        switch (enable){
            case 1:
                if(comand%2==0){
                    if(mStatus==0){
                        image_seat_status.setImageResource(R.drawable.img_seat_light_gray_heat);
                    }else{
                        image_seat_status.setImageResource(R.drawable.img_seat_light_red_heat);
                    }
                }else{
                    if(mStatus==0){
                        image_seat_status.setImageResource(R.drawable.img_seat_light_gray);
                    }else{
                        image_seat_status.setImageResource(R.drawable.img_seat_light_blue);
                    }
                }
                break;
            case 2:
                if(comand%2==0){
                    image_seat_status.setImageResource(R.drawable.img_seat_type_gray_heat);
                }else{
                    image_seat_status.setImageResource(R.drawable.img_seat_type_gray);
                }

                break;

        }


    }
//    private void showVibrator() {
//        Vibrator vibrator = (Vibrator) GlobalContext.getContext().getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(300L);//重复两次上面的pattern 如果只想震动一次，index设为-1
//    }

    @Override
    protected void initViews() {
        setInitUI();
    }

    @Override
    protected void initEvents() {
//        image_bg.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
////               showVibrator();
//                Log.e("弹窗", "开启");
////                ClipPopLoading.getInstance().show(mParentView);
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        if(ClipPopLoading.getInstance().getIsShowing()){
////                            ClipPopLoading.getInstance().stopLoading();
////                        }
////                    }
////                },5000L);
//                if(mEnable==1){
//                    //更改状态开的变成关 关变成开
//                    changeStatus();
//                    switch (comand){
//                        case 1:
//                        case 3:
//                            //发送通风命令
////                            OCtrlCar.getInstance().ccmd1253_seat_airheat_control(mCarId,12,getOpenOrClose());
//                            setNewStatusAirUI();
//                            break;
//                        case 2:
//                        case 4:
//                            setNewStatusHeatUI();
////                            //发送加热命令
////                            OCtrlCar.getInstance().ccmd1253_seat_airheat_control(mCarId,11,getOpenOrClose());
//
//                            break;
//                    }
//                }else{
//                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("当前设备不支持").show();
//                }
//                return false;
//            }
//        });
        image_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //               showVibrator();
                Log.e("弹窗", "开启");
//                ClipPopLoading.getInstance().show(mParentView);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(ClipPopLoading.getInstance().getIsShowing()){
//                            ClipPopLoading.getInstance().stopLoading();
//                        }
//                    }
//                },5000L);
                if(mEnable==1){
                    ODispatcher.dispatchEvent(OEventName.TOUCH_AIR_CONTROL);
                    //更改状态开的变成关 关变成开
                    changeStatus();
                    switch (comand){
                        case 1:
                        case 3:
                            //发送通风命令
//                            OCtrlCar.getInstance().ccmd1253_seat_airheat_control(mCarId,12,getOpenOrClose());
                            setNewStatusAirUI();
                            break;
                        case 2:
                        case 4:
                            setNewStatusHeatUI();
//                            //发送加热命令
//                            OCtrlCar.getInstance().ccmd1253_seat_airheat_control(mCarId,11,getOpenOrClose());

                            break;
                    }
                }else{
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "暂不支持该功能");
//                    new ToastTxt(GlobalContext.getCurrentActivity(), null).withInfo("暂不支持该功能").show();
                }
            }
        });
    }

    private void changeStatus(){
        if(mStatus==0){
            mStatus=1;
        }else{
            mStatus=0;
        }
    }

    private void setNewStatusAirUI(){
        if(mStatus==0){
            image_seat_status.setImageResource(R.drawable.img_seat_light_gray);
        }else{
            image_seat_status.setImageResource(R.drawable.img_seat_light_blue);
        }
    }

    private void setNewStatusHeatUI(){
        if(mStatus==0){
            image_seat_status.setImageResource(R.drawable.img_seat_light_gray_heat);
        }else{
            image_seat_status.setImageResource(R.drawable.img_seat_light_red_heat);
        }
    }

    private int getOpenOrClose(){
        switch (comand){
            case 1:
            case 2:
                if(mStatus==0){
                    return 1;
                }else{
                    return 0;
                }
            case 3:
            case 4:
                if(mStatus==0){
                    return 3;
                }else{
                    return 2;
                }
        }
        return 0;
    }

//    @Override
//    public void receiveEvent(String s, Object o) {
//        if(s.equals(OEventName.CAR_STATUS_SECOND_CHANGE)){
//            Log.e("弹窗", "关闭");
//            handler.sendEmptyMessage(1);
//        }
//    }
//    private Handler handler=new Handler(Looper.getMainLooper()){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//           if(msg.what==1){
//               if(ClipPopLoading.getInstance().getIsShowing()){
//                   ClipPopLoading.getInstance().stopLoading();
//               }
//           }
//        }
//    };

    @Override
    protected void invalidateUI() {

    }
}



