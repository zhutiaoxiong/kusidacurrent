package adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import model.ManagerSkins;
import ctrl.OCtrlInformation;
import model.information.DataSkin;
import view.clip.child.ClipBtnProgress;
import view.view4info.ViewFindForLookBigPic;
import view.view4info.ViewSelectCar;

/**
 * Created by qq522414074 on 2016/9/1.
 */
public class AdapterDownLoad extends BaseAdapter {
    private List<DataSkin> list = new ArrayList<DataSkin>();
    private LayoutInflater mInflater;
    private Context context;
    private ViewHolder     currentDownLoadItem;

    public AdapterDownLoad(Context context, List<DataSkin> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void changeData(List<DataSkin> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public DataSkin getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.view_find_downloadmanager_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.view_find_download_listview_title);
            holder.size = (TextView) convertView.findViewById(R.id.view_find_download_listview_packagesize);
            holder.img_icon = (SmartImageView) convertView.findViewById(R.id.img_icon);
            holder.btn_setup = (ClipBtnProgress) convertView.findViewById(R.id.btn_setup);
            convertView.setTag(holder);
            holder.btn_setup.setTag(holder);
            holder.img_icon.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设初始显示数据
        DataSkin info = list.get(position);
        holder.data = info;
        holder.title.setText(list.get(position).title);
        holder.size.setText(list.get(position).size);
        holder.img_icon.setImageUrl(list.get(position).smallPic);
        //查询以前是否下载过
        String IsDownLoadZip = ODBHelper.getInstance(context).queryCommonInfo("IsDownLoadZip"+info.ide);
        if(TextUtils.isEmpty(IsDownLoadZip))IsDownLoadZip = "false";
        if (!IsDownLoadZip.equals("false") || info.ide == 0) {//0是指默认车
            holder.btn_setup.setText(context.getResources().getString(R.string.set));
            holder.btn_setup.setBGColor(ClipBtnProgress.COLOR_YELLOW);
        }else{
            holder.btn_setup.setText(context.getResources().getString(R.string.download));
            holder.btn_setup.setBGColor(ClipBtnProgress.COLOR_GREEN);
        }
        //initClick
        holder.btn_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                if(context.getResources().getString(R.string.set).equals(holder.btn_setup.getText().toString())) {
                    ViewSelectCar.setDefaultData(holder.data.ide,R.layout.view_find_downloadmanager);//跳回皮肤管理
                    ViewSelectCar.type=holder.data.type;
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_select_car);
                }else{//下载
                    currentDownLoadItem = holder;
                    OCtrlInformation.getInstance().ccmd1402_getSkinAddress(holder.data.ide,holder.data.type);
                }
            }
        });
        holder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                ViewFindForLookBigPic.setDefaultData(holder.data.pics,R.layout.view_find_downloadmanager);//跳回皮肤管理
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_for_look_bigpic);
            }
        });
        return convertView;
    }

    public void startDownLoad(final int skinId,String url) {
        if (currentDownLoadItem == null || url == null) return;
        currentDownLoadItem.btn_setup.setText("下载中");
        currentDownLoadItem.btn_setup.setClickable(false);
        currentDownLoadItem.urlDownload = url;
        String filename = ManagerSkins.getInstance().getSkinZipFileName(url);
        FileDownloader.getImpl().create(url)
                .setPath(ManagerSkins.getSkinFolder(GlobalContext.getContext()) + File.separator + filename + ".zip")
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    @Override//等待
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        progressDialog.show();
                    }
                    @Override//下载进度回调
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        currentDownLoadItem.btn_setup.setMax(totalBytes);
//                        currentDownLoadItem.btn_setup.setProgress(soFarBytes);
                    }
                    @Override//完成下载
                    protected void completed(BaseDownloadTask task) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "下载成功");
                        ODBHelper.getInstance(context).changeCommonInfo("IsDownLoadZip"+skinId,"true");
                        //下载完变设置
                        currentDownLoadItem.btn_setup.setText("设置");
                        currentDownLoadItem.btn_setup.setClickable(true);
                        currentDownLoadItem.btn_setup.setBGColor(ClipBtnProgress.COLOR_YELLOW);
                    }
                    @Override//暂停
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }
                    @Override//下载出错
                    protected void error(BaseDownloadTask task, Throwable e) {
                        if (BuildConfig.DEBUG) Log.e("下载", "adapterDownload:下载出错:"+e.toString());
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "下载失败");
                        currentDownLoadItem.btn_setup.setProgress(0);
                        currentDownLoadItem.btn_setup.setBGColor(ClipBtnProgress.COLOR_GREEN);
                        currentDownLoadItem.btn_setup.setText("重下");
                        currentDownLoadItem.btn_setup.setClickable(true);
                    }
                    @Override//已存在相同下载
                    protected void warn(BaseDownloadTask task) {
                        if (BuildConfig.DEBUG) Log.e("下载", "adapterDownLoad:已存在相同下载");
                    }
                }).start();
    }


    private class ViewHolder {
        public DataSkin data;
        public String   urlDownload;
        public SmartImageView img_icon;
        public TextView       title, size;
        public ClipBtnProgress btn_setup;
    }
}
