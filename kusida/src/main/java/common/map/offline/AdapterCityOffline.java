package common.map.offline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.client.proj.kusida.R;

import java.util.List;

public class AdapterCityOffline extends BaseExpandableListAdapter {
    private Context                context;
    private List<MKOLSearchRecord> list;//多列数据合并放入
    private MKOfflineMap           offlineMap;
    private boolean                needProgress;
    public AdapterCityOffline(Context context, List<MKOLSearchRecord> combineList, MKOfflineMap offlineMap, boolean needProgress) {
        this.context = context;
        this.list = combineList;
        this.offlineMap = offlineMap;
        this.needProgress = needProgress;
    }
    @Override
    public int getGroupCount() {
        if (list == null)
            return 0;
        return list.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        if (list == null) return 0;
        if (list.get(groupPosition).childCities == null) return 0;
        return list.get(groupPosition).childCities.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        if (list.get(groupPosition).childCities == null) return null;
        return list.get(groupPosition).childCities;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (list == null) return null;
        if (list.get(groupPosition).childCities == null) {
            return list.get(groupPosition);
        } else {
            return list.get(groupPosition).childCities.get(childPosition);
        }
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_header_title, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();// ��ʼ��holder
        }
        holder.update(list.get(groupPosition));
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            ViewCityItem item = new ViewCityItem(context, null);
            convertView = item;
            holder.item = item;
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();// ��ʼ��holder
        }
        MKOLSearchRecord data = list.get(groupPosition).childCities.get(childPosition);
        holder.item.setData(data, offlineMap,needProgress);
        return convertView;
    }
    // ===================================================
    public final class GroupHolder {
        private TextView parentGroupTV;
        public GroupHolder(View v) {
            parentGroupTV = (TextView) v.findViewById(R.id.txt_title);
        }
        public void update(MKOLSearchRecord recordGroup) {
            parentGroupTV.setText(recordGroup.cityName);
        }
    }

    public final class ChildHolder {
        public ViewCityItem item;
//		private TextView parentGroupTV;
//		public ChildHolder(View v) {
//			parentGroupTV = (TextView) v.findViewById(R.id.txt_title);
//		}
//		public void update(MKOLSearchRecord recordGroup) {
//			parentGroupTV.setText(recordGroup.cityName);
//		}
    }
    // ===================================================
    private String formatDataSize(int size) {
        if (size == 0) return "";
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

}
