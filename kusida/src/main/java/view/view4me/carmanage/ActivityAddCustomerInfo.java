package view.view4me.carmanage;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsview.ActivityBase;

import com.kulala.staticsview.OTime60;
import com.kulala.staticsview.toast.OToastButton;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;

import common.GlobalContext;
import common.timetick.OTimeSchedule;
import ctrl.OCtrlCar;
import ctrl.OCtrlRegLogin;
import model.ManagerCarList;
import model.carlist.DataCarInfo;
import model.store.ManagerStore;
import model.store.ShopCarExamples;
import view.clip.ClipLTTxtDImg;
import view.clip.ClipLTxtCEditDLine;
import view.clip.ClipPopLoading;
import view.view4me.set.ClipTitleMeSet;

import static view.view4me.carmanage.ImageUtilMy.bitmapToBase64;


public class ActivityAddCustomerInfo extends ActivityBase {
    private ClipTitleMeSet title_head;
    private ClipLTxtCEditDLine store_name;
    private ClipLTxtCEditDLine contact_person;
    private ClipLTxtCEditDLine phone_number;
    private ClipLTxtCEditDLine region;
    private ClipLTxtCEditDLine full_address;
    private ClipLTTxtDImg business_license_photo;
    private ClipLTTxtDImg door_photo;
    private ClipLTTxtDImg car_photo;
    private Button btn_next;
    private ScrollView scroll_view;
    private LinearLayout lili;
    private Button btn_submit;
    private Button get_vercode;
    private EditText input_vercode;
    private static final int REQUESTCODE_TAKE_BUSINESS_LICENSE_PHOTO = 1;        // 相机拍营业执照照标记
    private static final int REQUESTCODE_PICK_BUSINESS_LICENSE_PHOTO = 2;        // 相册选营业执照照标记
    private static final int REQUESTCODE_TAKE_DOOR_PHOTO = 3;        // 相机拍门头照标记
    private static final int REQUESTCODE_PICK_DOOR_PHOTO = 4;        // 相册选门头标记
    private static final int REQUESTCODE_TAKE_CAR_PHOTO = 5;        // 相机拍车身标记
    private static final int REQUESTCODE_PICK_CAR_PHOTO = 6;        // 相册选车身标记
    public static final String IMAGE_BUSINESS_LICENSE_PHOTO_FILE_NAME = "business_license_photo";// 商户文件名称
    public static final String IMAGE_DOOR_PHOTO_FILE_NAME = "door_photo";// 门头文件名称
    public static final String IMAGE_CAR_PHOTO_FILE_NAME = "car_photo";// 车头文件名称
    private File cachePhoto = null;
    private Uri uriShopPic;
    private Uri uriFrontDoorPicture;
    private Uri uriCarPicture;
    private boolean isShopPicTake;
    private boolean isFrontDoorPictureTake;
    private boolean isCarPictureTake;
    private File fileShopPic;
    private File fileFrontDoorPicture;
    private File fileCarPicture;



    public static String getPhotoCacheDir() {
        return GlobalContext.getContext().getExternalFilesDir("images").getPath();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcustomi_info);
        title_head = findViewById(R.id.title_head);
        store_name = findViewById(R.id.store_name);
        contact_person = findViewById(R.id.contact_person);
        phone_number = findViewById(R.id.phone_number);
        region = findViewById(R.id.region);
        full_address = findViewById(R.id.full_address);
        business_license_photo = findViewById(R.id.business_license_photo);
        door_photo = findViewById(R.id.door_photo);
        car_photo = findViewById(R.id.car_photo);
        btn_next = findViewById(R.id.btn_next);
        scroll_view=findViewById(R.id.scroll_view);
        lili=findViewById(R.id.lili);
        btn_submit=findViewById(R.id.btn_submit);
        get_vercode=findViewById(R.id.get_vercode);
        input_vercode=findViewById(R.id.input_vercode);
        initViews();
        initEvents();
        OTime60.getInstance().listener(get_vercode);
        ODispatcher.addEventListener(OEventName.IMG_PUSH_RESULT, this);
        ODispatcher.addEventListener(OEventName.APPLY_STORE_CAR_RESULT, this);
        ODispatcher.addEventListener(OEventName.VERIFICATION_CODE_BACKOK, this);
        ODispatcher.addEventListener(OEventName.SUBMIT_AUDIT_SUCCESS, this);
    }

    protected void initViews() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        title_head.img_left.callOnClick();
    }
    public void initEvents() {
        business_license_photo.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OToastButton.getInstance().show(business_license_photo.image, new String[]{getResources().getString(R.string.taking_pictures), getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums)}, "business_license_photo", ActivityAddCustomerInfo.this);
            }
        });
        door_photo.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OToastButton.getInstance().show(door_photo.image, new String[]{getResources().getString(R.string.taking_pictures), getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums)}, "door_photo", ActivityAddCustomerInfo.this);
            }
        });
        car_photo.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OToastButton.getInstance().show(car_photo.image, new String[]{getResources().getString(R.string.taking_pictures), getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums)}, "car_photo", ActivityAddCustomerInfo.this);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkParam()){
                    ClipPopLoading.getInstance().show(btn_next);
                    pushImg("1");
                }
            }
        });

        title_head.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(ActivityAddCustomerInfo.this, ActivityKulalaMain.class);
