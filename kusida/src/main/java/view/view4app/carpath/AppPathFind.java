package view.view4app.carpath;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.listview.SwipeListView;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.static_interface.OnItemClickListenerMy;
import com.kulala.staticsview.input.ViewAboveInput;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.AdapterForSearchHistory;
import adapter.AdapterForSearchResult;
import common.pinyinzhuanhuan.KeyBoard;
import ctrl.OCtrlGps;
import model.ManagerCarList;
import model.ManagerGps;
import model.gps.DataGpsPath;
import model.gps.SearchHistory;

/**
 * 搜索轨迹的页面包括轨迹列表和搜索记录
 */

public class AppPathFind extends RelativeLayoutBase {
    private TextView cancle, text;
    private ImageView find, delete;
    private ListView path_history;//搜索历史
    private SwipeListView list_paths;//
    private AdapterForSearchHistory historyAdapter;//查找历史
    private AdapterForSearchResult resultAdapter;//搜索结果
    private List<SearchHistory> listHistory = new ArrayList<>();
    private List<DataGpsPath> listSearchResult = new ArrayList<>();
    private EditText edit;
    private RelativeLayout re2;
    private Context context;
    private String searchText;//每次输入框搜索的文字
    private View headerView;
    private boolean isChange;//输入框的文字是否改变
    private String inputStr;//输入的备注信息
    private ViewAboveInput inputAbove;
    private RelativeLayout     lin_pop_input;
    private long carId = ManagerCarList.getInstance().getCurrentCar().ide;

    public AppPathFind(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_apppath_find, this, true);
        cancle = (TextView) findViewById(R.id.cancle);
        find = (ImageView) findViewById(R.id.find);
        edit = (EditText) findViewById(R.id.edit);
        path_history = (ListView) findViewById(R.id.path_history);
        text = (TextView) findViewById(R.id.text);
        list_paths = (SwipeListView) findViewById(R.id.list_paths);
        re2 = (RelativeLayout) findViewById(R.id.re2);
        lin_pop_input = (RelativeLayout) findViewById(R.id.lin_pop_input);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GPS_PATHLIST_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.DELETE_SEARCH_HISTORY, this);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.GPS_PATHLIST_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.DELETE_SEARCH_HISTORY, this);
        super.onDetachedFromWindow();
    }

    @Override
    public void initViews() {
        list_paths.setRightViewWidth(ODipToPx.dipToPx(getContext(),90));
        edit.getBackground().setAlpha(128);
        //打开软键盘
        KeyBoard.openKeyBoard(edit);
        //进来先判断有没有历史纪录有就显示没有就不显示
        listHistory = ManagerGps.getInstance().loadSearchHistory();
        if (listHistory == null || listHistory.size() == 0) {
            re2.setVisibility(View.INVISIBLE);
        } else {
            re2.setVisibility(View.VISIBLE);
        }
        headerView = LayoutInflater.from(context).inflate(R.layout.gps_path_history_header, null);
        delete = (ImageView) headerView.findViewById(R.id.delete);
        path_history.addHeaderView(headerView);
        historyAdapter = new AdapterForSearchHistory(listHistory, context);
        path_history.setAdapter(historyAdapter);
//        for (int i = 0; i < 150; i++) {
//            listSearchResult.add("草他爹" + i);
//        }
    }


    @Override
    public void initEvents() {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                re2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();
                if (searchText != null && searchText.length() != 0) {
                    isChange = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OCtrlGps.getInstance().ccmd1242_findPath(carId, 0, 20, searchText);
                        }
                    },1000L);
                } else {
                    re2.setVisibility(View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    list_paths.setVisibility(View.INVISIBLE);
                    dataChange();
                }
            }
        });
        cancle.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_app_gps_path_list);
            }
        });
        find.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                super.onClickNoFast(v);
            }
        });
        path_history.setOnItemClickListener(new OnItemClickListenerMy() {
            @Override
            public void onItemClickNofast(AdapterView<?> parent, View view, int position, long id) {
                super.onItemClickNofast(parent, view, position, id);
            }
        });
        delete.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                listHistory.removeAll(listHistory);
                if (historyAdapter != null) {
                    historyAdapter.notifyDataSetChanged();
                }
                path_history.removeHeaderView(headerView);
                ManagerGps.getInstance().deleteSearchHistoryAll();
            }
        });
