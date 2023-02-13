package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

public class DataAuthorization {

	public int ide;//权限ID
	public String name="";//权限名
	public boolean isSelected = true;//默认全选中
	public void jsonObjectToAuthor(JsonObject obj){
		if(obj == null)return;
		ide = OJsonGet.getInteger(obj, "ide");
		name = OJsonGet.getString(obj, "name");
	}
	public static JsonArray listToIdArr(List<DataAuthorization> authorlist){
		if(authorlist == null)return null;
		List<Integer> arr = new ArrayList<Integer>();
		for(int i=0;i<authorlist.size();i++){
			DataAuthorization data = authorlist.get(i);
			if(data.isSelected){
				arr.add(data.ide);
			}
		}
		Gson gson = new Gson();
		String str = gson.toJson(arr);
		JsonArray json = gson.fromJson(str, JsonArray.class);
		return json;
	}
}
