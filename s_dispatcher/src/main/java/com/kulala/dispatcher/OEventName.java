package com.kulala.dispatcher;

public class OEventName {
    public static       String CALL_MY_PHONE                        = "CALL_MY_PHONE";
    public static       String GLOBAL_NEED_REFRESH_CAR              = "GLOBAL_NEED_REFRESH_CAR";
    public static       String GLOBAL_NEED_CANCEL_AREA              = "GLOBAL_NEED_CANCEL_AREA";
    public static       String GLOBAL_SHOW_LOG                     = "GLOBAL_SHOW_LOG";
    public static       String GLOBAL_POP_TOAST                     = "GLOBAL_POP_TOAST";
    public static       String GLOBAL_POP_LOADING_SHOW              = "GLOBAL_POP_LOADING_SHOW";
    public static       String GLOBAL_POP_LOADING_HIDE              = "GLOBAL_POP_LOADING_HIDE";
    public static       String GLOBAL_POP_LOADING_OUTTIME           = "GLOBAL_POP_LOADING_OUTTIME";
    public static       String CONTROL_SHOW_TEST_INFO               = "CONTROL_SHOW_TEST_INFO";
    public static       String CONTROL_SHOW_TEST_INFO1              = "CONTROL_SHOW_TEST_INFO1";
    /**
     * Connect
     */
    public static final String APPLICATION_INTO_BACKGROUND          = "APPLICATION_INTO_BACKGROUND";
    public static       String HTTP_CONN_ERROR                      = "HTTP_CONN_ERROR";
    public static       String HTTP_RESULT_BACK                     = "HTTP_RESULT_BACK";
    public static final String BLUE_TEST_SHOW                       = "BLUE_TEST_SHOW";
    public static final String BLUE_RECEIVE_MSG                     = "BLUE_RECEIVE_MSG";
    public static final String WEAR_LINK_STATE_CHANGE               = "WEAR_LINK_STATE_CHANGE";
//    public static final String WEAR_LINK_DATA_BACK                  = "WEAR_LINK_DATA_BACK";//dataMap
    /**
     * Login&&Reg
     */
    public static       String REGISTER_SUCCESS                     = "REGISTER_SUCCESS";
    public static       String LOGIN_SUCCESS                        = "LOGIN_SUCCESS";
    public static       String CHANGE_PASSWORD_RESULTBACK           = "CHANGE_PASSWORD_RESULTBACK";
    public static       String CHECK_PASSWORD_RESULTBACK            = "CHECK_PASSWORD_RESULTBACK";
    public static       String CHANGE_PHONENUM_RESULTBACK           = "CHANGE_PHONENUM_RESULTBACK";
    public static       String CHANGE_MAIL_RESULTBACK               = "CHANGE_MAIL_RESULTBACK";
    public static       String CHANGE_IDCARD_RESULTBACK             = "CHANGE_IDCARD_RESULTBACK";
    public static       String CHANGE_USER_INFO_OK                  = "CHANGE_USER_INFO_OK";
    public static       String FINDPASS_MAIL_RESULTBACK             = "FINDPASS_MAIL_RESULTBACK";
    public static       String FINDPASS_IDCARD_RESULTBACK           = "FINDPASS_IDCARD_RESULTBACK";
    public static       String CHECK_VERIFYCODE_SUCCESS             = "CHECK_VERIFYCODE_SUCCESS";
    public static       String RESET_PASSWORD_FROM_PHONENUM_SUCCESS = "RESET_PASSWORD_FROM_PHONENUM_SUCCESS";
    public static       String GET_FIND_PASSWORD_WAY                = "GET_FIND_PASSWORD_WAY";
    public static       String SUBMMIT_PASSWORD_PROBLEM             = "SUBMMIT_PASSWORD_PROBLEM";
    public static       String VERIFICATION_CODE_BACKOK             = "VERIFICATION_CODE_BACKOK";
    public static       String CHECK_VERFICODE_FAILED_THREE         = "CHECK_VERFICODE_FAILED_THREE";
    public static       String CHECK_VERFICODE_FAILED_FIVE          = "CHECK_VERFICODE_FAILED_FIVE";
    public static       String LOGIN_FAILED                         = "LOGIN_FAILED";
    public static       String REG_FAILED                           = "REG_FAILED";
    public static       String SELECT_FIND_PASSWAY                  = "SELECT_FIND_PASSWAY";
    public static       String LOGIN_USERNAME_SELECT                = "LOGIN_USERNAME_SELECT";
    public static       String LOGIN_RESULT                         = "LOGIN_RESULT";
    public static       String GET_USER_INFO_RESULT_BACK            = "GET_USER_INFO_RESULT_BACK";

