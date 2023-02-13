package view.view4me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.clip.ClipLineBtnTxt;
import com.kulala.staticsview.titlehead.ClipTitleHead;
public class ViewWallet extends LinearLayoutBase{
	private ClipTitleHead title_head;
	private ClipLineBtnTxt txt_pay;

	public ViewWallet(Context context, AttributeSet attrs) {
		super(context, attrs);//this layout for add and edit
		LayoutInflater.from(context).inflate(R.layout.view_me_wallet, this, true);
		title_head = (ClipTitleHead) findViewById(R.id.title_head);
		txt_pay = (ClipLineBtnTxt) findViewById(R.id.txt_pay);
		initViews();
		initEvents();
	}
	public void initViews() {
	}
	public void initEvents() {
		//back
		title_head.img_left.setOnClickListener(new OnClickListenerMy() {
			@Override
			public void onClickNoFast(View v) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
			}
		});
		txt_pay.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
				if(car==null){
					ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,getResources().getString(R.string.you_on_the_current_activation_vehicle_do_not_have_to_pay));
				}else{
					ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_pay);
				}
			}
		});
	}

	@Override
	protected void invalidateUI() {
	}

	public void receiveEvent(String eventName, Object paramObj) {

	}
}
