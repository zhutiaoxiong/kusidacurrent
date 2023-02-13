package view.view4me.userinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapter.AdapterUserInfoScoreDetailList;
import adapter.BasicListView;
import ctrl.OCtrlScore;
import model.ManagerScore;
import model.score.DataScore;
import view.view4me.set.ClipTitleMeSet;

public class ViewUserInfoScoreDetail extends RelativeLayoutBase{
	private ClipTitleMeSet title_head;
	private TextView                 txt_score;
	private BasicListView            list_detail;

	private AdapterUserInfoScoreDetailList adapter;
	public ViewUserInfoScoreDetail(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_me_userinfo_score_detail, this, true);
		title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
		txt_score = (TextView) findViewById(R.id.txt_score);
		list_detail = (BasicListView) findViewById(R.id.list_detail);
		initViews();
		initEvents();
		ODispatcher.addEventListener(OEventName.SCORE_DETAIL_RESULTBACK, this);
	}

	@Override
	public void initViews() {
		OCtrlScore.getInstance().ccmd1124_getScoreDetail(0,100);
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
				ODispatcher.dispatchEvent(OEventName.ACTIVITY_USERINFO_GOTOVIEW, R.layout.view_me_userinfo_score);
			}
		});
	}
	@Override
	public void callback(String key,Object obj) {
	}
	@Override
	public void receiveEvent(String eventName, Object paramObj) {
		if(eventName.equals(OEventName.SCORE_DETAIL_RESULTBACK)){
			handleChangeData();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}


	@Override
	public void invalidateUI()  {
		int all = ManagerScore.getInstance().scoreAll;
		txt_score.setText(""+all);
		List<DataScore> scoreInfos = ManagerScore.getInstance().scoreInfos;
		//排序
		MyComparator myComparator = new MyComparator();
		Collections.sort(scoreInfos, myComparator);
		if(scoreInfos!=null){
			adapter = new AdapterUserInfoScoreDetailList(getContext(),scoreInfos);
			list_detail.setAdapter(adapter);
		}
	}
	private class MyComparator implements Comparator<DataScore> {
		public int compare(DataScore o1, DataScore o2) {
			if (o1!=null&&o2!=null){
				if (o1.createTime >o2.createTime) {//大于放前
					return -1;
				} else {
					return 1;
				}
			}
			return Long.valueOf(o1.createTime).compareTo(o2.createTime);
		}
	}
	// =====================================================
}
