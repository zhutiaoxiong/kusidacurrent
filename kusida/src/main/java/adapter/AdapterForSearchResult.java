package adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.tencent.tauth.TencentCommon;

import java.text.DecimalFormat;
import java.util.List;

import common.global.OWXShare;
import common.pinyinzhuanhuan.DateReplace;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerCommon;
import model.ManagerGps;
import model.gps.CarPath;
import model.gps.DataGpsPath;
import view.view4app.carpath.OToastSharePath;
import view.view4app.carpath.ViewCarPath;

import static android.view.MotionEvent.ACTION_MOVE;
import static com.client.proj.kusida.R.id.beizhu_txt;
import static com.client.proj.kusida.R.id.car_num;
import static com.client.proj.kusida.R.id.first_time;
import static com.client.proj.kusida.R.id.location_details;
import static com.client.proj.kusida.R.id.millage;
import static com.client.proj.kusida.R.id.second_time;
import static com.client.proj.kusida.R.id.timelength;

/**
 * Created by qq522414074 on 2017/3/23.
 */

public class AdapterForSearchResult extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DataGpsPath> list;
    private int currentPosition;
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    private int beforeMoveX;
    private int beforeMoveY;
    private long downTime;
    private long upTime;
    private ImageView shoucang;
    private int newPosition;//表示点击收藏按钮的位置
    private long ide;
    private int mRightWidth = 0;
    private boolean isOpen = false;//判断当前的position是打开还是关闭

    public AdapterForSearchResult(Context context,int mRightWidth, List<DataGpsPath> listPath) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.list = listPath;
        this.mRightWidth = mRightWidth;
    }

    public void changeUI(List<DataGpsPath> listPath) {
        this.list = listPath;
        notifyDataSetChanged();
    }

    public DataGpsPath getCurrentItem() {
        return list.get(currentPosition);
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }


    @Override
    public DataGpsPath getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<DataGpsPath> getDataList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_gps_path, null);
            findId1(holder, convertView);
            convertView.setTag(holder);
            holder.delete.setTag(holder);
            holder.item_left.setTag(holder);
            holder.pathshare.setTag(holder);
            holder.pathbianji.setTag(holder);
            holder.pathshoucang.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_left.setTranslationX(0);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//ODipToPx.dipToPx(mContext,205)
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        if (list == null || list.size() == 0) return null;
        if (list.get(position).comment == null || list.get(position).comment.length() == 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(mContext, 173.5f));
            convertView.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ODipToPx.dipToPx(mContext, 205.5f));
            convertView.setLayoutParams(params);
        }
        if (list.get(position).comment == null || list.get(position).comment.length() == 0) {
            holder.beizhu_txt.setVisibility(View.GONE);
            holder.beizhu_img.setVisibility(View.GONE);
        } else {
            holder.beizhu_txt.setVisibility(View.VISIBLE);
            holder.beizhu_img.setVisibility(View.VISIBLE);
        }
        DataGpsPath info = list.get(position);
        holder.pathbianji.setTag(info);
        holder.first_time.setText(ODateTime.time2StringHHmm(info.endTime));
        holder.location_details.setText("| " + info.endLocation);
        holder.second_time.setText(ODateTime.time2StringHHmm(info.startTime));
        holder.location.setText("| " + info.startLocation);
        DecimalFormat df = new DecimalFormat("##0.00");
        holder.millage.setText("里程:" + df.format(info.miles) + "公里");
        holder.timelength.setText("时长:" + info.intervalTime);
        String createtime = DateReplace.returnDate(info.createTime);
        holder.time.setText(createtime);
        String num = ManagerCarList.getInstance().getCurrentCar().num;
        holder.car_num.setText(num);
        holder.beizhu_txt.setText(info.comment);
        holder.ide = list.get(position).ide;
        if (list.get(position).isCollect == 0) {
            holder.pathshoucang.setImageResource(R.drawable.pathshoucangred);
        } else {
            holder.pathshoucang.setImageResource(R.drawable.pathshoucang);
        }
        //执行点击事件
        if (holder != null) {
            click(holder, position);
            //执行ontouch事件
//            itemtouch(holder, convertView);
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView car_num, time, first_time, location_details, millage, timelength, beizhu_txt, second_time, location, delete;
        public ImageView pathbianji, pathshare, pathshoucang, beizhu_img;
//        public RelativeLayout movelayout;
        public View item_left,item_right, middle_layout, layout_black_bg;
        public long ide;
    }

    /**
     * touch事件的方法
     *
     * @param
     */
    public void itemtouch(ViewHolder holder, final View convertview) {
        holder.item_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ViewHolder holder = (ViewHolder) v.getTag();
                int deleteWidth = holder.delete.getWidth() + ODipToPx.dipToPx(mContext, 10);
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downTime = System.currentTimeMillis();
                        beforeMoveX = mDownX = (int) event.getRawX();//得到实时的位置
                        beforeMoveY = mDownY = (int) event.getRawY();
                        break;
                    case ACTION_MOVE:
                        moveX = (int) event.getRawX() - beforeMoveX;
                        moveY = (int) event.getRawY() - beforeMoveY;
                        beforeMoveX = (int) event.getRawX();//将现在的位置赋值给前一个位置
                        beforeMoveY = (int) event.getRawY();
                        int buttonX = (int) holder.item_left.getX();
                        int moveXL;
                        if (Math.abs(moveX) > Math.abs(moveY)) {
                            if (-deleteWidth <= buttonX && buttonX <= 0) {//zuo移动
                                if (moveX > 0) {//右滑

                                    moveXL = moveX + buttonX > 0 ? 0 : buttonX + moveX;
                                    v.setTranslationX(moveXL);
                                    Log.d("滑动", "moveX=" + moveX + "向右滑动到了" + moveXL);
                                } else if (moveX < 0) { //左滑
                                    moveXL = moveX + buttonX < -deleteWidth ? -deleteWidth : moveX + buttonX;
                                    v.setTranslationX(moveXL);
                                    Log.d("滑动", "moveX=" + moveX + "向左滑动到了" + moveXL);
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ObjectAnimator oa1 = null;
                        upTime = System.currentTimeMillis();//判断是点击还是长按
                        float moveDistance = v.getTranslationX();//移动距离
                        moveX = (int) event.getRawX() - mDownX;
                        moveY = (int) event.getRawY() - mDownY;
                        Log.d("手抬起", "moveDistance=" + moveDistance);
                        if (Math.abs(moveX) > Math.abs(moveY)) {
                            if (moveX > 0) {//向右移
                                if (moveDistance > -deleteWidth / 2) {//右移大于一般的情况
                                    Log.d("手抬起", "向右移大于一半" + moveDistance);
                                    oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, 0).setDuration(250);
                                    oa1.start();
                                    isOpen = false;
                                } else {//右移小于一般的情况
                                    Log.d("手抬起", "向右移小于一半" + moveDistance);
                                    oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, -deleteWidth).setDuration(250);
                                    oa1.start();
                                    isOpen = true;
                                }
                            } else if (moveX < 0) {//向左仪

                                if (Math.abs(moveDistance) > (deleteWidth / 2)) {//向左移动大于一般的情况
                                    Log.d("手抬起", "向左移大于一半" + moveDistance);
                                    oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, -deleteWidth).setDuration(250);
                                    oa1.start();
                                    isOpen = true;
                                } else {//向左移动小于一般的情况
                                    Log.d("手抬起", "向左移小于一半" + moveDistance);
                                    oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, 0).setDuration(250);
                                    oa1.start();
                                    isOpen = false;
                                }
                            }
                        }
                        if ((upTime - downTime) < 200 && (Math.abs(moveX) < 1 || Math.abs(moveY) < 1)) {
                            convertview.setTranslationX(0);
                            CarPath.position=R.layout.view_apppath_find;
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_about);
                        }
                        if ((upTime - downTime) > 200 && (Math.abs(moveX) < 3 || Math.abs(moveY) < 3)) {
                            CarPath.position=R.layout.view_apppath_find;
                            convertview.setTranslationX(0);
                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_setup);
                        }
                }
                return false;
            }
        });
    }

    /**
     * 找大Id的方法
     */
    public void findId1(ViewHolder holder, View convertView) {
        holder.middle_layout = convertView.findViewById(R.id.middle_layout);
        holder.layout_black_bg = convertView.findViewById(R.id.layout_black_bg);
        holder.item_right = (View) convertView.findViewById(R.id.item_right);
        holder.item_left = (View) convertView.findViewById(R.id.item_left);
        holder.car_num = (TextView) convertView.findViewById(car_num);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.first_time = (TextView) convertView.findViewById(first_time);
        holder.location_details = (TextView) convertView.findViewById(location_details);
        holder.millage = (TextView) convertView.findViewById(millage);
        holder.timelength = (TextView) convertView.findViewById(timelength);
        holder.beizhu_txt = (TextView) convertView.findViewById(beizhu_txt);
        holder.second_time = (TextView) convertView.findViewById(second_time);
        holder.location = (TextView) convertView.findViewById(R.id.location);
        holder.delete = (TextView) convertView.findViewById(R.id.delete);
        holder.pathbianji = (ImageView) convertView.findViewById(R.id.pathbianji);
        holder.pathshare = (ImageView) convertView.findViewById(R.id.pathshare);
        holder.pathshoucang = (ImageView) convertView.findViewById(R.id.pathshoucang);
        holder.beizhu_img = (ImageView) convertView.findViewById(R.id.beizhu_img);
    }

    /**
     * 点击事件的方法
     */
    public void click(final ViewHolder holder, final int position) {
        holder.pathshare.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ViewHolder holder1 = (ViewHolder) v.getTag();
                OToastSharePath.getInstance().show(holder1.pathshare, "sharepath003",  new OToastSharePath.OnClickButtonListener(){
                    @Override
                    public void onClick(int pos) {
                        switch (pos){
                            case 1:
                                OWXShare.ShareFriendURL(ManagerCommon.trackShareObj.shareTitle,ManagerCommon.trackShareObj.shareComment,ManagerCommon.trackShareObj.shareUrl.replace("$1",list.get(position).ide+"").replace("$2",1+""));
                                break;
                            case 2:
                                OWXShare.ShareTimeLineURL(ManagerCommon.trackShareObj.shareTitle,ManagerCommon.trackShareObj.shareComment,ManagerCommon.trackShareObj.shareUrl.replace("$1",list.get(position).ide+"").replace("$2",1+""));
                                break;
                            case 3:
                                TencentCommon.toTencent(mContext,ManagerCommon.trackShareObj.shareTitle,ManagerCommon.trackShareObj.shareComment,ManagerCommon.trackShareObj.shareUrl.replace("$1",list.get(position).ide+"").replace("$2",1+""),0,ManagerCommon.trackShareObj.sharePic);
                                break;
                            case 4:
                                TencentCommon.toTencent(mContext,ManagerCommon.trackShareObj.shareTitle,ManagerCommon.trackShareObj.shareComment,ManagerCommon.trackShareObj.shareUrl.replace("$1",list.get(position).ide+"").replace("$2",1+""),1,ManagerCommon.trackShareObj.sharePic);
                                break;
                        }

                    }
                });
            }
        });
        holder.pathbianji.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                newPosition=position;
                DataGpsPath info= (DataGpsPath) v.getTag();
                if(onClickBianJi!=null){
                    onClickBianJi.click(info);
                }
            }
        });
        holder.delete.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
