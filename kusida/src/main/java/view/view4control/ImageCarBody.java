package view.view4control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import common.GlobalContext;
import model.ManagerCarList;
import model.ManagerSkins;
import model.carlist.DataCarInfo;
import model.carlist.DataCarStatus;
import model.skin.DataCarSkin;
import view.EquipmentManager;
import view.basicview.CheckForgroundUtils;

import static annualreminder.model.ManagerAnnual.currentCarId;
import static model.ManagerSkins.DEFAULT_NAME_SKIN;
import static model.ManagerSkins.TRANSPARENT;

public class ImageCarBody extends androidx.appcompat.widget.AppCompatImageView implements OEventObject {
        private int bodyW,bodyH;
//    private Drawable stickerImage;
//    private long currentCarId;
//    private static String needReLoadName = "car_lightfind";

    public ImageCarBody(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
        ODispatcher.addEventListener(OEventName.SHOW_CAR_BODY,this);
//        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.SHOW_CAR_BODY,this);
//        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE, this);
        super.onDetachedFromWindow();
    }
//    private long refreshTime;

    @Override
    public void receiveEvent(String eventName, Object o) {
        if (eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)) {
//            long currentTime=System.currentTimeMillis();
//            if((currentTime-refreshTime)>1*1000){
//                refreshTime=currentTime;
//                if(CheckForgroundUtils.isAppForeground()){
//                    handlerSetImage();
//                    changeBodyParent();
//                }
//            }
        }else if (eventName.equals(OEventName.SHOW_CAR_BODY)) {
            handlerSetImage();
        }
    }

    //???????????????
