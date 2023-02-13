package common.global;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.static_interface.OCallBack;

import java.io.File;
import java.net.URISyntaxException;

public class OToastNavigate {
    private PopupWindow popContain;//弹出管理
    private View        parentView;//本对象显示
    private Context     context;

    private RelativeLayout thisView;
    private TextView       txt_title, txt_text;
    private TextView btn_cancel, btn_center, btn_confirm;
    private ImageView img_splitline,img_splitline1;
    private OCallBack callback;
    private LatLng    start, end;
    private        String         location;
    // ========================out======================
    private static OToastNavigate _instance;

    public static OToastNavigate getInstance() {
        if (_instance == null)
            _instance = new OToastNavigate();
        return _instance;
    }

    //===================================================
    public void showOpenNavigate(View parentView, final LatLng start, final LatLng end, final String location) {
        this.start = start;
        this.end = end;
        this.location = location;
        this.parentView = parentView;
        context = parentView.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (thisView == null) {
            thisView = (RelativeLayout) layoutInflater.inflate(R.layout.toast_navigate, null);
            txt_title = (TextView) thisView.findViewById(R.id.txt_title);
            txt_text = (TextView) thisView.findViewById(R.id.txt_text);

            btn_cancel = (TextView) thisView.findViewById(R.id.btn_cancel);
            btn_center = (TextView) thisView.findViewById(R.id.btn_center);
            btn_confirm = (TextView) thisView.findViewById(R.id.btn_confirm);

            img_splitline = (ImageView) thisView.findViewById(R.id.img_splitline);
            img_splitline1 = (ImageView) thisView.findViewById(R.id.img_splitline1);

            popContain = new PopupWindow(thisView);
            popContain.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popContain.setFocusable(true);
            popContain.setTouchable(true);
            popContain.setOutsideTouchable(false);
        }
        boolean baiduOK = false;
        boolean gaodeOK = false;
        if (isInstallByread("com.baidu.BaiduMap")) baiduOK = true;
        if (isInstallByread("com.autonavi.minimap")) gaodeOK = true;

        btn_center.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        img_splitline.setVisibility(View.GONE);
        img_splitline1.setVisibility(View.GONE);
        if (!baiduOK && !gaodeOK) {
            txt_text.setText(context.getResources().getString(R.string.you_do_not_install_any_navigation));
        } else {
            txt_text.setText(context.getResources().getString(R.string.click_open_the_navigation) + location);
            if (!baiduOK) {
                img_splitline1.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.VISIBLE);
            } else if (!gaodeOK) {
                img_splitline.setVisibility(View.VISIBLE);
                btn_center.setVisibility(View.VISIBLE);
            } else {
                img_splitline.setVisibility(View.VISIBLE);
                btn_center.setVisibility(View.VISIBLE);
                img_splitline1.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.VISIBLE);
            }
        }

        initEvents();
        initViews();
        popContain.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public void initViews() {
    }

    private void exitThis() {
        callback = null;
        popContain.dismiss();
    }

    public void initEvents() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) callback.callback("cancelConfirm", "");
                exitThis();
            }
        });
        btn_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = Intent.getIntent(NAVI.getBaiduNaviStr(end, location));
                    context.startActivity(intent);
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, context.getResources().getString(R.string.baidu_navigation_is_not_open_to_use));
                } catch (Exception e) {
                    Log.i("msg", e.toString());
                }
                exitThis();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = Intent.getIntentOld(NAVI.toAMAP(NAVI.BD09_to_gcj02(end), location));
                    context.startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, context.getResources().getString(R.string.scott_navigation_not_opened_to_use));
                }
                exitThis();
            }
        });
    }

}

