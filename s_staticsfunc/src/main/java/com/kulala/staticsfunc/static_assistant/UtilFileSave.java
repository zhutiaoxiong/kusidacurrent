package com.kulala.staticsfunc.static_assistant;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class UtilFileSave {
    public static String PNG_CHANGE_NAME = ".oipc";
    public static String GIF_CHANGE_NAME = ".oipf";
    public static String getBaseDir(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath();
    }
    public static String getSkinBaseDir(Context context) {
        return Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath()+ "/kukong/skin";//加条目录，区别旧版1
    }

    /**
     * 文件是否存在
     */
    public static boolean isFileInDisk(String fileDir, String fileName) {
        File file = new File(fileDir);
        if(fileName == null || fileName.length() == 0){
            if (file.exists()) return true;
            else return false;
        }else {
            file.mkdirs();
            File useFile = new File(file, fileName);
            if (useFile.exists()) return true;
        }
        return false;
    }

    public static File getFileFromDisk(String fileDir, String fileName) {
        File file = new File(fileDir);
        file.mkdirs();
        File useFile = new File(file, fileName);
        if (useFile.exists()) return useFile;
        return null;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 复制资源文件到本地
     * AssetManager asm   = getResources().getAssets();
     * InputStream is0     = asm.open("car_lightback.png");//name:图片的名称
     * Drawable    lightback = Drawable.createFromStream(is0, null);
     *
     * UtilFileSave.copyResFileToDisk(context, skinFolder, "skin_default_car.zip", fileName + ".zip");
     */
    public static void copyResFileToDisk(Context context, String dir, String res, String reName){
        try {
            AssetManager asm  = context.getResources().getAssets();
            InputStream is   = asm.open(res);//"car_lightback.png"
            File         file = new File(dir);
            file.mkdirs();//整条目录
            file.mkdir();
            file = new File(file, reName);//0.zip
            FileOutputStream os     = new FileOutputStream(file);
            int              len    = 0;
            byte[]           buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
                os.flush();
            }
            is.close();
            os.close();
        }catch (IOException e){
             Log.e("copyResFileToDisk","ERROR:"+e.getMessage());
        }
    }

}
