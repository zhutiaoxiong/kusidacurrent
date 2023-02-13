package view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.timepicker.TimePickerView;
import com.google.gson.JsonObject;
import com.kulala.staticsview.ActivityBase;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import common.GlobalContext;
import common.linkbg.BootBroadcastReceiver;
import common.pinyinzhuanhuan.CircleImg;
import common.pinyinzhuanhuan.FileUtil;
import ctrl.OCtrlCommon;
import ctrl.OCtrlRegLogin;
import model.ManagerLoginReg;
import model.loginreg.DataUser;
import view.clip.ClipLineBtnInptxt;

import view.view4me.set.ClipTitleMeSet;
import view.view4me.userinfo.ViewUserInfoAddress;
import view.view4me.userinfo.ViewUserInfoAddressCity;
import view.view4me.userinfo.ViewUserInfoNickname;
import view.view4me.userinfo.ViewUserInfoScore;
import view.view4me.userinfo.ViewUserInfoScoreDetail;
import view.view4me.userinfo.ViewUserInfoScoreHelp;
import view.view4me.userinfo.ViewUserInfoSex;
import view.view4me.userinfo.ViewUserInfoSign;
//import view.view4me.userinfo.ViewUserInfo;

/**
 * Created by qq522414074 on 2016/8/3.
 */
public class ViewUserInfoActivity extends ActivityBase {
    public static int PRE_NEED_POPVIEW_ID = -1;
    private RelativeLayout lin_child;
    private ClipTitleMeSet titleHead;
    private CircleImg      img_face;
    private ImageView      img_vip, img_mark;
    private ClipLineBtnInptxt txt_nickname, txt_phone, txt_score, txt_birthday, txt_sex, txt_address, txt_sign;
    //    private static final String IMAGE_FILE_NAME     = "avatarImage.png";// 头像文件名称
    private static final int    REQUESTCODE_TAKE    = 1;        // 相机拍照标记
    private static final int    REQUESTCODE_PICK    = 0;        // 相册选图标记
    private static final int    REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private String    urlpath;            // 图片本地缓存路径
    private Context   mContext;
    private CircleImg avatarImg;// 头像图片
    private String    prefix; //头像地址前缀
    private String    path;//传递到裁剪页面的路径


    private long chooseDate;//选择的日期

