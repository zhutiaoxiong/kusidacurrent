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
    //    private static final String IMAGE_FILE_NAME     = "avatarImage.png";// ??????????????????
    private static final int    REQUESTCODE_TAKE    = 1;        // ??????????????????
    private static final int    REQUESTCODE_PICK    = 0;        // ??????????????????
    private static final int    REQUESTCODE_CUTTING = 2;    // ??????????????????
    private String    urlpath;            // ????????????????????????
    private Context   mContext;
    private CircleImg avatarImg;// ????????????
    private String    prefix; //??????????????????
    private String    path;//??????????????????????????????


    private long chooseDate;//???????????????

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
        //??????????????????view
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
        //???????????????
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //??????????????????
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR)+0);//??????setTime ?????????????????????
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

                    //????????????
                    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //???????????????????????????????????????????????????????????????
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
                    //???????????????????????????????????????????????????????????????
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DataUser.getHeadCacheDir(), DataUser.IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                }
            } else if (select.equals(getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //????????????
                    if ( permissionRead != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // ????????????????????????????????????????????????????????????????????????"image/jpeg ??? image/png????????????"
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                pickIntent.setType("image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                }else{
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // ????????????????????????????????????????????????????????????????????????"image/jpeg ??? image/png????????????"
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
                    .withTitle("??????")
                    .withInfo("????????????????????????????????????????????????????????????????????????????????????????????????????????????")
                    .withButton("??????","?????????")
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
                    .withTitle("??????")
                    .withInfo("??????????????????????????????????????????????????????")
                    .withButton("??????","?????????")
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
     * ????????????????????????
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
            case REQUESTCODE_PICK:// ?????????????????????
                if (data == null) {
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "?????????????????????");
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
            case REQUESTCODE_TAKE:// ??????????????????
//                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
//                startPhotoZoom(Uri.fromFile(temp));
//                uri = Uri.fromFile(new File(path));
                path = DataUser.getHeadCacheDir() + "/" + DataUser.IMAGE_FILE_NAME;
                Intent cutPicIntentOne = new Intent(ViewUserInfoActivity.this, ClipCutPicActivity.class);
                cutPicIntentOne.putExtra("path", path);
                cutPicIntentOne.putExtra("from", "1");
                startActivityForResult(cutPicIntentOne, REQUESTCODE_CUTTING);
                break;
            case REQUESTCODE_CUTTING:// ????????????????????????
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
        // crop=true?????????????????????Intent??????????????????VIEW?????????
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ?????????????????????
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // ??????SDCard?????????????????????
            Bitmap photo = extras.getParcelable("data");
//          Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.png", photo);
//          avatarImg.setImageDrawable(drawable);
            //?????????????????????????????????
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
            //?????????????????????????????????????????????
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
                .connectTimeout(90)              // ?????????????????????90???
                .useHttps(true)                  // ????????????https????????????
                .useConcurrentResumeUpload(true) // ????????????????????????????????????????????????????????????????????????????????????????????????????????????4M???
                .concurrentTaskCount(3)          // ???????????????????????????3
                .responseTimeout(90)             // ??????????????????????????????90???
                .recorder(null)              // recorder????????????????????????????????????????????????null
                .recorder(null, null)      // keyGen ??????????????????????????????????????????????????????????????????????????????????????????
                .zone(FixedZone.zone2)
                .build();
        // ???????????????????????????????????????????????????????????????????????????????????????????????????I
//        Configuration config = new Configuration.Builder()
//                .chunkSize(256 * 1024)  //???????????????????????????????????? ??????256K
//                .putThreshhold(512 * 1024)  // ?????????????????????????????????512K
//                .connectTimeout(10) // ?????????????????????10???
//                .responseTimeout(60) // ??????????????????????????????60???
//                .recorder(null)  // recorder????????????????????????????????????????????????null
//                .recorder(null, null)  // keyGen ??????????????????????????????????????????????????????????????????????????????????????????
//                .zone(Zone.zone0) // ????????????????????????????????????????????????????????????????????????IP????????? Zone.zone0
//                .build();
        // ??????uploadManager????????????????????????????????????uploadManager??????
        UploadManager uploadManager = new UploadManager(config);
        long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
        String        key           = "pic/avatar/" + userId + "/" + System.currentTimeMillis() + ".png";
        //?????????1312???????????????token??????????????????????????????????????????

        uploadManager.put(urlpath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                System.out.print(info.isOK());
                if (info != null) {
                    if (info.isOK()) {
                         if (BuildConfig.DEBUG) Log.e("????????????", "?????? " );
                        DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
                        user.avatarUrl = prefix + key;
                        OCtrlRegLogin.getInstance().ccmd1110_changeUserInfo(user.toJsonObject());
                    }else{
                         if (BuildConfig.DEBUG) Log.e("????????????", "?????? " );
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
//        String picturePath = "";   // ??????Android4.4???????????????????????????????????????Uri??????????????????
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
//                callback.onSuccess(picturePath);// ??????????????????
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
//                callback.onSuccess(picturePath);// ??????????????????
//                 if (BuildConfig.DEBUG) Log.e("aa", "aa");
//            }
//            cursor.close();
//        }
//    }




    @Override
    protected void popView(int i) {
        lin_child.removeAllViews();
        switch (i){
            case R.layout.view_me_userinfo:break;//????????????
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
        //1????????????2???????????????3????????????4????????????5?????????????????????6?????????->?????????
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
