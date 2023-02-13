package model;

import android.content.Intent;
import android.util.Log;

import com.client.proj.kusida.BuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.wearkulala.www.wearfunc.WearReg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import common.blue.BlueLinkReceiver;
import model.advertising.DataAdvertising;
import model.common.DataAuthorization;
import model.common.DataBrands;
import model.common.DataContact;
import model.common.DataPayWay;
import model.common.DataShare;
import model.common.DataViolation;
import model.common.DataWxPay;
import model.common.TrackShareObj;
import model.demomode.DemoMode;
import model.messageuser.DataMessageUser;
import view.ActivityKulalaMain;
import view.ActivityLogin;
import view.basicview.FragmentActionBar;
import view.view4me.lcdkey.ActivityLCDkey;
import view.view4me.lcdkey.BindLcdKeyActivity;
import view.view4me.myblue.MyLcdBlueAdapter;

public class ManagerCommon {
    public List<DataAuthorization> authorlist;//汽车权限列表
    private List<DataBrands>        brandsList;        // 汽车品牌列表
    public List<DataPayWay>        payWayList;
    public List<DataPayWay>        miniXpayWayList;
    public List<DataViolation>     violationList;
    public static String cardShareUrl = "http://api.91kulala.com/kulala/share/share_card.jsp?";//cardId=001&userId=001
    public        DataMessageUser messageUserList;//紧急消息列表
    public static DataContact     contactInfo;
    private static DataShare       shareInfo;
    public static DataWxPay       wxpayInfo;
    public static TrackShareObj   trackShareObj;
    public static String          hotLine;                                            // 服务热线
    public static String          email;                                                // 电子邮件
    public static String          dealerLine;                                            // 经销商热线
    public static DataAdvertising dataAdvertising;//广告

    private long brandUpdateTime = 0;

