package com.kulala.staticsfunc.static_assistant;

public class UrlHelper {
    /**
     * 取文件名，无后缀
     * UrlHelper.getFileName(downLoadUrl)+".apk"
     */
    public static String getFileName(String url){
        int start=url.lastIndexOf("/");
        int end=url.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return url.substring(start+1,end);
        }else{
            return null;
        }

    }
}
