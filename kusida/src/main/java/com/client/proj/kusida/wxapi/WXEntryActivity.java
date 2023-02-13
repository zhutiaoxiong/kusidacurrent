package com.client.proj.kusida.wxapi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import common.global.OWXShare;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    public static boolean NEED_WXSHARE_RESULT = false;
    private IWXAPI api;
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        try {
            api = WXAPIFactory.createWXAPI(this, OWXShare.getAppIdWX(), false);
            api.handleIntent(getIntent(), this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState); 
    } 
    @Override 
    public void onReq(BaseReq arg0) { }
 
    @Override 
    public void onResp(BaseResp resp) {
        switch (resp.errCode) { 
        case BaseResp.ErrCode.ERR_OK:
            if(NEED_WXSHARE_RESULT)ODispatcher.dispatchEvent(OEventName.WX_SHARE_SUCESS);
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.successful_share));
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.cancel_the_share));
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.refuse_to_share));
            break;
        }
        NEED_WXSHARE_RESULT = false;
        this.finish();
    } 
}  
