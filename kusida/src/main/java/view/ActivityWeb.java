package view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.client.proj.kusida.R;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastTxt;

import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.view4me.set.ClipTitleMeSet;

/**
 * Intent intent = new Intent();
 * Bundle bundle = new Bundle();
 * bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
 * String address;
 * try {
 * ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
 * address = "http://api.91kulala.com/kulala/protocol/protocol.jsp?cid="+appInfo.metaData.getInt("cid");
 * } catch (PackageManager.NameNotFoundException e) {
 * address = "http://api.91kulala.com/kulala/protocol/protocol.jsp?cid="+0;
 * }
 * bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
 * intent.putExtras(bundle);
 * intent.setClass(getContext(), ActivityWeb.class);
 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 * getContext().startActivity(intent);
 * <p>
 * Bundle bundle = this.getIntent().getExtras();
 * String str=bundle.getString("USERNAME");
 */
public class ActivityWeb extends ActivityBase {
    public static String TITLE_NAME = "TITLE_NAME";
    public static String HTTP_ADDRESS = "HTTP_ADDRESS";
    private RelativeLayout linlin;
    private ClipTitleMeSet title_head;
    private WebView web_info;

    private String http_address_use;
    private boolean isComFromAd;//从广告页进来

    public ActivityWeb() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        title_head = (ClipTitleMeSet) findViewById(R.id.title_head);
        web_info = (WebView) findViewById(R.id.web_info);
        linlin = (RelativeLayout) findViewById(R.id.linlin);

        Bundle bundle = this.getIntent().getExtras();
        String title_name = bundle.getString(TITLE_NAME);
        http_address_use = bundle.getString(HTTP_ADDRESS);
        title_head.setTitle(title_name);
        initViews();
        initEvents();
    }

    @Override
    protected void onPause() {
        web_info.loadUrl("about:blank");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (web_info != null) {
            web_info.getSettings().setSupportZoom(false);
            web_info.getSettings().setBuiltInZoomControls(false);
            web_info.setVisibility(View.GONE);
            web_info.removeAllViews();
            web_info.stopLoading();
            web_info.setVisibility(View.GONE);
            linlin.removeView(web_info);
            web_info.destroy();
        }
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.onDestroy();
    }

    protected void initViews() {
        if (http_address_use != null) {
            handleChangeData();
        }
    }

    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (ActivityAdvertising.isActivityAdvertisingCome) {
                    Intent intent = new Intent();
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                    if (user == null || user.userId == 0) {
                        intent.setClass(ActivityWeb.this, ActivityLogin.class);//未登录
                    } else {
                        ActivityKulalaMain.GestureNeed = true;
                        intent.setClass(ActivityWeb.this, ActivityKulalaMain.class);//已登录
                    }
                    startActivity(intent);
                }
                ActivityKulalaMain.GestureNeed = false;
                ActivityWeb.this.finish();
                ActivityAdvertising.isActivityAdvertisingCome = false;
            }
        });
    }

    @Override
    public void invalidateUI() {
        web_info.setWebViewClient(new MyWebViewClient());
        web_info.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//取消硬件加速
        WebSettings webSettings = web_info.getSettings();
//设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
//设置可以访问文件
        webSettings.setAllowFileAccess(true);
//设置支持缩放
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
//加载需要显示的网页
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        web_info.loadUrl(http_address_use);
    }

    @Override
    protected void popView(int resId) {

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if(url.startsWith("http")||url.startsWith("https")){
//                view.loadUrl(url);
//                return false;
//            }
//            Intent intent= null;
//            try {
//                intent = Intent.parseUri(url,Intent.URI_INTENT_SCHEME);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
////            intent.setData(Uri.parse(url));
//            startActivity(intent);
            WebView.HitTestResult hit = view.getHitTestResult();
            // ------  对相关的scheme处理 -------
            if (!url.startsWith("http:") && !url.startsWith("https:")) {//对不是网页开头的当成scheme
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));//启动app操作
                } catch (Exception e) {
//                    Log.i(TAG, "未安装:" + url);
                    new ToastTxt(ActivityWeb.this, null).withInfo("未安装此app,请前往应用市场下载此app").show();
                    return false;
                }
                return true;
            }

            int hitType = hit.getType();

            if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE) {//点击超链接

                //访问链接
                view.loadUrl(url);
                return true;//返回true浏览器不再执行默认的操作
            } else if (hitType == 0) {//重定向时hitType为0
                return false;//不捕获302重定向
            } else {
                return false;
            }
        }
    }
}