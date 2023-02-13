package view.view4control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.time.CountDownTimerMy;

import java.util.List;

import adapter.viewpager.ViewPagerChild;
import common.GlobalContext;
import common.blue.BlueLinkNetSwitch;
import common.blue.BlueLinkReceiver;
import ctrl.OCtrlCar;
import model.ManagerCarList;
import model.ManagerLoginReg;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.demomode.DemoMode;
import model.status.DataSwitch;

import static model.ManagerSkins.DEFAULT_NAME_SKIN;
import static model.ManagerSkins.TRANSPARENT;

/**
 * 仅车相关的
 * countDownTimeLightShine
 */
public class ViewControlPanelCar extends RelativeLayout implements OEventObject {
    private ImageView img_bg, img_findcar;
    private TextView txt_carname;
    private AdapterViewPagerCar adapter;
    private ViewPagerChild view_pager;
    private LinearLayout dot_horizontal;
    private LayoutInflater inflater;

    public ViewControlPanelCar(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_control_panel_car, this, true);
        img_bg = (ImageView) findViewById(R.id.img_bg);
        txt_carname = (TextView) findViewById(R.id.txt_carname);
        img_findcar = (ImageView) findViewById(R.id.img_findcar);
        view_pager = (ViewPagerChild) findViewById(R.id.view_pager);
        dot_horizontal = (LinearLayout) findViewById(R.id.dot_horizontal);
        ODispatcher.addEventListener(OEventName.CONTROL_FINDCAR,this);
        ODispatcher.addEventListener(OEventName.CHANGE_CURRENT_CAR_TO_MANAGER,this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ODispatcher.removeEventListener(OEventName.CONTROL_FINDCAR,this);
        ODispatcher.removeEventListener(OEventName.CHANGE_CURRENT_CAR_TO_MANAGER,this);
    }


    /**
     * 1.listSize change changeAll
     * 2.skinchange 1 car bg/light/fan
     * 3.闪灯
     * 4.改皮肤
     */
    private int preSize = -1;