    /**
     * Pay
     */
    public static String PAY_CHECKPAY_RESULTBACK = "PAY_CHECKPAY_RESULTBACK";
    public static String PAY_WX_RESULTBACK       = "PAY_WX_RESULTBACK";
    public static String PAY_WX_SUCESS           = "PAY_WX_SUCESS";
    public static String WX_SHARE_SUCESS         = "WX_SHARE_SUCESS";
    public static String WX_LOGIN                = "WX_LOGIN";
    public static String VECHARGE_VOUCHERS_RESULTBACK                = "VECHARGE_VOUCHERS_RESULTBACK";

    /**
     * Information
     */
    public static String INFORMATION_LIST_RESULTBACK           = "INFORMATION_LIST_RESULTBACK";
    public static String INFORMATION_SKINLIST_RESULTBACK       = "INFORMATION_SKINLIST_RESULTBACK";
    public static String INFORMATION_SKINBOUGHTLIST_RESULTBACK = "INFORMATION_SKINBOUGHTLIST_RESULTBACK";
    public static String SKIN_URL_RESULTBACK                   = "SKIN_URL_RESULTBACK";
    public static String SKIN_PAY_RESULTBACK                   = "SKIN_PAY_RESULTBACK";
    public static String SKIN_SETUP_FILE_LOADED                = "SKIN_SETUP_FILE_LOADED";//相同皮肤有多辆车，需要全部刷新一次
    public static String GET_CARDRESSUP_RESULTBACK             = "GET_CARDRESSUP_RESULTBACK";
    /**
     * Common
     */
    public static String SETUP_GESTURE_RESULTBACK              = "SETUP_GESTURE_RESULTBACK";
    public static String GETVERSIONINFO_RESULTBACK             = "GETVERSIONINFO_RESULTBACK";
    public static String COMMON_RESULTBACK                     = "COMMON_RESULTBACK";
    public static String VIOLATION_LIST_BACK                   = "VIOLATION_LIST_BACK";
    public static String CHAT_INFO_BACK                        = "CHAT_INFO_BACK";
    public static String MESSAGE_USER_NEW_BACK                 = "MESSAGE_USER_NEW_BACK";
    public static String SUGGEST_HTTP_RESULTBACK               = "SUGGEST_HTTP_RESULTBACK";
    public static String ADDRESS_LOAD_OK                       = "ADDRESS_LOAD_OK";

