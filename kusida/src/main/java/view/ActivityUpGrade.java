package view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastTxt;

import common.GlobalContext;
import ctrl.OCtrlCommon;
import model.ManagerPublicData;
import view.view4me.set.ClipTitleMeSet;

public class ActivityUpGrade extends ActivityBase {
    public static String TITLE_NAME = "TITLE_NAME";
    public static String HTTP_ADDRESS = "HTTP_ADDRESS";
    private RelativeLayout linlin;
    private ClipTitleMeSet title_head;
    private WebView web_info;

    private String http_address_use;
    private boolean isComFromAd;//从广告页进来
    private FrameLayout mLayout;    // 用来显示视频的布局
    private View mCustomView;	//用于全屏渲染视频的View
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private TextView text;

    public ActivityUpGrade() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        title_head =  findViewById(R.id.title_head);
        web_info = (WebView) findViewById(R.id.web_info);
        linlin = (RelativeLayout) findViewById(R.id.linlin);
        Bundle bundle = this.getIntent().getExtras();
        String title_name = bundle.getString(TITLE_NAME);
        http_address_use = bundle.getString(HTTP_ADDRESS);
        mLayout =  findViewById(R.id.fl_video);
        text=  findViewById(R.id.text);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.UPGRADE_RESULT_BACK,this);
    }

    @Override
    protected void onPause() {
//        web_info.loadUrl("about:blank");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        web_info.loadUrl(http_address_use);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (web_info != null) {
            web_info.getSettings().setSupportZoom(false);
            web_info.getSettings().setBuiltInZoomControls(false);
            web_info.setVisibility(View.GONE);
            web_info.removeAllViews();
            web_info.stopLoading();
            web_info.setVisibility(View.GONE);
            linlin.removeView(web_info);
//            web_info.destroy();
            //清空所有cookie
            CookieSyncManager.createInstance(ActivityUpGrade.this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
            web_info.setWebChromeClient(null);
            web_info.setWebViewClient(null);
            web_info.getSettings().setJavaScriptEnabled(false);
            web_info.clearCache(true);
            web_info.destroy();
        }
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
    }

    protected void initViews() {
        OCtrlCommon.getInstance().cmmd_2402_qurryUpGrade();
        if (http_address_use != null) {
            handleChangeData();
        }
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        super.receiveEvent(eventName, paramObj);
        if(eventName.equals(OEventName.UPGRADE_RESULT_BACK)){
//            setTextEnable(paramObj);
        }
    }
    private void setTextEnable(Object isEnable){
        Message message=Message.obtain();
        message.obj=isEnable;
        message.what=11;
        handler.sendMessage(message);
    }
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==11){
                boolean isEnable= (boolean) msg.obj;
                if(isEnable){
                    text.setText("已预约");
                    text.setEnabled(false);
                    text.setBackgroundColor(getResources().getColor(R.color.gray));
                }else{
                    text.setText("现在预约");
                    text.setEnabled(true);
                    text.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }
        }
    };

    public void initEvents() {
        // back
        title_head.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                    ActivityUpGrade.this.finish();
            }
        });
        text.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