    private TimePickerView pvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_me_userinfo);
        lin_child = (RelativeLayout) findViewById(R.id.lin_child);
        titleHead = (ClipTitleMeSet) findViewById(R.id.title_head);
        img_face = (CircleImg) findViewById(R.id.img_face);
        img_vip = (ImageView) findViewById(R.id.img_vip);
        img_mark = (ImageView) findViewById(R.id.img_mark);
        txt_nickname = (ClipLineBtnInptxt) findViewById(R.id.txt_nickname);
        txt_phone = (ClipLineBtnInptxt) findViewById(R.id.txt_phone);
        txt_score = (ClipLineBtnInptxt) findViewById(R.id.txt_score);
        txt_birthday = (ClipLineBtnInptxt) findViewById(R.id.txt_birthday);
        txt_sex = (ClipLineBtnInptxt) findViewById(R.id.txt_sex);
        txt_address = (ClipLineBtnInptxt) findViewById(R.id.txt_address);
        txt_sign = (ClipLineBtnInptxt) findViewById(R.id.txt_sign);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.SCORE_JUMPPAGE, this);
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
        ODispatcher.addEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.ACTIVITY_USERINFO_GOTOVIEW, this);
        //其它位置弹出view
        if(PRE_NEED_POPVIEW_ID>=0){
            handlePopView(PRE_NEED_POPVIEW_ID);
            PRE_NEED_POPVIEW_ID = -1;
        }
    }


    protected void initViews() {
        handleChangeData();
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
        OCtrlRegLogin.getInstance().ccmd1126_getUserInfo();
//        OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR)+0);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
    }

    @Override
    protected void initEvents() {
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date datee, String mark) {
                if (mark.equals("chooseDate")) {
                    chooseDate = datee.getTime();
                    String date = ODateTime.time2StringOnlyDate(chooseDate);
                    txt_birthday.setText(date);
                    DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
                    user.birthday = chooseDate;
                    OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
                }
            }
        });
        titleHead.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                ActivityKulalaMain.GestureNeed = false;
                ViewUserInfoActivity.this.finish();
            }
        });
        txt_nickname.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                handlePopView(R.layout.view_me_userinfo_nickname);
            }
        });
        img_face.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                OToastButton.getInstance().show(img_face, new String[]{getResources().getString(R.string.taking_pictures), getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums)}, "changeHead", ViewUserInfoActivity.this);
            }
        });
        txt_score.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                handlePopView(R.layout.view_me_userinfo_score);
            }
        });
        txt_sex.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View view) {
                handlePopView(R.layout.view_me_userinfo_sex);
            }
        });
        txt_birthday.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                pvTime.show();
                pvTime.setMark("chooseDate");
                super.onClickNoFast(v);
            }
        });
        txt_sign.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlePopView(R.layout.view_me_userinfo_sign);
            }
        });
        txt_address.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                handlePopView(R.layout.view_me_userinfo_address);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        BootBroadcastReceiver.sendMessage(this, 9100, "");//9100 UI open
    }

    @Override
    public void callback(String s, Object o) {
        if (s.equals("selectBoy")) {
            txt_sex.setText(getResources().getString(R.string.men));
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
            user.sex = 1;
            OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
        } else if (s.equals("selectGirl")) {
            txt_sex.setText(getResources().getString(R.string.women));
            DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
            user.sex = 2;
            OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
        }else if (s.equals("changeHead")) {
            String select = (String) o;
            if (select.equals(getResources().getString(R.string.taking_pictures))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCamera = checkSelfPermission(Manifest.permission.CAMERA);

                    //拍照权限
                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        File fullPath = new File(DataUser.getHeadCacheDir(), DataUser.IMAGE_FILE_NAME);
//                        fullPath.mkdirs();
                        Uri uu = Uri.fromFile(fullPath);
                        if (Build.VERSION.SDK_INT<24){
                            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uu);
                            startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                        }else {
                            Uri contentUri = FileProvider.getUriForFile(this.mContext, getPackageName(), fullPath);
                            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                        }
                    }
                } else {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DataUser.getHeadCacheDir(), DataUser.IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                }
            } else if (select.equals(getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //拍照权限
                    if ( permissionRead != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                pickIntent.setType("image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                }else{
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (BuildConfig.DEBUG) Log.e("loadPermission", "onRequestPermissionsResult:" +requestCode+ "  " + permissions.toString());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //loadPermissions  LoadPermissions.getInstance()
        boolean isCaptureGranted=true;
        boolean isGalarryGranted1=true;
        if(requestCode==1){
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i]==-1){
                    isCaptureGranted=false;
                }
            }
            if(!isCaptureGranted){
                jumpSettingPage(1);
            }
        }else if(requestCode==2){
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i]==-1){
                    isGalarryGranted1=false;
                }
            }
            if(!isGalarryGranted1){
                jumpSettingPage(2);
            }
        }
    }


    private void jumpSettingPage(int isFromWhere){
        if(isFromWhere==1){
            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                    .withTitle("提示")
                    .withInfo("使用相机需要用到相机权限和存储权限，使用相册需要使用存储权限，请前往设置")
                    .withButton("取消","去设置")
                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                        @Override
                        public void onClickConfirm(boolean isClickConfirm) {
                            if (isClickConfirm) {
                                toSelfSetting( GlobalContext.getCurrentActivity());
                            }
                        }
                    }).show();
        }else{
            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                    .withTitle("提示")
                    .withInfo("使用相册需要使用存储权限，请前往设置")
                    .withButton("取消","去设置")
                    .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                        @Override
                        public void onClickConfirm(boolean isClickConfirm) {
                            if (isClickConfirm) {
                                toSelfSetting( GlobalContext.getCurrentActivity());
                            }
                        }
                    }).show();
        }
    }
    /**
     * 进入系统设置界面
     */
    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(mIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (BuildConfig.DEBUG) Log.e("ViewHead", "onActivityResult" + " requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri = null;
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                if (data == null) {
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "您的机型不支持");
                    return;
                }
                //            try {
////                    Intent intent =new Intent(ViewUserInfoActivity.this,CutPicActivity.class);
//                    startPhotoZoom(data.getData());
//                } catch (NullPointerException e) {
//                    e.printStackTrace();//
//                }]
                ContentResolver resolver = getContentResolver();
                Bitmap bitmap = null;
                try {
                    uri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor =  getContentResolver().query(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path1 = cursor.getString(column_index);
                 if (BuildConfig.DEBUG) Log.e("path5", "path1" + path1);
                Intent cutPicIntentTwo = new Intent(ViewUserInfoActivity.this, ClipCutPicActivity.class);
                cutPicIntentTwo.putExtra("path", path1);
                cutPicIntentTwo.putExtra("from", "2");
                startActivityForResult(cutPicIntentTwo, REQUESTCODE_CUTTING);
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
//                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
//                startPhotoZoom(Uri.fromFile(temp));
//                uri = Uri.fromFile(new File(path));
                path = DataUser.getHeadCacheDir() + "/" + DataUser.IMAGE_FILE_NAME;
                Intent cutPicIntentOne = new Intent(ViewUserInfoActivity.this, ClipCutPicActivity.class);
                cutPicIntentOne.putExtra("path", path);
                cutPicIntentOne.putExtra("from", "1");
                startActivityForResult(cutPicIntentOne, REQUESTCODE_CUTTING);
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    urlpath = data.getStringExtra("path");
                     if (BuildConfig.DEBUG) Log.e("urlpath3", "urlpath" + urlpath);
                    OCtrlCommon.getInstance().cmmd1312_uplloadPic();
//                    setPicToView(data);
//                  ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_userinfo);
                }
                break;
        }
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
//          Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.png", photo);
//          avatarImg.setImageDrawable(drawable);
            //在服务器端请求网址前缀
            OCtrlCommon.getInstance().cmmd1312_uplloadPic();
        }
    }


    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.CHANGE_USER_INFO_OK)) {
            handleChangeData();
        } else if (s.equals(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK)) {
            String token = OJsonGet.getString((JsonObject) o, "uptoken");
            prefix = OJsonGet.getString((JsonObject) o, "prefix");
            //上传到千牛然后上传到公司服务器
            uploadPicToQianNiuAndLocalClint(token);
        } else if (s.equals(OEventName.ACTIVITY_USERINFO_GOTOVIEW)) {
            int id = (Integer)o;
            handlePopView(id);
        } else if (s.equals(OEventName.SCORE_JUMPPAGE)) {
            int id = (Integer)o;
            gotoJumpPage(id);
        }
        super.receiveEvent(s, o);
    }


    private void uploadPicToQianNiuAndLocalClint(String token) {
        Configuration config = new Configuration.Builder()
                .connectTimeout(90)              // 链接超时。默认90秒
                .useHttps(true)                  // 是否使用https上传域名
                .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
                .concurrentTaskCount(3)          // 并发上传线程数量为3
                .responseTimeout(90)             // 服务器响应超时。默认90秒
                .recorder(null)              // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, null)      // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone2)
                .build();
        // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用I
//        Configuration config = new Configuration.Builder()
//                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
//                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
//                .connectTimeout(10) // 链接超时。默认10秒
//                .responseTimeout(60) // 服务器响应超时。默认60秒
//                .recorder(null)  // recorder分片上传时，已上传片记录器。默认null
//                .recorder(null, null)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
//                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
        String        key           = "pic/avatar/" + userId + "/" + System.currentTimeMillis() + ".png";
        //先使用1312接口请求拿token再上传千牛，在更新用户信息。

        uploadManager.put(urlpath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                System.out.print(info.isOK());
                if (info != null) {
                    if (info.isOK()) {
                         if (BuildConfig.DEBUG) Log.e("七牛上傳", "成功 " );
                        DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
                        user.avatarUrl = prefix + key;
                        OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
                    }else{
                         if (BuildConfig.DEBUG) Log.e("七牛上傳", "失敗 " );
                    }
                }
            }
        }, null);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == 1) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    img_face.setImageBitmap(bitmap);
                }
            }
        }
    };

    public void invalidateUI() {
        final DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null) return;
        //name
        if (user.name.equals("")) {
            txt_nickname.setText(getResources().getString(R.string.nickname));
            txt_nickname.txt_input.setTextColor(getResources().getColor(R.color.gray_text));
        } else {
            txt_nickname.setText(user.name);
            txt_nickname.txt_input.setTextColor(getResources().getColor(R.color.black));
        }
        //phone
        if (user.phoneNum.equals("")) {
            txt_phone.setText(getResources().getString(R.string.is_not_set));
        } else {
            txt_phone.setText(user.phoneNum);
        }
        //score
        txt_score.setText("" + user.score);
        //birthday
        if (user.birthday == 0) {
            txt_birthday.setText(getResources().getString(R.string.not_to_choose));
        } else {
            txt_birthday.setText(ODateTime.time2StringOnlyDate(user.birthday));
        }
        //sex
        if (user.sex == 0) {
            txt_sex.setText(getResources().getString(R.string.not_to_choose));
        } else if (user.sex == 1) {
            txt_sex.setText(getResources().getString(R.string.men));
        } else if (user.sex == 2) {
            txt_sex.setText(getResources().getString(R.string.women));
        }
        //address
        if(TextUtils.isEmpty(user.location)){
            txt_address.setText(getResources().getString(R.string.not_to_choose));
        }else{
            txt_address.setText(user.location);
        }
        if(TextUtils.isEmpty(user.comment)){
            txt_sign.setText(getResources().getString(R.string.did_not_fill_in));
        }else{
            txt_sign.setText(user.comment);
        }
        //head
        if(user.avatarUrl != null && user.avatarUrl.length() > 0){
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.head_no).placeholder(R.drawable.head_no);
            Glide.with(GlobalContext.getContext())
                    .load(user.avatarUrl)
                    .apply(options)
                    .into(img_face);
        }else{
            img_face.setImageResource(R.drawable.head_no);
        }