    /**
     * CLIP_VIEW
     */
    public static String LIST_VIEW_SIZE_CHANGE                 = "LIST_VIEW_SIZE_CHANGE";
    public static String CLIP_CHECKBOX_CHANGESELECT            = "CLIP_CHECKBOX_CHANGESELECT";
    /**
     * Score
     */
    public static String SCORE_JUMPPAGE                        = "SCORE_JUMPPAGE";
    public static String SCORE_JUMPPAGE_TO_CONTROLCAR          = "SCORE_JUMPPAGE_TO_CONTROLCAR";
    public static String SCORE_LIST_RESULTBACK                 = "SCORE_LIST_RESULTBACK";
    public static String SCORE_DETAIL_RESULTBACK               = "SCORE_DETAIL_RESULTBACK";
    /**
     * SECRET_INFO
     */
    public static String SECRETINFO_CHANGE_QUESTION            = "SECRETINFO_CHANGE_QUESTION";
    public static String SECRETINFOS_RESULTBACK                = "SECRETINFOS_RESULTBACK";
    /**
     * CarInfo
     */
    public static final String CHANGE_CURRENT_CAR_TO_MANAGER                    = "CHANGE_CURRENT_CAR_TO_MANAGER";
    public static String CAR_STATUS_SECOND_CHANGE                     = "CAR_STATUS_SECOND_CHANGE";//车辆状态变化
    public static String CAR_SELECT_CHANGE                     = "CAR_SELECT_CHANGE";//车辆状态变化
    public static String CAR_STYLE_CHANGE_ACTIONBAR                     = "CAR_STYLE_CHANGE_ACTIONBAR";//车辆状态变化
    public static String PRESS_REFRESH_RESULTOK                = "PRESS_REFRESH_RESULTOK";//车辆刷新成功
    public static String CAR_SEARCHWARNING_LISTBACK            = "CAR_SEARCHWARNING_LISTBACK";
    public static String CAR_NEW_SUCESS                        = "CAR_NEW_SUCESS";
    public static String CAR_DELETE_SUCESS                     = "CAR_DELETE_SUCESS";
    public static String CAR_ACTIVATE_SUCESS                   = "CAR_ACTIVATE_SUCESS";
    public static String CAR_UNACTIVATE_SUCESS                 = "CAR_UNACTIVATE_SUCESS";
    public static String CAR_CONTROL_RESULT                    = "CAR_CONTROL_RESULT";
    public static String CAR_CONTROL_SENDED                    = "CAR_CONTROL_SENDED";
    public static String CAR_LIST_CHANGE                       = "CAR_LIST_CHANGE";//车辆列表变化
    public static String CAR_LIST_OTHER_RESULTBACK             = "CAR_LIST_OTHER_RESULTBACK";//车辆列表变化
    public static String CAR_SKIN_TODEFAULT                    = "CAR_SKIN_TODEFAULT";//车辆皮肤变化
    public static String GET_MAINTAINLIST_RESULTBACK           = "GET_MAINTAINLIST_RESULTBACK";//拿汽车保养列表结果
    public static String MODIFICATION_MAINTAINLIST_RESULT_BACK = "MODIFICATION_MAINTAINLIST_RESULT_BACK";//拿汽车保养列表结果
    public static String SELECT_CAR_BACKPPPPP                  = "SELECT_CAR_BACKPPPPP";
    public static String MAINTAIN_COMPELETE                    = "MAINTAIN_COMPELETE";
    public static String MAINTAIN_DELETE                       = "MAINTAIN_DELETE";
    public static String MAINTAIN_MESSAGEBACK                  = "MAINTAIN_MESSAGEBACK";
    public static String SHANCHU_BAOYANG                       = "SHANCHU_BAOYANG";
    public static String BAOYANG_WANCHNEG                      = "BAOYANG_WANCHNEG";
    public static String I_KNOW                                = "I_KNOW";
    public static String WAKE_UP_ME_LATE                       = "WAKE_UP_ME_LATE";
    public static String ALREADY_MAINTAIN                      = "ALREADY_MAINTAIN";
    public static String BLUE_BOUND_CANCEL                     = "BLUE_BOUND_CANCEL";//解绑
    public static String BLUE_BOUND_FAILED                     = "BLUE_BOUND_FAILED";//绑定失败
    public static String CONTROL_FINDCAR                       = "CONTROL_FINDCAR";//尋車指令成功
    public static String LCDKEY_BIND_OPRATION                      = "LCDKEY_BIND_OPRATION";//绑定蓝牙液晶钥匙
    public static String LCDKEY_AUTO_OPEN                     = "LCDKEY_AUTO_OPEN";//蓝牙液晶钥匙自动开启关闭
    public static String CAR_SOUND_CONTROL_RESULT                     = "CAR_SOUND_CONTROL_RESULT";//蓝牙液晶钥匙自动开启关闭
    public static String CAR_SET_CONTROL_RESULT                     = "CAR_SET_CONTROL_RESULT";//蓝牙液晶钥匙自动开启关闭
    public static String CAR_MIDDLE_DOOR_CONTROL_RESULT                     = "CAR_MIDDLE_DOOR_CONTROL_RESULT";//中门开关
    public static String     CAR_SELF_REFRESH_STATUS           = "CAR_SELF_REFRESH_STATUS";//主動刷新




