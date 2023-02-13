package view.view4me.userinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.List;

import adapter.AdapterUserInfoScoreList;
import adapter.BasicListView;
import ctrl.OCtrlScore;
import model.ManagerScore;
import model.score.DataScore;
import view.view4me.set.ClipTitleMeSet;

public class ViewUserInfoScore extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private TextView                 txt_score,txt_detail;
	private BasicListView            list_task_everyday,list_task_unfin;
	private ImageView                img_help;

	private AdapterUserInfoScoreList adapterDay;
	private AdapterUserInfoScoreList adapterUnfin;
	public ViewUserInfoScore(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_userinfo_score, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_score = (TextView) findViewById(R.id.txt_score);
		txt_detail = (TextView) findViewById(R.id.txt_detail);
		img_help = (ImageView) findViewById(R.id.img_help);
		list_task_everyday = (BasicListView) findViewById(R.id.list_task_everyday);
		list_task_unfin = (BasicListView) findViewById(R.id.list_task_unfin);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.SCORE_LIST_RESULTBACK, this);
	}

	@Override
	public void initViews() {
		OCtrlScore.getInstance().ccmd1123_getScoreList();
	}
	@Override
	public void initEvents() {
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//会点到下层
			}
		});
		// back
		title_head.img_left.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo);
			}
		});
		img_help.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo_score_help);
			}
		});
		txt_detail.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View view) {
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo_score_detail);
			}
		});
		//加载跳转
		list_task_everyday.setOnItemClickListener(new OnItemClickListenerMy(){
			@Override
			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
				DataScore data = adapterDay.getItem(position);
				if(data!=null)ODispatcher.dispatchEvent(OEventName.SCORE_JUMPPAGE,data.jumpPage);
				super.onItemClickNofast(parent, view, position, id);
			}
		});
		list_task_unfin.setOnItemClickListener(new OnItemClickListenerMy(){
			@Override
			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
				DataScore data = adapterUnfin.getItem(position);
				if(data!=null)ODispatcher.dispatchEvent(OEventName.SCORE_JUMPPAGE,data.jumpPage);
				super.onItemClickNofast(parent, view, position, id);
			}
		});
	}
	@Override
	public void callback(String key,Object obj) {
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.SCORE_LIST_RESULTBACK)){
			handleChangeData();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		ODispatcher.removeEventListener(OEventName.SCORE_LIST_RESULTBACK, this);
		super.onDetachedFromWindow();
	}


	@Override
	public void invalidateUI()  {
		int all = ManagerScore.getInstance().scoreAll;
		txt_score.setText(""+all);
		List<DataScore> everyDayInfos = ManagerScore.getInstance().everyDayInfos;//每日任务
		if(everyDayInfos!=null){
			adapterDay = new AdapterUserInfoScoreList(getContext(),everyDayInfos);
			list_task_everyday.setAdapter(adapterDay);
		}
		List<DataScore> newComerInfos = ManagerScore.getInstance().newComerInfos;//未完任务
		if(newComerInfos!=null){
			adapterUnfin = new AdapterUserInfoScoreList(getContext(),newComerInfos);
			list_task_unfin.setAdapter(adapterUnfin);
		}
	}
	// =====================================================
}