//                        .withInfo("预约登记后，我们将会安排官方客服与您联系，请保持电话畅通！")
//                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                            @Override
//                            public void onClickConfirm(boolean isClickConfirm) {
//                                if (isClickConfirm) {
//                                    OCtrlCommon.getInstance().cmmd_2401_upGrade();
//                                }
//                            }
//                        }).show();
                if(checkPackage("com.taobao.taobao")){
                    toTaoBao();
                }else{
                    if(!TextUtils.isEmpty(ManagerPublicData.taobaoUrl)){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(ActivityWeb.TITLE_NAME, "现在预约");
//                        String address="https://item.taobao.com/item.htm?spm=a2126o.success.result.1.3f2d4831Rwr4tV&id=668997705760";
                        bundle.putString(ActivityWeb.HTTP_ADDRESS, ManagerPublicData.taobaoUrl);
                        intent.putExtras(bundle);
                        intent.setClass(GlobalContext.getContext(), ActivityWeb.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        GlobalContext.getContext().startActivity(intent);
                    }else{
                        OCtrlCommon.getInstance().cmmd_2303_qurryTaoBaoInfo();
                    }
                }
            }
        });
        web_info.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    public static void jumpAlipay(Context context) {
        try {
            String uri = "https://item.taobao.com/item.htm?spm=a2126o.success.result.1.3f2d4831Rwr4tV&id=668997705760";//启动参数
            Intent intent = Intent.parseUri(uri, Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            new ToastTxt(GlobalContext.getCurrentActivity(),null,false).withInfo("支付宝跳转失败").quicklyShow();
            e.printStackTrace();
        }
    }
    /**
     * 跳转详情界面
     *
     *
     */
    public static void toTaoBao( ){
        if(!TextUtils.isEmpty(ManagerPublicData.taobaoUrl)){
//            String url = "https://item.taobao.com/item.htm?spm=a2126o.success.result.1.3f2d4831Rwr4tV&id=668997705760";
            Intent intent = new Intent();
            intent.setAction("Android.intent.action.VIEW");
            Uri uri = Uri.parse(ManagerPublicData.taobaoUrl); // 商品地址
            intent.setData(uri);
            intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中调用startactivity方法必须添加标签
            GlobalContext.getContext().startActivity(intent);
        }else{
            OCtrlCommon.getInstance().cmmd_2303_qurryTaoBaoInfo();
        }
    }

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("com.taobao.taobao");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    public boolean checkPackage(String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try
        {
            this.getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    @Override
    public void invalidateUI() {
//        web_info.setWebViewClient(new MyWebViewClient());
//        web_info.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//取消硬件加速
//        WebSettings webSettings = web_info.getSettings();
////设置WebView属性，能够执行Javascript脚本
//        webSettings.setJavaScriptEnabled(true);
////设置可以访问文件
//        webSettings.setAllowFileAccess(true);
////设置支持缩放
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setSupportZoom(false);
//        webSettings.setBuiltInZoomControls(false);
////加载需要显示的网页
//        webSettings.setBlockNetworkImage(false);
//        webSettings.setBlockNetworkLoads(false);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        web_info.loadUrl(http_address_use);

        //===============
        // 设置编码
//        web_info.getSettings().setDefaultTextEncodingName("utf-8");
//        //web_info.getSettings().setTextZoom(70);
//        // 设置背景颜色 透明
//        //web_info.setBackgroundColor(Color.argb(0, 0, 0, 0));
//        // 设置可以支持缩放
//        web_info.getSettings().setSupportZoom(true);
//        // 设置缓存模式
//        web_info.getSettings().setAppCacheEnabled(true);
//        web_info.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        // //添加Javascript调用java对象
//        web_info.getSettings().setJavaScriptEnabled(true);
//        // 设置出现缩放工具
//        web_info.getSettings().setBuiltInZoomControls(true);
//        web_info.getSettings().setDisplayZoomControls(false);
//        // 扩大比例的缩放设置此属性，可任意比例缩放。
//        web_info.getSettings().setLoadWithOverviewMode(true);
//        web_info.getSettings().setBlockNetworkImage(false);
//        // 启用硬件加速
//        web_info.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        web_info.setWebChromeClient(new WebChromeClient());

        // 自适应屏幕
//        web_info.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        web_info.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        web_info.loadUrl(http_address_use);

        WebSettings webSettings = web_info.getSettings();
        webSettings.setBlockNetworkImage(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setPluginState(WebSettings.PluginState.ON);
//支持js脚本
        webSettings.setJavaScriptEnabled(true);
//支持缩放
        webSettings.setSupportZoom(true);
//支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//多窗口
        webSettings.supportMultipleWindows();
//当webview调用requestFocus时为webview设置节点
        webSettings.setNeedInitialFocus(true);
//设置支持缩放
        webSettings.setBuiltInZoomControls(true);
//支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
//优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
// 开启H5(APPCache)缓存功能
        webSettings.setAppCacheEnabled(true);
// 开启 DOM storage 功能
        webSettings.setDomStorageEnabled(true);
// 应用可以有数据库
        webSettings.setDatabaseEnabled(true);
// 可以读取文件缓存(manifest生效)
        webSettings.setAllowFileAccess(true);
        /*允许跨域访问*/
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        web_info.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        web_info.canGoBack();
        web_info.setWebViewClient(new WebViewClient());
        web_info.setWebChromeClient(new MyWebViewClient());
        web_info.loadUrl(http_address_use);
    }

    @Override
    protected void popView(int resId) {

    }
//    @Override
//    public void onConfigurationChanged(Configuration config) {
//        super.onConfigurationChanged(config);
//        switch (config.orientation) {
//            case Configuration.ORIENTATION_LANDSCAPE:
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                break;
//            case Configuration.ORIENTATION_PORTRAIT:
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                break;
//        }
//    }


    class MyWebViewClient extends WebChromeClient {

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
////            if(url.startsWith("http")||url.startsWith("https")){
////                view.loadUrl(url);
////                return false;
////            }
////            Intent intent= null;
////            try {
////                intent = Intent.parseUri(url,Intent.URI_INTENT_SCHEME);
////            } catch (URISyntaxException e) {
////                e.printStackTrace();
////            }
//////            intent.setData(Uri.parse(url));
////            startActivity(intent);
//            WebView.HitTestResult hit = view.getHitTestResult();
//            // ------  对相关的scheme处理 -------
//            if (!url.startsWith("http:") && !url.startsWith("https:")) {//对不是网页开头的当成scheme
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));//启动app操作
//                } catch (Exception e) {
////                    Log.i(TAG, "未安装:" + url);
//                    new ToastTxt(ActivityUpGrade.this, null).withInfo("未安装此app,请前往应用市场下载此app").show();
//                    return false;
//                }
//                return true;
//            }
//
//            int hitType = hit.getType();
//
//            if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE) {//点击超链接
//
//                //访问链接
//                view.loadUrl(url);
//                return true;//返回true浏览器不再执行默认的操作
//            } else if (hitType == 0) {//重定向时hitType为0
//                return false;//不捕获302重定向
//            } else {
//                return false;
//            }
//        }

        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            //如果view 已经存在，则隐藏
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomView = view;
            mCustomView.setVisibility(View.VISIBLE);
            mCustomViewCallback = callback;
            mLayout.addView(mCustomView);
            mLayout.setVisibility(View.VISIBLE);
            mLayout.bringToFront();

            //设置横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mLayout.removeView(mCustomView);
            mCustomView = null;
            mLayout.setVisibility(View.GONE);
            try {
                mCustomViewCallback.onCustomViewHidden();
            } catch (Exception e) {
            }
//                titleView.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }

        @Nullable
        @Override
        public Bitmap getDefaultVideoPoster() {
//            return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
            return BitmapFactory.decodeResource(getResources(),R.drawable.img_upgrade );
        }
    }
}
