package common.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.client.proj.kusida.R;
import com.google.zxing.WriterException;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxing.encoding.EncodingHandler;

import java.io.ByteArrayOutputStream;

import common.GlobalContext;

public class OWXShare {
	public static int WXSceneSession = SendMessageToWX.Req.WXSceneSession;//0 微信好友
	public static int WXSceneTimeline = SendMessageToWX.Req.WXSceneTimeline;//1 微信朋友圈
	/**取manifist appId**/
	public static String getAppIdWX() throws PackageManager.NameNotFoundException{
		Context context = GlobalContext.getContext();
		if(context == null)return "";
		ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
		String          appId   =appInfo.metaData.getString("WX_APPKEY");
		return appId;
	}
	// sharePos 0 微信好友
	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	/**分享位置**/
	public static void SharePlace(String info,String URL) {
		ShareUrl(WXSceneSession, R.drawable.icon_baidumap,GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you),info,URL,buildTransaction("webpage"));
	}
	/**分享微信好友**/
	public static void ShareFriendURL(String title,String info,String URL) {
		ShareUrl(WXSceneSession,R.drawable.kulala_icon,title,info,URL,buildTransaction("webpage"));
	}
	/**分享微信朋友圈**/
	public static void ShareTimeLineURL(String title,String info,String URL) {
		ShareUrl(WXSceneTimeline,R.drawable.kulala_icon,title,info,URL,buildTransaction("webpage"));
	}
	/**分享微信小程序**/
	public static void ShareWXSmallApp(String title,String info,String URL,String authCode ) {
		IWXAPI wxApi;
		try {
			wxApi = WXAPIFactory.createWXAPI(GlobalContext.getCurrentActivity(), getAppIdWX());
			wxApi.registerApp(getAppIdWX());
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return;
		}
		WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
		miniProgramObj.webpageUrl =URL ; // 兼容低版本的网页链接
//		miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
		miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
		miniProgramObj.userName = "gh_b611ce225aa0";     // 小程序原始id  gh_4d3382320111
		miniProgramObj.path = "pages/index/index?authCode="+authCode;            //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
		WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
		msg.title = title;                    // 小程序消息title
		msg.description = info;               // 小程序消息desc
		Bitmap bmp = BitmapFactory.decodeResource(GlobalContext.getContext().getResources(), R.drawable.wx_share_info_img);
		// 设置消息的缩略图
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 400, 320, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);   // 小程序消息封面图片，小于128k

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("miniProgram");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
		wxApi.sendReq(req);
//		ShareUrl(WXSceneTimeline,R.drawable.kulala_icon,title,info,URL,buildTransaction("webpage"));
	}
	/**分享二维码下载**/
	public static void ShareQrcodeDownLoad(){
		Bitmap image = BitmapFactory.decodeResource(GlobalContext.getContext().getResources(), R.drawable.sharewxqrcode);
		IWXAPI wxApi;
		try {
			wxApi = WXAPIFactory.createWXAPI(GlobalContext.getCurrentActivity(), getAppIdWX());
			wxApi.registerApp(getAppIdWX());
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return;
		}

		//image
		WXImageObject imgobj = new WXImageObject(image);//大图
		WXMediaMessage msg    = new WXMediaMessage();
		msg.mediaObject = imgobj;
		//必须传一个微信不认识的图
		try {
			Bitmap smBit = EncodingHandler.createQRCode("http://kc.kcmoco.com/download_cdj.html", BitmapFactory.decodeResource(GlobalContext.getContext().getResources(), R.drawable.kulala_icon));
			msg.setThumbImage(smBit);//小图等同
		} catch (WriterException e) {
			e.printStackTrace();
		}
		//开始发送
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;//后分享
		wxApi.sendReq(req);
		image.recycle();//自由选择是否进行回收
	}
	private static void ShareUrl(int type,int ImageResid,String title,String info,String URL,String transaction) {
		IWXAPI wxApi;
		try {
			wxApi = WXAPIFactory.createWXAPI(GlobalContext.getCurrentActivity(), getAppIdWX());
			wxApi.registerApp(getAppIdWX());
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return;
		}
		// 分享数据
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = URL;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = info;
		// 这里替换一张自己工程里的图片资源
		Bitmap thumb;
		if(ImageResid == 0){
			thumb = BitmapFactory.decodeResource(GlobalContext.getContext().getResources(), R.drawable.kulala_icon);
		}else{
			thumb = BitmapFactory.decodeResource(GlobalContext.getContext().getResources(), ImageResid);
		}
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = transaction;
		req.message = msg;
		req.scene = type;
		wxApi.sendReq(req);
	}
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