//                startActivity(intent);

                if(scroll_view.getVisibility()==View.INVISIBLE){
                    scroll_view.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.INVISIBLE);
                    lili.setVisibility(View.INVISIBLE);
                }else{
                    finish();
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verfycode=input_vercode.getText().toString();
                if(TextUtils.isEmpty(verfycode)){
                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查验证码是否已经填写");
                    return;
                }
                OCtrlCar.getInstance().ccmd2001_tijiaoshenhe(getPhoneNum(),verfycode,"10",getPide());
            }
        });
        get_vercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTimeSchedule.getInstance().init();
                OTime60.getInstance().startTime();
                OCtrlRegLogin.getInstance().ccmd1101_getVerificationCode(getPhoneNum(), 10, 1);
            }
        });
    }
    private String getPhoneNum(){
        return ManagerStore.getInstance().shopCarExamplesPid.getAgentPhone();
    }
    private long getPide(){
        return ManagerStore.getInstance().shopCarExamplesPid.getpId();
    }
    private final Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
    private void pushImg(String whichImg){
        switch (whichImg) {
            case "1":
                if (isShopPicTake) {
                    compressCamara(fileShopPic, whichImg);
                } else {
                    compressGallay(uriShopPic, whichImg);
                }
                break;
            case "2":
                if (isFrontDoorPictureTake) {
                    compressCamara(fileFrontDoorPicture, whichImg);
                } else {
                    compressGallay(uriFrontDoorPicture, whichImg);
                }
                break;
            case "3":
                if (isCarPictureTake) {
                    compressCamara(fileCarPicture, whichImg);
                } else {
                    compressGallay(uriCarPicture, whichImg);
                }
                break;
        }
    }
    private  void compressGallay(Uri uri,final String whichImg){
        if(uri==null)return;
        Tiny.getInstance().source(uri).asBitmap().withOptions(options).compress(new BitmapCallback() {
            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, Throwable t) {
                //将圖片转成base64
                String file=  bitmapToBase64(bitmap);
                OCtrlCar.getInstance().ccmd2000_pushPic(file,whichImg);
            }
        });
    }
    private  void compressCamara(File file,final String whichImg){
        if(file==null)return;
        Tiny.getInstance().source(file).asFile().compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile, Throwable t) {
                //将圖片转成base64
                File f = new File(outfile);
                String file=  ImageUtilMy.fileToBase64(f);
                OCtrlCar.getInstance().ccmd2000_pushPic(file,whichImg);
            }
        });
    }


    private boolean checkParam() {
        if (TextUtils.isEmpty(store_name.txt_input.getText().toString())) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查商户名是否已经填写");
            return false;
        }
        if (TextUtils.isEmpty(contact_person.txt_input.getText().toString())) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查联系人是否已经填写");
            return false;
        }
        if (TextUtils.isEmpty(phone_number.txt_input.getText().toString())) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查电话号码是否已经填写");
            return false;
        }
        if (TextUtils.isEmpty(region.txt_input.getText().toString())) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查地区是否已经填写");
            return false;
        }
        if (TextUtils.isEmpty(full_address.txt_input.getText().toString())) {
            ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查地址是否已经填写");
            return false;
        }
        if(isShopPicTake){
            if (fileShopPic==null){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查是否已选择营业执照照片");
                return false;
            }
        }else{
            if (uriShopPic==null){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查是否已选择营业执照照片");
                return false;
            }
        }
        if(isFrontDoorPictureTake){
            if (fileFrontDoorPicture==null){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查是否已选择门头照片");
                return false;
            }
        }else{
            if (uriFrontDoorPicture==null){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查是否已选择门头照片");
                return false;
            }
        }
        if(isCarPictureTake){
            if (fileCarPicture==null){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查是否已选择样车照片");
                return false;
            }
        }else{
            if (uriCarPicture==null){
                ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "请检查是否已选择样车照片");
                return false;
            }
        }
        return true;
    }



    @Override
    public void callback(String key, Object value) {
        requrePermisionAndTakePhotoOrReadGallary(key, (String) value);
    }

    private void requrePermisionAndTakePhotoOrReadGallary(String key, String value) {
        if (value.equals(getResources().getString(R.string.taking_pictures))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permissionCamera = checkSelfPermission(Manifest.permission.CAMERA);
                int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                //拍照权限
                if (permissionCamera != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    File fullPath = getFullPath(key);
                    cachePhoto = fullPath;//缓存的照相机圖片路径
                    Uri uu = Uri.fromFile(fullPath);
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    if (Build.VERSION.SDK_INT < 24) {
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uu);
                    } else {
                        Uri contentUri = FileProvider.getUriForFile(ActivityAddCustomerInfo.this, getPackageName(), fullPath);
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    }
                    startActivityForResult(takeIntent, getRequestCode(key, value));
                }
            } else {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFullPath(key)));
                startActivityForResult(takeIntent, getRequestCode(key, value));
            }
        } else if (value.equals(getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums))) {
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
                    startActivityForResult(pickIntent, getRequestCode(key, value));
                }
            }else{
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                pickIntent.setType("image/*");
                startActivityForResult(pickIntent, getRequestCode(key, value));
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
                    .withInfo("使用相机需要用到相机权限和存储权限，请前往设置")
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

    private File getFullPath(String key) {
        File fullPath = null;
        switch (key) {
            case "business_license_photo":
                fullPath = new File(getPhotoCacheDir(), IMAGE_BUSINESS_LICENSE_PHOTO_FILE_NAME + "_" + System.currentTimeMillis() + ".png");
                break;
            case "door_photo":
                fullPath = new File(getPhotoCacheDir(), IMAGE_DOOR_PHOTO_FILE_NAME + "_" + System.currentTimeMillis() + ".png");
                break;
            case "car_photo":
                fullPath = new File(getPhotoCacheDir(), IMAGE_CAR_PHOTO_FILE_NAME + "_" + System.currentTimeMillis() + ".png");
                break;
        }
        return fullPath;
    }

    private int getRequestCode(String key, String value) {
        if (value.equals(getResources().getString(R.string.taking_pictures))) {
            switch (key) {
                case "business_license_photo":
                    return REQUESTCODE_TAKE_BUSINESS_LICENSE_PHOTO;
                case "door_photo":
                    return REQUESTCODE_TAKE_DOOR_PHOTO;
                case "car_photo":
                    return REQUESTCODE_TAKE_CAR_PHOTO;
            }
        } else if (value.equals(getResources().getString(R.string.choose_from_the_mobile_phone_photo_albums))) {
            switch (key) {
                case "business_license_photo":
                    return REQUESTCODE_PICK_BUSINESS_LICENSE_PHOTO;
                case "door_photo":
                    return REQUESTCODE_PICK_DOOR_PHOTO;
                case "car_photo":
                    return REQUESTCODE_PICK_CAR_PHOTO;
            }
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BuildConfig.DEBUG)
            Log.e("ActivityAddCustomerInfo", "onActivityResult" + " requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
        if (resultCode != RESULT_OK) {
            return;
        }
        showResult(data, requestCode);
    }

    private void showResult(final Intent data, final int requestCode) {
        switch (requestCode) {
            case REQUESTCODE_PICK_BUSINESS_LICENSE_PHOTO:
                isShopPicTake=false;
                setImage(business_license_photo.image, data, false);
                if(data!=null&&data.getData() != null){
                    uriShopPic=data.getData();
                }
                break;
            case REQUESTCODE_TAKE_BUSINESS_LICENSE_PHOTO:
                isShopPicTake=true;
                setImage(business_license_photo.image, data, true);
                //从相机里取文件
                fileShopPic=cachePhoto;
//                uriShopPic=FileProvider.getUriForFile(ActivityAddCustomerInfo.this, getPackageName(), cachePhoto);
                break;
            case REQUESTCODE_PICK_DOOR_PHOTO:
                isFrontDoorPictureTake=false;
                setImage(door_photo.image, data, false);
                if(data!=null&&data.getData() != null){
                    uriFrontDoorPicture=data.getData();
                }
                break;
            case REQUESTCODE_TAKE_DOOR_PHOTO:
                isFrontDoorPictureTake=true;
                setImage(door_photo.image, data, true);
                //从相机里取文件
                fileFrontDoorPicture=cachePhoto;
//                uriFrontDoorPicture=FileProvider.getUriForFile(ActivityAddCustomerInfo.this, getPackageName(), cachePhoto);
                break;
            case REQUESTCODE_PICK_CAR_PHOTO:
                isCarPictureTake=false;
                setImage(car_photo.image, data, false);
                if(data!=null&&data.getData() != null){
                    uriCarPicture=data.getData();
                }
                break;
            case REQUESTCODE_TAKE_CAR_PHOTO:
                isCarPictureTake=true;
                setImage(car_photo.image, data, true);
                fileCarPicture=cachePhoto;
//                uriCarPicture=FileProvider.getUriForFile(ActivityAddCustomerInfo.this, getPackageName(), cachePhoto);
                break;
        }
    }

    private void setImage(final ImageView imageView, final Intent data, final boolean isTake) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isTake) {//相机拍照结果从文件中取
                    imageView.setImageURI(FileProvider.getUriForFile(ActivityAddCustomerInfo.this, getPackageName(), cachePhoto));////缓存的照相机圖片路径取出照片
                } else {//相册从返回值中取
                    if (data != null && data.getData() != null) {
                        imageView.setImageURI(data.getData());
                    }
                }
            }
        });
    }

    @Override
    public void invalidateUI() {

    }


    @Override
    protected void popView(int resId) {

    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        super.receiveEvent(eventName, paramObj);
        if(eventName.equals(OEventName.IMG_PUSH_RESULT)){
            String result=(String)paramObj;
            switch (result) {
                case "fail":
                    handleStopLoading();
                    break;
                case "1":
                    pushImg("2");
                    break;
                case "2":
                    pushImg("3");
                    break;
                case "3":
                    String terminalNum = getTerminalNum();
                    String shopName = getShopName();
                    String personLiable = getPersonLiable();
                    String agentPhone = getagentPhone();
                    String address = getAddress();
                    String shopLicense = ManagerStore.getInstance().getShopLicense();
                    String frontDoorPicture = ManagerStore.getInstance().getFrontDoorPicture();
                    String carPicture = ManagerStore.getInstance().getCarPicture();
                    String region = getregion();
                    ShopCarExamples examples = new ShopCarExamples(terminalNum, shopName, personLiable, agentPhone, address, shopLicense, frontDoorPicture, carPicture, region);
                    OCtrlCar.getInstance().ccmd2201_applySampleStore(examples);
                    break;
            }
        }else if(eventName.equals(OEventName.APPLY_STORE_CAR_RESULT)){
            boolean isSucess=(Boolean)paramObj;
            handleStopLoading();
            if(isSucess){
                handleShowCodePage();
            }
        } else if (eventName.equals(OEventName.VERIFICATION_CODE_BACKOK)) {
            OTime60.getInstance().endTime();
        } else if (eventName.equals(OEventName.SUBMIT_AUDIT_SUCCESS)) {
            handleBackMainActivity();
        }
    }
    private String getTerminalNum(){
       DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
       if(carInfo!=null&&!TextUtils.isEmpty(carInfo.terminalNum)){
           return carInfo.terminalNum;
       }
       return "";
    }
    private String getShopName(){
        String storeName=store_name.txt_input.getText().toString();
        if(!TextUtils.isEmpty(storeName)){
            return storeName;
        }
        return "";
    }
    private String getPersonLiable(){
        String personLiable=contact_person.txt_input.getText().toString();
        if(!TextUtils.isEmpty(personLiable)){
            return personLiable;
        }
        return "";
    }
    private String getagentPhone(){
        String agentPhone=phone_number.txt_input.getText().toString();
        if(!TextUtils.isEmpty(agentPhone)){
            return agentPhone;
        }
        return "";
    }
    private String getAddress(){
        String address=full_address.txt_input.getText().toString();
        if(!TextUtils.isEmpty(address)){
            return address;
        }
        return "";
    }
    private String getregion(){
        String regions=region.txt_input.getText().toString();
        if(!TextUtils.isEmpty(regions)){
            return regions;
        }
        return "";
    }

    private void handleStopLoading(){
        Message message=Message.obtain();
        message.what=369;
        handler.sendMessage(message);
    }
    private void handleShowCodePage(){
        Message message=Message.obtain();
        message.what=370;
        handler.sendMessage(message);
    }
    private void handleBackMainActivity(){
        Message message=Message.obtain();
        message.what=371;
        handler.sendMessage(message);
    }
    private final Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==369){
                ClipPopLoading.getInstance().stopLoading();
            }else if(msg.what==370){
                scroll_view.setVisibility(View.INVISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                lili.setVisibility(View.VISIBLE);
            }else if(msg.what==371){
//                Intent intent = new Intent();
//                intent.setClass(ActivityAddCustomerInfo.this, ActivityKulalaMain.class);
//                startActivity(intent);
                Toast.makeText(GlobalContext.getContext(),"提交成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

}