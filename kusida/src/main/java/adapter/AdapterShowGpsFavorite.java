package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.staticsview.toast.OToastInput;

import java.util.List;

import common.global.NAVI;
import common.global.OWXShare;
import ctrl.OCtrlGps;
import model.gps.DataGpsPoint;
import view.view4app.ViewGpsFavoriteShowPos;

public class AdapterShowGpsFavorite extends BaseAdapter implements OCallBack {
	private LayoutInflater		mInflater;
	private Context				mContext;
	private List<DataGpsPoint>	list;
	private View 				cacheViewPop;//for pop
	private int					currentPosition;
	protected final int			mItemLayoutId;
	private boolean showSelectItem = false;
	// private boolean checkNeed = false;
	private DataGpsPoint           pointSelected;
	private ShareUrlSearch   shareUrlSearch;

	// private int imageId = 0;
	public AdapterShowGpsFavorite(Context context, List<DataGpsPoint> listPath, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = listPath;
		this.mItemLayoutId = itemLayoutId;
	}
	public DataGpsPoint getCurrentItem() {
		return list.get(currentPosition);
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}
	//==========================out====================================
	public void showSelect(boolean show) {
		showSelectItem = show;
		notifyDataSetChanged();
	}
	public int getSelectNum(){
		if (list == null)
			return 0;
		int result = 0;
		for(DataGpsPoint po : list){
			if(po.isSelected)result++;
		}
		return result;
	}
	public List<DataGpsPoint> getDataList() {
		return list;
	}
	//==========================out end====================================

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {// ��ʼ��
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_gps_favorite, null);
			holder.txt_time_start = (TextView) convertView.findViewById(R.id.txt_time_start);
			holder.txt_place_start = (TextView) convertView.findViewById(R.id.txt_place_start);
			holder.txt_intro = (TextView) convertView.findViewById(R.id.txt_intro);
			holder.img_select = (ImageView) convertView.findViewById(R.id.img_select);
			holder.img_intro = (ImageView) convertView.findViewById(R.id.img_intro);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		// 设初始显示数据
		DataGpsPoint info = list.get(position);
		holder.position = position;
		holder.txt_time_start.setText(ODateTime.time2StringWithHH(info.createTime));
		holder.txt_place_start.setText(info.location);
		//备注的显示
		if(info.note.equals("")){
			holder.txt_intro.setText(mContext.getResources().getString(R.string.note));
			holder.img_intro.setImageResource(R.drawable.icon_intro_no);
		}else{
			holder.txt_intro.setText(mContext.getResources().getString(R.string.note)+info.note);
			holder.img_intro.setImageResource(R.drawable.icon_intro);
		}
		//选中的显示
		if(showSelectItem) {
			holder.img_select.setVisibility(View.VISIBLE);
			if (info.isSelected) {
				holder.img_select.setImageResource(R.drawable.select_true);
			} else {
				holder.img_select.setImageResource(R.drawable.select_false);
			}
		}else{
			holder.img_select.setVisibility(View.INVISIBLE);
		}
		convertView.setOnClickListener(new OnClickListenerMy(){
			@Override
			public void onClickNoFast(View v) {
				ViewHolder hold = (ViewHolder)v.getTag();
				if(hold!=null){
					pointSelected = list.get(position);
					if(hold.img_select.getVisibility() == View.VISIBLE){//选择删除时
						list.get(position).isSelected = !list.get(position).isSelected;
						notifyDataSetChanged();
					}else{//单击时
						cacheViewPop = hold.txt_intro;
						//,mContext.getResources().getString(R.string.navigation)
						OToastButton.getInstance().show(hold.txt_intro, new String[]{mContext.getString(R.string.delete_note),mContext.getResources().getString(R.string.editors_note),mContext.getResources().getString(R.string.see_on_the_map),mContext.getResources().getString(R.string.share_location)}, "operate", AdapterShowGpsFavorite.this);
//						ODispatcher.dispatchEvent(OEventName.ADAPTER_FAVORITE_POINT_CLICKONE,list.get(position));
					}
				}
			}
		});
		return convertView;
	}

	@Override
	public void callback(String key, Object value) {
		if(key.equals("operate")){
			//"删除备注","编辑备注","在地图上查看","分享位置","导航"
			String opp = (String)value;
			if(opp.equals(mContext.getResources().getString(R.string.delete_note))){
				OCtrlGps.getInstance().ccmd1225_favoriteIntro(pointSelected.ide,"");
			}else if(opp.equals(mContext.getResources().getString(R.string.editors_note))){
				if(cacheViewPop!=null)
				OToastInput.getInstance().showInput(cacheViewPop, mContext.getResources().getString(R.string.modify_note), mContext.getResources().getString(R.string.please_enter_the_note_information), new String[]{OToastInput.OTHER_TEXT}, "intro", AdapterShowGpsFavorite.this);
			}else if(opp.equals(mContext.getResources().getString(R.string.navigation))){

//				OToastNavigate.getInstance().showOpenNavigate(, selfPos, carPos, pointSelected.location);
//				OToastNavigate.getInstance().showOpenNavigate(cacheViewPop, selfPos, NAVI.Str2Latlng(pointSelected.latlng), pointSelected.location);
			}else if(opp.equals(mContext.getResources().getString(R.string.share_location))){

//				OWXShare.SharePlace(shareAddress, shareUrl);
				if (shareUrlSearch == null) shareUrlSearch = ShareUrlSearch.newInstance();
				shareUrlSearch.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {
					@Override
					public void onGetRouteShareUrlResult(ShareUrlResult result) {}
					@Override
					public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {}
					@Override
					public void onGetLocationShareUrlResult(ShareUrlResult result) {
						OWXShare.SharePlace(pointSelected.location, result.getUrl());
					}
				});
				shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
						.location(NAVI.Str2Latlng(pointSelected.latlng)).snippet(mContext.getResources().getString(R.string.my_location)).name(pointSelected.location));
			}else if(opp.equals(mContext.getResources().getString(R.string.see_on_the_map))){
				ViewGpsFavoriteShowPos.carPos = NAVI.Str2Latlng(pointSelected.latlng);
				Intent intent = new Intent();
				intent.setClass(mContext, ViewGpsFavoriteShowPos.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		}else if(key.equals("intro")){
			JsonObject ooo   = (JsonObject)value;
			String     intro = OJsonGet.getString(ooo, OToastInput.OTHER_TEXT);
			OCtrlGps.getInstance().ccmd1225_favoriteIntro(pointSelected.ide,intro);
		}
	}


	// ===================================================
	public final class ViewHolder {
		public int position;
		public TextView	txt_time_start, txt_place_start,txt_intro;
		public ImageView	img_intro, img_select;
	}
	// ===================================================

}