//                long trailInfoId,long carId,long startTime, long endTime,int start, int size
                newPosition = position;
                long carId = ManagerCarList.getInstance().getCurrentCar().ide;
                OCtrlGps.getInstance().ccmd1224_deletePath(list.get(position).ide, carId, 0, 0, 0, 20);
            }
        });
        holder.pathshoucang.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                shoucang = holder.pathshoucang;
                newPosition = position;
                long carId = ManagerCarList.getInstance().getCurrentCar().ide;
                if (list == null || list.size() == 0) return;
                if (list.get(position).isCollect == 0) {
                    OCtrlGps.getInstance().ccmd1244_GPSAreaCollect(carId, list.get(position).ide, 0, 20, 0);
                } else if (list.get(position).isCollect == 1) {
                    OCtrlGps.getInstance().ccmd1244_GPSAreaCollect(carId, list.get(position).ide, 0, 20, 1);
                }
            }
        });
        holder.middle_layout.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                newPosition = position;
                clickToMapPath();
            }
        });
        holder.middle_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                newPosition = position;
                clickToMapPath();
                return false;
            }
        });
        holder.layout_black_bg.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                newPosition = position;
                clickToMapPath();
            }
        });
        holder.layout_black_bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                newPosition = position;
                clickToMapPath();
                return false;
            }
        });
    }
    public void clickToMapPath(){
        CarPath. position=R.layout.view_apppath_find;
        ManagerGps.path=DataGpsPath.getLatLngs(list.get(newPosition).latlngs);
        ManagerGps.startLocation   =  list.get(newPosition).startLocation;
        Intent intent = new Intent();
        intent.setClass(mContext, ViewCarPath.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public interface onClickBianJi {
        void click(DataGpsPath info);
    }

    private onClickBianJi onClickBianJi;

    public void setonClickBianJi(onClickBianJi onClickBianJi) {
        this.onClickBianJi = onClickBianJi;
    }

    public void notifyChangeShouCangImg() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendMessage(message);
    }

    public void notifyDeleteRecord() {
        Message message = Message.obtain();
        message.what = 111;
        handler.sendMessage(message);
    }
    public void addComment(String comment){
        Message message = Message.obtain();
        message.what = 112;
        message.obj=comment;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 110) {
                if (list.get(newPosition).isCollect == 0) {
                    shoucang.setImageResource(R.drawable.pathshoucang);
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"取消收藏");
                    list.get(newPosition).isCollect =1;
                } else {
                    shoucang.setImageResource(R.drawable.pathshoucangred);
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"收藏成功");
                    list.get(newPosition).isCollect =0;
                }
            }else if(msg.what == 111){
                list.remove(list.get(newPosition));
                notifyDataSetChanged();
            }else if(msg.what == 112){
                String aa= (String) msg.obj;
                list.get(newPosition).comment=aa;
                notifyDataSetChanged();
            }
        }
    };
}