    /**
     * Authorization
     */
    public static String SWITCH_ALL_RESULTBACK                   = "SWITCH_ALL_RESULTBACK";
    public static String SWITCH_WEARS_RESULTBACK                    = "SWITCH_WEARS_RESULTBACK";
    public static String SWITCH_WARNINGS_RESULTBACK                 = "SWITCH_WARNINGS_RESULTBACK";
    public static String AUTHORIZATION_USERLIST_RESULTBACK          = "AUTHORIZATION_USERLIST_RESULTBACK";
    public static String AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK = "AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK";
    public static String AUTHORIZATION_USERLIST_RESULTBACK_CHANGE = "AUTHORIZATION_USERLIST_RESULTBACK_CHANGE";
    public static String AUTHORIZATION_USER_STOPED                  = "AUTHORIZATION_USER_STOPED";
    public static String AUTHOR_CODRIVER_RESULTBACK                 = "AUTHOR_CODRIVER_RESULTBACK";
    public static String CODRIVER_CONFIRM_AUTHOR_RESULTBACK         = "CODRIVER_CONFIRM_AUTHOR_RESULTBACK";
    public static String QRCODE_CODRIVER_RESULTBACK                 = "QRCODE_CODRIVER_RESULTBACK";
    public static String APPLECODE_RESULTBACK                 = "APPLECODE_RESULTBACK";
    public static String GET_UPLOADPIC_TOKEN_RESULTBACK             = "GET_UPLOADPIC_TOKEN_RESULTBACK";
    /**
     * Gps
     */
    public static String GPS_PATHLIST_RESULTBACK                    = "GPS_PATHLIST_RESULTBACK";
    public static String GPS_CARPOS_RESULTBACK                      = "GPS_CARPOS_RESULTBACK";
    public static String GPS_SETAREA_RESULTBACK                     = "GPS_SETAREA_RESULTBACK";
    public static String GPS_FAVORITE_LISTCHANGE                    = "GPS_FAVORITE_LISTCHANGE";
    public static String GPS_FAVORITE_INTRO_CHANGE_OK               = "GPS_FAVORITE_INTRO_CHANGE_OK";
    public static String GPS_FIND_SELFPOS_OK                        = "GPS_FIND_SELFPOS_OK";
    public static String GPS_COLLECT_INFO_BACK                      = "GPS_COLLECT_INFO_BACK";
    public static String DELETE_SEARCH_HISTORY                      = "DELETE_SEARCH_HISTORY";
    public static String GPS_NAVI_INFO_BACK                         = "GPS_NAVI_INFO_BACK";
    public static String GPS_NAVI_HOME_SET_SUCESS                   = "GPS_NAVI_HOME_SET_SUCESS";
    public static String GPS_CARPOS_PAGECHANGE_RESULT_BACK                      = "GPS_CARPOS_PAGECHANGE_RESULT_BACK";

    public static String NAVI_FINISHED_NEED_OPEN_HUD = "NAVI_FINISHED_NEED_OPEN_HUD";

    /**
     * change view
     */
    public static String LANGUAGE_CHANGE               = "LANGUAGE_CHANGE";
    public static String ACTIVITY_KULALA_GOTOVIEW      = "ACTIVITY_KULALA_GOTOVIEW";
    public static String ACTIVITY_KULALA_DESTORY       = "ACTIVITY_KULALA_DESTORY";
    public static String ACTIVITY_KULALA_TOAST_SCALE   = "ACTIVITY_KULALA_TOAST_SCALE";//显示文字动画
    public static String ACTIVITY_LOGIN_GOTOVIEW       = "ACTIVITY_LOGIN_GOTOVIEW";
    public static String ACTIVITY_USERINFO_GOTOVIEW    = "ACTIVITY_USERINFO_GOTOVIEW";
    public static String VIEW_ANNUAL_REMINDER_GOTOVIEW = "VIEW_ANNUAL_REMINDER_GOTOVIEW";//年检页面跳转

    public static String MAIN_CLICK_BACK                   = "MAIN_CLICK_BACK";
    /**
     * camera
     */
    public static String SCAN_RESULT_BACK                  = "SCAN_RESULT_BACK";
    /**
     * map
     */
    public static String MAP_POI_CLICKED                   = "MAP_POI_CLICKED";//poi点击了
    public static String MAP_OFFLINE_DATACHANGE            = "MAP_OFFLINE_DATACHANGE";//离线地图的数据变化了
    public static String MAP_OFFLINE_LISTCHANGE            = "MAP_OFFLINE_LISTCHANGE";//离线地图的数据变化了
    public static String MAP_OFFLINE_START_DOWNLOAD        = "MAP_OFFLINE_START_DOWNLOAD";//离线地图点了开始下载
    /**
     * cardinfo
     */