//        if (user.avatarUrl.length() <= 1 || user.avatarUrl == null) {
//            img_face.setImageResource(R.drawable.head_no);
//        } else {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Bitmap  bitmap  = DataUser.getBitmap(user.avatarUrl);
//                        Message message = Message.obtain();
//                        message.what = 1;
//                        message.obj = bitmap;
//                        handler.sendMessage(message);
//                    }catch (UnknownHostException e){
//                        img_face.setImageResource(R.drawable.head_no);
//                    } catch (IOException e) {
//                        img_face.setImageResource(R.drawable.head_no);
//                    }
//                }
//            }).start();
//        }
        //      img_face.setImageResource(DataUser.getUserHeadResId(user.avatarUrl));
    }

    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.CHANGE_USER_INFO_OK, this);
//        ODispatcher.removeEventListener(OEventName.ACTIVITY_KULALA_GOTOVIEW,this);
        ODispatcher.removeEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, this);
        super.onDestroy();
    }
    //    public static void getPhotoURLByAlbum(Context context, Intent data, PhotoCallBack callback) {
//        if (data == null) {
//            callback.onFailure();
//            return;
//        }
//        final Uri selectedImage = data.getData();
//        if (selectedImage == null) {
//            callback.onFailure();
//            return;
//        }
//        String picturePath = "";   // 关于Android4.4的图片路径获取，如果回来的Uri的格式有两种
//        if (Build.VERSION.SDK_INT >= 19
//                && DocumentsContract.isDocumentUri(context, selectedImage)) {
//
//            String wholeID = DocumentsContract.getDocumentId(selectedImage);
//            String id = wholeID.split(":")[1];
//            String[] column = {MediaStore.Images.Media.DATA};
//            String sel = MediaStore.Images.Media._ID + "=?";
//            Cursor cursor = context.getContentResolver().query(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
//                    new String[]{id}, null);
//            if (cursor.moveToNext()) {
//                int columnIndex = cursor.getColumnIndex(column[0]);
//                picturePath = cursor.getString(columnIndex);
//                callback.onSuccess(picturePath);// 获取图片路径
//            }
//            cursor.close();
//        } else {
//            String[] projection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = context.getContentResolver().query(selectedImage,
//                    projection, null, null, null);
//            if (cursor.moveToNext()) {
//                int column_index = cursor
//                        .getColumnIndex(MediaStore.Images.Media.DATA);
//                picturePath = cursor.getString(column_index);
//                callback.onSuccess(picturePath);// 获取图片路径
//                 if (BuildConfig.DEBUG) Log.e("aa", "aa");
//            }
//            cursor.close();
//        }
//    }




    @Override
    protected void popView(int i) {
        lin_child.removeAllViews();
        switch (i){
            case R.layout.view_me_userinfo:break;//只是删除
            case R.layout.view_me_userinfo_sign:lin_child.addView(new ViewUserInfoSign(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_nickname:lin_child.addView(new ViewUserInfoNickname(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_sex:lin_child.addView(new ViewUserInfoSex(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_address:lin_child.addView(new ViewUserInfoAddress(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_address_city:lin_child.addView(new ViewUserInfoAddressCity(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_score:lin_child.addView(new ViewUserInfoScore(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_score_help:lin_child.addView(new ViewUserInfoScoreHelp(getApplicationContext(),null));break;
            case R.layout.view_me_userinfo_score_detail:lin_child.addView(new ViewUserInfoScoreDetail(getApplicationContext(),null));break;
        }

    }
    private void gotoJumpPage(final int jumpPage){
        //1：分享页2：车控制页3：皮肤页4：卡片页5：完善个人信息6：设置->安全页
        if(jumpPage == 5){
            handlePopView(R.layout.view_me_userinfo);
        }else {
            ActivityKulalaMain.GestureNeed = false;
            ViewUserInfoActivity.this.finish();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (jumpPage) {
                        case 1:ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_share);break;
                        case 2:ODispatcher.dispatchEvent(OEventName.SCORE_JUMPPAGE_TO_CONTROLCAR);break;
                        case 3:ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_find_car_dressup);break;
                        case 4:ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_collection_achievement);break;
                        case 6:ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW,R.layout.view_me_safety);break;
                    }
                }
            }).start();
        }
    }
}