//        list_paths.setOnLoadBottomListener(new ListViewPushRefresh.OnLoadBottomListener() {
//            @Override
//            public void onLoad() {
////                /**底部加载，要减去header&footer*/
//                int startpos = list_paths.getCount() - 2;
////                Log.i("setOnLoadBottomListener","list_states.getCount():"+list_paths.getCount());
////                if(isCheckingForTime){
////                    OCtrlCar.getInstance().ccmd1221_getWarninglist(timestart,timeend,selectPos,startpos,20,selectCarId);
////                }else{
////
////                }
////                OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,selectPos,startpos,20,selectCarId);
//                // 为了显示效果，采用延迟加载
//                isChange = false;
//                OCtrlGps.getInstance().ccmd1242_findPath(carId, startpos, 20, searchText);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list_paths.loadComplete();
//                    }
//                }, 2000);
//            }
//        });
        //头部加载0-19
//        list_paths.setonLoadHeaderListener(new ListViewPushRefresh.OnLoadHeaderListener() {
//            @Override
//            public void onLoad() {
//                /**头部加载，要清掉所有旧数据*/
//                listSearchResult.removeAll(listSearchResult);
////                ManagerWarnings.getInstance().DBClearDataAll();
////                if(isCheckingForTime){
////                    OCtrlCar.getInstance().ccmd1221_getWarninglist(timestart,timeend,selectPos,0,20,selectCarId);
////                }else{
////                    OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,selectPos,0,20,selectCarId);
////                }
//                // 为了显示效果，采用延迟加载
//                isChange = false;
//                OCtrlGps.getInstance().ccmd1242_findPath(carId, 0, 20, searchText);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list_paths.loadComplete();
//                    }
//                }, 2000);
//            }
//        });
    }

    // ===============================================
    @Override
    public void callback(String key, Object value) {

    }

    @Override
    public void receiveEvent(String key, Object value) {
        if (key.equals(OEventName.GPS_PATHLIST_RESULTBACK)) {
            int result = (Integer) value;
            if (result == 42) {
                handleChangeData();
            } else if (result == 1244) {
                resultAdapter.notifyChangeShouCangImg();
            } else if (result == 1224) {
                resultAdapter.notifyDeleteRecord();
            }else if (result == 1243) {
                resultAdapter.addComment(inputStr);
            }
        } else if (key.equals(OEventName.DELETE_SEARCH_HISTORY)) {
            dataChange();
        }

    }

    @Override
    public void invalidateUI() {
        setIsvisible();
    }

    /**
     * 请求失败找不到设置结果文字
     *
     * @param text
     */
    public void setFindResultText(TextView text) {
        String tipText = "找不到" + searchText + "匹配的内容";
        SpannableString s = new SpannableString(tipText);
        Pattern p = Pattern.compile(searchText);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#0179ff")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        text.setText(s);
    }

    public void setIsvisible() {
        List<DataGpsPath> list = ManagerGps.getInstance().singleCarPath;
        if (list == null || list.size() == 0) {
            if (isChange) {
                list_paths.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);
                setFindResultText(text);
            }
        } else {
            if (isChange) {
                ManagerGps.getInstance().saveSearchHistory(searchText);
            }
            list_paths.setVisibility(View.VISIBLE);
            text.setVisibility(View.INVISIBLE);
            if (list != null) {
//                int prePos = list_paths.getFirstVisibleItem();
                resultAdapter = new AdapterForSearchResult(context,ODipToPx.dipToPx(getContext(),90), list);
                list_paths.setAdapter(resultAdapter);
//                list_paths.setSelection(prePos);
                resultAdapter.setonClickBianJi(new AdapterForSearchResult.onClickBianJi() {
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

    private void dataChange() {
        Message message = Message.obtain();
        message.what = 110;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 110) {
                if (historyAdapter != null) {
                    listHistory = ManagerGps.getInstance().loadSearchHistory();
                    historyAdapter.changeUI(listHistory);
                    if (listHistory == null||listHistory.size()==0) {
                        path_history.removeHeaderView(headerView);
                    }else{
                        if(path_history.getHeaderViewsCount()==0){
                            path_history.addHeaderView(headerView);
                        }
                    }
                }
            }
        }
    };
}
