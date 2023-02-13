package common.pinyinzhuanhuan;

/**
 * Created by qq522414074 on 2016/8/12.
 */
public interface  PhotoCallBack  {
    void onSuccess(String picturePath);// 拿取相片成功
    void onFailure();// 拿取相片失败
}