//    public void changeData(long carId) {
//        currentCarId = carId;
//    }

    private Drawable getImage(String url, String name) {
        if (ManagerSkins.TRANSPARENT.equals(name)) {
            return ManagerSkins.getInstance().getPngImage(TRANSPARENT);
        }
        return ManagerSkins.getInstance().getPngImage(ManagerSkins.getCacheKey(false, (TextUtils.isEmpty(url) ? DEFAULT_NAME_SKIN : url), name));
    }

    //????????????????????????????????????????????????
    private String preState = "-1",preSkin = "-1",preUrl = "-1",cacheState="-1";
    private String url;
    private DataCarStatus carStatus;

    private void handlerSetImage() {
        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (GlobalContext.getCurrentActivity() == null) return;//?????????UI
                DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                if(currentCar.isActive==0){
                    carStatus = new DataCarStatus();
                }else{
                    carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                }
//                String url = "";//???????????????
                url="";
                if (currentCar.carTypeInfo != null) {//???????????????
                    if(currentCar.getCarSkin().ide!=0){
                        url = currentCar.getCarSkin().url;
                    }
                }
                if(url.contains("ios")){
                    url="";
                }

                //??????
                String typeInfo = (currentCar.carTypeInfo == null) ? "" : currentCar.carTypeInfo.toString();
                if(MiniDataIsShowLock.isChange==1||MiniDataIsShowLock.isChange==2){
                    MiniDataIsShowLock.isChange=0;
                   ManagerSkins.getInstance().loadSkin(getContext(), url, "body", new ManagerSkins.OnLoadPngListener() {
                            @Override
                            public void loadCompleted(Drawable image) {
                                GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (BuildConfig.DEBUG)    Log.e("?????????", "???????????????" );
                                        setCarSkin(url);
                                    }
                                });

                            }

                            @Override
                            public void loadFail(String errorInfo) {
                                GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (BuildConfig.DEBUG)   Log.e("?????????", "???????????????" );
                                        url="";
                                        setCarSkin("");
                                    }
                                });
                            }
                        });

                }

                //??????

                ManagerSkins.getInstance().loadSkin(getContext(), url, "body", new ManagerSkins.OnLoadPngListener() {
                    @Override
                    public void loadCompleted(Drawable image) {
                        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (BuildConfig.DEBUG)    Log.e("?????????", "???????????????" );
                                setCarSkin(url);
                            }
                        });

                    }

                    @Override
                    public void loadFail(String errorInfo) {
                        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (BuildConfig.DEBUG)   Log.e("?????????", "???????????????" );
                                url="";
                                setCarSkin("");
                            }
                        });
                    }
                });

            }
        });
    }

    private CacheStatus createCacheStatus (DataCarStatus status){
        CacheStatus cacheStatus=new CacheStatus();
        cacheStatus.carId=status.carId;
        cacheStatus.isLock=status.isLock;
        cacheStatus.lightOpen=status.lightOpen;
        cacheStatus.skyWindowOpen=status.skyWindowOpen;
        cacheStatus.leftFrontOpen=status.leftFrontOpen;
        cacheStatus.rightFrontOpen=status.rightFrontOpen;
        cacheStatus.leftBehindOpen=status.leftBehindOpen;
        cacheStatus.rightBehindOpen=status.rightBehindOpen;
        cacheStatus.afterBehindOpen=status.afterBehindOpen;
        cacheStatus.leftFrontWindowOpen=status.leftFrontWindowOpen;
        cacheStatus.rightFrontWindowOpen=status.rightFrontWindowOpen;
        cacheStatus.leftBehindWindowOpen=status.leftBehindWindowOpen;
        cacheStatus.rightBehindWindowOpen=status.rightBehindWindowOpen;
        cacheStatus.isTheft =status.isTheft;
        return cacheStatus;
    }

    private void setCarSkin(String url){
        //????????????
        Drawable[] array = new Drawable[9];
        //body
        array[0] = getImage(url, "body");
        if(array[0] instanceof BitmapDrawable){
            BitmapDrawable draw = (BitmapDrawable)array[0];
            bodyW = draw.getBitmap().getWidth();
            bodyH = draw.getBitmap().getHeight();
        }
        //light_out
        if (carStatus.lightOpen == 1) {
            array[1] = getImage(url, "light_out");
        } else {
            array[1] = getImage(url, "TRANSPARENT");
        }
        //car_lt????????? door_lefttop_open_win
        array[2] = getImage(url, "door_lefttop_"
                + DataCarStatus.getOpenCloseChar(carStatus.leftFrontOpen)
                + DataCarStatus.getOpenCloseWinChar(carStatus.leftFrontWindowOpen));
        //car_lt?????????
        array[3] = getImage(url, "door_leftbottom_"
                + DataCarStatus.getOpenCloseChar(carStatus.leftBehindOpen)
                + DataCarStatus.getOpenCloseWinChar(carStatus.leftBehindWindowOpen));
        //car_lt?????????
        array[4] = getImage(url, "door_righttop_"
                + DataCarStatus.getOpenCloseChar(carStatus.rightFrontOpen)
                + DataCarStatus.getOpenCloseWinChar(carStatus.rightFrontWindowOpen));
        //car_lt?????????
        array[5] = getImage(url, "door_rightbottom_"
                + DataCarStatus.getOpenCloseChar(carStatus.rightBehindOpen)
                + DataCarStatus.getOpenCloseWinChar(carStatus.rightBehindWindowOpen));

        //?????????
        if((EquipmentManager.isMini()&&!MiniDataIsShowLock.isShowLock)||(EquipmentManager.isShouweiSix()&&!MiniDataIsShowLock.isShowLock)){
            if (BuildConfig.DEBUG) Log.e("????????????", "ViewCarBody"+"????????????");
            array[6] = getImage(url, ManagerSkins.TRANSPARENT);
        }else{
            if (BuildConfig.DEBUG) Log.e("????????????", "ViewCarBody"+"????????????");
            if (carStatus.isLock == 1) {
                array[6] = getImage(url, (carStatus.isTheft == 1) ? "lock_red" : "lock");
                if(array[6] == null)array[6] = getImage(url, "lock");//????????????
            } else {
                array[6] = getImage(url, (carStatus.isTheft == 1) ? "unlock_red" : "unlock");
                if(array[6] == null)array[6] = getImage(url, "unlock");//????????????
            }
        }

        //backpag
        if (carStatus.afterBehindOpen == 1) {
            array[7] = getImage(url, "trunk");
        } else {
            array[7] = getImage(url, "TRANSPARENT");
        }
        //skylight
        if (carStatus.skyWindowOpen == 1) {
            array[8] = getImage(url, "skylight");
            if(array[8] == null)array[8] = getImage(url, ManagerSkins.TRANSPARENT);//????????????
        } else {
            array[8] = getImage(url, ManagerSkins.TRANSPARENT);
        }
        for (Drawable drawable : array) {
            if (drawable == null) return;//???????????????????????????????????????
        }
        //??????
//        preState = DataCarStatus.toJsonObject(carStatus).toString();
//        cacheState=(CacheStatus.toJsonObject(createCacheStatus(carStatus)).toString());
//        preSkin = (carInfoUse.carTypeInfo == null) ? "" : carInfoUse.carTypeInfo.toString();
//        preUrl = url;
        //??????
        DataCarSkin skinSetup = ManagerSkins.getInstance().getSkinSetup(ManagerSkins.getSkinZipFileName(url));
        int width= skinSetup.body.w;
        int higjt= skinSetup.body.h;
        if (skinSetup != null) {
            array[1] = scaleImage(array[1], skinSetup.light_out, width, higjt);//?????????
            array[2] = scaleImage(array[2], skinSetup.door_lefttop_open, width, higjt);//?????????
            array[3] = scaleImage(array[3], skinSetup.door_leftbottom_open, width, higjt);
            array[4] = scaleImage(array[4], skinSetup.door_righttop_open, width, higjt);
            array[5] = scaleImage(array[5], skinSetup.door_rightbottom_open, width, higjt);
            array[6] = scaleImage(array[6], skinSetup.lock, width, higjt);
            array[7] = scaleImage(array[7], skinSetup.trunk, width, higjt);
            array[8] = scaleImage(array[8], skinSetup.skylight, width, higjt);
        }
        //???????????????
        LayerDrawable ldraw = new LayerDrawable(array); //??????????????????Drawable??????
        ldraw.setLayerInset(0, 0, 0, 0, 0);//car_lightback
        ldraw.setLayerInset(1, 0, 0, 0, 0);//body
        ldraw.setLayerInset(2, 0, 0, 0, 0);//?????????
        ldraw.setLayerInset(3, 0, 0, 0, 0);//?????????
        ldraw.setLayerInset(4, 0, 0, 0, 0);//?????????
        ldraw.setLayerInset(5, 0, 0, 0, 0);//?????????
        ldraw.setLayerInset(6, 0, 0, 0, 0);//?????????
        ldraw.setLayerInset(7, 0, 0, 0, 0);//backpag
        ldraw.setLayerInset(8, 0, 0, 0, 0);//skylight
        //??????
        ImageCarBody.this.setImageDrawable(ldraw);
////                 if (BuildConfig.DEBUG) Log.e("??????","Car body ?????? ?????????");
//        if (BuildConfig.DEBUG) Log.e("????????????", "ViewCarBody"+"?????????");
//        changeBodyParent();
    }
    /**??????????????????????????????????????????**/
    public interface OnImageLoadCompleted{
        public abstract void OnImageLoadCompleted(int bodyW,int bodyH);
    }
    private ViewCarBody.OnImageLoadCompleted onImageLoadCompletedListener;
    public void setOnImageLoadCompleted(ViewCarBody.OnImageLoadCompleted listener){
        onImageLoadCompletedListener = listener;
    }
    private void changeBodyParent(){
//        if(onImageLoadCompletedListener!=null && bodyW>100 && bodyH >100){
//            onImageLoadCompletedListener.OnImageLoadCompleted(bodyW,bodyH);
//        }
    }
    /**?????????????????????????????????????????? end**/

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
