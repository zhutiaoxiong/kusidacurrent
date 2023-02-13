package view.view4control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.linkscarpods.service.NetManager;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import model.ManagerCarList;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.skin.DataCarSkin;
import view.EquipmentManager;
import view.basicview.CheckForgroundUtils;

import static model.ManagerSkins.DEFAULT_NAME_SKIN;
import static model.ManagerSkins.TRANSPARENT;


/**
 * 车身
 */
public class ViewCarThreePart extends LinearLayout implements OEventObject {
    private ViewCarPartThreeItem itemitem_door, item_window, item_lock, item_truck, item_light;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                if(EquipmentManager.isMini()||EquipmentManager.isShouweiSix()){
                    if(MiniDataIsShowLock.isShowLock){
                        ViewCarThreePart.this.setVisibility(View.VISIBLE);
                        item_lock.setVisibility(View.VISIBLE);
                        itemitem_door.setVisibility(View.INVISIBLE);
                        item_window.setVisibility(View.INVISIBLE);
                        item_truck.setVisibility(View.INVISIBLE);
                        item_light.setVisibility(View.INVISIBLE);
                    }else{
                        ViewCarThreePart.this.setVisibility(View.INVISIBLE);
                        item_lock.setVisibility(View.VISIBLE);
                        itemitem_door.setVisibility(View.INVISIBLE);
                        item_window.setVisibility(View.INVISIBLE);
                        item_truck.setVisibility(View.INVISIBLE);
                        item_light.setVisibility(View.INVISIBLE);
                    }
                }else{
                    ViewCarThreePart.this.setVisibility(View.VISIBLE);
                    item_lock.setVisibility(View.VISIBLE);
                    itemitem_door.setVisibility(View.VISIBLE);
                    item_window.setVisibility(View.VISIBLE);
                    item_truck.setVisibility(View.VISIBLE);
                    item_light.setVisibility(View.VISIBLE);
                }
                DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                if (car == null) {
                    itemitem_door.iv_top.setImageResource(R.drawable.img_part_car_door_close_no);
                    item_window.iv_top.setImageResource(R.drawable.img_part_window_close_no);
                    item_lock.iv_top.setImageResource(R.drawable.img_part_car_lock_yes);
                    item_truck.iv_top.setImageResource(R.drawable.img_part_car_truck_close_no);
                    item_light.iv_top.setImageResource(R.drawable.img_part_car_light_close_no);
                    itemitem_door.txt_bottom.setText("车门已关");
                    item_window.txt_bottom.setText("车窗已关");
                    item_lock.txt_bottom.setText("车辆已锁");
                    item_truck.txt_bottom.setText("尾箱已关");
                    item_light.txt_bottom.setText("大灯已关");
                } else {
                    if (car.isActive == 0) {
                        itemitem_door.iv_top.setImageResource(R.drawable.img_part_car_door_close_no);
                        item_window.iv_top.setImageResource(R.drawable.img_part_window_close_no);
                        item_lock.iv_top.setImageResource(R.drawable.img_part_car_lock_yes);
                        item_truck.iv_top.setImageResource(R.drawable.img_part_car_truck_close_no);
                        item_light.iv_top.setImageResource(R.drawable.img_part_car_light_close_no);
                        itemitem_door.txt_bottom.setText("车门已关");
                        item_window.txt_bottom.setText("车窗已关");
                        item_lock.txt_bottom.setText("车辆已锁");
                        item_truck.txt_bottom.setText("尾箱已关");
                        item_light.txt_bottom.setText("大灯已关");
                    } else {
                        if (status == null) {
                            itemitem_door.iv_top.setImageResource(R.drawable.img_part_car_door_close_no);
                            item_window.iv_top.setImageResource(R.drawable.img_part_window_close_no);
                            item_lock.iv_top.setImageResource(R.drawable.img_part_car_lock_yes);
                            item_truck.iv_top.setImageResource(R.drawable.img_part_car_truck_close_no);
                            item_light.iv_top.setImageResource(R.drawable.img_part_car_light_close_no);
                            itemitem_door.txt_bottom.setText("车门已关");
                            item_window.txt_bottom.setText("车窗已关");
                            item_lock.txt_bottom.setText("车辆已锁");
                            item_truck.txt_bottom.setText("尾箱已关");
                            item_light.txt_bottom.setText("大灯已关");
                        } else {
                            if(status.lightOpen==1){
                                item_light.iv_top.setImageResource(R.drawable.img_part_car_light_close_yes);
                                item_light.txt_bottom.setText("大灯未关");
                            }else{
                                item_light.iv_top.setImageResource(R.drawable.img_part_car_light_close_no);
                                item_light.txt_bottom.setText("大灯已关");
                            }

                            if(status.leftFrontWindowOpen==0&&status.rightFrontWindowOpen==0&&status.leftBehindWindowOpen==0&&status.rightBehindWindowOpen==0){
                                item_window.iv_top.setImageResource(R.drawable.img_part_window_close_no);
                                item_window.txt_bottom.setText("车窗已关");
                            }else{
                                item_window.iv_top.setImageResource(R.drawable.img_part_window_close_yes);
                                item_window.txt_bottom.setText("车窗未关");
                            }

                            if(status.leftFrontOpen==0&&status.rightFrontOpen==0&&status.leftBehindOpen==0&&status.rightBehindOpen==0){
                                itemitem_door.iv_top.setImageResource(R.drawable.img_part_car_door_close_no);
                                itemitem_door.txt_bottom.setText("车门已关");
                            }else{
                                itemitem_door.iv_top.setImageResource(R.drawable.img_part_car_door_close_yes);
                                itemitem_door.txt_bottom.setText("车门未关");
                            }

                            if(status.isLock==1){
                                item_lock.iv_top.setImageResource(R.drawable.img_part_car_lock_yes);
                                item_lock.txt_bottom.setText("车辆已锁");
                            }else{
                                item_lock.iv_top.setImageResource(R.drawable.img_part_car_lock_no);
                                item_lock.txt_bottom.setText("车辆未锁");
                            }

                            if(status.afterBehindOpen==0){
                                item_truck.iv_top.setImageResource(R.drawable.img_part_car_truck_close_no);
                                item_truck.txt_bottom.setText("尾箱已关");
                            }else{
                                item_truck.iv_top.setImageResource(R.drawable.img_part_car_truck_close_yes);
                                item_truck.txt_bottom.setText("尾箱未关");
                            }
                        }
                    }
                }
            }
        }
    };

    public ViewCarThreePart(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_control_car_three_part, this, true);
        itemitem_door = findViewById(R.id.item_door);
        item_window = findViewById(R.id.item_window);
        item_lock = findViewById(R.id.item_lock);
        item_truck = findViewById(R.id.item_truck);
        item_light = findViewById(R.id.item_light);
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
    }

    @Override
    public void receiveEvent(String eventName, Object o) {
        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
            handler.sendEmptyMessage(1);
        }
    }
}
