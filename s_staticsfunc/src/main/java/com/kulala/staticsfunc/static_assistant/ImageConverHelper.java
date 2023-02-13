package com.kulala.staticsfunc.static_assistant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;

public class ImageConverHelper {
    public static Drawable getImageDrawableFromFile(String path) {
        BitmapFactory.Options options = null;
        if (options == null) options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options); //filePath代表图片路径
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            //表示图片已损毁，删除
            UtilFileSave.RecursionDeleteFile(new File(path));
            return null;
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                //表示图片已损毁，删除
                UtilFileSave.RecursionDeleteFile(new File(path));
                return null;
            }
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            return bd;
        }
    }
    public static Bitmap getBitmapFromFile(String path) {
        BitmapFactory.Options options = null;
        if (options == null) options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options); //filePath代表图片路径
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            //表示图片已损毁，删除
            UtilFileSave.RecursionDeleteFile(new File(path));
            return null;
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                //表示图片已损毁，删除
                UtilFileSave.RecursionDeleteFile(new File(path));
                return null;
            }
            return bitmap;
        }
    }
}
