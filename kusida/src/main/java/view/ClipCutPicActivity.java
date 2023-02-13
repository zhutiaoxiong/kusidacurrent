package view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.proj.kusida.BuildConfig;
import com.client.proj.kusida.R;
import com.kulala.staticsview.toast.ToastConfirmNormal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import common.GlobalContext;
import common.pinyinzhuanhuan.ClipImageLayout;
import common.pinyinzhuanhuan.ImageTools;
import model.loginreg.DataUser;

/**
 * Created by qq522414074 on 2016/8/5.
 */
public class ClipCutPicActivity extends Activity {
    private ClipImageLayout mClipImageLayout;
    private String path;
    private String from;
    private ProgressDialog loadingDialog;
    private static final String IMAGE_FILE_NAME = "avatarImage.png";// 头像文件名称

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipimage);
        TextView besure = findViewById(R.id.besure);
        ImageView backtocamaraorgallery = (ImageView) findViewById(R.id.backto_camaraorgallery);
        backtocamaraorgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //这步必须要加
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setTitle(getResources().getString(R.string.please_later));
        if(getIntent()!=null){
            path=getIntent().getStringExtra("path");
            from=getIntent().getStringExtra("from");
        }

         if (BuildConfig.DEBUG) Log.e( "1234"," path:" + path );
        if(TextUtils.isEmpty(path)||!(new File(path).exists())){
            Toast.makeText(this, getResources().getString(R.string.image_loading_failure),Toast.LENGTH_SHORT).show();
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            //拍照权限
            if ( permissionRead != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            } else {
                comPressImgAndShow();
            }
        }else{
                comPressImgAndShow();
        }



        besure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();
//                        Bitmap covertbitmap = mClipImageLayout.clip();
//                        Bitmap    bitmap = Bitmap.createScaledBitmap(covertbitmap, 300, 300, true);
//                        String path= Environment.getExternalStorageDirectory()+"/ "+IMAGE_FILE_NAME;
                        String path= DataUser.getHeadCacheDir()+"/ "+IMAGE_FILE_NAME;
                        ImageTools.savePhotoToSDCard(bitmap,path);
                        loadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("path",path);
                         if (BuildConfig.DEBUG) Log.e( "path2"," path:" + path );
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).start();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (BuildConfig.DEBUG) Log.e("loadPermission", "onRequestPermissionsResult:" +requestCode+ "  " + permissions.toString());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //loadPermissions  LoadPermissions.getInstance()
        boolean isCunchuGranted=true;
        if(requestCode==3){
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i]==-1){
                    isCunchuGranted=false;
                }
            }
            if(!isCunchuGranted){
                jumpSettingPage();
            }
        }
    }


    private void jumpSettingPage(){

            new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null,false)
                    .withTitle("提示")
                    .withInfo("显示相机相册圖片需要使用存储权限，请前往设置")
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
    private void comPressImgAndShow(){
        Bitmap bitmap=null;
        if(from.equals("1")){
            //
//             Bitmap    covertBitmap=ImageTools.convertToBitmap(path, 480,800);

            Bitmap    yuantuBitmap   = getBitmapFromFile(path);
            Bitmap    covertBitmap=   compressScale(yuantuBitmap);
            int degre=getExifOrientation(path) ;
            bitmap= rotateBimap(this,degre,covertBitmap);
        }else if(from.equals("2")){

            Bitmap      bitmapCache=ImageTools.getBitmapFromUri(ClipCutPicActivity.this,ImageTools.getImageContentUri(ClipCutPicActivity.this,path));
//                Bitmap    covertBitmap1 = Bitmap.createScaledBitmap(bitmapCache, 480, 800, true);
            Bitmap    covertBitmap1=   compressScale(bitmapCache);
            int degre=getExifOrientation(path) ;
            bitmap= rotateBimap(this,degre,covertBitmap1);
        }
//            Bitmap bitmap=ImageTools.getBitmapFromUri(ClipCutPicActivity.this,ImageTools.getImageContentUri(ClipCutPicActivity.this,path));
//             if (BuildConfig.DEBUG) Log.e( "bitmap"," bitmap:" + bitmap );
        if(bitmap==null){
            Toast.makeText(this, getResources().getString(R.string.image_loading_failure),Toast.LENGTH_SHORT).show();
            return;
        }

        mClipImageLayout = findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setBitmap(bitmap);
    }

    /**
     * 通过文件路径来获取Bitmap
     * @param pathName
     */
    public static Bitmap getBitmapFromFile(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }


    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.d("ClipCutPicActivity", "cannot read exif" + ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }
    //获取旋转后的角度
    private Bitmap rotateBimap(Context context, float degree, Bitmap srcBitmap) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap,0,0,srcBitmap.getWidth(),srcBitmap.getHeight()
                ,matrix,true);
        return bitmap;
    }
    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    /**
     * 图片按比例大小压缩方法
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }
    /**
     * 图片按比例大小压缩方法
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
//        Log.i(TAG, w + "---------------" + h);
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 512f;
        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        //return bitmap;
    }
}
