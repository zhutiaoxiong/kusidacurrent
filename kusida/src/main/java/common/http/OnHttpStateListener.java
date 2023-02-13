package common.http;

/**
 * Created by Administrator on 2017/2/10.
 */
public interface OnHttpStateListener {
    void onSendStart(int cmd);

    void onSendFailed(int cmd,String exceptionInfo);

//    void onReceiveData(int cmd, JsonObject data);//收包了就是成功了
    void onReceiveData(int cmd, String receiveStr);//收包了就是成功了
}
