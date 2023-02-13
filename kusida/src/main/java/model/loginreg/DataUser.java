package model.loginreg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.client.proj.kusida.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;

public class DataUser {
    public long   userId    = 0;
    public String name      = "";//nickname
    //	public String password;
    public String phoneNum  = "";
    public String email     = "";
    public String idCard    = "";
    public String avatarUrl = "0";//图片地址
    public String sortLetters;  //显示联系人拼音的首字母
    public boolean isSelected = false;
    public int hasSecretQuestion ;

    public int score ;//积分
    public int sex ;//性别 0：未选择，1：男，2：女
    public long birthday ;//生日，为零时不要显示1970年
    public String location = "" ;//位置
    public String comment = "" ;//个性签名
    /*****************************头像文件******************************/
    public static String getHeadCacheDir(){
//        return Uri.fromFile(new File("Android/data/com.harry.shopping/files/Pictures"));
//        return Uri.fromFile(Environment.getExternalStorageDirectory());//external-path
        String path = GlobalContext.getContext().getExternalFilesDir("images").getPath();
//        String path = GlobalContext.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        return path;
//        return Uri.fromFile(GlobalContext.getContext().getCacheDir());//cache-path
//        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }
    public static final String IMAGE_FILE_NAME     = "avatarImage.png";// 头像文件名称
    /*****************************************************************/

    public DataUser copy() {
        JsonObject object = toJsonObject();
        return fromJsonObject(object);
    }

    public static DataUser fromJsonObject(JsonObject obj) {
        Gson     gson    = new Gson();
        DataUser thisobj = gson.fromJson(obj, DataUser.class);
        return thisobj;
    }
    public static List<DataUser> fromJsonArray(JsonArray array) {
        if (array == null || array.size() == 0)return null;
        List<DataUser> data = new ArrayList<DataUser>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject jsonObject = array.get(i).getAsJsonObject();
            DataUser user = DataUser.fromJsonObject(jsonObject);
            data.add(user);
        }
        return data;
    }

    public JsonObject toJsonObject() {
        Gson       gson = new Gson();
        String     json = gson.toJson(this);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
    public static JsonArray toJsonArray(List<DataUser> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }


    public static int getUserHeadResId(String pos) {
        if (pos == null || pos.equals("") || pos.length() > 1) return R.drawable.head_no;
        int headpos = 0;
        try {
            headpos = Integer.valueOf(pos);
        } catch (Exception e) {
        }
        switch (headpos) {
            case 0:
                return R.drawable.head_no;
            case 1:
                return R.drawable.head_1;
            case 2:
                return R.drawable.head_2;
            case 3:
                return R.drawable.head_3;
            case 4:
                return R.drawable.head_4;
            case 5:
                return R.drawable.head_5;
            case 6:
                return R.drawable.head_6;
            case 7:
                return R.drawable.head_7;
            case 8:
                return R.drawable.head_8;
        }
        return R.drawable.head_no;
    }
    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            if(inputStream==null)return null;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }


}
