package common.pinyinzhuanhuan;

import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.Calendar;

/**
 * Created by qq522414074 on 2017/3/16.
 */

public class DateReplace {
    public static String returnDate(long thetime) {
        String formatTime = null;
        long currentTime = System.currentTimeMillis();
        long time = currentTime - thetime;
//        Calendar now = Calendar.getInstance();
//       int hour= now.get(Calendar.HOUR_OF_DAY);
//        int fen=now.get(Calendar.MINUTE);
//       int second= now.get(Calendar.SECOND);
//        long time0=currentTime-hour*60*60*1000L-fen*60*1000L-second*1000L;

        String currentDate = ODateTime.time2StringOnlyDateCache(currentTime);
        long theCurrentOTime = ODateTime.str2long(currentDate);
        //不足一分钟
        if (time < 60 * 1000L) {
            formatTime = "刚刚";
        } else if ( time < 60 * 60 * 1000L) {//不足一个小时
            formatTime = (int) (time / (60 * 1000)) + "分钟前";
        } else if ( thetime>getTimesmorning()&&thetime<getTimesmorning()+24*60*60*1000L) {//不足一天
            formatTime = " 今天" + ODateTime.time2StringHHmm(thetime);
        } else if ( thetime <getTimesmorning()&&thetime>getTimesmorning()-24*60*60*1000L) {//不足两天
            formatTime = " 昨天" + ODateTime.time2StringHHmm(thetime);
        } else if (thetime<getTimesmorning()-24*60*60*1000L) {//不足一年
            formatTime = ODateTime.time2StringWithMMHH(thetime);
        } else if (60 * 60 * 1000L * 24 * 365 <= time) {//大于一年
            formatTime = ODateTime.time2StringWithHH(thetime);
        }
        return formatTime;
    }

    //获得当天0点时间
    public static Long getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (Long) cal.getTimeInMillis();
    }
}
