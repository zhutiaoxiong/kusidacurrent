package view.view4app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.zxing.activity.CaptureActivity;

import common.GlobalContext;
import view.clip.ClipPopUserLinking;
import view.view4me.set.ClipSetItem;
import view.view4me.set.ClipTitleMeSet;


public class ViewCodriverManage extends LinearLayoutBase{
	private ClipTitleMeSet title_head;
	private ClipSetItem txt_authorization,txt_userlist;
	private ClipSetItem txt_authored;
	public ViewCodriverManage(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_app_codriver, this, true);
		title_head =  findViewById(R.id.title_head);
		txt_authorization = findViewById(R.id.txt_authorization);
		txt_userlist =  findViewById(R.id.txt_userlist);
		txt_authored =  findViewById(R.id.txt_authored);
		initViews();
		initEvents();
	}

	@Override
	public void initViews() {
	}

	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
			}
		});
		txt_authorization.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ClipPopUserLinking.getInstance().show(true, title_head, "chooseuser", ViewCodriverManage.this);
//				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver_authorization);
			}
		});
		txt_userlist.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
//				ODispatcher.dispatchEvent(OEventName.FLAGMENT_APP_GOTOVIEW, R.layout.view_app_codriver_userlist);
				ClipPopUserLinking.getInstance().show(false,title_head,"",ViewCodriverManage.this);
			}
		});
		txt_authored.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriverlist);
			}
		});
		// scan
		title_head.img_right.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
					//拍照权限
					if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
						GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
					} else {
						Intent intent = new Intent();
						intent.setClass(ViewCodriverManage.this.getContext(), CaptureActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("scantype", "qrcode");
						intent.putExtras(bundle);
						ViewCodriverManage.this.getContext().startActivity(intent);
					}
				} else {
					Intent intent = new Intent();
					intent.setClass(ViewCodriverManage.this.getContext(), CaptureActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("scantype", "qrcode");
					intent.putExtras(bundle);
					ViewCodriverManage.this.getContext().startActivity(intent);
				}
			}
		});
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
	}

	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.removeEventListener(OEventName.AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK, this);
		super.onDetachedFromWindow();
	}

	@Override
	public void invalidateUI(){
	}
	// =====================================================
}
