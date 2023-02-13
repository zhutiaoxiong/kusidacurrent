package view.clip;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.static_interface.OCallBack;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import adapter.AdapterUserListLocal;
import common.pinyinzhuanhuan.GetContacts;
import common.pinyinzhuanhuan.PinyinComparator;
import ctrl.OCtrlAuthorization;
import model.loginreg.DataUser;
import view.clip.child.SliderBarForUserlist;
import view.view4me.set.ClipTitleMeSet;

/**
 * Created by qq522414074 on 2016/7/19.
 */
public class ClipPopUserLocal implements OEventObject, OCallBack {
    private PopupWindow          popContain;//弹出管理
    private View                 parentView;//本对象显示
    private Context              context;
    private LinearLayout         thisView;
    private ClipTitleMeSet      title_head;
    private ListView             list_users;
    private AdapterUserListLocal adapter;
    private int                  selectedPos;
    private DataUser             selectedUser;
    public static boolean isForSelect = false;
    private String mark;//选择标记
    private OCallBack callback;
    private SliderBarForUserlist sideBar;
    private TextView dialog;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    //    private List<SortModel> SourceDateList;
    private List<DataUser> list;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    /**
     * 分组的布局
     */
    private LinearLayout titleLayout;

    /**
     * 分组上显示的字母
     */
    private TextView title;


    private MyHandler handler = new MyHandler();
    // ========================out======================
    private static ClipPopUserLocal _instance;

    public static ClipPopUserLocal getInstance() {
        if (_instance == null)
            _instance = new ClipPopUserLocal();
        return _instance;
    }

    public void show(boolean isForSelect, View parentView, String mark, OCallBack callback) {
        this.isForSelect = isForSelect;
        this.mark = mark;
        this.callback = callback;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        thisView = (LinearLayout) layoutInflater.inflate(R.layout.view_app_userlist_local, null);
        title_head = (ClipTitleMeSet) thisView.findViewById(R.id.title_head);
        list_users = (ListView) thisView.findViewById(R.id.list_users_local);


//      titleLayout = (LinearLayout) thisView.findViewById(R.id.title_layout_local);
//        title = (TextView) thisView.findViewById(R.id.title_local);


        pinyinComparator = new PinyinComparator();
        sideBar = (SliderBarForUserlist) thisView.findViewById(R.id.sidrbar_local);
        dialog = (TextView) thisView.findViewById(R.id.dialog_local);
        sideBar.setTextView(dialog);
        initView();
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USERLIST_RESULTBACK_CHANGE, this);

    }

    public void initView() {

    }

    public void initViews() {
        popContain = new PopupWindow(thisView);
        popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popContain.setFocusable(true);
        popContain.setTouchable(true);
        popContain.setOutsideTouchable(true);
        popContain.setAnimationStyle(R.style.LayoutEnterExitAnimation);
        popContain.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popContain.dismiss();
                    return true;
                }
                return false;
            }
        });
        popContain.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        invalidateUI();
    }

    public void initEvents() {

        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                popContain.dismiss();
            }
        });
        //
//        title_head.img_right.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
////                OToastOMG.getInstance().showInput(context, "新增联系人", "请输入联系人手机号", new String[]{OToastOMG.PHONE}, "phoneNum", ClipPopUserLinking.this);
////                OToastButton.getInstance().show(parentView, new String[]{"添加新联系人","打开手机通讯录"}, "operate", ClipPopUserLinking.this);
//            }
//        });

        list_users.setOnItemClickListener(new OnItemClickListenerMy() {
            @Override
            public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {

                selectedPos = position;
                selectedUser = adapter.getItem(position);
                String phoneNum = adapter.getItem(position).phoneNum;
                 OCtrlAuthorization.getInstance().ccmd1211_findnewuser(phoneNum);
//               adapter.setCurrentItem(position);
                if (isForSelect) {
                    callback.callback(mark, selectedUser);
                    popContain.dismiss();
                } else {
//                    OToastButton.getInstance().show(parentView, new String[]{"删除联系人", "拔打电话"}, "operate", ClipPopUserLinking.this);
                }
            }
        });
//        list_users.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
//                                 int totalItemCount) {
////                LogUtils.i(+visibleItemCount+"=当前对呀的Item是="+firstVisibleItem);
//                //字母连续断层使不能置顶，例如  D （空） F使D到F阶段不存在置顶
//                int section;
//                try {
//                    section = adapter.getSectionForPosition(firstVisibleItem);
//                } catch (Exception e) {
//                    return;
//                }
//                int nextSecPosition = adapter.getPositionForSection(section + 1);
//                //解决断层置顶
//                for (int i = 1; i < 30; i++) {
//                    //26个英文字母充分循环
//                    if (nextSecPosition == -1) {
//                        //继续累加
//                        int data = section + 1 + i;
//                        nextSecPosition = adapter.getPositionForSection(data);
//                    } else {
//                        break;
//                    }
//                }
//                if (firstVisibleItem != lastFirstVisibleItem) {
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
//                    params.topMargin = 0;
//                    titleLayout.setLayoutParams(params);
//                    title.setText(String.valueOf((char) section));
//
//                }
//                if (nextSecPosition == firstVisibleItem + 1) {
//                    View childView = view.getChildAt(0);
//                    if (childView != null) {
//                        int titleHeight = titleLayout.getHeight();
//                        int bottom = childView.getBottom();
//                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
//                                .getLayoutParams();
//                        if (bottom < titleHeight) {
//                            float pushedDistance = bottom - titleHeight;
//                            params.topMargin = (int) pushedDistance;
//                            titleLayout.setLayoutParams(params);
//                        } else {
//                            if (params.topMargin != 0) {
//                                params.topMargin = 0;
//                                titleLayout.setLayoutParams(params);
//                            }
//                        }
//                    }
//                }
//                lastFirstVisibleItem = firstVisibleItem;
//            }
//        });
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SliderBarForUserlist.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if(s==null || s.length() == 0 || adapter == null)return;
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    list_users.setSelection(position);
                }
            }
        });
    }

    @Override
    public void callback(String s, Object o) {

    }

    public void invalidateUI() {
        GetContacts getContacts = new GetContacts();
        Map<String, String> map1 = getContacts.getLocalContactInfo(context);
        Map<String, String> map2 = getContacts.getLocalContactInfo(context);
        Map<String, String> map3 = getContacts.mergeMap(map1, map2);
        List<DataUser> list = getContacts.getData(map3);
        if (list == null||list.size()==0)
            return;
        Collections.sort(list, pinyinComparator);
        adapter = new AdapterUserListLocal(context, list, R.layout.view_app_userlist_local);
        list_users.setAdapter(adapter);
//       list=null;
//       adapter.notifyDataSetChanged();
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.AUTHORIZATION_USERLIST_RESULTBACK_CHANGE)) {
            boolean error = (Boolean) paramObj;
//            if (error) {
//
//            }else{
//                handleChangeData();
//            }
            handleChangeData();
        }
    }

    public void handleChangeData() {
        Message message = new Message();
        message.what = 311;
        handler.sendMessage(message);
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 311:
                    popContain.dismiss();
                    break;
            }
        }
    }
}
