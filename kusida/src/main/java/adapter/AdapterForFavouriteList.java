package adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastButton;
import com.tencent.tauth.TencentCommon;

import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import common.global.NAVI;
import common.global.OWXShare;
import common.map.DataPos;
import common.map.MapPosGet;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.gps.DataGpsPoint;
import view.view4app.carpath.OToastSharePath;

/**
 * Created by qq522414074 on 2017/3/25.
 */

public class AdapterForFavouriteList extends BaseAdapter{
    public static int whereUse;//哪里使用这个adpter 0代表轨迹里的收藏页面1代表导航的首页
    private List<DataGpsPoint> list;
    private Context context;

    private LayoutInflater inflater;
    private int newPosition;
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    private int beforeMoveX;
    private int beforeMoveY;
    private long downTime;
    private long upTime;
    private DataPos dataPos;
    private int deleteBtnWidth;

    public AdapterForFavouriteList(List<DataGpsPoint> list, Context context,int deleteBtnWidth) {
        this.list = list;
        this.context = context;
        this.deleteBtnWidth = deleteBtnWidth;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.from(context).inflate(R.layout.view_favourite_list_item,null);
            holder.tv_comment= (TextView) convertView.findViewById(R.id.tv_comment);
            holder.tv_aderess= (TextView) convertView.findViewById(R.id.tv_aderess);
            holder.tv_delete= (TextView) convertView.findViewById(R.id.tv_delete);
            holder.img_findmore= (ImageView) convertView.findViewById(R.id.img_findmore);
            holder.re_movelayout= (RelativeLayout) convertView.findViewById(R.id.re_movelayout);
            holder.item_right= (RelativeLayout) convertView.findViewById(R.id.item_right);

            convertView.setTag(holder);
            holder.tv_comment.setTag(holder);
            holder.tv_aderess.setTag(holder);
            holder.tv_delete.setTag(holder);
            holder.img_findmore.setTag(holder);
            holder.re_movelayout.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if(list==null||list.size()==0)return convertView;
        DataGpsPoint info=list.get(position);
        holder.img_findmore.setTag(info);
        holder.tv_comment.setText("备注:"+list.get(position).note);
        holder.tv_aderess.setText(list.get(position).location);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.re_movelayout.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(deleteBtnWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
//        holder.item_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataPos data = (DataPos)v.getTag();
//                if (onClickListener != null) {
//                    if(data==null)return;
//                    onClickListener.onClicDelete(data);
//                }
//            }
//        });
        holder.re_movelayout.setTranslationX(0);
        initEvent(holder,position);
        return convertView;
    }
    public void initEvent(final ViewHolder holder,final int position){
        holder.tv_delete.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                newPosition = position;
                List<Long> collectIds=new ArrayList<Long>();
                collectIds.add(list.get(position).ide);
                list.remove(list.get(position));
                OCtrlGps.getInstance().ccmd1217_favoriteDelete(collectIds);
            }
        });
        holder.img_findmore.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(final View v) {
//            final  ViewHolder holder1= (ViewHolder) v.getTag();
             final   DataGpsPoint info= (DataGpsPoint) v.getTag();//"导航去这里"
                OToastButton.getInstance().show( v,new String[]{"设置备注","分享"},"opertate",new OCallBack(){

                    @Override
                    public void callback(String key, Object value) {
                            if(key.equals("opertate")){
                                String what=(String)value;
                                if(what.equals("导航去这里")){
                                ManagerCarList.getInstance().getCurrentCar();
                                    MapPosGet.searchCurrentPos(new MapPosGet.OnCurrentPosGetListener() {
                                        @Override
                                        public void onCurrentPosGet(DataPos posData) {
                                            dataPos=posData;
                                        }
                                    });
                                    openNavi(dataPos, DataPos.changeStrToLatLng(list.get(position).latlng,list.get(position).location));
                                }else if(what.equals("分享")){
                                    ShareUrlSearch shareUrlSearch = ShareUrlSearch.newInstance();
                                    shareUrlSearch.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {
                                        @Override
                                        public void onGetRouteShareUrlResult(ShareUrlResult result) {}
                                        @Override
                                        public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {}
                                        @Override
                                        public void onGetLocationShareUrlResult(final  ShareUrlResult result) {
                                            OToastSharePath.getInstance().show(v, "sharepath001", new OToastSharePath.OnClickButtonListener(){
                                                @Override
                                                public void onClick(int pos) {
                                                        switch (pos){
                                                            case 1:
                                                                OWXShare.SharePlace(list.get(position).location, result.getUrl());
                                                                break;
                                                            case 2:
                                                                OWXShare.SharePlace(list.get(position).location, result.getUrl());
                                                                break;
                                                            case 3:
                                                                TencentCommon.toTencent(context,GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you),list.get(position).location,result.getUrl(),0,"");
                                                                break;
                                                            case 4:
                                                                TencentCommon.toTencent(context, GlobalContext.getContext().getString(R.string.cool_your_friends_to_share_a_place_to_you),list.get(position).location,result.getUrl(),1,"");
                                                                break;
                                                        }

                                                }
                                            });
                                        }
                                    });
                                    shareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                                            .location( NAVI.Str2Latlng(list.get(position).latlng)).snippet("我的位置").name(list.get(position).location));
                                }else if(what.equals("设置备注")){
                                    newPosition=position;
                                    if(onClickBianJi!=null){
                                        onClickBianJi.click(info);
                                    }
                                }
                            }
                    }
                },info.location,"");
            }
        });
        holder.re_movelayout.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                super.onClickNoFast(v);
            }
        });

