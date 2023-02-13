package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.client.proj.kusida.R;
import com.client.proj.kusida.wxapi.WXEntryActivity;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ImageHttpLoader;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;

import com.tencent.tauth.TencentCommon;

import common.GlobalContext;
import common.global.OWXShare;
import model.ManagerCommon;
import model.common.DataShare;
import view.view4app.carpath.OToastSharePath;
import view.view4me.set.ClipSetItem;
import view.view4me.set.ClipTitleMeSet;


public class ViewMichelle extends LinearLayoutBase{
	private ClipTitleMeSet title_head;
//	private static int[]	images	= {R.drawable.icon_share_wx_friend, R.drawable.icon_share_wx_bolo};
//	private static String[]	names	= {"分享微信", "分享朋友圈"};
//	private GridView		grid;
	private ClipSetItem txt_share_soft,txt_share_web;
	private View view_background;
	private LinearLayout lin_share_panel;
	private ImageView img_share_bolo,img_share_friend;
	private ImageView img_qrcode;
//	private String shareDownLoad = "http://api.91kulala.com/kulala/share/index.html";//unuse
	
	private int flagShare = 0;//0soft,1web
	private int SHARE_FRIEND = 0,SHARE_BOLO = 1,SHARE_SOFT = 0,SHARE_WEB = 1;
	public ViewMichelle(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_share, this, true);
		title_head =  findViewById(R.id.title_head);
		img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
		txt_share_soft =  findViewById(R.id.txt_share_soft);
		txt_share_web = findViewById(R.id.txt_share_web);
		view_background = (View) findViewById(R.id.view_background);
		lin_share_panel = (LinearLayout) findViewById(R.id.lin_share_panel);
		img_share_bolo = (ImageView) findViewById(R.id.img_share_bolo);
		img_share_friend = (ImageView) findViewById(R.id.img_share_friend);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.WX_SHARE_SUCESS,this);
	}


	@Override
	public void initViews() {
		ImageHttpLoader.getInstance().asyncloadImage(getContext(),img_qrcode,R.drawable.sharewxqrcode);
//			img_qrcode.setImageBitmap(EncodingHandler.createQRCode(shareDownLoad,BitmapFactory.decodeResource(getResources(), R.drawable.kulala_icon)));//
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
			}
		});
		txt_share_soft.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				flagShare = 0;
//				popShare();
				OToastSharePath.getInstance().show(title_head, "share", new OToastSharePath.OnClickButtonListener() {
					@Override
					public void onClick(int pos) {
						switch (pos){
							case 1:
								DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
								if(shareInfo ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									OWXShare.ShareFriendURL(shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl);
								}else if(flagShare == SHARE_WEB){
									OWXShare.ShareFriendURL(shareInfo.shareOfficeTitle,shareInfo.shareOfficeComment,shareInfo.shareOfficeUrl);
								}
								break;
							case 2:
								DataShare shareInfoo = ManagerCommon.getInstance().getShareInfo();
								if(shareInfoo ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;

									OWXShare.ShareTimeLineURL(shareInfoo.shareTitle,shareInfoo.shareComment,shareInfoo.shareUrl);
								}else if(flagShare == SHARE_WEB){
									OWXShare.ShareTimeLineURL(shareInfoo.shareOfficeTitle,shareInfoo.shareOfficeComment,shareInfoo.shareOfficeUrl);
								}
								break;
							case 3:
								DataShare shareInfoo3 = ManagerCommon.getInstance().getShareInfo();
								if(shareInfoo3 ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									TencentCommon.toTencent(getContext(),shareInfoo3.shareTitle,shareInfoo3.shareComment,shareInfoo3.shareUrl,0,shareInfoo3.sharePic);
								}else if(flagShare == SHARE_WEB){
									TencentCommon.toTencent(getContext(),shareInfoo3.shareOfficeTitle,shareInfoo3.shareOfficeComment,shareInfoo3.shareOfficeUrl,0,shareInfoo3.sharePic);
								}
								break;
							case 4:
								DataShare shareInfoo4 = ManagerCommon.getInstance().getShareInfo();
								if(shareInfoo4 ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									TencentCommon.toTencent(getContext(),shareInfoo4.shareTitle,shareInfoo4.shareComment,shareInfoo4.shareUrl,1,shareInfoo4.sharePic);
								}else if(flagShare == SHARE_WEB){
									TencentCommon.toTencent(getContext(),shareInfoo4.shareOfficeTitle,shareInfoo4.shareOfficeComment,shareInfoo4.shareOfficeUrl,1,shareInfoo4.sharePic);
								}
								break;
						}
					}
				});
			}
		});
		txt_share_web.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				flagShare = 1;
				OToastSharePath.getInstance().show(title_head, "share", new OToastSharePath.OnClickButtonListener() {
					@Override
					public void onClick(int pos) {
						switch (pos){
							case 1:
								DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
								if(shareInfo ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									OWXShare.ShareFriendURL(shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl);
								}else if(flagShare == SHARE_WEB){
									OWXShare.ShareFriendURL(shareInfo.shareOfficeTitle,shareInfo.shareOfficeComment,shareInfo.shareOfficeUrl);
								}

								break;
							case 2:
								DataShare shareInfoo = ManagerCommon.getInstance().getShareInfo();
								if(shareInfoo ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;

									OWXShare.ShareTimeLineURL(shareInfoo.shareTitle,shareInfoo.shareComment,shareInfoo.shareUrl);
								}else if(flagShare == SHARE_WEB){
									OWXShare.ShareTimeLineURL(shareInfoo.shareOfficeTitle,shareInfoo.shareOfficeComment,shareInfoo.shareOfficeUrl);
								}
								break;
							case 3:
								DataShare shareInfoo3 = ManagerCommon.getInstance().getShareInfo();
								if(shareInfoo3 ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									TencentCommon.toTencent(getContext(),shareInfoo3.shareTitle,shareInfoo3.shareComment,shareInfoo3.shareUrl,0,shareInfoo3.sharePic);
								}else if(flagShare == SHARE_WEB){
									TencentCommon.toTencent(getContext(),shareInfoo3.shareOfficeTitle,shareInfoo3.shareOfficeComment,shareInfoo3.shareOfficeUrl,0,shareInfoo3.sharePic);
								}
								break;
							case 4:
								DataShare shareInfoo4 = ManagerCommon.getInstance().getShareInfo();
								if(shareInfoo4 ==null)return;
								if(flagShare == SHARE_SOFT){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									TencentCommon.toTencent(getContext(),shareInfoo4.shareTitle,shareInfoo4.shareComment,shareInfoo4.shareUrl,1,shareInfoo4.sharePic);
								}else if(flagShare == SHARE_WEB){
									TencentCommon.toTencent(getContext(),shareInfoo4.shareOfficeTitle,shareInfoo4.shareOfficeComment,shareInfoo4.shareOfficeUrl,1,shareInfoo4.sharePic);
								}
								break;
						}
					}
				});
