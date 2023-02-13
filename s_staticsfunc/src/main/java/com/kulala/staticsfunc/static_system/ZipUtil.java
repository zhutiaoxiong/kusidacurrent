package com.kulala.staticsfunc.static_system;

import android.util.Log;

import com.kulala.staticsfunc.LogMe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static com.kulala.staticsfunc.static_assistant.UtilFileSave.GIF_CHANGE_NAME;
import static com.kulala.staticsfunc.static_assistant.UtilFileSave.PNG_CHANGE_NAME;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ZipUtil {
    public static OnUnzipFileListener onUnzipFileListener;

    public static void zip(String src, String dest) throws IOException {
        //提供了一个数据项压缩成一个ZIP归档输出流
        ZipOutputStream out = null;
        try {

            File outFile         = new File(dest);//源文件或者目录
            File fileOrDirectory = new File(src);//压缩文件路径
            out = new ZipOutputStream(new FileOutputStream(outFile));
            //如果此文件是一个文件，否则为false。
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                //返回一个文件或空阵列。
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], "");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //关闭输出流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void zipFileOrDirectory(ZipOutputStream out,
                                           File fileOrDirectory, String curPath) throws IOException {
        //从文件中读取字节的输入流
        FileInputStream in = null;
        try {
            //如果此文件是一个目录，否则返回false。
            if (!fileOrDirectory.isDirectory()) {
                // 压缩文件
                byte[] buffer = new byte[4096];
                int    bytes_read;
                in = new FileInputStream(fileOrDirectory);
                //实例代表一个条目内的ZIP归档
                ZipEntry entry = new ZipEntry(curPath
                        + fileOrDirectory.getName());
                //条目的信息写入底层流
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            } else {
                // 压缩目录
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], curPath
                            + fileOrDirectory.getName() + "/");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // throw ex;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    /**解压文件*/
    //ZipUtil.unzip(file.getPath(),file.getPath().replace(skinId+".zip","")+"/"+skinId);
    public static void unzip(final String zipFilePath, final String outputDirectory) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("UNZIP", "zipFileName: " + zipFilePath);
                File file = new File(zipFilePath);
                if (!file.exists()) {
                    Log.i("UNZIP", "压缩文件不存在!"); return;
                }
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(zipFilePath);//这里有可能zip文件错
                    Enumeration e        = zipFile.entries();
                    ZipEntry    zipEntry = null;
                    File        dest     = new File(outputDirectory);
                    dest.mkdirs();
                    while (e.hasMoreElements()) {
                        zipEntry = (ZipEntry) e.nextElement();
//                Log.i("UNZIP","file name: "+zipEntry.getName());
                        String           entryName = zipEntry.getName();
                        LogMe.e("UNZIP", "解压:"+entryName);
                        InputStream      in        = null;
                        FileOutputStream out       = null;
                        try {
                            if (zipEntry.isDirectory()) {
                                String name = zipEntry.getName();
                                name = name.substring(0, name.length() - 1);
                                File f = new File(outputDirectory + File.separator + name);
                                f.mkdirs();
                            } else {
                                int index = entryName.lastIndexOf("\\");
                                if (index != -1) {
                                    File df = new File(outputDirectory + File.separator + entryName.substring(0, index));
                                    df.mkdirs();
                                }
                                index = entryName.lastIndexOf("/");
                                if (index != -1) {
                                    File df = new File(outputDirectory + File.separator + entryName.substring(0, index));
                                    df.mkdirs();
                                }
                                //change *.png--->*.oipc
                                String replaceName = zipEntry.getName().replace(".png", PNG_CHANGE_NAME);
                                //change *.gif--->*.oipf
                                replaceName = replaceName.replace(".gif", GIF_CHANGE_NAME);
//                         if (BuildConfig.DEBUG) Log.e("SkinCheck","unziping: "+replaceName);
                                File f = new File(outputDirectory + File.separator + replaceName);
                                // f.createNewFile();
                                in = zipFile.getInputStream(zipEntry);
                                out = new FileOutputStream(f);
                                int    c;
                                byte[] by = new byte[1024];
                                while ((c = in.read(by)) != -1) {
                                    out.write(by, 0, c);
                                }
                                out.flush();
                            }
                        } catch (Exception ex) {
                            if (onUnzipFileListener != null) onUnzipFileListener.onUnzipFailed();
                            onUnzipFileListener = null;
                            ex.printStackTrace();
                        } finally {
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException ex) {
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException ex) {
                                }
                            }
                        }
                    }
                    if (onUnzipFileListener != null) onUnzipFileListener.onUnzipOK();
                    onUnzipFileListener = null;
                } catch (Exception ex) {
                    if (onUnzipFileListener != null) onUnzipFileListener.onUnzipFailed();
                    onUnzipFileListener = null;
                    ex.printStackTrace();
                } finally {
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException ex) {
                        }
                    }
                }

            }
        }).start();
    }
    public interface OnUnzipFileListener {
        void onUnzipOK();

        void onUnzipFailed();
    }
    //成功后自动释放
    public static void setOnUnzipFileListener(OnUnzipFileListener listener) {
        onUnzipFileListener = listener;
    }
}