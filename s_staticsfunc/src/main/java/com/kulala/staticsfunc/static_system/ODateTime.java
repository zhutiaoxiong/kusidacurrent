package com.kulala.staticsfunc.static_system;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class ODateTime {
	/**yyyy年*/
	public static String time2OnlyYear(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date(time);
		String re_StrTime = sdf.format(date);
		return re_StrTime;
	}
	/**yyyy年MM月dd日*/
	public static String time2StringOnlyDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date(time);
		String re_StrTime = sdf.format(date);
		return re_StrTime;
	}
	/**yyyy年MM月dd日*/
	public static String time2StringDateDetail(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date(time);
		String re_StrTime = sdf.format(date);
		return re_StrTime;
	}
	/**yyyy-MM-dd*/
	public static String time2StringOnlyDateCache(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		String re_StrTime = sdf.format(date);
		return re_StrTime;
	}
	/**yyyy-MM-dd HH:mm*/
	public static String time2StringWithHH(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String re_StrTime = sdf.format(new Date(time));
		return re_StrTime;
	}
	/**yyyy-MM-dd HH:mm*/
	public static String time2StringWithMMHH(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		String re_StrTime = sdf.format(new Date(time));
		return re_StrTime;
	}
	/**HH:mm*/
	public static String  time2StringHHmm(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String re_StrTime = sdf.format(new Date(time));
		return re_StrTime;
	}
	/**HH:mm:ss*/
	public static String time2StringHHmmss(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String re_StrTime = sdf.format(new Date(time));//-8*60*60*1000L
		return re_StrTime;
	}
	/** timestamp */
	public static long date2long(int yyyy, int MM, int dd) {
		try {
			String timeStr = yyyy + "年" + MM + "月" + dd + "日";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			Date date = sdf.parse(timeStr);
			long l = date.getTime();
			return l;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/** timestamp */
	public static long time2long(int yyyy, int MM, int dd, int HH, int mm, int ss) {
		try {
			String timeStr = yyyy + "年" + MM + "月" + dd + "日" + HH + "时" + mm + "分" + ss + "秒";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
			Date date = sdf.parse(timeStr);
			long l = date.getTime();
			return l;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/** timestamp */
	public static long str2long(String yyyy_MM_dd) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(yyyy_MM_dd);
			long l = date.getTime();
			return l;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/** timestamp */
	public static long getNow() {
		Date date = new Date();
		long l = date.getTime();
		return l;
	}
}