//				popShare();
			}
		});
		img_qrcode.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
						.withTitle("分享好友")
						.withInfo("现在就把下载地址分享给好友吧！")
						.withClick(new ToastConfirmNormal.OnButtonClickListener() {
							@Override
							public void onClickConfirm(boolean isClickConfirm) {
								if (isClickConfirm){
									WXEntryActivity.NEED_WXSHARE_RESULT = true;
									OWXShare.ShareQrcodeDownLoad();
								}
							}
						})
						.show();
			}
		});
		view_background.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				hideShare();
			}
		});
		img_share_bolo.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
//				DataShare shareInfo =ManagerCommon. shareInfo;
//				if(shareInfo ==null)return;
//				if(flagShare == SHARE_SOFT){
//					WXEntryActivity.NEED_WXSHARE_RESULT = true;
//					OWXShare.ShareTimeLineURL(shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl);
//				}else if(flagShare == SHARE_WEB){
//					OWXShare.ShareTimeLineURL(shareInfo.shareOfficeTitle,shareInfo.shareOfficeComment,shareInfo.shareOfficeUrl);
//				}
//				hideShare();
			}
		});
		img_share_friend.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
//				DataShare shareInfo = ManagerCommon. shareInfo;
//				if(shareInfo ==null)return;
//				if(flagShare == SHARE_SOFT){
//					WXEntryActivity.NEED_WXSHARE_RESULT = true;
//					OWXShare.ShareFriendURL(shareInfo.shareTitle,shareInfo.shareComment,shareInfo.shareUrl);
//				}else if(flagShare == SHARE_WEB){
//					OWXShare.ShareFriendURL(shareInfo.shareOfficeTitle,shareInfo.shareOfficeComment,shareInfo.shareOfficeUrl);
//				}
//				hideShare();
			}
		});
	}
	private void popShare(){
		view_background.setVisibility(VISIBLE);
		lin_share_panel.setVisibility(VISIBLE);
	}
	private void hideShare(){
		view_background.setVisibility(INVISIBLE);
		lin_share_panel.setVisibility(INVISIBLE);
	}
	@Override
	public void callback(String key, Object value) {
		if(key.equals("share")){

		}
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.WX_SHARE_SUCESS)){
//			OCtrlScore.getInstance().ccmd1125_newScore(1);//积分增加分享
		}
	}


	@Override
	public void invalidateUI()  {
	}
	// =====================================================
}
