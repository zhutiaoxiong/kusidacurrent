package com.client.proj.kusida.wxapi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import common.global.OWXShare;

public class DDWXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口 
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
    public void onReq(BaseReq req) {
    	Log.i("wx", "req back: "+req.getType());
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
	    	Log.i("wx", "req back1");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,req.toString());
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
	    	Log.i("wx", "req back2");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,req.toString());
			break;
		case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
	    	Log.i("wx", "req back3");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,req.toString());
			break;
		default:
			break;
		}
    } 
 
    @Override 
    public void onResp(BaseResp resp) {
    	Log.i("wx", "resp back: "+resp.errCode);
        switch (resp.errCode) { 
        case BaseResp.ErrCode.ERR_OK: 
        	Log.i("wx", "resp back1");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.be_success));
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL: 
        	Log.i("wx", "resp back2");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.cancle));
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED: 
        	Log.i("wx", "resp back3");
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.refuse));
            break;
        } 
    } 
}  
