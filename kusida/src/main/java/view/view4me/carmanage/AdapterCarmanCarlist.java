package view.view4me.carmanage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.client.proj.kusida.R;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.linkscarpods.blue.BluePermission;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.toast.OToastInput;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.zxing.activity.CaptureActivityForBindEquipment;

import java.util.List;

import common.GlobalContext;
import ctrl.OCtrlCar;
import ctrl.OCtrlRegLogin;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import view.find.BasicParamCheckPassWord;

import static common.GlobalContext.getContext;
import static common.GlobalContext.getCurrentActivity;

public class AdapterCarmanCarlist extends BaseAdapter implements OCallBack, OEventObject {
    public static int movetoPos = 0;
    private LayoutInflater    mInflater;
    private Context           mContext;
    private List<DataCarInfo> list;
    private static ViewHolder cacheHolder;

    private int mRightWidth = 0;
    public AdapterCarmanCarlist(Context context, int rightWidth, List<DataCarInfo> listcar) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = listcar;
        this.mRightWidth = rightWidth;
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK, this);
    }
    public void changeData(List<DataCarInfo> listPath) {
        this.list = listPath;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public DataCarInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<DataCarInfo> getDataList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.carman_listitem, null);
            ininId(holder, convertView);
            holder.swipeMenuLayout= (SwipeMenuLayout) convertView.findViewById(R.id.swipeMenuLayout);
            convertView.setTag(holder);
            holder.img_bt_blue.setTag(holder);
            holder.img_bt_model.setTag(holder);
            holder.img_bt_detail.setTag(holder);
            holder.img_bt_select.setTag(holder);
            holder.txt_bt_blue.setTag(holder);
            holder.txt_bt_model.setTag(holder);
            holder.txt_bt_detail.setTag(holder);
            holder.txt_bt_select.setTag(holder);
            holder.txt_equipment.setTag(holder);
            holder.delete.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//ODipToPx.dipToPx(mContext,205)
