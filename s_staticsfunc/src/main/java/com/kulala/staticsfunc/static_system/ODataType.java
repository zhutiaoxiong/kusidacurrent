package com.kulala.staticsfunc.static_system;
public class ODataType {

	public static int str2int(String str){
		if(str == null || str.equals(""))return 0;
		return Integer.valueOf(str.trim());
	}
	public static float str2float(String str){
		if(str == null || str.equals(""))return 0;
		return Float.valueOf(str.trim());
	}
	public static double str2double(String str){
		if(str == null || str.equals(""))return 0;
		return Double.valueOf(str.trim());
	}
}
