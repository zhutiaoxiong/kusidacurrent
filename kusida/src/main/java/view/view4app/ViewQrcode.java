package view.view4app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.google.zxing.WriterException;
import com.kulala.staticsview.image.smart.SmartImage;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.zxing.encoding.EncodingHandler;

import view.view4me.set.ClipTitleMeSet;

public class ViewQrcode extends LinearLayoutBase{
	private ClipTitleMeSet title_head;
	private SmartImageView image;
	public static String AUTHOR_CODE = "";
	public ViewQrcode(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_app_qrcode, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		image = (SmartImageView) findViewById(R.id.image);
		initViews();
		initEvents();
	}

	@Override
	public void initViews() {
		handleChangeData();
	}
	@Override
	public void initEvents() {
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_codriver);
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
	public void invalidateUI(){
		if(!AUTHOR_CODE.equals("")){
			SmartImage img = new SmartImage() {
				@Override
				public Bitmap getBitmap(Context context) {
					try {
						return EncodingHandler.createQRCode(AUTHOR_CODE, null);
					} catch (WriterException e) {
						e.printStackTrace();
					}
					return null;
				}};
			image.setImage(img,null);//BitmapFactory.decodeResource(getResources(), R.drawable.kulala_icon)
		}
	}
	// =====================================================
}