//        holder.item_left.setLayoutParams(lp1);
//        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
//        holder.item_right.setLayoutParams(lp2);
        if (list == null || list.size() == 0) return null;
        DataCarInfo carInfo = list.get(position);
        holder.data = carInfo;
        holder.pos = position;
        //车名
        holder.txt_carname.setText(carInfo.num);
        holder.txt_cartype.setText(carInfo.brand + "-" + carInfo.series);
        //图标
        if(carInfo.logo!=null && carInfo.logo.length()>0)holder.img_icon.setImageUrl(carInfo.logo);
        //当前车
        if (ManagerCarList.getInstance().getCurrentCar().ide == carInfo.ide) {
            holder.img_usenow.setVisibility(View.VISIBLE);
            holder.img_bt_select.setImageResource(R.drawable.carman_bt_select_on);
        } else {
            holder.img_usenow.setVisibility(View.INVISIBLE);
            holder.img_bt_select.setImageResource(R.drawable.carman_bt_select_off);
        }
        //状态
        if (carInfo.isActive == 1 && carInfo.isMyCar == 1 && carInfo.isAuthority == 0) {
            holder.txt_carstate.setText("已激活");
            holder.txt_carstate.setTextColor(mContext.getResources().getColor(R.color.blue_light));
        } else if (carInfo.isActive == 0 && carInfo.isMyCar == 1) {
            holder.txt_carstate.setText("未激活");
            holder.txt_carstate.setTextColor(mContext.getResources().getColor(R.color.black));
        } else if (carInfo.isAuthority == 1) {
            holder.txt_carstate.setText("已授权");
            holder.txt_carstate.setTextColor(mContext.getResources().getColor(R.color.blue_light));
        } else if (carInfo.isMyCar == 0) {
            holder.txt_carstate.setText("副车主");
            holder.txt_carstate.setTextColor(mContext.getResources().getColor(R.color.blue_light));
        }
        if(carInfo.isActive==0){
            holder.txt_equipment.setText("绑定设备");
        }else{
            holder.txt_equipment.setText("解绑设备");
        }
        //蓝牙
        if (carInfo.isBindBluetooth == 1) {
            holder.img_bluetooth.setVisibility(View.VISIBLE);
        } else {
            holder.img_bluetooth.setVisibility(View.INVISIBLE);
        }
        initClick(holder, position);
        Log.e("车列表拿的时间", "显示covertview" +System.currentTimeMillis());
        return convertView;
    }

    public void ininId(ViewHolder holder, View convertView) {
        holder.item_left = convertView.findViewById(R.id.item_left);
        holder.item_right = convertView.findViewById(R.id.item_right);
        holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
        holder.txt_carname = (TextView) convertView.findViewById(R.id.txt_carname);
        holder.txt_cartype = (TextView) convertView.findViewById(R.id.txt_cartype);
        holder.txt_carstate = (TextView) convertView.findViewById(R.id.txt_carstate);

        holder.img_icon = (SmartImageView) convertView.findViewById(R.id.img_icon);
        holder.img_usenow = (ImageView) convertView.findViewById(R.id.img_usenow);
        holder.img_bluetooth = (ImageView) convertView.findViewById(R.id.img_bluetooth);

        holder.img_bt_blue = (ImageView) convertView.findViewById(R.id.img_bt_blue);
        holder.img_bt_model = (ImageView) convertView.findViewById(R.id.img_bt_model);
        holder.img_bt_detail = (ImageView) convertView.findViewById(R.id.img_bt_detail);
        holder.img_bt_select = (ImageView) convertView.findViewById(R.id.img_bt_select);
        holder.txt_bt_blue = (TextView) convertView.findViewById(R.id.txt_bt_blue);
        holder.txt_bt_model = (TextView) convertView.findViewById(R.id.txt_bt_model);
        holder.txt_bt_detail = (TextView) convertView.findViewById(R.id.txt_bt_detail);
        holder.txt_bt_select = (TextView) convertView.findViewById(R.id.txt_bt_select);
        holder.re_out = (RelativeLayout) convertView.findViewById(R.id.re_out);
        holder.txt_equipment= (TextView) convertView.findViewById(R.id.txt_equipment);
    }
    private int permisonCount;
    /**
     * 点击事件的方法
     */
    public void initClick(final ViewHolder holder, final int position) {
        holder.txt_equipment.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(holder.txt_equipment.getText().equals("绑定设备")){
                    ViewCarmanModelBind.equipmentNunber="";
                    ManagerCarList.getInstance().setCurrentCar(holder.data.ide);
                    ViewCarmanModelBind.data = holder.data;
                    //                ViewHolder holder = (ViewHolder) v.getTag();
//                ViewCarmanInfoDetail.data = holder.data;
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_info_detail);
                    //拍照权限
                    boolean needPermission = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int permissionCamera = GlobalContext.getCurrentActivity().checkSelfPermission(Manifest.permission.CAMERA);
                        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                            Log.e("权限处理","未取得权限");
                            if(permisonCount==2){
                                Log.e("权限处理","已经申请两次");
                                if(!ActivityCompat.shouldShowRequestPermissionRationale(GlobalContext.getCurrentActivity(), Manifest.permission.CAMERA)){
                                    Log.e("权限处理","需要向用户解释");
                                    needPermission = true;
                                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                                            .withTitle("提示")
                                            .withInfo("激活设备时需要使用相机，需要相机权限，请前往设置")
                                            .withButton("取消","去设置")
                                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                                @Override
                                                public void onClickConfirm(boolean isClickConfirm) {
                                                    if (isClickConfirm) {
                                                        permisonCount=0;
                                                        PermissionUtil.gotoPermission(GlobalContext.getContext());
                                                    }
                                                }
                                            }).show();
                                }else{
                                    Log.e("权限处理","不需要向用户解释");
                                }
                            }else{
                                Log.e("权限处理","申请权限");
                                permisonCount++;
                                needPermission = true;
                                GlobalContext.getCurrentActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                            }

                        }else{
                            Log.e("权限处理","已有权限");
                            permisonCount=0;
                        }
                    }
                    if (!needPermission) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), CaptureActivityForBindEquipment.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("scantype", "oned");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                }else{
                    ViewCarmanModelUnBind.data = holder.data;
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_model_set_unbind);
                }
            }
        });
        holder.img_bt_blue.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