    // ========================out======================
    private static ManagerCommon _instance;
    private ManagerCommon() {
        violationList = new ArrayList<DataViolation>();
        payWayList = new ArrayList<DataPayWay>();
        messageUserList = new DataMessageUser();
        miniXpayWayList= new ArrayList<DataPayWay>();
    }
    public static ManagerCommon getInstance() {
        if (_instance == null)
            _instance = new ManagerCommon();
        return _instance;
    }
    // =================================================
    public long getBrandUpdateTime() {
        if (brandUpdateTime == 0) {
            String brandTime = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("brandUpdateTime");
            if (brandTime != null) {
                if (!brandTime.equals(""))
                    brandUpdateTime = Long.valueOf(brandTime);
            }
        }
        return brandUpdateTime;
    }
    public List<DataBrands>  getBrandsList(){
        getBrandUpdateTime();
        if (brandsList == null || brandsList.size() == 0){
            //品牌列表from缓存
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("brandsList");
            if (result != null && result.length()>0) {
                this.brandsList = DataBrands.fromJsonArray(ODBHelper.convertJsonArray(result));
            } else {
                //品牌列表from文件
//                InputStream inputStream = GlobalContext.getCurrentActivity().getResources().openRawResource(R.raw.brand_default_info);
//                String      strrr       = getRawString(inputStream);
//                Gson        gson        = new Gson();
//                JsonArray   arrr        = gson.fromJson(strrr, JsonArray.class);
//                this.brandsList = DataBrands.fromJsonArray(arrr);
                this.brandsList=null;
            }
        }
        return this.brandsList;
    }
    public DataBrands getBrands(String brandsName) {
        getBrandsList();
        if(brandsList!=null&&brandsList.size()>0){
            for (DataBrands brand : brandsList) {
                if (brand.name.equals(brandsName)) {
                    return brand;
                }
            }
        }
        return null;
    }
    public DataShare  getShareInfo(){
        if(shareInfo == null){
            //分享内容
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("commonShare");
            JsonObject share = ODBHelper.convertJsonObject(result);
            if (share != null) {
                shareInfo = DataShare.fromJsonObject(share);
            }
        }
        return shareInfo;
    }
    public static String getRawString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer   sb     = new StringBuffer("");
        String         line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    // =================================================
    //最多只有一个消息
    public void saveMessageUserList(JsonObject message) {
        this.messageUserList = DataMessageUser.fromJsonObject(message);
    }
    public void saveBrandList(JsonArray brands, long updateTime) {
        if (updateTime <= brandUpdateTime) return;
        if (brands == null || brands.size() <= 0) return;
        this.brandsList = DataBrands.fromJsonArray(brands);
        brandUpdateTime = updateTime;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("brandUpdateTime",String.valueOf(updateTime));
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("brandsList",ODBHelper.convertString(brands));
    }
    public void saveAuthorList(JsonArray authors) {
        authorlist = new ArrayList<DataAuthorization>();
        if (authors == null) return;
        for (int i = 0; i < authors.size(); i++) {
            JsonObject        object = authors.get(i).getAsJsonObject();
            DataAuthorization author = new DataAuthorization();
            author.jsonObjectToAuthor(object);
            authorlist.add(author);
        }
    }
    public void savePayWay(JsonArray onlinePayInfos) {
        payWayList = DataPayWay.fromJsonArray(onlinePayInfos);
        if (payWayList == null)
            payWayList = new ArrayList<DataPayWay>();
    }
    public void savePayWayMiniX(JsonArray minixOnlinePayInfos) {
        miniXpayWayList = DataPayWay.fromJsonArray(minixOnlinePayInfos);
        if (miniXpayWayList == null)
            miniXpayWayList = new ArrayList<DataPayWay>();
    }
    public void saveContact(JsonObject contact) {
        if (contact == null)
            return;
        contactInfo = DataContact.fromJsonObject(contact);
        if (contactInfo == null)
            contactInfo = new DataContact();
    }
    public void saveShare(JsonObject share) {
        if (share == null)
            return;
        shareInfo = DataShare.fromJsonObject(share);
        if (shareInfo == null)
            shareInfo = new DataShare();
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("commonShare", ODBHelper.convertString(share));
    }
    public void saveWxPay(JsonObject wx) {
        if (wx == null)
            return;
        wxpayInfo = DataWxPay.fromJsonObject(wx);
        if (wxpayInfo == null)
            wxpayInfo = new DataWxPay();
    }
    public void saveViolationList(JsonArray jsonArray) {
        if (jsonArray == null) return;
        violationList = DataViolation.fromJsonArray(jsonArray);
    }
    public void saveTrackShareObj(JsonObject share) {
        if (share == null)
            return;
        trackShareObj = TrackShareObj.fromJsonObject(share);
        if (trackShareObj == null)
            trackShareObj = new TrackShareObj();
    }
    public void saveDataAdvertising(JsonObject dataAdvertisingInfo) {
        if (dataAdvertisingInfo == null)
            return;
        dataAdvertising = DataAdvertising.fromJsonObject(dataAdvertisingInfo);
        if (dataAdvertising == null)
            dataAdvertising = new DataAdvertising();
    }
    //=====================================================
    /**
     * 只用于主Activity
     */
    private long preTime = 0;
    public void exitToLogin(final String error) {
        long now = System.currentTimeMillis();
        if (now - preTime < 1000) return;
        preTime = now;
        if (error == null || error.length() == 0) {
            //直接退
             if (BuildConfig.DEBUG) Log.e("isUserExitUser", "設置為true" );
            GlobalContext.isUserExitUser=true;
            if(MyLcdBlueAdapter.getInstance().getIsConnectted()){
                MyLcdBlueAdapter.getInstance().closeBlueReal();
            }
            exitLo();
        } else {
             if (BuildConfig.DEBUG) Log.e("isUserExitUser", "設置為true" );
            GlobalContext.isUserExitUser=true;
            if(MyLcdBlueAdapter.getInstance().getIsConnectted()){
                MyLcdBlueAdapter.getInstance().closeBlueReal();
            }
//            BlueLinkReceiver.getInstance().changeCar(null);
//            BlueLinkReceiver.getInstance().closeBlueConnClearName("Common退出到登录");//灭屏两种都不能关
//            BlueAdapter.getInstance().closeBlueTooth("Common退出到登录");
//            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
//                    .withInfo(error)
//                    .withButton("", "确定")
//                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                        @Override
//                        public void onClickConfirm(boolean isClickConfirm) {
//                            if (isClickConfirm) exitLo();
//                        }
//                    }, new ToastConfirmNormal.OnOutsideClickListener() {
//                        @Override
//                        public void onClickoutside() {
//                            exitLo();
//                        }
//                    }).show();
            exitLo(error,"逼退");
        }
    }
    private void exitLo(String error,String tips) {
        WearReg.getInstance().wearServiceChangeUser(0,"");
        DemoMode.saveIsDemoMode(false);
        ManagerCarList.getInstance().saveCarList(null, "exitToLogin");

        Intent intent = new Intent();
        intent.setClass(GlobalContext.getContext(), ActivityLogin.class);//未登录
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(GlobalContext.getContext()!=null){
            intent.putExtra("MESSAGE", error);
            GlobalContext.getContext().startActivity(intent);
        }
        if(ActivityKulalaMain.ActivityKulalaMainThis!=null)ActivityKulalaMain.ActivityKulalaMainThis.finish();
        if(ActivityLCDkey.ActivityLCDkeyThis!=null)ActivityLCDkey.ActivityLCDkeyThis.finish();
        if(BindLcdKeyActivity.BindLcdKeyActivityThis!=null)BindLcdKeyActivity.BindLcdKeyActivityThis.finish();
        ManagerLoginReg.getInstance().exitLogin();//这里会重置socket
        ManagerCarList.getInstance().exit();
        ManagerAuthorization.getInstance().exit();
        ManagerWarnings.getInstance().exit();
        BlueLinkReceiver.getInstance().closeBlueConnClearName("Exit Login");
        BlueLinkReceiver.getInstance().closeBlueConnClearNameLcdKey("Exit Login");
        FragmentActionBar.currentPos=0;//退出登录回到首页
//        BlueLinkReceiver.getInstance().changeCar(null);
//        BlueLinkReceiver.getInstance().closeBlueConnClearName("Common退出到登录");
//        BlueAdapter.getInstance().closeBlueTooth("Common退出到登录");
    }
    private void exitLo() {
        WearReg.getInstance().wearServiceChangeUser(0,"");
        DemoMode.saveIsDemoMode(false);
        ManagerCarList.getInstance().saveCarList(null, "exitToLogin");

        Intent intent = new Intent();
        intent.setClass(GlobalContext.getContext(), ActivityLogin.class);//未登录
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(GlobalContext.getContext()!=null){
            GlobalContext.getContext().startActivity(intent);
        }
        if(ActivityKulalaMain.ActivityKulalaMainThis!=null)ActivityKulalaMain.ActivityKulalaMainThis.finish();
        if(ActivityLCDkey.ActivityLCDkeyThis!=null)ActivityLCDkey.ActivityLCDkeyThis.finish();
        if(BindLcdKeyActivity.BindLcdKeyActivityThis!=null)BindLcdKeyActivity.BindLcdKeyActivityThis.finish();
        ManagerLoginReg.getInstance().exitLogin();//这里会重置socket
        ManagerCarList.getInstance().exit();
        ManagerAuthorization.getInstance().exit();
        ManagerWarnings.getInstance().exit();
        BlueLinkReceiver.getInstance().closeBlueConnClearName("Exit Login");
        BlueLinkReceiver.getInstance().closeBlueConnClearNameLcdKey("Exit Login");
//        BlueLinkReceiver.getInstance().changeCar(null);
//        BlueLinkReceiver.getInstance().closeBlueConnClearName("Common退出到登录");
//        BlueAdapter.getInstance().closeBlueTooth("Common退出到登录");
        FragmentActionBar.currentPos=0;//退出登录回到首页
    }

    public void ExitAndFinish() {
//        BlueLinkReceiver.getInstance().changeCar(null);
//        BlueLinkReceiver.getInstance().closeBlueConnClearName("Common退出到登录");
//        BlueAdapter.getInstance().closeBlueTooth("Common退出到登录");
        DemoMode.saveIsDemoMode(false);
    }

}
