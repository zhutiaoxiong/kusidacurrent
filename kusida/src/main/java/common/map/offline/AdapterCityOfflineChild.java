package common.map.offline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMap;

import java.util.List;

public class AdapterCityOfflineChild extends BaseAdapter {
	private Context                context;
	private List<MKOLSearchRecord> list;
	private MKOfflineMap           offlineMap;
	private boolean      needProgress;
	public AdapterCityOfflineChild(Context context, List<MKOLSearchRecord> updateList,MKOfflineMap offlineMap, boolean needProgress) {
		this.context = context;
		this.list = updateList;
		this.offlineMap = offlineMap;
		this.needProgress = needProgress;
	}
	@Override
	public int getCount() {
		if (list == null)
			return 0;
		return list.size();
	}
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
			ViewCityItem item = new ViewCityItem(context,null);
			convertView = item;
			holder.item = item;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// ��ʼ��holder
		}
		MKOLSearchRecord data = list.get(position);
		holder.item.setData(data,offlineMap,needProgress);
		return convertView;
	}
	// ===================================================
	public final class ViewHolder {
		private ViewCityItem item;
//        private MKOLSearchRecord data;
//		public TextView	city_name,city_size,download_info;
//		public ImageView	img_download,img_dropdown;
//		public ProgressBar      progress_download;
//		public ListViewNoScroll list_child;
	}
	// ===================================================
	private String formatDataSize(int size) {
		if(size == 0)return "";
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

}