    public static String GET_CARSIGN_RESULTBACK           = "GET_CARSIGN_RESULTBACK";
    public static String GET_CARDINFO_RESULTBACK           = "GET_CARDINFO_RESULTBACK";
    public static String POP_NAVIGATION                    = "POP_NAVIGATION";
    public static final String TIME_TICK_SECOND                  = "TIME_TICK_SECOND";
    public static String COMMIT_CARDINFO_RESULTBACK        = "COMMIT_CARDINFO_RESULTBACK";
    public static String COMMIT_CARDINFO_RESULTBACK_FAILED = "COMMIT_CARDINFO_RESULTBACK_FAILED";
    public static String GET_CARDINFO_LIST_RESULTBACK      = "GET_CARDINFO_LIST_RESULTBACK";
    public static String SHOW_GIVE_CARD_PROMEBOX           = "SHOW_GIVE_CARD_PROMEBOX";
    public static String CARD_GIVE_SUCCESS                 = "CARD_GIVE_SUCCESS";
    public static String CARD_GIVE_FAILED                  = "CARD_GIVE_FAILED";
    public static String CARD_SYNTHETIC_SUCCESS            = "CARD_SYNTHETIC_SUCCESS";
    public static String CARD_SYNTHETIC_FAILED             = "CARD_SYNTHETIC_FAILED";
    public static String HIDE_BG_GREEN                     = "HIDE_BG_GREEN";//@param location 0——30显示每一个button，-1所有都不显示
    public static String SYSTHETIC_CARD                    = "SYSTHETIC_CARD";
    public static String TO_NEWCARDPOSITION                = "TO_NEWCARDPOSITION";
    public static String CARD_GIVE_RESULT                  = "CARD_GIVE_RESULT";
    public static String CARD_SYNTHETIC_RESULT             = "CARD_SYNTHETIC_RESULT";
    public static String SHOW_GIVE_SHARE_PROMEBOX          = "SHOW_SHARE_CARD_PROMEBOX";
    public static String CARD_RECIVIE                      = "CARD_RECIVIE";

    /**
     * 点击演示模式的回调
     */
    public static String TAB_CLICK_TRUE               = "TAB_CLICK_TRUE";
    public static String POP_UP_BOX                   = "POP_UP_BOX";
    public static String NO_CAR_DEMO_MODE             = "NO_CAR_DEMO_MODE";
    public static String DEMO_MODE_START              = "DEMO_MODE_START";
    public static String EXIT_DEMOMODE_WINDOW_SHOW    = "EXIT_DEMOMODE_WINDOW_SHOW";
    public static String EXIT_DEMOMODE_WINDOW_DISMISS = "EXIT_DEMOMODE_WINDOW_DISMISS";
    public static String ADDCAR_WINDOW_TOAST          = "ADDCAR_WINDOW_TOAST";
    /**
     * Annual
     */
    public static String ANNUAL_FROM_CAR_RESULTBACK   = "ANNUAL_FROM_CAR_RESULTBACK";
    public static String ANNUAL_CHANGE_RESULTBACK     = "ANNUAL_CHANGE_RESULTBACK";
    public static String ANNUAL_RECODE_RESULTBACK     = "ANNUAL_RECODE_RESULTBACK";

    /**
     * ik
     */
    public static String ACTIVITY_FINISH_ALL_KEEP_VIEWS     = "ACTIVITY_FINISH_ALL_KEEP_VIEWS";
    public static String GPS_NEAR_SEARVICE_LIST_RESULT_BACK = "GPS_NEAR_SEARVICE_LIST_RESULT_BACK";
    public static String PHONE_CALL_RECORD_LIST_BACK        = "PHONE_CALL_RECORD_LIST_BACK";
    public static String PHONE_COMMON_CONTACT_LIST_BACK     = "PHONE_COMMON_CONTACT_LIST_BACK";
    public static String PHEAD_USER_INFO_CHANGE             = "PHEAD_USER_INFO_CHANGE";

