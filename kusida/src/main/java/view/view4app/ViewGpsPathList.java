package view.view4app;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.listview.SwipeAndPushRefreshListView;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.input.ViewAboveInput;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterShowGpsPath;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerGps;
import model.gps.DataGpsPath;
import view.view4me.set.ClipTitleMeSet;

/**
 * 行车轨迹
 */
public class ViewGpsPathList extends RelativeLayoutBase {
    private ClipTitleMeSet               title_head;
    private SwipeAndPushRefreshListView list_paths;
    private ImageView                   find;
    private long                        carId;
    private AdapterShowGpsPath          adapter;
    private List<DataGpsPath>           singleCarPath;
    private RelativeLayout              lin_pop_input, re_nolayout;
    private ViewAboveInput inputAbove;
    private String inputStr;//输入的备注信息
    private boolean needAddData,gettingData = false;
    private int startPosition;
    public ViewGpsPathList(Context context, AttributeSet attrs) {
        super(context, attrs);
         if (BuildConfig.DEBUG) Log.e("ViewShow","ViewGpsPathList");
        LayoutInflater.from(context).inflate(R.layout.view_app_gps_path_list, this, true);
        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        list_paths = (SwipeAndPushRefreshListView) findViewById(R.id.list_paths);
//        list_paths.setRightViewWidth(ODipToPx.dipToPx(getContext(),90));
        list_paths.setRightViewWidth(0);
        find = (ImageView) findViewById(R.id.find);
        lin_pop_input = (RelativeLayout) findViewById(R.id.lin_pop_input);
        re_nolayout = (RelativeLayout) findViewById(R.id.re_nolayout);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_PATHLIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.GPS_COLLECT_INFO_BACK, this);
    }

    @Override
    public void initViews() {
        re_nolayout.setVisibility(View.INVISIBLE);
        if (ManagerCarList.getInstance().getCurrentCar().ide > 0) {
            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide, 0, 0, 0, 20);
            startPosition=0;
        } else {
            re_nolayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.activity_kulala_main);
            }
        });
        // select car
        title_head.img_right.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.gps_path_collect_list);
            }
        });
//		list_paths.setOnItemClickListener(new OnItemClickListenerMy() {
//			@Override
//			public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
//				ManagerGps.getInstance().currentPath = adapter.getItem(position);
//				OToastButton.getInstance().show(title_head, new String[]{getResources().getString(R.string.check_the_track), getResources().getString(R.string.delete_the_trajectory)}, "operate", ViewGpsPathList.this);
//			}
//		});

        list_paths.setOnLoadBottomListener(new SwipeAndPushRefreshListView.OnLoadBottomListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list_paths.loadComplete();
                    }
                }, 2000);
            }
        });
        list_paths.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
//					adapter.notifyDataSetInvalidated();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://滚动状态停止
//						adapter.notifyDataSetInvalidated();
                        boolean needScoll = false;
                        if(view.getFirstVisiblePosition() == 0){//顶
                            needAddData=false;
                            needScoll = true;
                            list_paths.loadStart();
                            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide, 0, 0, 0, 20);
                            startPosition=0;
                        }else if(view.getLastVisiblePosition() >= adapter.getCount()){//底
                            if(gettingData)return;
                            needAddData=true;
                            needScoll = true;
                            gettingData = true;
                            list_paths.loadStart();
                            OCtrlGps.getInstance().ccmd1215_getPathList(ManagerCarList.getInstance().getCurrentCar().ide, 0, 0, adapter.getCount(), 20);
                            startPosition=adapter.getCount();
                             if (BuildConfig.DEBUG) Log.e("观察listsize", "invalidateUI: "+"这里发协议");
                        }
                        // 为了显示效果，采用延迟加载
                        if(needScoll) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    gettingData = false;
                                    list_paths.loadComplete();
                                }
                            }, 2000);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        find.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_apppath_find);
            }
        });
