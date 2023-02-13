package view.view4info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import model.ManagerInformation;
import com.kulala.staticsview.titlehead.ClipTitleHead;
public class ViewInfoWeb extends LinearLayoutBase{
	private ClipTitleHead	title_head;
	private WebView			web_info;
	public ViewInfoWeb(Context context, AttributeSet attrs) {
		super(context, attrs);// this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.activity_web, this, true);
		title_head = (ClipTitleHead) findViewById(R.id.title_head);
		web_info = (WebView) findViewById(R.id.web_info);
		initViews();
		initEvents();
	}
	public void initViews() {
		if (ManagerInformation.jumpUrl != null) {
			handleChangeData();
		}
	}
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
			}
		});
	}
	public void receiveEvent(String eventName, Object paramObj) {
	}
	@Override
	public void callback(String key, Object value) {
		super.callback(key, value);
	}

	@Override
	public void invalidateUI()  { 
		web_info.setWebViewClient(new MyWebViewClient());  
		web_info.loadUrl(ManagerInformation.jumpUrl);
	}
    class MyWebViewClient extends WebViewClient{
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		view.loadUrl(url);
    		return true;
    	}
    }
	// ==============================================================
}