//                holder.re_movelayout.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                         ViewHolder holder1 = (ViewHolder) v.getTag();
//                        int deleteWidth = holder1.tv_delete.getWidth() ;
//                        int action = event.getAction();
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                downTime = System.currentTimeMillis();
//                                beforeMoveX = mDownX = (int) event.getRawX();//得到实时的位置
//                                beforeMoveY = mDownY = (int) event.getRawY();
//                                break;
//                            case ACTION_MOVE:
//                                moveX = (int) event.getRawX() - beforeMoveX;
//                                moveY = (int) event.getRawY() - beforeMoveY;
//                                beforeMoveX = (int) event.getRawX();//将现在的位置赋值给前一个位置
//                                beforeMoveY = (int) event.getRawY();
//                                int buttonX = (int) holder1.re_movelayout.getX();
//                                int moveXL;
//                                if (Math.abs(moveX) > Math.abs(moveY)) {
//                                    if (-deleteWidth <= buttonX && buttonX <= 0) {//zuo移动
//                                        if (moveX > 0) {//右滑
//
//                                            moveXL = moveX + buttonX > 0 ? 0 : buttonX + moveX;
//                                            v.setTranslationX(moveXL);
//                                            Log.d("滑动", "moveX=" + moveX + "向右滑动到了" + moveXL);
//                                        } else if (moveX < 0) { //左滑
//                                            moveXL = moveX + buttonX < -deleteWidth ? -deleteWidth : moveX + buttonX;
//                                            v.setTranslationX(moveXL);
//                                            Log.d("滑动", "moveX=" + moveX + "向左滑动到了" + moveXL);
//                                        }
//                                    }
//                                }
//                                break;
//                            case MotionEvent.ACTION_UP:
//                                ObjectAnimator oa1 = null;
//                                upTime = System.currentTimeMillis();//判断是点击还是长按
//                                float moveDistance = v.getTranslationX();//移动距离
//                                moveX = (int) event.getRawX() - mDownX;
//                                moveY = (int) event.getRawY() - mDownY;
//                                Log.d("手抬起", "moveDistance=" + moveDistance);
//                                if (Math.abs(moveX) > Math.abs(moveY)) {
//                                    if (moveX > 0) {//向右移
//                                        if (moveDistance > -deleteWidth / 2) {//右移大于一般的情况
//                                            Log.d("手抬起", "向右移大于一半" + moveDistance);
//                                            oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, 0).setDuration(250);
//                                            oa1.start();
//                                        } else {//右移小于一般的情况
//                                            Log.d("手抬起", "向右移小于一半" + moveDistance);
//                                            oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, -deleteWidth).setDuration(250);
//                                            oa1.start();
//                                        }
//                                    } else if (moveX < 0) {//向左仪
//
//                                        if (Math.abs(moveDistance) > (deleteWidth / 2)) {//向左移动大于一般的情况
//                                            Log.d("手抬起", "向左移大于一半" + moveDistance);
//                                            oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, -deleteWidth).setDuration(250);
//                                            oa1.start();
//                                        } else {//向左移动小于一般的情况
//                                            Log.d("手抬起", "向左移小于一半" + moveDistance);
//                                            oa1 = ObjectAnimator.ofFloat(v, "translationX", moveDistance, 0).setDuration(250);
//                                            oa1.start();
//                                        }
//                                    }
//                                }
//                        }
//                        return false;
//                    }
//
//                });

    }
    public class ViewHolder{
        public TextView tv_comment,tv_aderess,tv_delete;
        public ImageView img_findmore;
        public RelativeLayout re_movelayout;
        public View item_right;

    }
    public interface onClickBianJi {
        void click(DataGpsPoint info);
    }

    private onClickBianJi onClickBianJi;

    public void setonClickBianJi(onClickBianJi onClickBianJi) {
        this.onClickBianJi = onClickBianJi;
    }
    public void notifyDeleteRecord() {
        Message message = Message.obtain();
        message.what = 1217;
        handler.sendMessage(message);
    }
    public void addComment(String comment){
        Message message = Message.obtain();
        message.what = 112;
        message.obj=comment;
        handler.sendMessage(message);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1217){
                notifyDataSetChanged();
            }else if(msg.what == 112){
                String aa= (String) msg.obj;
                list.get(newPosition).note=aa;
                notifyDataSetChanged();
            }
        }
    };
    private void openNavi(DataPos start,DataPos end){
        if(start == null || start.pos == null){
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"无法获取自已定位");
            return;
        }
//        BNRoutePlanNode startNode = new BNRoutePlanNode(start.pos.longitude, start.pos.latitude, start.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode endNode   = new BNRoutePlanNode(end.pos.longitude, end.pos.latitude, end.address, null, BNRoutePlanNode.CoordinateType.BD09LL);
//        Intent intent    = new Intent(context, ActivityNaviMap.class);
//        Bundle bundle    = new Bundle();
//        bundle.putSerializable(ActivityNaviMap.START_NODE,startNode);
//        bundle.putSerializable(ActivityNaviMap.END_NODE,endNode);
//        intent.putExtras(bundle);
//      context.startActivity(intent);
    }
}
