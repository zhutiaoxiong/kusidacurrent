package com.kulala.staticsfunc.static_system;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.List;

public class OArrayConvent {

	public static String[] convent(JsonArray arr, String key){
		if(arr==null)return new String[0];
		String[] result = new String[arr.size()];
		for(int i=0;i<arr.size();i++){
			JsonObject object = arr.get(i).getAsJsonObject();
			String     name   = object.get(key).getAsString();
			result[i] = name;
		}
		return result;
	}
	public static String[] convent(JsonArray arr){
		if(arr==null)return new String[0];
		String[] result = new String[arr.size()];
		for(int i=0;i<arr.size();i++){
			String name = arr.get(i).getAsString();
			result[i] = name;
		}
		return result;
	}
	/**取字串表  Arrays.asList(arr)**/
	public static String[] ListGetStringArr(List ve,String fieldName){
		if(ve==null)return null;
		String str = "";
		for (int i = 0; i < ve.size(); i++) {
			Object obj = ve.get(i);
			str += objectGetStr(obj,fieldName);
			if(i<ve.size()-1)str += "wer7ert5rty3tyu";
		}
		String[] arr;
		try{
			arr = str.split("wer7ert5rty3tyu");
		} catch (Exception e) {
			arr = new String[]{str};
		}
		return arr;
	}
	/**取字串表,不要空格  Arrays.asList(arr)**/
	public static String[] ListGetStringArrNoSpace(List ve,String fieldName){
		if(ve==null)return null;
		String str = "";
		for (int i = 0; i < ve.size(); i++) {
			Object obj = ve.get(i);
			str += objectGetStr(obj,fieldName).replace(" ","");
			if(i<ve.size()-1)str += "wer7ert5rty3tyu";
		}
		String[] arr;
		try{
			arr = str.split("wer7ert5rty3tyu");
		} catch (Exception e) {
			arr = new String[]{str};
		}
		return arr;
	}
	/**取字串位置**/
	public static int ListGetStringPos(List<String> list,String value){
		if(list==null)return 0;
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if(str.equals(value))return i;
		}
		return 0;
	}
	private static String objectGetStr(Object obj,String fieldName) {
		String txt = "";
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int j = 0; j < fields.length; j++) {
				if (fields[j].getName().equals(fieldName)) {
					txt = (String) fields[j].get(obj);//user
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return txt;
	}
}
