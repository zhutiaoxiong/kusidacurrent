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
import java.util.List;

import common.GlobalContext;
import model.ManagerSkins;
import ctrl.OCtrlInformation;
import model.information.DataSkin;
import view.clip.child.ClipBtnProgress;
import view.view4info.ViewFindForLookBigPic;
import view.view4info.ViewFindPay;
import view.view4info.ViewSelectCar;

/**
 * Created by qq522414074 on 2016/9/1.
 */
public class AdapterSkin extends BaseAdapter {
    private List<DataSkin> list;
    private LayoutInflater mInflater;
    private Context mContext;
    private ViewHolder currentDownLoadItem;

    public AdapterSkin(Context context, List<DataSkin> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
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
        if (convertView == null) {//��ʼ��
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.view_find_skin_item, null);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_size = (TextView) convertView.findViewById(R.id.txt_size);
            holder.txt_price = (TextView) convertView.findViewById(R.id.txt_price);
            holder.txt_listtitle = (TextView) convertView.findViewById(R.id.txt_listtitle);
            holder.img_icon = (SmartImageView) convertView.findViewById(R.id.img_icon);
            holder.btn_setup = (ClipBtnProgress) convertView.findViewById(R.id.btn_setup);
            convertView.setTag(holder);
            holder.btn_setup.setTag(holder);
            holder.img_icon.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();//��ʼ��holder
        }
        //设初始显示数据
        DataSkin info = list.get(position);
        if (info.isType) {
            holder.txt_listtitle.setVisibility(View.VISIBLE);
            holder.txt_listtitle.setText(info.name);
        } else {
            holder.data = info;
            holder.txt_listtitle.setVisibility(View.INVISIBLE);
            holder.txt_name.setText(info.title);
            holder.txt_size.setText(info.size);
            holder.txt_price.setText(info.feeStr);//价格
            holder.img_icon.setImageUrl(info.smallPic);
            //查询以前是否下载过
            String IsDownLoadZip = ODBHelper.getInstance(mContext).queryCommonInfo("IsDownLoadZip" + info.ide);
            if (TextUtils.isEmpty(IsDownLoadZip)) IsDownLoadZip = "false";
            if (info.ide == 0 || !IsDownLoadZip.equals("false")) {//0是指默认车
                holder.btn_setup.setText(mContext.getResources().getString(R.string.set));
                holder.btn_setup.setBGColor(ClipBtnProgress.COLOR_YELLOW);
            } else {
                holder.btn_setup.setText(mContext.getResources().getString(R.string.download));
                holder.btn_setup.setBGColor(ClipBtnProgress.COLOR_GREEN);
            }


            holder.btn_setup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    if (mContext.getResources().getString(R.string.set).equals(holder.btn_setup.getText().toString())) {
                        ViewSelectCar.setDefaultData(holder.data.ide, R.layout.view_find_car_dressup);//跳回皮肤下载
                        ViewSelectCar.type = holder.data.type;
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_select_car);
                    } else if (holder.data.status == 1) {//1.已买直接下载
                        currentDownLoadItem = holder;//点击后还无法下载，需要http取url
                        OCtrlInformation.getInstance().ccmd1402_getSkinAddress(holder.data.ide, holder.data.type);
                    } else {//2.未买跳转去买
                        ViewFindPay.PAY_MONEY = holder.data.fee;
                        ViewFindPay.SKIN_ID = holder.data.ide;
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_pay_for_skinderm);
                    }
                }
            });
            holder.img_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    ViewFindForLookBigPic.setDefaultData(holder.data.pics, R.layout.view_find_car_dressup);//跳回皮肤下载
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_find_for_look_bigpic);
                }
            });
        }
        return convertView;
    }


    public void startDownLoad(final int skinId, String url) {
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
                        ODBHelper.getInstance(mContext).changeCommonInfo("IsDownLoadZip" + skinId, "true");
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
                        if (BuildConfig.DEBUG) Log.e("下载", "adapterSkin:下载出错:"+e.toString());
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "下载失败");
                        currentDownLoadItem.btn_setup.setProgress(0);
                        currentDownLoadItem.btn_setup.setText("重下");
                        currentDownLoadItem.btn_setup.setClickable(true);
                    }
                    @Override//已存在相同下载
                    protected void warn(BaseDownloadTask task) {
                        if (BuildConfig.DEBUG) Log.e("下载", "adapterSkin:已存在相同下载");
                    }
                }).start();
    }

    //===================================================
    public final class ViewHolder {
        public DataSkin data;
        public String urlDownload;
        public TextView txt_name, txt_size, txt_price, txt_listtitle;
        public SmartImageView img_icon;
        public ClipBtnProgress btn_setup;
    }
}