//                if(holder.data.isActive == 1) {
//                    ViewCarmanBlue.setData(holder.data);
////                    TurnOffKeyBoard.openKeyBoardOpenScoll(GlobalContext.getCurrentActivity());
//                    ManagerCarList.getInstance().setCurrentCar(holder.data.ide);
//                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_blue_set);
//                }else{
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"车辆未激活，请先激活");
//                }
                if(holder.data.isActive== 1) {
                    if (holder.data.isMyCar == 0) {
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"副车主无法解绑蓝牙");
                        return;
                    }
                    if(holder.data.terminalNum.startsWith("NFC")||holder.data.terminalNum.startsWith("MIN")){
                        ViewCarmanBlue.setData(holder.data);
//                    TurnOffKeyBoard.openKeyBoardOpenScoll(GlobalContext.getCurrentActivity());
                        ManagerCarList.getInstance().setCurrentCar(holder.data.ide);
                        ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_blue_set);
                    }else{
                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"PLUS版本无法解绑蓝牙");
                    }
                }else{
                    if(GpsUtil.isOPen(getContext())){
                        if (BluePermission.checkPermission(getCurrentActivity()) != 1) {//蓝牙未打开
                            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                                    .withInfo("绑定MINI版需要打开蓝牙")
                                    .withButton("否", "是")
                                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(boolean isClickConfirm) {
                                            if(isClickConfirm){
                                                BluePermission.openBlueTooth(getCurrentActivity());
                                            }
                                        }
                                    }).show();
                        }else{//蓝牙已打开
                            ViewCarmanBlueNew.setData(holder.data);
//                    TurnOffKeyBoard.openKeyBoardOpenScoll(GlobalContext.getCurrentActivity());
                            ManagerCarList.getInstance().setCurrentCar(holder.data.ide);
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.carman_blue_set_new);
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"车辆未激活，请先激活");
                        }
                    }else{
                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                                .withButton("否", "是")
                                .withInfo("绑定MINI版需要开启gps")
                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                    @Override
                                    public void onClickConfirm(boolean isClickConfirm) {
                                        if(isClickConfirm){
                                            GpsUtil.openGPS(getContext());
                                        }
                                    }
                                }).show();
                    }
                }
            }
        });
        holder.txt_bt_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_bt_blue.callOnClick();
            }
        });
        holder.img_bt_model.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
//                ViewCarmanModel.data = holder.data;
                if( holder.data.isMyCar==0||holder.data.isActive==1){
                    //解绑
                    ViewCarmanModelUnBind.data = holder.data;
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_model_set_unbind);
                }else{
                    //激活
                    ViewCarmanModelBind.data = holder.data;
                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_model_set_bind);
                }
//                TurnOffKeyBoard.openKeyBoardOpenScoll(GlobalContext.getCurrentActivity());
//                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_model_set);
            }
        });
        holder.txt_bt_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_bt_model.callOnClick();
            }
        });
        holder.img_bt_detail.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                ViewCarmanInfoDetail.data = holder.data;
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.carman_info_detail);
            }
        });
        holder.txt_bt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_bt_detail.callOnClick();
            }
        });
        holder.img_bt_select.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                movetoPos = holder.pos;
                ManagerCarList.getInstance().setCurrentCar(holder.data.ide);
                changeData(ManagerCarList.getInstance().getCarInfoList());
            }
        });
        holder.txt_bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_bt_select.callOnClick();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewHolder holder = (ViewHolder) v.getTag();
                cacheHolder=holder;
                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                        .withTitle("删除车辆")
                        .withInfo("删除车辆后，当前车辆绑定过的设备将不能在您的账号被激活且当前车辆的剩余服务期将永久失效")
                        .withButton("取消","继续")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if (isClickConfirm){
                                    //跳转nfc界面
                                    OToastInput.getInstance().showInput(holder.delete, "删除车辆", "请输入登录密码:", new String[]{OToastInput.PASS}, "deletemycar", AdapterCarmanCarlist.this);
//                                    OCtrlCar.getInstance().ccmd1202_deletecar(holder.data.ide);
                                }
                            }
                        })
                        .show();
            }
        });
        holder.re_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if(clickCount==5){
                    Intent intent = new Intent();
                    intent.setClass(getContext(), ActivityAddCustomerInfo.class);
                    GlobalContext.getCurrentActivity().startActivity(intent);
                    clickCount=0;
                }
            }
        });

    }
    private int clickCount;

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
            if(BasicParamCheckPassWord.isFindMain==9){
                boolean check = (Boolean) paramObj;
                if (check) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                            .withTitle("删除车辆")
                            .withInfo("删除后，当前车辆绑定过的设备将永久不能被您的账号激活")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm ) {
                                        OCtrlCar.getInstance().ccmd1202_deletecar(cacheHolder.data.ide);
                                        cacheHolder.swipeMenuLayout.quickClose();
                                    }
                                }
                            })
                            .show();
                }
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
        if (key.equals("deletemycar")) {
            JsonObject obj  = (JsonObject) value;
            String     pass = OJsonGet.getString(obj, OToastInput.PASS);
            BasicParamCheckPassWord.isFindMain=9;
            OCtrlRegLogin.getInstance().ccmd1104_checkPassword(pass);
        }
    }

    // ===================================================
    public final class ViewHolder {
        public DataCarInfo    data;
        public int            pos;
        public SmartImageView img_icon;
        public TextView       txt_carname, txt_cartype, txt_carstate,txt_equipment;
        public LinearLayout delete;
        public ImageView img_usenow, img_bluetooth;
        public ImageView img_bt_blue, img_bt_model, img_bt_detail, img_bt_select;
        public TextView  txt_bt_blue, txt_bt_model, txt_bt_detail, txt_bt_select;
        public View item_left, item_right;
        public RelativeLayout re_out;
        public SwipeMenuLayout swipeMenuLayout;
    }
}
