package model.AppList;

import com.client.proj.kusida.R;

/**
 * Created by qq522414074 on 2016/12/12.
 */

public class AppListData {
    public int pic;
    public String name;
    public AppListData(int pic, String name){
        this.pic=pic;
        this.name=name;
    }
//    public static  String[] names = {"信任借车","临时借车", "状态记录", "围栏", "轨迹"
//            , "保养提醒","年检提醒","导航", "违章"};
//    public static  String[] names = {"信任借车","临时借车", "状态记录", "围栏", "轨迹"
//            , "保养提醒","年检提醒", "违章"};
    public static  String[] names = {"信任借车","临时借车", "状态记录", "围栏", "轨迹"
            };

//    public static int[] images = {
//            R.drawable.app_driverco,R.drawable.app_lendcar_temp, R.drawable.app_states, R.drawable.app_area,R.drawable.app_track,
//            R.drawable.maintenance_remind, R.drawable.icon_annual_reminder,R.drawable.app_violation};
    public static int[] images = {
            R.drawable.app_driverco,R.drawable.app_lendcar_temp, R.drawable.app_states, R.drawable.app_area,R.drawable.app_track
           };
//    public static int[] images = {
//            R.drawable.app_driverco,R.drawable.app_lendcar_temp, R.drawable.app_states, R.drawable.app_area,R.drawable.app_track,
//            R.drawable.maintenance_remind, R.drawable.icon_annual_reminder, R.drawable.app_navigation,R.drawable.app_violation};

//    public static  String[] names_codriver = {"状态记录", "围栏", "轨迹"
//            , "保养提醒","年检提醒","导航", "违章"};
//    public static  String[] names_codriver = {"状态记录", "围栏", "轨迹"
//            , "保养提醒","年检提醒", "违章"};
    public static  String[] names_codriver = {"状态记录", "围栏", "轨迹"
          };

//    public static int[] images_codriver = {
//            R.drawable.app_states, R.drawable.app_area,R.drawable.app_track,
//            R.drawable.maintenance_remind, R.drawable.icon_annual_reminder,R.drawable.app_violation};
    public static int[] images_codriver = {
            R.drawable.app_states, R.drawable.app_area,R.drawable.app_track
           };

}
