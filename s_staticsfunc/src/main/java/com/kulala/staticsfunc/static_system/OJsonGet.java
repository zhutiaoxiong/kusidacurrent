package com.kulala.staticsfunc.static_system;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class OJsonGet {

	public static JsonArray getJsonArray(JsonObject object, String name){
		try {
			return object.get(name).getAsJsonArray();
		} catch (Exception e) {
			return null;
		}
	}
	public static JsonObject getJsonObject(JsonObject object, String name){
		try {
			return object.get(name).getAsJsonObject();
		} catch (Exception e) {
			return null;
		}
	}
	public static String getString(JsonObject object, String name){
		try {
			return object.get(name).getAsString();
		} catch (Exception e) {
			return "";
		}
	}
	public static long getLong(JsonObject object, String name){
		try {
			return object.get(name).getAsLong();
		} catch (Exception e) {
			return 0;
		}
	}
	public static boolean getBoolean(JsonObject object, String name){
		try {
			return object.get(name).getAsBoolean();
		} catch (Exception e) {
			return false;
		}
	}
	public static double getDouble(JsonObject object, String name){
		try {
			return object.get(name).getAsDouble();
		} catch (Exception e) {
			return 0;
		}
	}
	public static int getInteger(JsonObject object, String name){
		try {
			return object.get(name).getAsInt();
		} catch (Exception e) {
			return 0;
		}
	}
}