    public static String SPEAKING_GROUP_ALL_FRIENDS_BACK  = "SPEAKING_GROUP_ALL_FRIENDS_BACK";
    public static String SPEAKING_GROUP_GET_INFO_OK       = "SPEAKING_GROUP_GET_INFO_OK";
    public static String SPEAKING_GROUP_DELETE_FRIENDS_OK = "SPEAKING_GROUP_DELETE_FRIENDS_OK";
    public static String SPEAKING_GROUP_ADD_FRIENDS_OK    = "SPEAKING_GROUP_ADD_FRIENDS_OK";
    public static String SPEAKING_GROUP_CHANGE_SELF_POS   = "SPEAKING_GROUP_CHANGE_SELF_POS";
    public static String SPEAKING_GROUP_ONLINE_MEMBERS    = "SPEAKING_GROUP_ONLINE_MEMBERS";
    public static String SPEAKING_GROUP_INTO_TALKING      = "SPEAKING_GROUP_INTO_TALKING";
    public static String SPEAKING_GROUP_LIST_BACK         = "SPEAKING_GROUP_LIST_BACK";
    public static String SPEAKING_GROUP_ADD_OK            = "SPEAKING_GROUP_ADD_OK";
    public static String SPEAKING_GROUP_BUILD_OK          = "SPEAKING_GROUP_BUILD_OK";
    public static String SPEAKING_SHARE                   = "SPEAKING_SHARE";
    public static String SPEAKING_EXIT_GROUP              = "SPEAKING_EXIT_GROUP";
    public static String SPEAKING_FIND_HE                 = "SPEAKING_FIND_HE";

    public static String MUSIC_COLLECT_LIST_BACK = "MUSIC_COLLECT_LIST_BACK";
    public static String MUSIC_STOP              = "MUSIC_STOP";
    public static final String STOP_ANIM_ROTATE              = "STOP_ANIM_ROTATE";

    public static final int    RESULT_CODE_OF_POSDATA_GET     = 100;//整个页面用来取一个坐标的返回值
    public static final String TYPE_VIEW                      = "TYPE_VIEW";//这个是页面用来传值的文字
    public static final int    TYPE_VIEW_IS_GROUP_DESTIANTION = 90095;//这个页面是用来设群目的地的
    public static final int    TYPE_VIEW_IS_GROUP_SETHOME     = 90096;//这个页面是用来设家的
    public static final int    TYPE_VIEW_IS_GROUP_SETCOMPANY  = 90097;//这个页面是用来设公司的
    public static final int    TYPE_VIEW_IS_FOR_NAVI          = 90098;//这个页面是用来导航的

    public static final String VOICE_CONTROL_EXIT_NAVI_HUD = "VOICE_CONTROL_EXIT_NAVI_HUD";//语音退导航的
    public static final String BLUE_VERSIOIN_INFO_BACK     = "BLUE_VERSIOIN_INFO_BACK";
    public static final String SEND_CAR_STATUS_NET_OR_SOCKET     = "SEND_CAR_STATUS_NET_OR_SOCKET";
    public static final String SEND_CAR_STATUS_BLUETOOTH    = "SEND_CAR_STATUS_BLUETOOTH";

    /**
     * 摇一摇
     */
    public static final String KULALA_SHAKE = "KULALA_SHAKE";

    /**
     * nfc列表返回
     */
    public static final String QUERY_NFC_DATA_BACK = "QUERY_NFC_DATA_BACK";
    public static final String CHANGE_NFC_DATA_BACK = "CHANGE_NFC_DATA_BACK";
    public static final String ADD_OR_DELETE_NFC_DATA_BACK = "ADD_OR_DELETE_NFC_DATA_BACK";
    public static final String READ_CARD_SUCCESS = "READ_CARD_SUCCESS";
    public static final String REGET_NFC_RESULT_BACK = "REGET_NFC_RESULT_BACK";
    public static final String SERVICE_IS_CONNECT = "SERVICE_IS_CONNECT";
    public static final String BLUETOOTH_CONNECTED_STATUS = "BLUETOOTH_CONNECTED_STATUS";
    public static final String IMG_PUSH_RESULT = "IMG_PUSH_RESULT";
    public static final String APPLY_STORE_CAR_RESULT = "APPLY_STORE_CAR_RESULT";
    public static final String QURRY_STORE_CAR_RESULT = "QURRY_STORE_CAR_RESULT";
    public static final String SUBMIT_AUDIT_SUCCESS = "SUBMIT_AUDIT_SUCCESS";
    public static final String CHANGE_CAR_STORE_RESULT = "CHANGE_CAR_STORE_RESULT";