    public void changeData() {
        if (BuildConfig.DEBUG) Log.e("PanelCar"," GlobalContext.getCurrentActivity()>>>>>>>>>"+ GlobalContext.getCurrentActivity());
        if( GlobalContext.getCurrentActivity()==null)return;
        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 List<DataCarInfo> carInfoList = ManagerCarList.getInstance().getCarInfoList();
                if (preSize == carInfoList.size()) {
                    if (BuildConfig.DEBUG) Log.e("PanelCar","Size no change>>>>>>>>>");
                } else {//size有变化
                     if (BuildConfig.DEBUG) Log.e("PanelCar","Size change>>>>>>>>>+view_pager"+view_pager);
                     int currentI=getCurrentItemViewPager();
                    view_pager.addOnPageChangeListener(new DotIndicator(getContext(), dot_horizontal, carInfoList.size(), currentI,new DotIndicator.onPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            if (BuildConfig.DEBUG) Log.e("PanelCar","page select");
                            List<DataCarInfo> carInfoList = ManagerCarList.getInstance().getCarInfoList();
                            MiniDataIsShowLock.isLockChange=-1;
                            if( ViewControlPanelControl.countControlTimer!=null){
                                ViewControlPanelControl.countControlTimer.cancel();
                            }
                            if(carInfoList == null || carInfoList.size() == 0)return;
                            if(position>= carInfoList.size())return;
                            DataCarInfo car = carInfoList.get(position);
                            boolean isDemoMode = DemoMode.getIsDemoMode();
//                            if (isDemoMode) {
//////                    if (DemoMode.isDemoMode.equals("演示开始")) {
////                                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 1);
////                            } else {
////                                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 0);
////                            }
                            if (isDemoMode) {
//                    if (DemoMode.isDemoMode.equals("演示开始")) {
                                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 1,true,0);
                            } else {
                                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 0);
//                                OCtrlCar.getInstance().ccmd1219_getCarState(car.ide, 0,true,0);
                            }
                            BlueLinkReceiver.clearBluetooth();
                            ManagerCarList.getInstance().setCurrentCar(car.ide);
//                            ManagerCarList.getInstance().saveCarStatus(new JsonObject(),"pagechange");
                            //是否开启咬一咬，自动连蓝牙
                            DataSwitch dataSwitch = BlueLinkNetSwitch.getSwitchShakeInfo();
//                            if(dataSwitch.isOpen == 1){
//                                BlueLinkReceiver.needChangeCar(car.ide,car.bluetoothName,car.blueCarsig,car.isBindBluetooth,car.carsig);
//                            }else{
//                                BlueLinkReceiver.needChangeCar(0,"","",car.isBindBluetooth,car.carsig);
//                            }

//                            if(car==null||car.ide==0||car.isActive==0){
//                                BlueLinkReceiver.needChangeCar(0,"","",car.isBindBluetooth,car.carsig);
//                            }else{
//                                BlueLinkReceiver.needChangeCar(car.ide,car.bluetoothName,car.blueCarsig,car.isBindBluetooth,car.carsig);
//                            }

                            int isBind=car.isKeyBind;//是否綁定藍牙液晶鑰匙
                            int isKeyOpen=car.isKeyOpen;
                            //每次切换车的时候先断蓝牙连接
                          long userId=  ManagerLoginReg.getInstance().getCurrentUser().userId;
                            if(isBind==1){
                                BlueLinkReceiver.needChangeLcdKey(car.ide,car.keyBlueName,car.keySig,isBind,isKeyOpen,userId);
                            }else{
                                BlueLinkReceiver.needChangeLcdKey(0,"","",isBind,isKeyOpen,userId);
                            }
//                            if(MyLcdBlueAdapter.getInstance().getIsConnectted()){
//                                MyLcdBlueAdapter.getInstance().closeBlueReal();
//                            }
                            ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
                            ODispatcher.dispatchEvent(OEventName.CAR_SELECT_CHANGE);
                        }
                    }));
                    if (adapter == null) {
                        adapter = new AdapterViewPagerCar(view_pager, carInfoList);
                        view_pager.setAdapter(adapter);
                    } else {
                        adapter.setData(carInfoList);
                        adapter.notifyDataSetChanged();
                    }
                    //初始化设当前车
                    for(int i = 0;i<carInfoList.size();i++){
                        if(ManagerCarList.getInstance().getCurrentCar().ide == carInfoList.get(i).ide){
                            view_pager.setCurrentItem(i);
                            ManagerCarList.getInstance().setCurrentCar(carInfoList.get(i).ide);
                        }
                    }
                    preSize = carInfoList.size();
                }
                changeStyle();
            }
        });
    }

    private int getCurrentItemViewPager(){
        DataCarInfo info=ManagerCarList.getInstance().getCurrentCar();
        if(info==null||info.ide==0)return 0;
        List<DataCarInfo> carInfoList = ManagerCarList.getInstance().getCarInfoList();
        if(carInfoList==null||carInfoList.size()==0)return 0;
        for(int i = 0;i<carInfoList.size();i++){
            if(info.ide == carInfoList.get(i).ide){
               return i;
            }
        }
        return 0;
    }
    /**
     * 改皮肤
     */
    private void changeStyle() {
        DataCarInfo dataCarInfo = ManagerCarList.getInstance().getCurrentCar();
        if (dataCarInfo != null) {
            adapter.setCurrentPos(dataCarInfo.carPos);
            //车牌号
            textSet(txt_carname,dataCarInfo.num);
            if (dataCarInfo.isActive == 1) {//未激活的车名置灰
                txt_carname.setTextColor(getResources().getColor(R.color.white));
            } else {
                txt_carname.setTextColor(getResources().getColor(R.color.gray));
            }
            //灯光
            if(dataCarInfo.getCarSkin()!=null&&!TextUtils.isEmpty(dataCarInfo.getCarSkin().url)){
                Drawable light =  getImage(dataCarInfo.getCarSkin().url,"bottom_light");
                if(light==null){
                    dataCarInfo.getCarSkin().url="";
                    light =  getImage("","bottom_light");
                }
                imageSet(img_findcar,light,dataCarInfo.getCarSkin().url);
            }

            //背景
            Drawable bg;
            if(dataCarInfo.getCarBackground()!=null){

                String urlbg = dataCarInfo.getCarBackground().url;
                if(urlbg!=null){
                    if(urlbg.endsWith("png")){
                        imageSetResourse(img_bg,urlbg);
                    }else if(urlbg.equals("0")){
                        bg = getImage(ManagerSkins.DEFAULT_NAME_TEMP, "car_bg");
                        imageSet(img_bg,bg,ManagerSkins.DEFAULT_NAME_TEMP);
                    }else{
                        ManagerSkins.getInstance().loadCarBg(getContext(),urlbg,"car_bg",null);
                        bg = getImage(urlbg, "car_bg");
                        if(bg == null){
                            bg = getImage(ManagerSkins.DEFAULT_NAME_TEMP, "car_bg");//处理还未下载完的
                            imageSet(img_bg,bg,ManagerSkins.DEFAULT_NAME_TEMP);
                        }else{
                            imageSet(img_bg,bg,urlbg);
                        }
                    }
                }
            }else if (dataCarInfo.skinTemplateInfo != null) {
                if(!TextUtils.isEmpty(dataCarInfo.getCarTemplate().url)){
                    bg = getImage(dataCarInfo.getCarTemplate().url, "car_bg");
                    imageSet(img_bg,bg,dataCarInfo.getCarTemplate().url);
                }
            } else {//是默认的
                bg = getImage(ManagerSkins.DEFAULT_NAME_TEMP, "car_bg");
                imageSet(img_bg,bg,ManagerSkins.DEFAULT_NAME_TEMP);
            }
        }
    }

    private void textSet(TextView view, String text) {
        if(text.equals(view.getText().toString()))return;
        view.setText(text);
    }
    //不重复设图
    private void imageSet(ImageView image, Drawable drawable,String url) {
         if (BuildConfig.DEBUG) Log.e("设置图片", "url"+url );
        if(image == null || drawable == null || url == null)return;
        if (image.getTag() == null || !((String) image.getTag()).equals(url)) {
            image.setTag(url);
            image.setImageDrawable(drawable);
//             if (BuildConfig.DEBUG) Log.e("秒刷","Car light bg 刷新了"+url);
        }
    }
    //不重复设图
    private void imageSetResourse(ImageView image,String url) {
         if (BuildConfig.DEBUG) Log.e("设置图片", "url"+url+"科幻" );
        if(image == null || url == null)return;
        if (image.getTag() == null || !((String) image.getTag()).equals(url)) {
            image.setTag(url);
            image.setImageResource(R.drawable.car_bg_kehuan);
//             if (BuildConfig.DEBUG) Log.e("秒刷","Car light bg 刷新了"+url);
        }
    }
    private Drawable getImage(String url, String name) {
        if(ManagerSkins.TRANSPARENT.equals(name)){
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false, (TextUtils.isEmpty(url) ? DEFAULT_NAME_SKIN : url), name));
    }
    @Override
    public void receiveEvent(String s, Object o) {
        if(OEventName.CONTROL_FINDCAR.equals(s)){
//            countDownTimeLightShine.start();
        }else if(OEventName.CHANGE_CURRENT_CAR_TO_MANAGER.equals(s)){
            handlerParent.obtainMessage(CHANGE_CURRENT_CAR_TO_MANAGER).sendToTarget();
//            if(view_pager!=null && adapter!=null){
//                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
//                if(adapter.listCar!=null && adapter.listCar.size()>=2){
//                    for(int i = 0;i<adapter.listCar.size();i++){
//                        if(adapter.listCar.get(i).ide == currentCar.ide){
//                            if(view_pager.getCurrentItem()!=i){
//                                view_pager.setCurrentItem(i);
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    private CountDownTimerMy countDownTimeLightShine = new CountDownTimerMy(1800, 300) {
        @Override
        public void onTick(long millisUntilFinished) {
             if (BuildConfig.DEBUG) Log.e("寻车灯", " " + millisUntilFinished);
            if (1500 < millisUntilFinished && millisUntilFinished <= 1800) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_SHOW).sendToTarget();
            } else if (1200 < millisUntilFinished && millisUntilFinished <= 1500) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_HIDE).sendToTarget();
            } else if (900 < millisUntilFinished && millisUntilFinished <= 1200) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_SHOW).sendToTarget();
            } else if (600 < millisUntilFinished && millisUntilFinished <= 900) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_HIDE).sendToTarget();
            } else if (300 < millisUntilFinished && millisUntilFinished <= 600) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_SHOW).sendToTarget();
            }
        }

        @Override
        public void onFinish() {
            handlerParent.obtainMessage(CAR_LIGHTFIND_HIDE).sendToTarget();
        }
    };

    private static final int CAR_LIGHTFIND_SHOW = 1112;
    private static final int CAR_LIGHTFIND_HIDE = 1113;
    private static final int CHANGE_CURRENT_CAR_TO_MANAGER = 1114;

    protected Handler handlerParent = new MyHandler();


    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1112:
                     if (BuildConfig.DEBUG) Log.e("寻车灯", " 显示");
                    img_findcar.setVisibility(View.VISIBLE);
                    break;
                case 1113:
                     if (BuildConfig.DEBUG) Log.e("寻车灯", "隐藏 ");
                    img_findcar.setVisibility(View.INVISIBLE);
                    break;
                case 1114:
                    if(view_pager!=null && adapter!=null){
                        DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                        if(adapter.listCar!=null && adapter.listCar.size()>=2){
                            for(int i = 0;i<adapter.listCar.size();i++){
                                if(adapter.listCar.get(i).ide == currentCar.ide){
                                    if(view_pager.getCurrentItem()!=i){
                                        view_pager.setCurrentItem(i);
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    // ===================================================

}
