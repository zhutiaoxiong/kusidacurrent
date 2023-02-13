package view.view4control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import view.basicview.CheckForgroundUtils;

import static model.ManagerSkins.DEFAULT_NAME_SKIN;
import static model.ManagerSkins.TRANSPARENT;


/**
 * 车身
 */
public class ViewCarBodyContain extends RelativeLayout implements OEventObject {
    private long currentCarId;
    private ViewCarBody car_body;
//    private ImageView   car_fan;
//    private Animation anmiRotate;
//    private int bodyW,bodyH,thisHigh,thisWidth;
    public ViewCarBodyContain(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        LayoutInflater.from(context).inflate(R.layout.view_control_car_contain, this, true);
        car_body = (ViewCarBody) findViewById(R.id.car_body);
//        car_fan = (ImageView) findViewById(R.id.car_fan);
//        Log.i("动画", "创建一个动画对象");
//        anmiRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
//        LinearInterpolator lir = new LinearInterpolator();
//        anmiRotate.setInterpolator(lir);//必设不然无法均速
//        anmiRotate.setFillAfter(true);
//        anmiRotate.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {}
//            @Override
//            public void onAnimationEnd(Animation animation) {}
//            @Override
//            public void onAnimationRepeat(Animation animation) {}
//        });
//        car_body.setOnImageLoadCompleted(new ViewCarBody.OnImageLoadCompleted() {
//            @Override
//            public void OnImageLoadCompleted(int bodyW, int bodyH) {
//                ViewCarBodyContain.this.bodyW = bodyW;
//                ViewCarBodyContain.this.bodyH = bodyH;
//            }
//        });
//        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
//        ODispatcher.addEventListener(OEventName.STOP_ANIM_ROTATE, this);
    }


    @Override
    protected void onDetachedFromWindow() {
//        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
//        ODispatcher.removeEventListener(OEventName.STOP_ANIM_ROTATE, this);
        super.onDetachedFromWindow();
    }
//    private long reFreshTime;

    @Override
    public void receiveEvent(String eventName, Object o) {
//        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
////            long currentTime=System.currentTimeMillis();
////            if((currentTime-reFreshTime>1*1000)){
////                reFreshTime=currentTime;
//                if(CheckForgroundUtils.isAppForeground()){
//                    handlerSetImage();
//                }
////            }
//        }else if(OEventName.STOP_ANIM_ROTATE.equals(eventName)){
//            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(car_fan!=null){
//                        car_fan.clearAnimation();
//                         if (BuildConfig.DEBUG) Log.e("Fan","STOP Rotate");
//                    }
//                }
//            });
//        }
    }

    //修改车状态
//    public void changeData(long carId) {
//        currentCarId = carId;
//        car_body.changeData(carId);
//    }

    private Drawable getImage(String url, String name) {
        if (ManagerSkins.TRANSPARENT.equals(name)) {
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false, (TextUtils.isEmpty(url) ? DEFAULT_NAME_SKIN : url), name));
    }

    private void handlerSetImage() {
//        if (GlobalContext.getCurrentActivity() == null) return;
//        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//               //重开无UI
//                final DataCarInfo carInfoUse = ManagerCarList.getInstance().getCarByID(currentCarId);
//                if (carInfoUse == null) return;
//                DataCarStatus carStatus;
//                if(carInfoUse.isActive==0){
//                    carStatus=new DataCarStatus();
//                }else{
//                    if(!NetManager.instance().isNetworkConnected()&&!BlueLinkReceiver.getIsBlueConnOK()){
//                        carStatus=new DataCarStatus();
//                    }else{
//                        carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
//                    }
//                }
//                if(carStatus.isStart == 1){
//                    car_fan.startAnimation(anmiRotate);
//                }else{
//                    car_fan.clearAnimation();
//                }
//                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
//                if (currentCar.ide != carInfoUse.ide) return;
//
//                String url = "";//使用默认的
//                if (carInfoUse != null && carInfoUse.carTypeInfo != null) {//使用网络的
//                    if(carInfoUse.getCarSkin().ide!=0)
//                    url = carInfoUse.getCarSkin().url;
//                }
//                if(url.contains("ios")){
//                    url="";
//                }
//                //开始设置
//                DataCarSkin skinSetup = ManagerSkins.getInstance().getSkinSetup(ManagerSkins.getSkinZipFileName(url));
//                if(skinSetup==null){
//                    skinSetup=ManagerSkins.getInstance().getSkinSetup(ManagerSkins.getSkinZipFileName(""));
//                }
//                Drawable fan = getImage(url,"fan");
//                if(fan==null){
//                    url="";
//                    fan=getImage("","fan");
//                }
//                /**用于解决部分手机车身变小问题**/
//                if(bodyW!=0 && bodyH!=0 && thisHigh!=0 && thisWidth!=0){
//                    ViewGroup.LayoutParams lp = car_body.getLayoutParams();
////                    lp.width = bodyW*(thisHigh-20)/bodyH;
////                    lp.height = thisHigh-20;
//                    lp.width = bodyW*(thisHigh)/bodyH;
//                    lp.height = thisHigh;
//                    car_body.setLayoutParams(lp);
//                    car_body.setVisibility(VISIBLE);
//                }
//                /**用于解决部分手机车身变小问题**/
//                if(bodyH == 0 || thisHigh == 0 || fan == null || skinSetup == null)return;//数据未加载完
////                double density = (double) (thisHigh-20)/(double) bodyH;//缩放比 1114/688
//                double density = (double) (thisHigh)/(double) bodyH;//缩放比 1114/688
//
//                LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                param.height = (int) (skinSetup.fan.h*density);
//                param.width = (int) (skinSetup.fan.w*density);
//                param.setMargins(0, (int) (skinSetup.fan.y*density) , 0, 0); // Margin
//                param.addRule(RelativeLayout.CENTER_HORIZONTAL);//水平居中
//                if(imageSet(car_fan,fan,url)){
//                    car_fan.setLayoutParams(param);
//                }
//            }
//        });
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        thisHigh = this.getMeasuredHeight();
//        thisWidth = this.getMeasuredWidth();
    }
    /**用于解决部分手机车身变小问题end**/

    private void textSet(TextView view, String text) {
        if(text.equals(view.getText().toString()))return;
        view.setText(text);
    }
    //不重复设图
    private boolean imageSet(ImageView image, Drawable drawable,String url) {
        if(image == null || drawable == null || url == null)return false;
        if (image.getTag() == null || !((String) image.getTag()).equals(url)) {
            image.setTag(url);
            image.setImageDrawable(drawable);
//             if (BuildConfig.DEBUG) Log.e("秒刷","Car fan 刷新了");
            return true;
        }
        return false;
    }
    private Drawable scaleImage(Drawable src, DataCarSkin.SkinPos skinPos, int maxW, int maxH) {
        if(src instanceof PaintDrawable)return src;
        Bitmap mBitmap = Bitmap.createBitmap(maxW, maxH, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);
        BitmapDrawable bt = (BitmapDrawable) src;
        mCanvas.drawBitmap(bt.getBitmap(), skinPos.x, skinPos.y, null);
        mCanvas.save();
        return new BitmapDrawable(getContext().getResources(), mBitmap);
    }
}
