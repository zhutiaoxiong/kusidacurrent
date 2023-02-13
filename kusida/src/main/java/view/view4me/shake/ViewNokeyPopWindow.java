package view.view4me.shake;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.RelativeLayoutBase;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import common.global.TextViewEC;

public class ViewNokeyPopWindow extends RelativeLayoutBase {
    private TextView text_type;
    private TextView text_info;
    private ImageView delete;
    private ImageView add;
    private TextViewEC txt_elc_close;
    private Button set;
    private int mType;
    private TextView text_info_open;
    private int defaultOpenData;
    private int defaultCloseData;
    private int defaultChazhi;
    private int defaultTouchInRssi;
    public ViewNokeyPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_me_nokey_pop_window, this, true);
        text_type=findViewById(R.id.text_type);
        text_info=findViewById(R.id.text_info);
        delete=findViewById(R.id.delete);
        add=findViewById(R.id.add);
        txt_elc_close=findViewById(R.id.txt_elc_close);
        set=findViewById(R.id.set);
        text_info_open=findViewById(R.id.text_info_open);
        initViews();
        initEvents();
    }
    @Override
    protected void initViews() {

    }
    /**
     * 1 靠近开 2离开锁
     * */
    public void setType(int type,int openData,int closeData){
        Log.e("查看设置的数据", "  打开后设置的值cacheNokeyInfo.openData"+openData+"---"+closeData);
        mType=type;
        defaultOpenData=openData;
        defaultCloseData=closeData;
        if(mType==1){
            text_type.setText("靠近开距离设置");
            text_info.setText("");
            text_info_open.setVisibility(View.VISIBLE);
            text_info_open.setText("提示：建议测试靠近开距离的时候将手机放口袋中，确保手机放口袋靠近车辆时能及时开锁。");
//            txt_elc_close.setText(openData+"");
            setOpenText(openData);
            setLevelNotNeedUI(defaultOpenData,defaultCloseData);
        }else if(mType==2){
            text_type.setText("离开锁灵敏度设置");
            text_info.setText("灵敏度越高锁车越快，但会增加跳锁概率，请根据使用习惯设置合适灵敏度。");
            text_info_open.setVisibility(View.INVISIBLE);
            setLevel(defaultOpenData,defaultCloseData);
        }else if(mType==3){
            text_type.setText("触摸有效距离设置");
            text_info.setText("");
            text_info_open.setVisibility(View.VISIBLE);
            text_info_open.setText("提示：因为手机放口袋信号最弱，建议把手机放口袋中后测试触摸有效距离，确保靠近车辆后能稳定的通过触摸开锁或关锁。");
            setTouchInUI(defaultOpenData);
        }
    }
    public void setType(int type,int touchinRssi){
        Log.e("查看设置的数据", "  打开后设置的值cacheNokeyInfo.openData"+touchinRssi);
        mType=type;
        defaultTouchInRssi=touchinRssi;
        if(mType==3){
            text_type.setText("触摸有效距离设置");
            text_info.setText("");
            text_info_open.setVisibility(View.VISIBLE);
            text_info_open.setText("提示：因为手机放口袋信号最弱，建议把手机放口袋中后测试触摸有效距离，确保靠近车辆后能稳定的通过触摸开锁或关锁。");
            setTouchInUI(defaultTouchInRssi);
        }
    }

    private void setTouchInUI(int touinRssi){
        String openStr="";
        switch (touinRssi){
            case 65:
                openStr="最近";
                break;
            case 68:
                openStr="近";
                break;
            case 71:
                openStr="稍近";
                break;
            case 74:
                openStr="适中";
                break;
            case 77:
                openStr="稍远";
                break;
            case 80:
                openStr="远";
                break;
            case 83:
                openStr="最远";
                break;
        }
        txt_elc_close.setText(openStr);
    }
    private void setOpenText(int openValue){
        String openStr="";
        switch (openValue){
                case 65:
                    openStr="最近";
                    break;
                case 68:
                    openStr="近";
                    break;
                case 71:
                    openStr="稍近";
                    break;
                case 74:
                    openStr="适中";
                    break;
                case 77:
                    openStr="稍远";
                    break;
                case 80:
                    openStr="远";
                    break;
                case 83:
                    openStr="最远";
                    break;
        }
        txt_elc_close.setText(openStr);
    }
    private void setLevel(int openData,int closeData){
        int chazhi=closeData-openData;
        if(chazhi==2){
            defaultChazhi=2;
            txt_elc_close.setText("最高");
        }else if(chazhi==4){
            defaultChazhi=4;
            txt_elc_close.setText("高");
        }else if(chazhi==6){
            defaultChazhi=6;
            txt_elc_close.setText("中");
        }else if(chazhi==8){
            defaultChazhi=8;
            txt_elc_close.setText("低");
        }else if(chazhi==10){
            defaultChazhi=10;
            txt_elc_close.setText("最低");
        }else{
            defaultChazhi=6;
            txt_elc_close.setText("中");
        }
        defaultCloseData=defaultOpenData+defaultChazhi;
    }

    private void setLevelNotNeedUI(int openData,int closeData){
        int chazhi=closeData-openData;
        if(chazhi==2){
            defaultChazhi=2;
        }else if(chazhi==4){
            defaultChazhi=4;
        }else if(chazhi==6){
            defaultChazhi=6;
        }else if(chazhi==8){
            defaultChazhi=8;
        }else if(chazhi==10){
            defaultChazhi=10;
        }else{
            defaultChazhi=6;
        }
        defaultCloseData=defaultOpenData+defaultChazhi;
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        super.receiveEvent(eventName, paramObj);
    }
    @Override
    protected void initEvents() {
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mType==3){
                    ODispatcher.dispatchEvent(OEventName.TOUCH_IN_SET_INFO,defaultTouchInRssi);
                }else{
                    CacheNokeyInfo cacheNokeyInfo=new CacheNokeyInfo();
                    cacheNokeyInfo.openData=defaultOpenData;
                    cacheNokeyInfo.closeData=defaultOpenData+defaultChazhi;
                    Log.e("查看设置的数据", "  cacheNokeyInfo.openData"+cacheNokeyInfo.openData+cacheNokeyInfo.closeData);
                    ODispatcher.dispatchEvent(OEventName.NOKEY_SET_INFO,cacheNokeyInfo);
                }
            }
        });
    }
    private void add(){
        if(mType==1){
           if(defaultOpenData==83){
               new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("已达到最大距离").quicklyShow();
                return;
           }
            defaultOpenData=defaultOpenData+3;
            setOpenText(defaultOpenData);
//            txt_elc_close.setText(defaultOpenData+"");

        }else if(mType==2){
            setCLevelAdd();
        }else if(mType==3){
            if(defaultTouchInRssi==83){
                new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("已达到最大距离").quicklyShow();
                return;
            }
            defaultTouchInRssi=defaultTouchInRssi+3;
            setOpenText(defaultTouchInRssi);
        }
    }
    private void setCLevelAdd(){
        if(defaultChazhi==2){
            new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("已经是最高灵敏度").quicklyShow();
            return;
        }
        defaultChazhi=defaultChazhi-2;
        setTextLevel();
    }

    private void delete(){
        if(mType==1){
            if(defaultOpenData==65){
                new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("已达到最小距离").quicklyShow();
                return;
            }
            defaultOpenData=defaultOpenData-3;
            setOpenText(defaultOpenData);
//            txt_elc_close.setText(defaultOpenData+"");
        }else if(mType==2){
            setCLevelDelete();
        }else if(mType==3){
            if(defaultTouchInRssi==65){
                new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("已达到最小距离").quicklyShow();
                return;
            }
            defaultTouchInRssi=defaultTouchInRssi-3;
            setOpenText(defaultTouchInRssi);
        }
    }
    private void setCLevelDelete(){
        if(defaultChazhi==10){
            new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("已经是最低灵敏度").quicklyShow();
            return;
        }
        defaultChazhi=defaultChazhi+2;
        setTextLevel();
    }
    private void setTextLevel(){
        switch(defaultChazhi){
            case 2:
                txt_elc_close.setText("最高");
                break;
            case 4:
                txt_elc_close.setText("高");
                break;
            case 6:
                txt_elc_close.setText("中");
                break;
            case 8:
                txt_elc_close.setText("低");
                break;
            case 10:
                txt_elc_close.setText("最低");
                break;
        }
    }

    @Override
    protected void invalidateUI() {

    }
}