//        if(adapter!=null){
//            adapter.setonClickBianJi(new AdapterShowGpsPath.onClickBianJi() {
//                @Override
//                public void initClick() {
//
//                    lin_pop_input.setVisibility(View.VISIBLE);
//                    if (lin_pop_input.getChildCount() > 0)return;//已加入过
//                    if (inputAbove == null)inputAbove = new ClipAnnualInputAbove(getContext(), null);
//                    inputAbove.show("请输入备注内容", inputAbove.edit_input.getText().toString(), 20);
//                    lin_pop_input.addView(inputAbove);
//                    //输入出来后禁用组件
//                    inputAbove.SetOnClickConfirmListener(new ClipAnnualInputAbove.OnClickConfirmListener() {
//                        @Override
//                        public void onClickConfirm(String InputTxt) {
//                            if (InputTxt != null) inputAbove.edit_input.setText(InputTxt);
//                            //隐藏输入
//                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(inputAbove.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                            lin_pop_input.removeAllViews();
//                            //输入关闭后开启组件
//                            lin_pop_input.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                }
//            });
//        }

    }

    public interface scollListner {
        void scoll();
    }

    public void scoll() {

    }

    private scollListner listner;

    public void setscollListner(scollListner listner) {
        this.listner = listner;
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.GPS_PATHLIST_RESULTBACK)) {
            int result = (Integer) paramObj;
            if (result == 1244) {
                adapter.notifyChangeShouCangImg();
            } else if (result == 1224) {
                adapter.notifyDeleteRecord();
            } else if (result == 1215) {
                handleChangeData();
            }else if(result == 1243){
              adapter.addComment(inputStr);
            }
        }
    }

    @Override
    public void callback(String key, Object value) {
    }

    @Override
    protected void onDetachedFromWindow() {
        list_paths.setAdapter(null);
        adapter = null;
        ODispatcher.removeEventListener(OEventName.GPS_PATHLIST_RESULTBACK, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void invalidateUI() {
        List<DataGpsPath>          currentScollCarPath= ManagerGps.getInstance().singleCarPath;
       if(singleCarPath==null){
          singleCarPath=new ArrayList<>();
       }
         if (BuildConfig.DEBUG) Log.e("观察listsize", "invalidateUI: "+singleCarPath.size() +"------"+currentScollCarPath.size());
           if(needAddData){
                if (BuildConfig.DEBUG) Log.e("观察listsize", "invalidateUI: "+singleCarPath.size() +"------"+currentScollCarPath.size()+"当前位置"+startPosition);
               if(singleCarPath.size()<=startPosition){
                    if (BuildConfig.DEBUG) Log.e("观察listsize", "invalidateUI: "+singleCarPath.size() +"------"+currentScollCarPath.size()+"当前位置"+startPosition);
                   singleCarPath.addAll(currentScollCarPath);
               }
           }else{
//               if(currentScollCarPath.size()!=0){
//                   singleCarPath.clear();
//               }
               if(singleCarPath.size()>0){
                   singleCarPath.clear();
               }
               if(singleCarPath.size()==0){
                   singleCarPath.addAll(currentScollCarPath);
               }
           }
         if (BuildConfig.DEBUG) Log.e("观察listsize", "invalidateUI: "+singleCarPath.size() +"------"+currentScollCarPath.size());
        if ( singleCarPath==null||singleCarPath.size() == 0) {
            re_nolayout.setVisibility(View.VISIBLE);
            list_paths.setVisibility(View.INVISIBLE);
        } else {
            re_nolayout.setVisibility(View.INVISIBLE);
            list_paths.setVisibility(View.VISIBLE);
        }
        if (adapter == null) {
            adapter = new AdapterShowGpsPath(getContext(),ODipToPx.dipToPx(getContext(),90), singleCarPath);
            list_paths.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (adapter != null) {
            adapter.setonClickBianJi(new AdapterShowGpsPath.onClickBianJi() {
                @Override
                public void click(final DataGpsPath info) {
                    if (lin_pop_input.getChildCount() > 0) return;//已加入过
//                    TurnOffKeyBoard.openKeyBoard(GlobalContext.getCurrentActivity());
                    if (inputAbove == null)inputAbove = new ViewAboveInput(getContext(), null);
                    inputAbove.show("请输入备注内容", "", 20);
                    lin_pop_input.addView(inputAbove);
                    inputAbove.SetOnClickConfirmListener(new ViewAboveInput.OnClickConfirmListener() {
                        @Override
                        public void onClickConfirm(String InputTxt) {
                            inputStr=InputTxt;
                            if(ManagerCarList.getInstance().getCurrentCar().ide>=0){
                                OCtrlGps.getInstance().ccmd1243_addComment(ManagerCarList.getInstance().getCurrentCar().ide,info.ide,InputTxt,0,0,0,20);
                            }
                            lin_pop_input.removeAllViews();
                        }
                    });
                }
            });
        }
    }
}
