package com.kulala.dispatcher;

/**
 * Created by Administrator on 2017/7/17.
 */

public class IKStaticValue {
    //手势指令
    public static final String MOTION_EVENT_UP           = "E0010100E0";//上
    public static final String MOTION_EVENT_DOWN         = "E0010203E0";//下
    public static final String MOTION_EVENT_LEFT         = "E0010405E0";//左
    public static final String MOTION_EVENT_RIGHT        = "E0010302E0";//右
    public static final String MOTION_EVENT_ROUNDL       = "E0010809E0";//逆时针
    public static final String MOTION_EVENT_ROUNDR       = "E0010706E0";//顺时针
    public static final String MOTION_EVENT_PUSH_FORWORD = "E0010504E0";//前推
    public static final String MOTION_EVENT_PUSH_BACK    = "E0010607E0";//后推
    public static final String MOTION_EVENT_SHAKE        = "E0010908E0";//摇手
}