    public static final String QURRY_REMOTE_CONTROL_RESULT = "QURRY_REMOTE_CONTROL_RESULT";
    public static final String ADD_OR_DELETE_REMOTE_CONTROL = "ADD_OR_DELETE_REMOTE_CONTROL";
    public static final String TEM_CONTROL_SOCKET_RESULT_BACK = "TEM_CONTROL_SOCKET_RESULT_BACK";
    public static final String NOKEY_SET_INFO = "NOKEY_SET_INFO";
    /**
     * MINI版本绑定结果返回
     * */
    public static final String MINI_BIND_RESULT = "MINI_BIND_RESULT";
    public static final String MINI_UNBIND_RESULT = "MINI_UNBIND_RESULT";
    public static final String MINI_CONTROL_SWITCH_RESULT_BACK = "MINI_CONTROL_SWITCH_RESULT_BACK";
    public static final String MINI_CONTROL_FINDCAR_RESULT_BACK = "MINI_CONTROL_FINDCAR_RESULT_BACK";
    public static final String MINI_LOCK_OR_UNLOCK_RESULT_BACK = "MINI_LOCK_OR_UNLOCK_RESULT_BACK";
    public static final String LOCK_COUNT_RESULT = "LOCK_COUNT_RESULT";
    public static final String MINI_NFC_RESULT_BACK = "MINI_NFC_RESULT_BACK";
    public static final String MINI_SET_FACTORY_RESULT = "MINI_SET_FACTORY_RESULT";
    //广告页数据返回
    public static final String DISPLAY_RESULT_BACK = "DISPLAY_RESULT_BACK";
    public static final String SHOW_PROGRESS_DIOLOG = "SHOW_PROGRESS_DIOLOG";
    public static final String HIDE_PROGRESS_DIOLOG = "HIDE_PROGRESS_DIOLOG";
    public static final String SHOW_PLUE_UPGRADE_VIEW = "SHOW_PLUE_UPGRADE_VIEW";
    //升级plus信息查询
    public static final String UPGRADE_RESULT_BACK = "UPGRADE_RESULT_BACK";
    //mini加强版锁设置
    public static final String LOCK_UNLOCK_LEVEL_RESULT_BACK = "LOCK_UNLOCK_LEVEL_RESULT_BACK";
    //mini加强版锁设置
    public static final String EVASIVE_DEVICE_RESULT_BACK = "EVASIVE_DEVICE_RESULT_BACK";
    public static final String TOUCH_IN_SWITCH_RESULT = "TOUCH_IN_SWITCH_RESULT";
    public static final String TOUCH_IN_RSSI_RESULT = "TOUCH_IN_RSSI_RESULT";
    public static final String TOUCH_IN_SET_INFO = "TOUCH_IN_SET_INFO";
    public static final String TOUCH_AIR_CONTROL = "TOUCH_AIR_CONTROL";
    public static final String SOCKET_NOKEY_SET = "SOCKET_NOLEY_SET";
    public static final String SOCKET_PRO_SET = "SOCKET_PRO_SET";
    public static final String TAOBAO_INFO_BACK = "TAOBAO_INFO_BACK";
    public static final String SHOW_CHONGZHI = "SHOW_CHONGZHI";

    //定位器
    public static final String POSITOR_LIST_BACK = "POSITOR_LIST_BACK";
    public static final String POSITOR_DETAIL_ALL_BACK = "POSITOR_DETAIL_ALL_BACK";
    public static final String POSITOR_INFO_BACK = "POSITOR_INFO_BACK";
    public static final String POSITOR_SET_BACK = "POSITOR_SET_BACK";
    public static final String POSITOR_TRAIL_LIST_BACK = "POSITOR_TRAIL_LIST_BACK";
    public static final String POSITOR_RECORDING_LIST_BACK = "POSITOR_RECORDING_LIST_BACK";
    public static final String POSITOR_WARNING_RECORD_LIST_BACK = "POSITOR_WARNING_RECORD_LIST_BACK";
    public static final String POSITOR_COMMAND_DOWN_BACK = "POSITOR_COMMAND_DOWN_BACK";
    public static final String POSITOR_FENCE_SET_BACK = "POSITOR_FENCE_SET_BACK";

    public static final String SHOW_CAR_BODY = "SHOW_CAR_BODY";

    public OEventName() {
        super();
    }
}
